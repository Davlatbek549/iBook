package com.example.dz.presentation.book.category_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.book.GetBooksByCategoryUseCase
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryDetailViewModel(
    private val categoryId: String,
    private val getBooksByCategory: GetBooksByCategoryUseCase,
    private val getCategories: GetCategoriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CategoryDetailUiState(categoryId = categoryId, title = categoryId.toDisplayTitle(), isLoading = true)
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CategoryDetailEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: CategoryDetailEvent) {
        when (event) {
            CategoryDetailEvent.BackClicked -> emitEffect(CategoryDetailEffect.NavigateBack)
            is CategoryDetailEvent.BookClicked -> emitEffect(CategoryDetailEffect.NavigateToBook(event.bookId))
            is CategoryDetailEvent.BookmarkClicked -> toggleBookmark(event.bookId)
            CategoryDetailEvent.SearchClicked,
            CategoryDetailEvent.SortClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val title = when (val result = getCategories()) {
                is AppResult.Success -> result.data.firstOrNull { it.id == categoryId }?.name
                is AppResult.Error -> null
            } ?: categoryId.toDisplayTitle()

            when (val result = getBooksByCategory(categoryId)) {
                is AppResult.Success -> {
                    val books = result.data.map { it.toCategoryDetailBookUi() }
                    _uiState.update {
                        it.copy(
                            title = title,
                            description = "${books.size} books · quiet, character-first novels that stay with you.",
                            books = books,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(title = title, isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun toggleBookmark(bookId: String) {
        _uiState.update { state ->
            state.copy(
                books = state.books.map { book ->
                    if (book.id == bookId) book.copy(bookmarked = !book.bookmarked) else book
                }
            )
        }
    }

    private fun emitEffect(effect: CategoryDetailEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}

private fun String.toDisplayTitle(): String =
    replace('-', ' ').replaceFirstChar { it.uppercase() }
