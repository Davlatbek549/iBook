package com.example.dz.presentation.collections.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.collection.GetCollectionDetailsUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionDetailsViewModel(
    private val collectionId: String,
    private val getCollectionDetails: GetCollectionDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CollectionDetailsUiState(collectionId = collectionId, isLoading = true)
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CollectionDetailsEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: CollectionDetailsEvent) {
        when (event) {
            CollectionDetailsEvent.BackClicked -> emitEffect(CollectionDetailsEffect.NavigateBack)
            CollectionDetailsEvent.EditClicked -> emitEffect(CollectionDetailsEffect.NavigateToEdit(collectionId))
            is CollectionDetailsEvent.BookClicked -> emitEffect(CollectionDetailsEffect.NavigateToBook(event.bookId))
            CollectionDetailsEvent.AddBooksClicked,
            CollectionDetailsEvent.ShareClicked,
            is CollectionDetailsEvent.BookOptionsClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getCollectionDetails(collectionId)) {
                is AppResult.Success -> {
                    val details = result.data.toCollectionDetailsUiState()
                    _uiState.update {
                        details.copy(isLoading = false, errorMessage = null)
                    }
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: CollectionDetailsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
