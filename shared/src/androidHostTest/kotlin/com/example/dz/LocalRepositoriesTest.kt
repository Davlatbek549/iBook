package com.example.dz

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.repository.LocalCollectionRepository
import com.example.dz.data.repository.LocalLibraryRepository
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
