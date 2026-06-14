package com.example.dz.domain.usecase.collection

import com.example.dz.domain.repository.CollectionRepository

class DeleteCollectionUseCase(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(collectionId: String) = repository.deleteCollection(collectionId)
}
