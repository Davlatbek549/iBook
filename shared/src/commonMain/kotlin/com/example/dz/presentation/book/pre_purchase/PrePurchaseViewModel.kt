package com.example.dz.presentation.book.pre_purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.usecase.book.GetBookDetailsUseCase
import com.example.dz.domain.usecase.book.GetBooksByCategoryUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrePurchaseViewModel(
    private val bookId: String,
    private val getBookDetails: GetBookDetailsUseCase,
    private val getBooksByCategory: GetBooksByCategoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrePurchaseUiState(bookId = bookId, isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PrePurchaseEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load(bookId)
    }

    fun onEvent(event: PrePurchaseEvent) {
        when (event) {
            PrePurchaseEvent.BackClicked -> emitEffect(PrePurchaseEffect.NavigateBack)
            PrePurchaseEvent.FavoriteClicked -> _uiState.update { it.copy(isFavorite = !it.isFavorite) }
            PrePurchaseEvent.ViewSampleClicked -> emitEffect(PrePurchaseEffect.NavigateToReading(_uiState.value.bookId))
            PrePurchaseEvent.PurchaseClicked -> emitEffect(PrePurchaseEffect.NavigateToPurchase(_uiState.value.bookId))
            PrePurchaseEvent.AuthorClicked ->
                _uiState.value.authorId?.let { authorId -> emitEffect(PrePurchaseEffect.NavigateToAuthor(authorId)) }
            is PrePurchaseEvent.RelatedBookClicked -> emitEffect(PrePurchaseEffect.NavigateToBook(event.bookId))
            PrePurchaseEvent.TagClicked,
            PrePurchaseEvent.ShareClicked -> Unit
        }
    }

    private fun load(bookId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getBookDetails(bookId)) {
                is AppResult.Success -> loadRelated(result.data)
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private suspend fun loadRelated(book: Book) {
        val categoryId = book.categories.firstOrNull()?.id
        val relatedBooks = if (categoryId != null) {
            when (val result = getBooksByCategory(categoryId)) {
                is AppResult.Success -> result.data.filterNot { it.id == book.id }
                is AppResult.Error -> emptyList()
            }
        } else {
            emptyList()
        }

        _uiState.update { book.toPrePurchaseUiState(relatedBooks).copy(isFavorite = it.isFavorite) }
    }

    private fun emitEffect(effect: PrePurchaseEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
