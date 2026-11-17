package com.example.dz.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
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
    private val getRecentSearches: GetRecentSearchesUseCase,
    private val saveRecentSearch: SaveRecentSearchUseCase,
    private val getLibraryBooks: GetLibraryBooksUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SearchEffect>()
    val effects = _effects.asSharedFlow()

    /** The search for what is in the box now. Anything older is cancelled when this is replaced. */
    private var searchJob: Job? = null
    private var libraryBookIds: Set<String> = emptySet()

    init {
        loadRecent()
        refreshLibraryIds()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                _uiState.update { it.copy(query = event.value) }
                if (event.value.isBlank()) {
                    // Cleared box: an answer still on its way would put results back under it.
                    searchJob?.cancel()
                    _uiState.update { it.copy(results = emptyList(), errorMessage = null) }
                } else {
                    search(saveToRecent = false)
                }
            }
            SearchEvent.SearchClicked -> search(saveToRecent = true)
            SearchEvent.BackClicked -> emitEffect(SearchEffect.NavigateBack)
            is SearchEvent.BookClicked -> emitEffect(SearchEffect.NavigateToBook(event.bookId))
            is SearchEvent.RecentSearchClicked -> {
                _uiState.update { it.copy(query = event.query) }
                search(saveToRecent = true)
            }
            SearchEvent.Resumed -> refreshLibraryIds(remapResults = true)
        }
    }

    private fun loadRecent() {
        viewModelScope.launch {
            when (val result = getRecentSearches()) {
                is AppResult.Success -> _uiState.update { it.copy(recentSearches = result.data) }
                is AppResult.Error -> Unit
            }
        }
    }

    private fun refreshLibraryIds(remapResults: Boolean = false) {
        viewModelScope.launch {
            when (val result = getLibraryBooks()) {
                is AppResult.Success -> {
                    libraryBookIds = result.data.map { it.book.id }.toSet()
                    if (remapResults) {
                        _uiState.update { state ->
                            state.copy(results = state.results.map { it.copy(inLibrary = it.id in libraryBookIds) })
                        }
                    }
                }
                is AppResult.Error -> Unit
            }
        }
    }

    /**
     * Searches for what is in the box, after a pause in typing, replacing any search still running.
     *
     * The field calls this on every keystroke. Each call used to start its own search and none was
     * ever stopped, so whichever answer arrived last won. Cancelling the previous search is what
     * makes the current query the only one that can land.
     *
     * [saveToRecent] is false for the live-as-you-type path — only an explicit submit (the search
     * button, or re-running a recent chip) is worth remembering, not every half-typed word.
     */
    private fun search(saveToRecent: Boolean) {
        val query = _uiState.value.query.trim()
        searchJob?.cancel()
        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            if (saveToRecent) {
                launch {
                    saveRecentSearch(query)
                    loadRecent()
                }
            }
            when (val result = searchBooks(query)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        results = result.data.map { book -> book.toSearchResultUi(libraryBookIds) },
                        isLoading = false
                    )
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

private fun Book.toSearchResultUi(libraryBookIds: Set<String>): SearchResultUi =
    SearchResultUi(
        id = id,
        title = title,
        author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        coverUrl = coverUrl,
        inLibrary = id in libraryBookIds,
        isFree = isFree,
        price = price
    )
