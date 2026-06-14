package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Collection

interface CollectionRepository {
    suspend fun getCollections(): AppResult<List<Collection>>
    suspend fun getCollectionDetails(collectionId: String): AppResult<Collection>
    suspend fun createCollection(title: String): AppResult<Collection>
    suspend fun updateCollection(collection: Collection): AppResult<Collection>
    suspend fun deleteCollection(collectionId: String): AppResult<Unit>
}
