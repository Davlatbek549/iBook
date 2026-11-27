package com.example.dz.data.repository

import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

/**
 * Recent searches, kept as one JSON array in local settings.
 *
 * JSON rather than a joined string because a query is free text — "war, and peace" has the comma a
 * separator would split on. The key starts with `search_`, which is one of the per-account prefixes
 * [com.example.dz.data.local.LocalDataSourceImpl] erases when an account is deleted, so one reader's
 * searches are not left on the device for the next.
 */
class LocalSearchHistoryRepository(
    private val local: LocalDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : SearchHistoryRepository {

    override suspend fun getRecentSearches(): List<String> = withContext(io) { read() }

    override suspend fun saveSearch(query: String): List<String> = withContext(io) {
        val cleaned = query.trim().replace(WHITESPACE, " ")
        if (cleaned.isEmpty()) return@withContext read()

        // The same words typed in a different case are the same search, and would otherwise sit
        // side by side as two chips; the latest spelling is the one kept.
        val updated = (listOf(cleaned) + read().filterNot { it.equals(cleaned, ignoreCase = true) })
            .take(MAX_RECENT_SEARCHES)
        local.saveSetting(KEY, json.encodeToString(serializer, updated))
        updated
    }

    /** A value that is missing or unreadable is no history, not a crash on opening Search. */
    private fun read(): List<String> {
        val stored = local.getSetting(KEY, "")
        if (stored.isBlank()) return emptyList()
        return runCatching { json.decodeFromString(serializer, stored) }.getOrDefault(emptyList())
    }

    companion object {
        const val KEY = "search_recent"

        /** Enough to fill two rows of chips on a phone; older than that is rarely what someone wants back. */
        const val MAX_RECENT_SEARCHES = 6

        private val WHITESPACE = Regex("\\s+")
        private val serializer = ListSerializer(String.serializer())
        private val json = Json { ignoreUnknownKeys = true }
    }
}
