package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.domain.model.Collection
import com.example.dz.domain.repository.CollectionRepository

/**
 * Persists user collections and their book membership via SQLDelight, so they survive an app
 * restart. Updating or deleting a collection's books goes through [updateCollection] — the caller
 * passes the full [Collection] (with its desired [Collection.books]) and membership is replaced
 * atomically by [CollectionLocalDataSource.update].
 */
class LocalCollectionRepository(
    private val local: CollectionLocalDataSource
) : CollectionRepository {
    override suspend fun getCollections(): AppResult<List<Collection>> =
        AppResult.Success(local.getCollections())

    override suspend fun getCollectionDetails(collectionId: String): AppResult<Collection> =
        local.getCollection(collectionId)
            ?.let { AppResult.Success(it) }
            ?: AppResult.Error(AppError.NotFound)

    override suspend fun createCollection(title: String): AppResult<Collection> {
        val createdAt = currentEpochMillis()
        val collection = Collection(id = "${slugify(title)}-$createdAt", title = title)
        local.create(collection, createdAt)
        return AppResult.Success(collection)
    }

    override suspend fun updateCollection(collection: Collection): AppResult<Collection> {
        local.update(collection)
        return AppResult.Success(collection)
    }

    override suspend fun deleteCollection(collectionId: String): AppResult<Unit> {
        local.delete(collectionId)
        return AppResult.Success(Unit)
    }
}

/** Readable id prefix; the caller appends a timestamp so two collections with the same title don't collide. */
private fun slugify(title: String): String =
    title.trim().lowercase().replace(" ", "-").ifBlank { "collection" }
