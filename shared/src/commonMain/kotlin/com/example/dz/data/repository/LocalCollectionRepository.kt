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
        val collection = Collection(
            id = title.trim().lowercase().replace(" ", "-").ifBlank { "collection" },
            title = title
        )
        collections.create(collection, createdAt = currentEpochMillis())
        return AppResult.Success(collection)
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
