package com.example.dz.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.library.GetContinueReadingUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.domain.usecase.library.UpdateReadingProgressUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val getLibraryBooks: GetLibraryBooksUseCase,
    private val getContinueReading: GetContinueReadingUseCase,
    private val updateReadingProgress: UpdateReadingProgressUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LibraryEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.BookClicked -> emitEffect(LibraryEffect.NavigateToBook(event.bookId))
            is LibraryEvent.ProgressChanged -> updateProgress(event.bookId, event.progressPercent)
            LibraryEvent.GoalClicked -> emitEffect(LibraryEffect.NavigateToGoal)
            LibraryEvent.SortClicked -> emitEffect(LibraryEffect.OpenSort)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val booksResult = getLibraryBooks()
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

    private fun updateProgress(bookId: String, progressPercent: Int) {
        viewModelScope.launch {
            when (val result = updateReadingProgress(bookId, progressPercent)) {
                is AppResult.Success -> load()
                is AppResult.Error -> _uiState.update {
                    it.copy(errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: LibraryEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
