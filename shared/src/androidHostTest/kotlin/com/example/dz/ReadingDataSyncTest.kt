package com.example.dz

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.*
import com.example.dz.data.remote.api.ReadingDataApi
import com.example.dz.data.repository.LocalReadingPositionRepository
import com.example.dz.data.repository.SyncedLibraryRepository
import com.example.dz.data.sync.ReadingDataSync
import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.*
import kotlin.test.*

class ReadingDataSyncTest {
    private class Server {
        val books = mutableMapOf<String, MutableMap<String, LibraryBook>>()
        val shelves = mutableMapOf<String, MutableMap<String, Collection>>()
        var online = true
        var loseNextSaveResponse = false
        fun api(local: LocalDataSource) = object : ReadingDataApi {
            fun user(): String { check(online) { "Offline" }; return requireNotNull(local.getUserId()) }
            override suspend fun library() = books.getOrPut(user()) { mutableMapOf() }.values.toList()
            override suspend fun saveBook(book: LibraryBook) {
                books.getOrPut(user()) { mutableMapOf() }[book.book.id] = book.copy(isDownloaded = false)
                if (loseNextSaveResponse) { loseNextSaveResponse = false; error("Response lost") }
            }
            override suspend fun progress(bookId: String, percent: Int) {
                val shelf = books.getOrPut(user()) { mutableMapOf() }
                shelf[bookId] = shelf.getValue(bookId).copy(progressPercent = percent)
            }
            override suspend fun collections() = shelves.getOrPut(user()) { mutableMapOf() }.values.toList()
            override suspend fun saveCollection(collection: Collection) {
                shelves.getOrPut(user()) { mutableMapOf() }[collection.id] = collection.copy(colorIndex = 0, isShared = false)
            }
            override suspend fun deleteCollection(id: String) { shelves.getOrPut(user()) { mutableMapOf() }.remove(id) }
        }
    }
    private class Device(val server: Server) : AutoCloseable {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        val database = DzDatabase(driver).also { DzDatabase.Schema.create(driver) }
        val local = LocalDataSourceImpl(MapSettings())
        val library = LibraryLocalDataSource(database)
        val collections = CollectionLocalDataSource(database)
        // Explicit sync calls make the tests deterministic and simulate process restarts.
        val scope = CoroutineScope(Job().apply { cancel() })
        var sync = ReadingDataSync(server.api(local), local, library, collections, scope)
        init { login("alice") }
        fun login(id: String) = local.saveUserSession(id, id, "$id@example.com", "token-$id", "refresh", true)
        fun restart() { sync = ReadingDataSync(server.api(local), local, library, collections, scope) }
        override fun close() { scope.cancel(); driver.close() }
    }
    private val book = Book("gutenberg-1", "A book", isFree = true, textUrl = "https://example.com/book.txt")

    @Test fun twoDevicesShareLibraryProgressAndCollections() = runBlocking {
        val server = Server()
        Device(server).use { a -> Device(server).use { b ->
            assertIs<AppResult.Success<Unit>>(a.sync.add(book))
            a.sync.progress(book.id, 42)
            val collection = (a.sync.create("Favorites") as AppResult.Success).data.copy(books = listOf(book))
            a.sync.put(collection)
            assertIs<AppResult.Success<Unit>>(a.sync.syncNow())
            assertIs<AppResult.Success<Unit>>(b.sync.syncNow())
            assertEquals(42, b.library.getLibraryBook(book.id)?.progressPercent)
            assertEquals(listOf(book.id), b.collections.getCollection(collection.id)?.books?.map { it.id })
            b.sync.progress(book.id, 58)
            b.sync.syncNow(); a.sync.syncNow()
            assertEquals(58, a.library.getLibraryBook(book.id)?.progressPercent)
            val reader = LocalReadingPositionRepository(a.local, SyncedLibraryRepository(a.sync, a.library))
            assertEquals(5800, reader.resumeOffset(book.id, 10_000))
        } }
    }

    @Test fun offlineEditsSurviveRestartAndDeletionDoesNotResurrect() = runBlocking {
        val server = Server()
        Device(server).use { a -> Device(server).use { b ->
            a.sync.add(book)
            val collection = (a.sync.create("Travel") as AppResult.Success).data
            a.sync.syncNow(); b.sync.syncNow()
            server.online = false
            a.sync.progress(book.id, 61)
            a.sync.delete(collection.id)
            assertIs<AppResult.Error>(a.sync.syncNow())
            a.restart()
            assertEquals(61, a.library.getLibraryBook(book.id)?.progressPercent)
            server.online = true
            a.sync.syncNow(); b.sync.syncNow(); b.sync.syncNow()
            assertEquals(61, b.library.getLibraryBook(book.id)?.progressPercent)
            assertNull(b.collections.getCollection(collection.id))
            assertTrue(server.shelves.getValue("alice").isEmpty())
        } }
    }

