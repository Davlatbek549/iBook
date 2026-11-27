package com.example.dz.domain.repository

/**
 * The queries a reader has searched for on this device, newest first — the Recent chips on Search.
 *
 * Plain lists rather than results: this is a few strings in local settings, and a Search screen
 * that could not read them has nothing to say about it beyond showing no chips.
 */
interface SearchHistoryRepository {
    suspend fun getRecentSearches(): List<String>

    /** Puts [query] at the front, dropping any earlier copy of it, and returns the list that results. */
    suspend fun saveSearch(query: String): List<String>
}
