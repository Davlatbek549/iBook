package com.example.dz.presentation.collections.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.collection.GetCollectionsUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionsViewModel(
    private val getCollections: GetCollectionsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CollectionsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<CollectionsEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    /** Reloads the collections, e.g. when returning from the create/edit screen. */
    fun refresh() = load()

    fun onEvent(event: CollectionsEvent) {
        when (event) {
            CollectionsEvent.BackClicked -> emitEffect(CollectionsEffect.NavigateBack)
            CollectionsEvent.SearchClicked -> emitEffect(CollectionsEffect.NavigateToEdit("all"))
            CollectionsEvent.NewCollectionClicked -> emitEffect(CollectionsEffect.NavigateToEdit("new"))
            is CollectionsEvent.CollectionClicked -> emitEffect(CollectionsEffect.NavigateToDetail(event.collectionId))
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getCollections()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        collections = result.data.map { collection -> collection.toCollectionUiState() },
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: CollectionsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
