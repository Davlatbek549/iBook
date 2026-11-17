package com.example.dz.domain.usecase.search

import com.example.dz.domain.repository.SearchHistoryRepository

class GetRecentSearchesUseCase(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke() = repository.getRecentSearches()
}
