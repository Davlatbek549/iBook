package com.example.dz.domain.usecase.search

import com.example.dz.domain.repository.SearchHistoryRepository

class SaveRecentSearchUseCase(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke(query: String) = repository.saveSearch(query)
}
