package com.example.dz.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.collection.GetCollectionsUseCase
import com.example.dz.domain.usecase.library.GetContinueReadingUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.domain.usecase.library.UpdateReadingProgressUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val getLibraryBooks: GetLibraryBooksUseCase,
    private val getContinueReading: GetContinueReadingUseCase,
    private val updateReadingProgress: UpdateReadingProgressUseCase,
    private val getCollections: GetCollectionsUseCase
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
            is LibraryEvent.FilterSelected -> _uiState.update { it.copy(filter = event.filter) }
            is LibraryEvent.CollectionClicked ->
                emitEffect(LibraryEffect.NavigateToCollection(event.collectionId))
            LibraryEvent.CollectionsClicked -> emitEffect(LibraryEffect.NavigateToCollections)
            LibraryEvent.SearchClicked -> emitEffect(LibraryEffect.NavigateToSearch)
            LibraryEvent.GoalClicked -> emitEffect(LibraryEffect.NavigateToGoal)
            // Reading happens on another screen, and finishing a book moves it between shelves.
            LibraryEvent.Resumed -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val (booksResult, continueReadingResult, collectionsResult) = coroutineScope {
                val books = async { getLibraryBooks() }
                val continueReading = async { getContinueReading() }
                val collections = async { getCollections() }
                Triple(books.await(), continueReading.await(), collections.await())
            }

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
                    // Shelves the reader built themselves; an empty list just hides the section.
                    collections = (collectionsResult as? AppResult.Success)?.data.orEmpty(),
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
