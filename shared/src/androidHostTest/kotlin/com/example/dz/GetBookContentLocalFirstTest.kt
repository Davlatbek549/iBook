package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import com.example.dz.domain.model.DownloadedContent
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.DownloadRepository
import com.example.dz.domain.usecase.book.GetBookContentUseCase
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetBookContentLocalFirstTest {

    @Test
    fun usesLocalContentWhenDownloaded_withoutHittingNetwork() = runBlocking {
        val network = RecordingBookRepository()
        val downloads = StubDownloadRepository(
            DownloadedContent("b1", "Offline Title", "Local paragraph one.\n\nLocal paragraph two.")
        )
        val useCase = GetBookContentUseCase(network, downloads)

        val result = useCase("b1")

        assertTrue(result is AppResult.Success, "got $result")
        assertEquals("Offline Title", result.data.title)
        assertTrue(result.data.pages.isNotEmpty())
        assertFalse(network.textFetched, "should not fetch from network when downloaded")
        Unit
    }

    @Test
    fun fallsBackToNetworkWhenNotDownloaded() = runBlocking {
        val network = RecordingBookRepository(
            book = Book(id = "b1", title = "Online Title", textUrl = "https://example.org/b1.txt"),
            text = "Remote paragraph."
        )
        val useCase = GetBookContentUseCase(network, StubDownloadRepository(null))

        val result = useCase("b1")

        assertTrue(result is AppResult.Success, "got $result")
        assertEquals("Online Title", result.data.title)
        assertTrue(network.textFetched, "should fetch from network when not downloaded")
        Unit
    }
}

private class StubDownloadRepository(private val content: DownloadedContent?) : DownloadRepository {
    override suspend fun downloadBook(bookId: String) = AppResult.Success(Unit)
    override suspend fun deleteDownload(bookId: String) = AppResult.Success(Unit)
    override suspend fun getDownloadedContent(bookId: String): DownloadedContent? = content
    override suspend fun isDownloaded(bookId: String): Boolean = content != null
}

private class RecordingBookRepository(
    private val book: Book = Book(id = "b1", title = "Online Title", textUrl = "https://example.org/b1.txt"),
    private val text: String = "Remote paragraph."
) : BookRepository {
    var textFetched = false
        private set

    override suspend fun searchBooks(query: String): AppResult<List<Book>> = AppResult.Success(emptyList())
    override suspend fun getHomeBooks(): AppResult<List<Book>> = AppResult.Success(emptyList())
    override suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>> = AppResult.Success(emptyList())
    override suspend fun getBookDetails(bookId: String): AppResult<Book> = AppResult.Success(book)
    override suspend fun getCategories(): AppResult<List<Category>> = AppResult.Success(emptyList())
    override suspend fun getBookText(textUrl: String): AppResult<String> {
        textFetched = true
        return AppResult.Success(text)
    }
}
