package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.domain.model.Collection
import com.example.dz.domain.repository.CollectionRepository

/**
 * Collection persistence backed by the local SQLDelight database ([CollectionLocalDataSource]),
 * replacing the in-memory fake. Membership and metadata survive app restarts.
 */
class LocalCollectionRepository(
    private val collections: CollectionLocalDataSource
) : CollectionRepository {

    override suspend fun getCollections(): AppResult<List<Collection>> =
        AppResult.Success(collections.getCollections())

    override suspend fun getCollectionDetails(collectionId: String): AppResult<Collection> =
        collections.getCollection(collectionId)
            ?.let { AppResult.Success(it) }
            ?: AppResult.Error(AppError.NotFound)

    override suspend fun createCollection(title: String): AppResult<Collection> {
        val slug = title.trim().lowercase().replace(" ", "-").ifBlank { "collection" }
        val collection = Collection(id = freeId(slug), title = title)
        collections.create(collection, createdAt = currentEpochMillis())
        return AppResult.Success(collection)
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

    override suspend fun updateCollection(collection: Collection): AppResult<Collection> {
        collections.update(collection)
        return AppResult.Success(collection)
    }

    override suspend fun deleteCollection(collectionId: String): AppResult<Unit> {
        collections.delete(collectionId)
        return AppResult.Success(Unit)
    }
}
