package com.example.dz.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.book.SearchBooksUseCase
import com.example.dz.domain.usecase.library.GetLibraryBooksUseCase
import com.example.dz.domain.usecase.search.GetRecentSearchesUseCase
import com.example.dz.domain.usecase.search.SaveRecentSearchUseCase
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
    private val searchBooks: SearchBooksUseCase,
    private val getLibraryBooks: GetLibraryBooksUseCase,
    private val getRecentSearches: GetRecentSearchesUseCase,
    private val saveRecentSearch: SaveRecentSearchUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SearchEffect>()
    val effects = _effects.asSharedFlow()

    /** The search for what is in the box now. Anything older is cancelled when this is replaced. */
    private var searchJob: Job? = null

    init {
        loadRecentSearches()
        refreshLibrary()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                _uiState.update { it.copy(query = event.value) }
                if (event.value.isBlank()) {
                    // Cleared box: an answer still on its way would put results back under it.
                    searchJob?.cancel()
                    _uiState.update {
                        it.copy(books = emptyList(), resultsFor = null, isLoading = false, errorMessage = null)
                    }
                }
            }
            SearchEvent.SearchClicked -> search(afterPause = true)
            SearchEvent.SearchSubmitted -> {
                search(afterPause = false)
                rememberSearch(_uiState.value.query)
            }
            is SearchEvent.RecentSearchClicked -> {
                _uiState.update { it.copy(query = event.query) }
                search(afterPause = false)
                rememberSearch(event.query)
            }
            // Opening a result is the surest sign the query was the one meant — surer than a pause
            // in typing, which would file "dick" and "dicke" on the way to "dickens".
            is SearchEvent.BookClicked -> {
                rememberSearch(_uiState.value.query)
                emitEffect(SearchEffect.NavigateToBook(event.bookId))
            }
            SearchEvent.BackClicked -> emitEffect(SearchEffect.NavigateBack)
            SearchEvent.Resumed -> refreshLibrary()
        }
    }

    /**
     * Searches for what is in the box, replacing any search still running — after a pause in typing
     * when [afterPause], at once when the reader has said the query is done.
     *
     * The field calls this on every keystroke. Each call used to start its own search and none was
     * ever stopped, so whichever answer arrived last won — a slow answer for "dick" could land after
     * the one for "dickens" and stay on screen under a box that said "dickens". Cancelling the
     * previous search is what makes the current query the only one that can land.
     */
    private fun search(afterPause: Boolean) {
        val query = _uiState.value.query.trim()
        searchJob?.cancel()
        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            if (afterPause) delay(SEARCH_DEBOUNCE_MILLIS)
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = searchBooks(query)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(books = result.data, resultsFor = query, isLoading = false)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            val recent = getRecentSearches()
            _uiState.update { it.copy(recentSearches = recent) }
        }
    }

    private fun rememberSearch(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            val recent = saveRecentSearch(query)
            _uiState.update { it.copy(recentSearches = recent) }
        }
    }

    /** A shelf that cannot be read leaves every result unmarked — search itself still works. */
    private fun refreshLibrary() {
        viewModelScope.launch {
            val library = (getLibraryBooks() as? AppResult.Success)?.data.orEmpty()
            _uiState.update { state ->
                state.copy(librarySignatures = library.flatMap { it.book.librarySignatures() }.toSet())
            }
        }
    }

    private fun emitEffect(effect: SearchEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
