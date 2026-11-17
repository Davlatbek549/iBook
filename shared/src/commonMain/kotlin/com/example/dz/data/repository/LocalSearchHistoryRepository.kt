package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Recent search queries, newest first, as one pipe-joined setting — there is no list-valued
 * storage here, and a handful of short strings doesn't need one.
 */
class LocalSearchHistoryRepository(
    private val local: LocalDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : SearchHistoryRepository {

    override suspend fun getRecentSearches(): AppResult<List<String>> = withContext(io) {
        AppResult.Success(readAll())
    }

    override suspend fun saveRecentSearch(query: String): AppResult<Unit> = withContext(io) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return@withContext AppResult.Success(Unit)

        val updated = listOf(trimmed) + readAll().filterNot { it.equals(trimmed, ignoreCase = true) }
        local.saveSetting(KEY, updated.take(MAX_ENTRIES).joinToString(SEPARATOR))
        AppResult.Success(Unit)
    }

    private fun readAll(): List<String> =
        local.getSetting(KEY, "")
            .split(SEPARATOR)
            .map { it.trim() }
            .filter { it.isNotBlank() }

    private companion object {
        const val KEY = "recent_searches"
        const val SEPARATOR = "|"
        const val MAX_ENTRIES = 8
    }
}
