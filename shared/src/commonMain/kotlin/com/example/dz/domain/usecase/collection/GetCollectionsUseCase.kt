package com.example.dz.domain.usecase.collection

import com.example.dz.domain.repository.CollectionRepository

class GetCollectionsUseCase(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke() = repository.getCollections()
}
