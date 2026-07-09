package com.example.dz.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.book.GetHomeBooksUseCase
import com.example.dz.domain.usecase.library.GetContinueReadingUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeBooks: GetHomeBooksUseCase,
    private val getContinueReading: GetContinueReadingUseCase
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
            is HomeEvent.AuthorClicked -> emitEffect(HomeEffect.NavigateToAuthor(event.authorId))
            HomeEvent.KeepReadingClicked,
            HomeEvent.GoalsKeepReadingClicked -> emitEffect(
                HomeEffect.NavigateToReading(_uiState.value.continueReading?.book?.id ?: DEFAULT_BOOK_ID)
            )
            HomeEvent.NotificationsClicked -> emitEffect(HomeEffect.NavigateToNotifications)
            HomeEvent.ProfileClicked -> emitEffect(HomeEffect.NavigateToProfile)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val booksResult = getHomeBooks()
            val continueReadingResult = getContinueReading()

            val books = (booksResult as? AppResult.Success)?.data.orEmpty()
            val continueReading = (continueReadingResult as? AppResult.Success)?.data
            val error = listOf(booksResult, continueReadingResult)
                .firstNotNullOfOrNull { result ->
                    (result as? AppResult.Error)?.error?.toPresentationMessage()
                }

            _uiState.update {
                it.copy(
                    books = books,
                    continueReading = continueReading,
                    isLoading = false,
                    errorMessage = error
                )
            }
        }
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
