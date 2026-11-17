package com.example.dz.data.sync

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.api.ReadingDataApi
import com.example.dz.data.remote.runRemote
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.uuid.Uuid

/**
 * Persistent per-account outbox and cache. Only pending edits are uploaded, never an entire stale
 * device snapshot. Mutations are saved before any network work; cancelled/failed requests remain
 * pending. The server's present contract resolves concurrent edits by last successful upload.
 * The mutex also guards auth session changes so a queued write cannot use another user's token.
 */
class ReadingDataSync(
    private val api: ReadingDataApi,
    private val local: LocalDataSource,
    private val library: LibraryLocalDataSource,
    private val collections: CollectionLocalDataSource,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) {
    private val mutex = Mutex()
    private val json = Json { ignoreUnknownKeys = true }
    private var scheduled: Job? = null
    private var lastSuccessUser: String? = null
    private var lastSuccessAt = 0L

    suspend fun <T> sessionChange(block: suspend () -> T): T = mutex.withLock {
        withContext(Dispatchers.IO) {
            // Claim legacy data before logout/login changes the identity used to label it.
            local.getUserId()?.let { prepare(it) }
            val result = block()
            local.getUserId()?.let { prepare(it) }
            result
        }
    }

    private fun key(user: String) = "reading_sync_account_$user"
    private fun read(user: String): SyncState = local.getSetting(key(user)).let {
        if (it.isEmpty()) SyncState() else json.decodeFromString<SyncState>(it)
    }
    private fun save(user: String, state: SyncState) = local.saveSetting(key(user), json.encodeToString(state))

    /** SQLite is the active account's working cache; snapshots/outboxes survive account switches. */
    private fun prepare(user: String): SyncState {
        val owner = local.getSetting(OWNER)
        if (owner == user) return read(user)
        if (owner.isNotEmpty()) {
            save(owner, snapshot(read(owner)))
            library.removeAll()
            collections.deleteAll()
        }
        var state = read(user)
        if (owner.isEmpty() && !state.adopted) {
            // One-time adoption of pre-sync data. Existing server copies win during migration.
            state = snapshot(state).let {
                it.copy(adopted = true, newBooks = it.books.map { b -> b.book.id }.toSet(),
                    newCollections = it.collections.map { c -> c.id }.toSet())
            }
            local.saveSetting("reading_sync_legacy_owner", user)
        } else {
            restore(state)
            state = state.copy(adopted = true)
        }
        save(user, state)
        local.saveSetting(OWNER, user)
        return state
    }

    private fun snapshot(state: SyncState) = state.copy(
        books = library.getLibraryBooks(), collections = collections.getCollections(),
        downloads = library.getLibraryBooks().mapNotNull { entry ->
            library.getDownloadPath(entry.book.id)?.let { entry.book.id to it }
        }.toMap(),
    )

    private fun restore(state: SyncState) {
        library.removeAll()
        collections.deleteAll()
        state.books.forEachIndexed { index, entry ->
            library.upsert(entry, (state.books.size - index).toLong())
            state.downloads[entry.book.id]?.let { library.setDownload(entry.book.id, true, it) }
        }
        state.collections.forEachIndexed { index, entry ->
            collections.create(entry, (state.collections.size - index).toLong())
        }
    }

    suspend fun <T> cached(block: suspend () -> T): AppResult<T> = mutex.withLock {
        withContext(Dispatchers.IO) {
            val user = local.getUserId() ?: return@withContext AppResult.Error(AppError.Unauthorized)
            prepare(user)
            AppResult.Success(block())
        }
    }

    suspend fun add(book: Book): AppResult<Unit> = mutate { state ->
        if (library.getLibraryBook(book.id) != null) state else {
            library.upsert(LibraryBook(book), currentEpochMillis())
            state.copy(newBooks = state.newBooks + book.id)
        }
    }

    suspend fun progress(bookId: String, percent: Int): AppResult<Unit> = mutate { state ->
        if (library.getLibraryBook(bookId) == null) state else {
            library.updateProgress(bookId, percent.coerceIn(0, 100))
            state.copy(progress = state.progress + (bookId to percent.coerceIn(0, 100)))
        }
    }

    suspend fun create(title: String): AppResult<Collection> {
        val collection = Collection(Uuid.random().toString(), title.trim())
        return when (val result = put(collection)) {
            is AppResult.Success -> AppResult.Success(collection)
            is AppResult.Error -> result
        }
    }

    suspend fun put(collection: Collection): AppResult<Unit> = mutate { state ->
        if (collections.getCollection(collection.id) == null) collections.create(collection, currentEpochMillis())
        else collections.update(collection)
        state.copy(dirtyCollections = state.dirtyCollections + collection.id,
            deletedCollections = state.deletedCollections - collection.id)
    }

    suspend fun delete(id: String): AppResult<Unit> = mutate { state ->
        collections.delete(id)
        state.copy(dirtyCollections = state.dirtyCollections - id, newCollections = state.newCollections - id,
            deletedCollections = state.deletedCollections + id)
    }

    private suspend fun mutate(edit: (SyncState) -> SyncState): AppResult<Unit> {
        val result = mutex.withLock {
            withContext(Dispatchers.IO) {
                val user = local.getUserId() ?: return@withContext AppResult.Error(AppError.Unauthorized)
                lastSuccessAt = 0L
                val state = edit(prepare(user))
                save(user, snapshot(state))
                AppResult.Success(Unit)
            }
        }
        if (result is AppResult.Success) requestSync()
        return result
    }

    fun requestSync() {
        // Coalesce page turns. A regular foreground pass also retries interrupted/offline work.
        if (scheduled?.isActive == true) return
        scheduled = scope.launch { delay(500); syncNow() }
    }

    suspend fun syncNow(force: Boolean = true): AppResult<Unit> = mutex.withLock {
        withContext(Dispatchers.IO) {
            val user = local.getUserId() ?: return@withContext AppResult.Error(AppError.Unauthorized)
            if (!local.isEmailVerified()) return@withContext AppResult.Error(AppError.Unauthorized)
            if (!force && lastSuccessUser == user && currentEpochMillis() - lastSuccessAt < 2_000) {
                return@withContext AppResult.Success(Unit)
            }
            var state = snapshot(prepare(user))
            // Downloads can insert shelf rows directly. Discover those without re-uploading
            // previously synchronized rows removed on another device.
            state = state.copy(newBooks = state.newBooks +
                (state.books.map { it.book.id }.toSet() - state.knownBooks))
            save(user, state)
            withTimeoutOrNull(8_000) {
                runRemote {
                    val remoteBooks = api.library().associateBy { it.book.id }.toMutableMap()
                    for (id in state.newBooks) {
                        val entry = state.books.firstOrNull { it.book.id == id } ?: continue
                        if (id !in remoteBooks) {
                            api.saveBook(entry)
                            remoteBooks[id] = entry.copy(isDownloaded = false)
                        }
                        state = state.copy(newBooks = state.newBooks - id, knownBooks = state.knownBooks + id)
                        save(user, state)
                    }
                    for ((id, percent) in state.progress) {
                        val entry = state.books.firstOrNull { it.book.id == id } ?: continue
                        if (id !in remoteBooks) api.saveBook(entry)
                        api.progress(id, percent)
                        remoteBooks[id] = (remoteBooks[id] ?: entry).copy(progressPercent = percent)
                        state = state.copy(progress = state.progress - id)
                        save(user, state)
                    }
                    val merged = remoteBooks.values.map { remote ->
                        val cached = state.books.firstOrNull { it.book.id == remote.book.id }
                        remote.copy(isDownloaded = cached?.isDownloaded ?: false,
                            book = remote.book.copy(pageCount = cached?.book?.pageCount))
                    }
                    state = state.copy(books = merged, knownBooks = remoteBooks.keys,
                        downloads = state.downloads.filterKeys { it in remoteBooks })
                    save(user, state)
                    restore(state)

                    val remoteCollections = api.collections().associateBy { it.id }.toMutableMap()
                    for (id in state.deletedCollections) {
                        api.deleteCollection(id)
                        remoteCollections.remove(id)
                        state = state.copy(deletedCollections = state.deletedCollections - id)
                        save(user, state)
                    }
                    for (id in state.newCollections + state.dirtyCollections) {
                        val entry = state.collections.firstOrNull { it.id == id } ?: continue
                        if (id in state.dirtyCollections || id !in remoteCollections) {
                            api.saveCollection(entry)
                            remoteCollections[id] = entry
                        }
                        state = state.copy(newCollections = state.newCollections - id,
                            dirtyCollections = state.dirtyCollections - id)
                        save(user, state)
                    }
                    state = state.copy(collections = remoteCollections.values.map { remote ->
                        val cached = state.collections.firstOrNull { it.id == remote.id }
                        // These two fields aren't part of the current server contract.
                        remote.copy(colorIndex = cached?.colorIndex ?: 0, isShared = cached?.isShared ?: false)
                    })
                    save(user, state)
                    restore(state)
                }
            }.let { result ->
                if (result is AppResult.Success) {
                    lastSuccessUser = user
                    lastSuccessAt = currentEpochMillis()
                }
                result ?: AppResult.Error(AppError.Timeout)
            }
        }
    }

    companion object {
        const val OWNER = "reading_sync_owner"
    }
}

@Serializable
private data class SyncState(
    val adopted: Boolean = false,
    val books: List<LibraryBook> = emptyList(),
    val collections: List<Collection> = emptyList(),
    val downloads: Map<String, String> = emptyMap(),
    val knownBooks: Set<String> = emptySet(),
    val newBooks: Set<String> = emptySet(),
    val progress: Map<String, Int> = emptyMap(),
    val newCollections: Set<String> = emptySet(),
    val dirtyCollections: Set<String> = emptySet(),
    val deletedCollections: Set<String> = emptySet(),
)
