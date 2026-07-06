package com.example.dz.presentation.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.domain.usecase.library.UpdateReadingProgressUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReadingViewModel(
    private val bookId: String,
    private val updateReadingProgress: UpdateReadingProgressUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadingUiState(bookId = bookId))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ReadingEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: ReadingEvent) {
        when (event) {
            ReadingEvent.BackClicked -> emitEffect(ReadingEffect.NavigateBack)
            ReadingEvent.MenuClicked -> emitEffect(ReadingEffect.NavigateToSettings)
            ReadingEvent.CommentsClicked -> emitEffect(ReadingEffect.NavigateToComments(bookId))
            ReadingEvent.BookmarkToggled -> _uiState.update { it.copy(bookmarked = !it.bookmarked) }
            ReadingEvent.NextPageClicked -> movePage(1)
            ReadingEvent.PreviousPageClicked -> movePage(-1)
        }
    }

    private fun movePage(delta: Int) {
        val state = _uiState.value
        val target = (state.currentPage + delta).coerceIn(1, state.totalPages)
        if (target == state.currentPage) return
        _uiState.update { it.copy(currentPage = target) }
        persistProgress()
    }

    private fun persistProgress() {
        viewModelScope.launch {
            updateReadingProgress(bookId, _uiState.value.progressPercent)
        }
    }

    private fun emitEffect(effect: ReadingEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
