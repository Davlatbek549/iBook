package com.example.dz

import com.example.dz.data.local.LocalDataSourceImpl
import com.example.dz.data.repository.LocalSearchHistoryRepository
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The Recent chips on Search: newest first, one chip per search however it was capitalised, a
 * short list rather than an archive — and gone with the account it belongs to.
 */
class RecentSearchesTest {

    private fun history(local: FakeLocalDataSource = FakeLocalDataSource()) =
        LocalSearchHistoryRepository(local, io = Dispatchers.Unconfined)

    @Test
    fun `the latest search comes first`() = runBlocking {
        val history = history()

        history.saveSearch("strout")
        history.saveSearch("short stories")

        assertEquals(listOf("short stories", "strout"), history.getRecentSearches())
    }

    @Test
    fun `searching again moves a query to the front instead of adding a second chip`() = runBlocking {
        val history = history()

        history.saveSearch("Gothic")
        history.saveSearch("audio")
        history.saveSearch("  gothic ")

        assertEquals(listOf("gothic", "audio"), history.getRecentSearches())
    }

    @Test
    fun `only the most recent few are kept`() = runBlocking {
        val history = history()

        (1..10).forEach { history.saveSearch("query $it") }

        val recent = history.getRecentSearches()
        assertEquals(LocalSearchHistoryRepository.MAX_RECENT_SEARCHES, recent.size)
        assertEquals("query 10", recent.first())
    }

    @Test
    fun `a blank query is not a search`() = runBlocking {
        val history = history()

        history.saveSearch("   ")

        assertEquals(emptyList(), history.getRecentSearches())
    }

    @Test
    fun `a query with commas survives the round trip`() = runBlocking {
        val history = history()

        history.saveSearch("war, and peace")

        assertEquals(listOf("war, and peace"), history.getRecentSearches())
    }

    @Test
    fun `an unreadable stored value reads as no history rather than failing`() = runBlocking {
        val local = FakeLocalDataSource().apply {
            saveSetting(LocalSearchHistoryRepository.KEY, "not json")
        }

        assertEquals(emptyList(), history(local).getRecentSearches())
    }

    @Test
    fun `deleting the account erases its searches`() = runBlocking {
        val local = LocalDataSourceImpl(MapSettings())
        LocalSearchHistoryRepository(local, io = Dispatchers.Unconfined).saveSearch("dickens")

        local.clearUserData()

        assertEquals("", local.getSetting(LocalSearchHistoryRepository.KEY))
    }
}
