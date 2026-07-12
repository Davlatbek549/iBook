package com.example.dz

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.local.file.FileStorage
import com.example.dz.data.repository.DownloadRepositoryImpl
import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import com.example.dz.domain.repository.BookRepository
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DownloadRepositoryTest {

    private fun newLibrary(): LibraryLocalDataSource {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DzDatabase.Schema.create(driver)
        return LibraryLocalDataSource(DzDatabase(driver))
    }

    private fun repo(
        library: LibraryLocalDataSource,
        storage: FileStorage,
        book: Book? = Book(id = "b1", title = "Offline Book", textUrl = "https://example.org/b1.txt"),
        text: String = "Body text."
    ) = DownloadRepositoryImpl(FakeBookRepository(book, text), storage, library)

    @Test
    fun downloadBook_savesFileAndRecordsInLibrary() = runBlocking {
        val library = newLibrary()
        val storage = FakeFileStorage()
        val result = repo(library, storage, text = "Hello offline").downloadBook("b1")

        assertTrue(result is AppResult.Success, "got $result")
        assertEquals(1, storage.files.size)
        assertTrue(storage.files.values.single() == "Hello offline")
        val entry = library.getLibraryBook("b1")
        assertTrue(entry?.isDownloaded == true)
        assertTrue(library.getDownloadPath("b1") != null)
        Unit
    }

    @Test
    fun getDownloadedContent_returnsStoredTitleAndText() = runBlocking {
        val library = newLibrary()
        val storage = FakeFileStorage()
        val download = repo(library, storage, text = "Chapter one.")
        download.downloadBook("b1")

        val content = download.getDownloadedContent("b1")
        assertEquals("Offline Book", content?.title)
        assertEquals("Chapter one.", content?.text)
        assertTrue(download.isDownloaded("b1"))
        Unit
    }

    @Test
    fun deleteDownload_removesFileAndClearsRecord() = runBlocking {
        val library = newLibrary()
        val storage = FakeFileStorage()
        val download = repo(library, storage)
        download.downloadBook("b1")

        download.deleteDownload("b1")

        assertTrue(storage.files.isEmpty())
        assertFalse(download.isDownloaded("b1"))
        assertNull(library.getDownloadPath("b1"))
        assertNull(download.getDownloadedContent("b1"))
        Unit
    }

    @Test
    fun downloadBook_withoutTextUrl_returnsNotFound() = runBlocking {
        val library = newLibrary()
        val book = Book(id = "b1", title = "No text", textUrl = null)
        val result = repo(library, FakeFileStorage(), book = book).downloadBook("b1")

        assertTrue(result is AppResult.Error)
        assertEquals(AppError.NotFound, result.error)
        Unit
    }
}

/** In-memory [FileStorage] keyed by a synthetic path. */
private class FakeFileStorage : FileStorage {
    val files = mutableMapOf<String, String>()
    override fun save(bookId: String, text: String): String {
        val path = "/mem/$bookId.txt"
        files[path] = text
        return path
    }
    override fun read(path: String): String? = files[path]
    override fun delete(path: String): Boolean {
        files.remove(path)
        return true
    }
    override fun exists(path: String): Boolean = files.containsKey(path)
}

/** Minimal [BookRepository] returning a fixed book + text. */
private class FakeBookRepository(private val book: Book?, private val text: String) : BookRepository {
    override suspend fun searchBooks(query: String): AppResult<List<Book>> = AppResult.Success(emptyList())
    override suspend fun getHomeBooks(): AppResult<List<Book>> = AppResult.Success(emptyList())
    override suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>> = AppResult.Success(emptyList())
    override suspend fun getBookDetails(bookId: String): AppResult<Book> =
        book?.let { AppResult.Success(it) } ?: AppResult.Error(AppError.NotFound)
    override suspend fun getCategories(): AppResult<List<Category>> = AppResult.Success(emptyList())
    override suspend fun getBookText(textUrl: String): AppResult<String> = AppResult.Success(text)
}
