package com.example.dz.domain.usecase.collection

import com.example.dz.domain.repository.CollectionRepository

class CreateCollectionUseCase(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(title: String) = repository.createCollection(title)
}
