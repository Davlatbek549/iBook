package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.Collection
import com.example.dz.domain.repository.CollectionRepository

class FakeCollectionRepository : CollectionRepository {
    override suspend fun getCollections(): AppResult<List<Collection>> =
        AppResult.Success(FakeData.collections)

    override suspend fun getCollectionDetails(collectionId: String): AppResult<Collection> =
        FakeData.collections.firstOrNull { it.id == collectionId }
            ?.let { AppResult.Success(it) }
            ?: AppResult.Error(AppError.NotFound)

    override suspend fun createCollection(title: String): AppResult<Collection> =
        AppResult.Success(
            Collection(
                id = title.trim().lowercase().replace(" ", "-").ifBlank { "collection" },
                title = title
            )
        )

    override suspend fun updateCollection(collection: Collection): AppResult<Collection> =
        AppResult.Success(collection)

    override suspend fun deleteCollection(collectionId: String): AppResult<Unit> = AppResult.Success(Unit)
}
