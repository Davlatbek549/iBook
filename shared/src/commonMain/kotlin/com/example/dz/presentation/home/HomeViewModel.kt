package com.example.dz.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Friend
import com.example.dz.domain.model.Category
import com.example.dz.domain.usecase.book.GetBooksByCategoryUseCase
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.GetHomeBooksUseCase
import com.example.dz.domain.usecase.goal.GetReadingGoalUseCase
import com.example.dz.domain.usecase.library.GetContinueReadingUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.domain.usecase.social.GetFriendsUseCase
import com.example.dz.domain.usecase.user.GetProfileUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeBooks: GetHomeBooksUseCase,
    private val getContinueReading: GetContinueReadingUseCase,
    private val getProfile: GetProfileUseCase,
    private val getFriends: GetFriendsUseCase,
    private val getLibraryBooks: GetLibraryBooksUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val getReadingGoal: GetReadingGoalUseCase,
    private val getBooksByCategory: GetBooksByCategoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.BookClicked -> emitEffect(HomeEffect.NavigateToBook(event.bookId))
            HomeEvent.KeepReadingClicked -> emitEffect(
                HomeEffect.NavigateToReading(_uiState.value.continueReading?.book?.id ?: DEFAULT_BOOK_ID)
            )
            HomeEvent.PresenceClicked -> emitEffect(HomeEffect.NavigateToFriends)
            HomeEvent.GoalClicked -> emitEffect(HomeEffect.NavigateToGoal)
            HomeEvent.Resumed -> refreshLocal()
            is HomeEvent.CategoryClicked -> emitEffect(HomeEffect.NavigateToCategory(event.categoryId))
            HomeEvent.ProfileClicked -> emitEffect(HomeEffect.NavigateToProfile)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // None of these depend on each other, and awaited one after another they stacked up
            // into a visibly slow Home — seven round trips before the first pixel changed. The only
            // ordering that survives is the genre shelves, which cannot start until the category
            // list has named the genres.
            coroutineScope {
            val booksAsync = async { getHomeBooks() }
            val continueReadingAsync = async { getContinueReading() }
            val profileAsync = async { getProfile() }
            val friendsAsync = async { getFriends() }
            val libraryAsync = async { getLibraryBooks() }
            val categoriesAsync = async { getCategories() }
            val goalAsync = async { getReadingGoal() }

            val booksResult = booksAsync.await()
            val continueReadingResult = continueReadingAsync.await()
            val profileResult = profileAsync.await()
            val friendsResult = friendsAsync.await()
            val libraryResult = libraryAsync.await()
            val categoriesResult = categoriesAsync.await()
            val goalResult = goalAsync.await()

            val categories = (categoriesResult as? AppResult.Success)?.data.orEmpty()
            val categoryShelves = shelvesFor(categories)

            val books = (booksResult as? AppResult.Success)?.data.orEmpty()
            val continueReading = (continueReadingResult as? AppResult.Success)?.data
            val friends = (friendsResult as? AppResult.Success)?.data.orEmpty()

            // Part-read books, minus whichever one the Keep going card already has. Showing the
            // same book twice, a card apart, reads as a bug rather than as emphasis.
            val shelf = (libraryResult as? AppResult.Success)?.data
                .orEmpty()
                .filter { it.progressPercent in 1..99 && it.book.id != continueReading?.book?.id }
            // Greeted by first name, as the design is: a full name is a heading that truncates
            // ("Davlatbek Ma…") where a first name fits and reads like a greeting.
            val userName = (profileResult as? AppResult.Success)?.data?.user?.name
                ?.trim()
                ?.substringBefore(' ')
                ?.takeIf { it.isNotEmpty() }

            // The shelf is the screen; a name or a presence figure that failed to load is a
            // quieter Home, not an error worth putting in front of the reader.
            val error = listOf(booksResult, continueReadingResult)
                .firstNotNullOfOrNull { result ->
                    (result as? AppResult.Error)?.error?.toPresentationMessage()
                }

            _uiState.update {
                it.copy(
                    books = books,
                    continueReading = continueReading,
                    shelf = shelf,
                    // The shelf carousel already leads with the first title, so the hero takes the
                    // one after it rather than showing the same cover twice.
                    editorsPick = books.getOrNull(1) ?: books.firstOrNull(),
                    friendsReading = friends.filter { it.currentBook != null },
                    categories = categories,
                    categoryShelves = categoryShelves,
                    goal = (goalResult as? AppResult.Success)?.data,
                    userName = userName,
                    presence = presenceOf(friends),
                    isLoading = false,
                    errorMessage = error
                )
            }
            }
        }
    }

    /**
     * A carousel per genre, fetched together rather than one after another.
     *
     * Only the first few genres get one: each is its own request, and a screen that opens with a
     * dozen of them in flight is slower than one that shows three and lets the tiles below carry
     * the rest. Genres that come back empty are dropped rather than drawn as a headed void.
     */
    private suspend fun shelvesFor(categories: List<Category>): List<CategoryShelf> = coroutineScope {
        categories.take(CATEGORY_SHELF_COUNT)
            .map { category ->
                async {
                    val books = (getBooksByCategory(category.id) as? AppResult.Success)?.data.orEmpty()
                    CategoryShelf(category = category, books = books.take(SHELF_BOOK_COUNT))
                }
            }
            .awaitAll()
            .filter { it.books.isNotEmpty() }
    }

    /**
     * Re-reads what reading changes, without going back to the network.
     *
     * The view model outlives a trip into the reader, so `init` alone left Home showing the
     * minutes it loaded on first open: read for ten minutes, come back, and the goal ring still
     * said zero.
     */
    private fun refreshLocal() {
        viewModelScope.launch {
            val continueReading = (getContinueReading() as? AppResult.Success)?.data
            val shelf = (getLibraryBooks() as? AppResult.Success)?.data
                .orEmpty()
                .filter { it.progressPercent in 1..99 && it.book.id != continueReading?.book?.id }

            _uiState.update {
                it.copy(
                    continueReading = continueReading,
                    shelf = shelf,
                    goal = (getReadingGoal() as? AppResult.Success)?.data ?: it.goal
                )
            }
        }
    }

    /**
     * The design's card reads "1,284 reading right now · Patricia is one of them" — an app-wide
     * presence figure. No endpoint serves one, so this counts the friends the app can see in a
     * book right now instead. Same card, a number the app can stand behind; swap the source when
     * there is a real presence feed.
     */
    private fun presenceOf(friends: List<Friend>): HomePresence? {
        val reading = friends.filter { it.isOnline && it.currentBook != null }
        val first = reading.firstOrNull() ?: return null
        return HomePresence(
            readerCount = reading.size,
            firstName = first.name.trim().substringBefore(' '),
        )
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    private companion object {
        const val DEFAULT_BOOK_ID = "current-book"

        /** Three genre carousels: enough to browse, few enough that Home still opens quickly. */
        const val CATEGORY_SHELF_COUNT = 3
        const val SHELF_BOOK_COUNT = 10
    }
}
