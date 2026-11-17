package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.BookRatings
import com.example.dz.domain.model.Category
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.LibraryRepository
import com.example.dz.domain.repository.SearchHistoryRepository
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.domain.usecase.book.SearchBooksUseCase
import com.example.dz.domain.usecase.search.GetRecentSearchesUseCase
import com.example.dz.domain.usecase.search.SaveRecentSearchUseCase
import com.example.dz.presentation.search.SearchEvent
import com.example.dz.presentation.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * The search field fires a search on every keystroke. These pin what typing a word must come to:
 * one search for the word, and its results — never the answer to an earlier, shorter query that
 * happened to arrive last.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SearchTypingTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    /** Answers each query after [latencyFor] it, recording every query it was asked. */
    private class SlowSearchRepository(
        private val latencyFor: (String) -> Long = { 0L },
    ) : BookRepository {
        val searched = mutableListOf<String>()

        override suspend fun searchBooks(query: String): AppResult<List<Book>> {
            searched += query
            delay(latencyFor(query))
            return AppResult.Success(listOf(Book(id = "result-for-$query", title = query)))
        }

        override suspend fun getBookRatings(bookId: String): AppResult<BookRatings?> = AppResult.Success(null)

        override suspend fun getCategories(): AppResult<List<Category>> = AppResult.Success(emptyList())

        private fun <T> unused(): AppResult<T> = AppResult.Error(AppError.NotFound)
        override suspend fun getHomeBooks(): AppResult<List<Book>> = unused()
        override suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>> = unused()
        override suspend fun getBookDetails(bookId: String): AppResult<Book> = unused()
        override suspend fun getBookText(textUrl: String): AppResult<String> = unused()
    }

    private object StubSearchHistoryRepository : SearchHistoryRepository {
        override suspend fun getRecentSearches(): AppResult<List<String>> = AppResult.Success(emptyList())
        override suspend fun saveRecentSearch(query: String): AppResult<Unit> = AppResult.Success(Unit)
    }

    private object StubLibraryRepository : LibraryRepository {
        override suspend fun add(book: Book): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getLibraryBooks(): AppResult<List<LibraryBook>> = AppResult.Success(emptyList())
        override suspend fun getContinueReading(): AppResult<LibraryBook?> = AppResult.Success(null)
        override suspend fun updateReadingProgress(bookId: String, progressPercent: Int): AppResult<ReadingProgress> =
            AppResult.Error(AppError.NotFound)
    }

    private fun viewModel(repository: SlowSearchRepository) =
        SearchViewModel(
            SearchBooksUseCase(repository),
            GetRecentSearchesUseCase(StubSearchHistoryRepository),
            SaveRecentSearchUseCase(StubSearchHistoryRepository),
            GetLibraryBooksUseCase(StubLibraryRepository),
        )

    /** What the field sends on every keystroke — a submit is a separate, explicit event. */
    private fun SearchViewModel.type(text: String) {
        onEvent(SearchEvent.QueryChanged(text))
    }

    @Test
    fun `a word typed quickly is searched once, for the whole word`() = runTest(dispatcher) {
        val repository = SlowSearchRepository()
        val viewModel = viewModel(repository)

        for (prefix in listOf("d", "di", "dic", "dick", "dicke", "dicken", "dickens")) {
            viewModel.type(prefix)
            testScheduler.advanceTimeBy(80) // a quick typist, well inside the pause
        }
        testScheduler.advanceUntilIdle()

        assertEquals(listOf("dickens"), repository.searched, "one request per letter is waste")
        assertEquals(listOf("result-for-dickens"), viewModel.uiState.value.results.map { it.id })
    }

    @Test
    fun `a slow answer to an earlier query never replaces the current one`() = runTest(dispatcher) {
        // Exactly what happened on the simulator: "dick" answered after "dickens" and stayed.
        val repository = SlowSearchRepository(latencyFor = { if (it == "dick") 5_000 else 100 })
        val viewModel = viewModel(repository)

        viewModel.type("dick")
        testScheduler.advanceTimeBy(1_000) // past the pause: the "dick" request is now in flight
        viewModel.type("dickens")
        testScheduler.advanceUntilIdle()   // long enough for both to have answered

        assertEquals(listOf("result-for-dickens"), viewModel.uiState.value.results.map { it.id })
        assertNull(viewModel.uiState.value.errorMessage, "a cancelled search is not a failed one")
    }
}
