package com.example.dz

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.repository.LocalLibraryRepository
import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import com.example.dz.domain.model.DownloadedContent
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.DownloadRepository
import com.example.dz.domain.usecase.book.GetBookContentUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * The work that used to freeze the screen — splitting a book into pages, reading the database —
 * has to happen on a dispatcher the caller did not come from. Callers are view models on the main
 * thread, so anything left on the caller's thread is drawn late.
 *
 * These pin that the switch happens, rather than how long the work takes: a regression here is
 * someone removing a `withContext`, and it would pass every other test.
 */
class OffMainThreadTest {

    /** Runs what it is handed straight away, counting how often it was asked to. */
    private class RecordingDispatcher : CoroutineDispatcher() {
        var dispatches = 0
            private set

        override fun dispatch(context: CoroutineContext, block: Runnable) {
            dispatches++
            block.run()
        }
    }

    @Test
    fun `a book is split into pages on the text dispatcher, not the caller's`() = runBlocking {
        val textWork = RecordingDispatcher()
        val downloads = object : DownloadRepository {
            override suspend fun downloadBook(bookId: String) = AppResult.Success(Unit)
            override suspend fun deleteDownload(bookId: String) = AppResult.Success(Unit)
            override suspend fun isDownloaded(bookId: String) = true
            override suspend fun getDownloadedContent(bookId: String) =
                DownloadedContent("b1", "A Novel", "First paragraph.\n\nSecond paragraph.")
        }
        val useCase = GetBookContentUseCase(
            repository = UnusedBookRepository,
            downloadRepository = downloads,
            textWork = textWork,
        )

        val result = useCase("b1")

        assertTrue(result is AppResult.Success, "got $result")
        assertTrue(textWork.dispatches > 0, "pagination ran on the caller's thread")
    }

    @Test
    fun `the library is read on the io dispatcher, not the caller's`() = runBlocking {
        val io = RecordingDispatcher()
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DzDatabase.Schema.create(driver)
        val repository = LocalLibraryRepository(LibraryLocalDataSource(DzDatabase(driver)), io = io)

        repository.getLibraryBooks()

        assertTrue(io.dispatches > 0, "the query ran on the caller's thread")
    }
}

/** The downloaded copy is served first, so the network is never reached in these tests. */
private object UnusedBookRepository : BookRepository {
    private fun <T> unused(): AppResult<T> = AppResult.Error(AppError.NotFound)
    override suspend fun searchBooks(query: String): AppResult<List<Book>> = unused()
    override suspend fun getHomeBooks(): AppResult<List<Book>> = unused()
    override suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>> = unused()
    override suspend fun getBookDetails(bookId: String): AppResult<Book> = unused()
    override suspend fun getCategories(): AppResult<List<Category>> = unused()
    override suspend fun getBookText(textUrl: String): AppResult<String> = unused()
}
