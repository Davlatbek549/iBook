package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult

interface SearchHistoryRepository {
    /** Newest first, capped to a handful of chips. */
    suspend fun getRecentSearches(): AppResult<List<String>>

    /** Moves [query] to the front, deduping an earlier run of the same search. */
    suspend fun saveRecentSearch(query: String): AppResult<Unit>
}
