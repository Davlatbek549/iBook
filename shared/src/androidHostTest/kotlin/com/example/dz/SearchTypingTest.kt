package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.repository.LocalSearchHistoryRepository
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.BookRatings
import com.example.dz.domain.model.Category
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.LibraryRepository
import com.example.dz.domain.usecase.book.SearchBooksUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
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
        override suspend fun getCategoryBookCount(categoryId: String): AppResult<Int> = unused()
        override suspend fun getHomeBooks(): AppResult<List<Book>> = unused()
        override suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>> = unused()
        override suspend fun getBookDetails(bookId: String): AppResult<Book> = unused()
        override suspend fun getBookText(textUrl: String): AppResult<String> = unused()
    }

    /** A shelf with nothing on it; these tests are about typing, not about what is already owned. */
    private object EmptyLibrary : LibraryRepository {
        override suspend fun add(book: Book): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getLibraryBooks(): AppResult<List<LibraryBook>> = AppResult.Success(emptyList())
        override suspend fun getContinueReading(): AppResult<LibraryBook?> = AppResult.Success(null)
        override suspend fun updateReadingProgress(bookId: String, progressPercent: Int) =
            AppResult.Success(ReadingProgress(bookId = bookId, progressPercent = progressPercent))
    }

    private val history = LocalSearchHistoryRepository(FakeLocalDataSource(), io = dispatcher)

    private fun viewModel(repository: SlowSearchRepository) =
        SearchViewModel(
            SearchBooksUseCase(repository),
            GetLibraryBooksUseCase(EmptyLibrary),
            GetRecentSearchesUseCase(history),
            SaveRecentSearchUseCase(history),
        )

    /** What the screen sends on every keystroke. */
    private fun SearchViewModel.type(text: String) {
        onEvent(SearchEvent.QueryChanged(text))
        onEvent(SearchEvent.SearchClicked)
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
        assertEquals(listOf("result-for-dickens"), viewModel.uiState.value.books.map { it.id })
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

        assertEquals(listOf("result-for-dickens"), viewModel.uiState.value.books.map { it.id })
        assertNull(viewModel.uiState.value.errorMessage, "a cancelled search is not a failed one")
    }

    @Test
    fun `typing alone files nothing as a recent search`() = runTest(dispatcher) {
        val viewModel = viewModel(SlowSearchRepository())

        for (prefix in listOf("d", "di", "dic", "dick", "dicke", "dicken", "dickens")) {
            viewModel.type(prefix)
            testScheduler.advanceTimeBy(80)
        }
        testScheduler.advanceUntilIdle()

        // A pause in typing is not a decision; "dick" on the way to "dickens" is not a search
        // anyone would want offered back to them.
        assertEquals(emptyList(), viewModel.uiState.value.recentSearches)
    }

    @Test
    fun `opening a result files the query it came from`() = runTest(dispatcher) {
        val viewModel = viewModel(SlowSearchRepository())

        viewModel.type("dickens")
        testScheduler.advanceUntilIdle()
        viewModel.onEvent(SearchEvent.BookClicked("result-for-dickens"))
        testScheduler.advanceUntilIdle()

        assertEquals(listOf("dickens"), viewModel.uiState.value.recentSearches)
    }

    @Test
    fun `a recent search runs at once, without waiting for a pause`() = runTest(dispatcher) {
        val repository = SlowSearchRepository()
        val viewModel = viewModel(repository)

        viewModel.onEvent(SearchEvent.RecentSearchClicked("melville"))
        testScheduler.runCurrent() // no time allowed to pass: a tapped chip is not typing

        assertEquals(listOf("melville"), repository.searched)
        assertEquals("melville", viewModel.uiState.value.query)
    }

    @Test
    fun `clearing the box clears the results under it`() = runTest(dispatcher) {
        val viewModel = viewModel(SlowSearchRepository())

        viewModel.type("dickens")
        testScheduler.advanceUntilIdle()
        viewModel.type("")
        testScheduler.advanceUntilIdle()

        assertEquals(emptyList(), viewModel.uiState.value.books)
        assertNull(viewModel.uiState.value.resultsFor)
    }
}
