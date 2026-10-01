package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.BookRatings
import com.example.dz.domain.model.Category
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.GetCategoryBookCountUseCase
import com.example.dz.presentation.browse.BrowseEvent
import com.example.dz.presentation.browse.BrowseMood
import com.example.dz.presentation.browse.BrowseSort
import com.example.dz.presentation.browse.BrowseUiState
import com.example.dz.presentation.browse.BrowseViewModel
import com.example.dz.presentation.browse.withThousands
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** Browse: which genres a mood narrows to, how A–Z orders them, and what a tile says it holds. */
@OptIn(ExperimentalCoroutinesApi::class)
class BrowseTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    private val genres = listOf(
        Category(id = "gothic-fiction", name = "Gothic"),
        Category(id = "cooking", name = "Cooking"),
        Category(id = "horror", name = "Horror"),
        Category(id = "poetry", name = "Poetry"),
        Category(id = "business", name = "Business"),
    )

    /** Counts per genre; a genre missing from [counts] fails, as a request that errored would. */
    private class CountingRepository(
        private val categories: List<Category>,
        private val counts: Map<String, Int>,
    ) : BookRepository {
        override suspend fun getCategories(): AppResult<List<Category>> = AppResult.Success(categories)
        override suspend fun getCategoryBookCount(categoryId: String): AppResult<Int> =
            counts[categoryId]?.let { AppResult.Success(it) } ?: AppResult.Error(AppError.Network)

        private fun <T> unused(): AppResult<T> = AppResult.Error(AppError.NotFound)
        override suspend fun searchBooks(query: String): AppResult<List<Book>> = unused()
        override suspend fun getHomeBooks(): AppResult<List<Book>> = unused()
        override suspend fun getBooksByCategory(categoryId: String): AppResult<List<Book>> = unused()
        override suspend fun getBookDetails(bookId: String): AppResult<Book> = unused()
        override suspend fun getBookRatings(bookId: String): AppResult<BookRatings?> = unused()
        override suspend fun getBookText(textUrl: String): AppResult<String> = unused()
    }

    private fun viewModel(counts: Map<String, Int> = emptyMap()): BrowseViewModel {
        val repository = CountingRepository(genres, counts)
        return BrowseViewModel(GetCategoriesUseCase(repository), GetCategoryBookCountUseCase(repository))
    }

    @Test
    fun `a mood narrows the grid to its genres, in catalogue order`() {
        val state = BrowseUiState(categories = genres, mood = BrowseMood.UNSETTLING)

        assertEquals(listOf("Gothic", "Horror"), state.shownCategories.map { it.name })
    }

    @Test
    fun `A to Z orders whatever the mood left`() {
        val state = BrowseUiState(categories = genres, mood = BrowseMood.COSY, sort = BrowseSort.ALPHABETICAL)

        assertEquals(listOf("Cooking", "Poetry"), state.shownCategories.map { it.name })
    }

    @Test
    fun `choosing the chosen mood again goes back to every genre`() = runTest(dispatcher) {
        val viewModel = viewModel()
        testScheduler.advanceUntilIdle()

        viewModel.onEvent(BrowseEvent.MoodClicked(BrowseMood.SHORT))
        assertEquals(BrowseMood.SHORT, viewModel.uiState.value.mood)

        viewModel.onEvent(BrowseEvent.MoodClicked(BrowseMood.SHORT))
        assertNull(viewModel.uiState.value.mood)
        assertEquals(genres, viewModel.uiState.value.shownCategories)
    }

    @Test
    fun `A to Z is a switch`() = runTest(dispatcher) {
        val viewModel = viewModel()

        viewModel.onEvent(BrowseEvent.SortClicked)
        assertEquals(BrowseSort.ALPHABETICAL, viewModel.uiState.value.sort)

        viewModel.onEvent(BrowseEvent.SortClicked)
        assertEquals(BrowseSort.CATALOGUE, viewModel.uiState.value.sort)
    }

    @Test
    fun `a tile only claims a count the catalogue actually gave`() = runTest(dispatcher) {
        // Horror answers zero and Poetry fails; neither should read as "0 books".
        val viewModel = viewModel(counts = mapOf("gothic-fiction" to 2140, "cooking" to 1041, "horror" to 0))
        testScheduler.advanceUntilIdle()

        assertEquals(
            mapOf("gothic-fiction" to 2140, "cooking" to 1041),
            viewModel.uiState.value.bookCounts
        )
    }

    @Test
    fun `counts are written the way the frame writes them`() {
        assertEquals("864", 864.withThousands())
        assertEquals("2,140", 2140.withThousands())
        assertEquals("1,234,567", 1234567.withThousands())
    }
}
