package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.domain.model.Collection
import com.example.dz.domain.repository.CollectionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Collection persistence backed by the local SQLDelight database ([CollectionLocalDataSource]),
 * replacing the in-memory fake. Membership and metadata survive app restarts.
 *
 * Every query runs on [io], for the same reason as the library: the data source is synchronous
 * and its callers sit on the main thread.
 */
class LocalCollectionRepository(
    private val collections: CollectionLocalDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : CollectionRepository {

    override suspend fun getCollections(): AppResult<List<Collection>> = withContext(io) {
        AppResult.Success(collections.getCollections())
    }

    override suspend fun getCollectionDetails(collectionId: String): AppResult<Collection> =
        withContext(io) {
            collections.getCollection(collectionId)
                ?.let { AppResult.Success(it) }
                ?: AppResult.Error(AppError.NotFound)
        }

    override suspend fun createCollection(title: String): AppResult<Collection> = withContext(io) {
        val slug = title.trim().lowercase().replace(" ", "-").ifBlank { "collection" }
        val collection = Collection(id = freeId(slug), title = title)
        collections.create(collection, createdAt = currentEpochMillis())
        AppResult.Success(collection)
    }

    /**
     * Nothing stops two collections sharing a title, so the slug on its own cannot be the key —
     * a second "Favorites" used to fail on the primary key. Suffixed rather than randomised so a
     * stored row still reads as the collection it belongs to.
     */
    private fun freeId(slug: String): String {
        if (collections.getCollection(slug) == null) return slug
        var suffix = 2
        while (collections.getCollection("$slug-$suffix") != null) suffix++
        return "$slug-$suffix"
    }

    override suspend fun updateCollection(collection: Collection): AppResult<Collection> =
        withContext(io) {
            collections.update(collection)
            AppResult.Success(collection)
        }

    override suspend fun deleteCollection(collectionId: String): AppResult<Unit> = withContext(io) {
        collections.delete(collectionId)
        AppResult.Success(Unit)
    }
}
