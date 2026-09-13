package com.example.dz.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.SearchBooksUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * How long typing has to pause before a search goes out. Long enough that a word typed at speed
 * costs one request rather than one per letter; short enough not to feel like waiting.
 */
private const val SEARCH_DEBOUNCE_MILLIS = 300L

class SearchViewModel(
    private val getCategories: GetCategoriesUseCase,
    private val searchBooks: SearchBooksUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SearchEffect>()
    val effects = _effects.asSharedFlow()

    /** The search for what is in the box now. Anything older is cancelled when this is replaced. */
    private var searchJob: Job? = null

    init {
        loadCategories()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                _uiState.update { it.copy(query = event.value) }
                if (event.value.isBlank()) {
                    // Cleared box: an answer still on its way would put results back under it.
                    searchJob?.cancel()
                    _uiState.update { it.copy(books = emptyList(), errorMessage = null) }
                }
            }
            SearchEvent.SearchClicked -> search()
            SearchEvent.BackClicked -> emitEffect(SearchEffect.NavigateBack)
            is SearchEvent.BookClicked -> emitEffect(SearchEffect.NavigateToBook(event.bookId))
            is SearchEvent.AuthorClicked -> emitEffect(SearchEffect.NavigateToAuthor(event.authorId))
            is SearchEvent.CategoryClicked -> emitEffect(SearchEffect.NavigateToCategory(event.categoryId))
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            when (val result = getCategories()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(categories = result.data, isLoading = false, errorMessage = null)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    /**
     * Searches for what is in the box, after a pause in typing, replacing any search still running.
     *
     * The field calls this on every keystroke. Each call used to start its own search and none was
     * ever stopped, so whichever answer arrived last won — a slow answer for "dick" could land after
     * the one for "dickens" and stay on screen under a box that said "dickens". Cancelling the
     * previous search is what makes the current query the only one that can land.
     */
    private fun search() {
        val query = _uiState.value.query.trim()
        searchJob?.cancel()
        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = searchBooks(query)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(books = result.data, isLoading = false)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: SearchEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
