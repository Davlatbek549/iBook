package com.example.dz.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Friend
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.GetHomeBooksUseCase
import com.example.dz.domain.usecase.goal.GetReadingGoalUseCase
import com.example.dz.domain.usecase.library.GetContinueReadingUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.domain.usecase.social.GetFriendsUseCase
import com.example.dz.domain.usecase.user.GetProfileUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
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
    private val getReadingGoal: GetReadingGoalUseCase
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
            HomeEvent.ProfileClicked -> emitEffect(HomeEffect.NavigateToProfile)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val booksResult = getHomeBooks()
            val continueReadingResult = getContinueReading()
            val profileResult = getProfile()
            val friendsResult = getFriends()
            val libraryResult = getLibraryBooks()
            val categoriesResult = getCategories()
            val goalResult = getReadingGoal()

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
                    categories = (categoriesResult as? AppResult.Success)?.data.orEmpty(),
                    goal = (goalResult as? AppResult.Success)?.data,
                    userName = userName,
                    presence = presenceOf(friends),
                    isLoading = false,
                    errorMessage = error
                )
            }
        }
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
    }
}
