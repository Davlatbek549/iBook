package com.example.dz.domain.usecase.collection

import com.example.dz.domain.model.Collection
import com.example.dz.domain.repository.CollectionRepository

class UpdateCollectionUseCase(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(collection: Collection) = repository.updateCollection(collection)
}
