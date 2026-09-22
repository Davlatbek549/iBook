package com.example.dz.presentation.store.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Category
import com.example.dz.domain.usecase.book.GetCategoriesUseCase
import com.example.dz.domain.usecase.book.GetCategoryBookCountUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class BrowseViewModel(
    private val getCategories: GetCategoriesUseCase,
    private val getCategoryBookCount: GetCategoryBookCountUseCase
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
            is BrowseEvent.MoodClicked -> _uiState.update {
                it.copy(mood = if (it.mood == event.mood) null else event.mood)
            }
            BrowseEvent.SortClicked -> _uiState.update {
                it.copy(
                    sort = when (it.sort) {
                        BrowseSort.CATALOGUE -> BrowseSort.ALPHABETICAL
                        BrowseSort.ALPHABETICAL -> BrowseSort.CATALOGUE
                    }
                )
            }
            // The handoff sends a genre tile back to Store, which shows no genre. Category detail
            // is the screen that lists one, so the tile opens that.
            is BrowseEvent.CategoryClicked -> emitEffect(BrowseEffect.NavigateToCategory(event.categoryId))
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getCategories()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(categories = result.data, isLoading = false, errorMessage = null)
                    }
                    loadCounts(result.data)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    /**
     * Fills in each tile's count as it arrives, rather than holding the grid until the slowest one.
     *
     * Every count is its own request, and the grid has sixteen genres. A few at a time keeps the
     * screen from opening sixteen connections at once — the tiles are already drawn and useful, so
     * the numbers can afford to trickle in. A count that fails is left out, not shown as zero: "0
     * books" would be a claim about the catalogue, and a missing line is only a missing line.
     */
    private fun loadCounts(categories: List<Category>) {
        val permits = Semaphore(COUNT_REQUESTS_AT_ONCE)
        categories.forEach { category ->
            viewModelScope.launch {
                val result = permits.withPermit { getCategoryBookCount(category.id) }
                if (result is AppResult.Success && result.data > 0) {
                    _uiState.update { it.copy(bookCounts = it.bookCounts + (category.id to result.data)) }
                }
            }
        }
    }

    private fun emitEffect(effect: BrowseEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    private companion object {
        const val COUNT_REQUESTS_AT_ONCE = 4
    }
}
