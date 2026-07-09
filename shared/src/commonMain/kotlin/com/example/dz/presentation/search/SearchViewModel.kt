package com.example.dz.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.SearchBooksUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getCategories: GetCategoriesUseCase,
    private val searchBooks: SearchBooksUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SearchEffect>()
    val effects = _effects.asSharedFlow()

    init {
        loadCategories()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                _uiState.update { it.copy(query = event.value) }
                if (event.value.isBlank()) {
                    _uiState.update { it.copy(books = emptyList(), errorMessage = null) }
                }
            }
            SearchEvent.SearchClicked -> search()
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

    private fun search() {
        val query = _uiState.value.query.trim()
        if (query.isBlank()) return

        viewModelScope.launch {
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
