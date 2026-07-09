package com.example.dz

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.Author
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Exercises the SQLDelight schema and local data sources end-to-end against an in-memory database,
 * validating table definitions, queries, and row<->domain mapping.
 */
class DatabaseDataSourceTest {

    private fun newDatabase(): DzDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DzDatabase.Schema.create(driver)
        return DzDatabase(driver)
    }

    private fun book(id: String, title: String) =
        Book(id = id, title = title, authors = listOf(Author(id = "a", name = "Jane Author")))

    @Test
    fun library_upsertReadUpdateAndDelete() {
        val local = LibraryLocalDataSource(newDatabase())
        local.upsert(LibraryBook(book = book("b1", "First"), isDownloaded = true), addedAt = 1L)

        val stored = local.getLibraryBook("b1")
        assertEquals("First", stored?.book?.title)
        assertEquals("Jane Author", stored?.book?.authors?.firstOrNull()?.name)
        assertTrue(stored?.isDownloaded == true)

        local.updateProgress("b1", 42)
        assertEquals(42, local.getLibraryBook("b1")?.progressPercent)

        local.remove("b1")
        assertNull(local.getLibraryBook("b1"))
        assertTrue(local.getLibraryBooks().isEmpty())
    }

    @Test
    fun library_continueReadingReturnsInProgressBook() {
        val local = LibraryLocalDataSource(newDatabase())
        local.upsert(LibraryBook(book = book("done", "Done")), addedAt = 1L)
        local.updateProgress("done", 100)
        local.upsert(LibraryBook(book = book("reading", "Reading")), addedAt = 2L)
        local.updateProgress("reading", 30)

        assertEquals("reading", local.getContinueReading()?.book?.id)
    }

    @Test
    fun collection_createWithBooksReadAndDelete() {
        val local = CollectionLocalDataSource(newDatabase())
        val collection = Collection(
            id = "c1",
            title = "Favorites",
            description = "My picks",
            books = listOf(book("b1", "First"), book("b2", "Second"))
        )
        local.create(collection, createdAt = 1L)

        val stored = local.getCollection("c1")
        assertEquals("Favorites", stored?.title)
        assertEquals(listOf("First", "Second"), stored?.books?.map { it.title })

        local.delete("c1")
        assertNull(local.getCollection("c1"))
        assertTrue(local.getCollections().isEmpty())
    }

    @Test
    fun collection_updateReplacesMetadataAndBooks() {
        val local = CollectionLocalDataSource(newDatabase())
        local.create(Collection(id = "c1", title = "Old", books = listOf(book("b1", "First"))), createdAt = 1L)

        local.update(Collection(id = "c1", title = "New", description = "d", books = listOf(book("b2", "Second"))))

        val stored = local.getCollection("c1")
        assertEquals("New", stored?.title)
        assertEquals(listOf("Second"), stored?.books?.map { it.title })
    }
}
