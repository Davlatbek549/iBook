package com.example.dz.presentation.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.GetHomeBooksUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel(
    private val getHomeBooks: GetHomeBooksUseCase,
    private val getCategories: GetCategoriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(StoreUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<StoreEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: StoreEvent) {
        when (event) {
            is StoreEvent.BookClicked -> emitEffect(StoreEffect.NavigateToBook(event.bookId))
            is StoreEvent.CategoryClicked -> emitEffect(StoreEffect.NavigateToCategory(event.categoryId))
            StoreEvent.ViewMoreClicked -> emitEffect(StoreEffect.NavigateToMembership)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val booksResult = getHomeBooks()
            val categoriesResult = getCategories()

            val books = (booksResult as? AppResult.Success)?.data.orEmpty()
            val categories = (categoriesResult as? AppResult.Success)?.data.orEmpty()
            val error = listOf(booksResult, categoriesResult)
                .firstNotNullOfOrNull { result ->
                    (result as? AppResult.Error)?.error?.toPresentationMessage()
                }

            _uiState.update {
                it.copy(
                    featuredBooks = books,
                    categories = categories,
                    isLoading = false,
                    errorMessage = error
                )
            }
        }
    }

    private fun emitEffect(effect: StoreEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