    @Test fun staleDeviceDoesNotOverwriteProgressOrRecreateRemovedBook() = runBlocking {
        val server = Server()
        Device(server).use { a -> Device(server).use { b ->
            a.sync.add(book); a.sync.progress(book.id, 10); a.sync.syncNow(); b.sync.syncNow()
            a.sync.progress(book.id, 70); a.sync.syncNow(); b.sync.syncNow()
            assertEquals(70, server.books.getValue("alice").getValue(book.id).progressPercent)
            server.books.getValue("alice").remove(book.id)
            b.sync.syncNow(); b.sync.syncNow()
            assertTrue(server.books.getValue("alice").isEmpty())
            assertNull(b.library.getLibraryBook(book.id))
        } }
    }

    @Test fun downloadPathSurvivesSyncButNeverTransfersToSecondDevice() = runBlocking {
        val server = Server()
        Device(server).use { a -> Device(server).use { b ->
            a.sync.add(book)
            a.library.setDownload(book.id, true, "/local/book.txt")
            a.sync.syncNow(); a.sync.syncNow(); b.sync.syncNow()
            assertEquals("/local/book.txt", a.library.getDownloadPath(book.id))
            assertTrue(a.library.getLibraryBook(book.id)!!.isDownloaded)
            assertFalse(b.library.getLibraryBook(book.id)!!.isDownloaded)
            assertNull(b.library.getDownloadPath(book.id))
        } }
    }

    @Test fun switchingAccountsKeepsOfflineOutboxesSeparate() = runBlocking {
        val server = Server()
        Device(server).use { a ->
            server.online = false
            a.sync.add(book); a.sync.progress(book.id, 37)
            a.sync.sessionChange { a.login("bob") }
            assertTrue(a.library.getLibraryBooks().isEmpty())
            server.online = true
            a.sync.syncNow()
            assertTrue(server.books.getValue("bob").isEmpty())
            a.sync.sessionChange { a.login("alice") }
            assertEquals(37, a.library.getLibraryBook(book.id)?.progressPercent)
            a.sync.syncNow()
            assertEquals(37, server.books.getValue("alice").getValue(book.id).progressPercent)
        }
    }

    @Test fun lostResponseCanBeRetriedWithoutDuplicateCollectionOrProgressLoss() = runBlocking {
        val server = Server()
        Device(server).use { a ->
            a.sync.add(book); a.sync.progress(book.id, 29)
            server.loseNextSaveResponse = true
            assertIs<AppResult.Error>(a.sync.syncNow())
            a.restart()
            assertIs<AppResult.Success<Unit>>(a.sync.syncNow())
            assertEquals(1, server.books.getValue("alice").size)
            assertEquals(29, server.books.getValue("alice").getValue(book.id).progressPercent)
        }
    }

    @Test fun legacyDataUploadsOnceAndExistingCloudProgressWins() = runBlocking {
        val server = Server()
        Device(server).use { a ->
            a.library.upsert(LibraryBook(book, progressPercent = 12), 1)
            a.collections.create(Collection("legacy", "Existing shelf", books = listOf(book)), 1)
            server.books["alice"] = mutableMapOf(book.id to LibraryBook(book, progressPercent = 75))
            a.sync.syncNow()
            assertEquals(75, a.library.getLibraryBook(book.id)?.progressPercent)
            assertEquals("Existing shelf", server.shelves.getValue("alice")["legacy"]?.title)
        }
    }

    @Test fun sameCollectionTitleOnTwoDevicesHasDifferentIds() = runBlocking {
        val server = Server()
        Device(server).use { a -> Device(server).use { b ->
            val first = (a.sync.create("Favorites") as AppResult.Success).data
            val second = (b.sync.create("Favorites") as AppResult.Success).data
            assertNotEquals(first.id, second.id)
            a.sync.syncNow(); b.sync.syncNow(); a.sync.syncNow()
            assertEquals(2, a.collections.getCollections().size)
        } }
    }
    @Test fun cancellationRetainsPendingWritesForRetry() = runBlocking {
        val server = Server()
        Device(server).use { device ->
            device.sync.add(book)
            val delegate = server.api(device.local)
            val entered = CompletableDeferred<Unit>()
            val blocked = object : ReadingDataApi by delegate {
                override suspend fun saveBook(book: LibraryBook) {
                    entered.complete(Unit)
                    awaitCancellation()
                }
            }
            val interrupted = ReadingDataSync(blocked, device.local, device.library, device.collections, device.scope)
            val job = launch { interrupted.syncNow() }
            entered.await()
            job.cancelAndJoin()
            device.restart()
            assertIs<AppResult.Success<Unit>>(device.sync.syncNow())
            assertEquals(book.id, server.books.getValue("alice").values.single().book.id)
        }
    }

    @Test fun deletingOneAccountDoesNotEraseAnotherAccountsPendingWork() = runBlocking {
        val server = Server()
        Device(server).use { device ->
            device.sync.add(book)
            device.sync.sessionChange { device.login("bob") }
            device.local.clearSession()
            device.library.removeAll()
            device.collections.deleteAll()
            device.local.clearUserData()
            device.login("alice")
            device.sync.syncNow()
            assertEquals(book.id, server.books.getValue("alice").values.single().book.id)
        }
    }

}
