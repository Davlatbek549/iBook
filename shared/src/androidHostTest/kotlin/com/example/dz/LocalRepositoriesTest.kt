package com.example.dz

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.repository.LocalCollectionRepository
import com.example.dz.data.repository.LocalLibraryRepository
import com.example.dz.data.repository.LocalReadingPositionRepository
import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.Author
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.LibraryBook
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Exercises [LocalLibraryRepository] and [LocalCollectionRepository] against a real (in-memory)
 * SQLDelight database, confirming they satisfy the repository contract on top of the SQLDelight
 * data sources — the same persistence guarantees a restarted app relies on.
 */
class LocalRepositoriesTest {

    private fun newDatabase(): DzDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DzDatabase.Schema.create(driver)
        return DzDatabase(driver)
    }

    private fun book(id: String, title: String) =
        Book(id = id, title = title, authors = listOf(Author(id = "a", name = "Jane Author")))

    @Test
    fun library_readsAndUpdatesGoThroughTheDatabase() = runBlocking {
        val database = newDatabase()
        val local = LibraryLocalDataSource(database)
        local.upsert(LibraryBook(book = book("b1", "First")), addedAt = 1L)
        val repository = LocalLibraryRepository(local)

        val books = repository.getLibraryBooks()
        assertTrue(books is AppResult.Success && books.data.single().book.id == "b1")

        val progress = repository.updateReadingProgress("b1", 150)
        assertTrue(progress is AppResult.Success)
        assertEquals(100, progress.data.progressPercent)
        assertEquals(100, local.getLibraryBook("b1")?.progressPercent)
    }

    @Test
    fun library_addShelvesABookAndProgressThenSticks() = runBlocking {
        val database = newDatabase()
        val local = LibraryLocalDataSource(database)
        val repository = LocalLibraryRepository(local)

        // This is the whole point: before the book is shelved there is no row to update, so the
        // progress a reader writes on every page turn goes nowhere.
        repository.updateReadingProgress("b1", 30)
        assertNull(local.getLibraryBook("b1"))

        repository.add(book("b1", "First"))
        repository.updateReadingProgress("b1", 30)
        assertEquals(30, local.getLibraryBook("b1")?.progressPercent)
    }

    @Test
    fun library_addLeavesAShelvedBookAlone() = runBlocking {
        val database = newDatabase()
        val local = LibraryLocalDataSource(database)
        val repository = LocalLibraryRepository(local)

        repository.add(book("b1", "First"))
        repository.updateReadingProgress("b1", 64)

        // Re-opening a book must not reset how far through it the reader is.
        repository.add(book("b1", "First"))
        assertEquals(64, local.getLibraryBook("b1")?.progressPercent)
    }

    @Test
    fun readingPosition_carriesOverAPlaceLeftByThePageCountingReader() {
        val local = FakeLocalDataSource()
        val positions = LocalReadingPositionRepository(local)

        // Written by the reader that cut every book into 1500-character pages.
        local.saveSetting("reader_page_b1", "5")

        assertEquals(6_000, positions.lastOffset("b1"), "four whole pages are behind them")

        // Once they read on, the offset is theirs and the old page number is ignored.
        positions.saveLastOffset("b1", 6_420)
        assertEquals(6_420, positions.lastOffset("b1"))
    }

    @Test
    fun readingPosition_startsAtTheBeginningWhenThereIsNoPlaceToGoBackTo() {
        val positions = LocalReadingPositionRepository(FakeLocalDataSource())

        assertEquals(0, positions.lastOffset("never-opened"))
        assertNull(positions.bookmark("never-opened"))
    }

    @Test
    fun collection_createGeneratesUniqueIdsForDuplicateTitles() = runBlocking {
        val local = CollectionLocalDataSource(newDatabase())
        val repository = LocalCollectionRepository(local)

        val first = repository.createCollection("Favorites")
        val second = repository.createCollection("Favorites")

        check(first is AppResult.Success && second is AppResult.Success)
        assertNotEquals(first.data.id, second.data.id)
        assertEquals(2, local.getCollections().size)
    }

    @Test
    fun collection_updateAndDeletePersistThroughTheRepository() = runBlocking {
        val local = CollectionLocalDataSource(newDatabase())
        val repository = LocalCollectionRepository(local)

        val created = repository.createCollection("Weekend Reads")
        check(created is AppResult.Success)
        val collectionId = created.data.id

        val updated = repository.updateCollection(
            created.data.copy(title = "Weekend", books = listOf(book("b1", "First")))
        )
        assertTrue(updated is AppResult.Success)
        assertEquals(listOf("First"), local.getCollection(collectionId)?.books?.map { it.title })

        repository.deleteCollection(collectionId)
        assertNull(local.getCollection(collectionId))
    }
}
