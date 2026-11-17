package com.example.dz.presentation.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.GetCategoryBookCountUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrowseViewModel(
    private val getCategories: GetCategoriesUseCase,
    private val getCategoryBookCount: GetCategoryBookCountUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BrowseUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<BrowseEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: BrowseEvent) {
        when (event) {
            BrowseEvent.BackClicked -> emitEffect(BrowseEffect.NavigateBack)
            BrowseEvent.SearchClicked -> emitEffect(BrowseEffect.NavigateToSearch)
            is BrowseEvent.MoodClicked -> _uiState.update { it.copy(selectedMoodIndex = event.index) }
            // No real sort order to switch to yet — the handoff shows only "A–Z", which is what
            // getCategories() already returns.
            BrowseEvent.SortClicked -> Unit
            is BrowseEvent.CategoryClicked -> emitEffect(BrowseEffect.NavigateToCategory(event.categoryId))
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getCategories()) {
                is AppResult.Success -> {
                    val categories = coroutineScope {
                        result.data.map { category ->
                            async {
                                val count = (getCategoryBookCount(category.id) as? AppResult.Success)?.data ?: 0
                                BrowseCategoryUi(id = category.id, name = category.name, bookCount = count)
                            }
                        }.map { it.await() }
                    }
                    _uiState.update { it.copy(categories = categories, isLoading = false, errorMessage = null) }
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: BrowseEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
