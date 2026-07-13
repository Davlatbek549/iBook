package com.example.dz.presentation.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.BookContent
import com.example.dz.domain.repository.DownloadRepository
import com.example.dz.domain.usecase.book.DeleteDownloadUseCase
import com.example.dz.domain.usecase.book.DownloadBookUseCase
import com.example.dz.domain.usecase.book.GetBookContentUseCase
import com.example.dz.domain.usecase.library.UpdateReadingProgressUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReadingViewModel(
    private val bookId: String,
    private val getBookContent: GetBookContentUseCase,
    private val updateReadingProgress: UpdateReadingProgressUseCase,
    private val downloadBook: DownloadBookUseCase,
    private val deleteDownload: DeleteDownloadUseCase,
    private val downloadRepository: DownloadRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadingUiState(bookId = bookId, isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ReadingEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
        refreshDownloadState()
    }

    fun onEvent(event: ReadingEvent) {
        when (event) {
            ReadingEvent.BackClicked -> emitEffect(ReadingEffect.NavigateBack)
            ReadingEvent.MenuClicked -> emitEffect(ReadingEffect.NavigateToSettings)
            ReadingEvent.CommentsClicked -> emitEffect(ReadingEffect.NavigateToComments(bookId))
            ReadingEvent.BookmarkToggled -> _uiState.update { it.copy(bookmarked = !it.bookmarked) }
            ReadingEvent.NextPageClicked -> movePage(1)
            ReadingEvent.PreviousPageClicked -> movePage(-1)
            ReadingEvent.RetryClicked -> load()
            ReadingEvent.DownloadClicked -> download()
            ReadingEvent.DownloadSuccessDismissed ->
                _uiState.update { it.copy(showDownloadSuccess = false) }
            ReadingEvent.DeleteDownloadClicked ->
                _uiState.update { it.copy(showDeleteDownloadDialog = true) }
            ReadingEvent.DismissDeleteDownloadDialog ->
                _uiState.update { it.copy(showDeleteDownloadDialog = false) }
            ReadingEvent.ConfirmDeleteDownload -> confirmDeleteDownload()
        }
    }

    private fun refreshDownloadState() {
        viewModelScope.launch {
            if (downloadRepository.isDownloaded(bookId)) {
                _uiState.update { it.copy(downloadPhase = DownloadPhase.Downloaded) }
            }
        }
    }

    private fun download() {
        if (_uiState.value.downloadPhase != DownloadPhase.NotDownloaded) return
        viewModelScope.launch {
            _uiState.update { it.copy(downloadPhase = DownloadPhase.Downloading, downloadErrorMessage = null) }
            when (val result = downloadBook(bookId)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(downloadPhase = DownloadPhase.Downloaded, showDownloadSuccess = true)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(
                        downloadPhase = DownloadPhase.NotDownloaded,
                        downloadErrorMessage = result.error.toPresentationMessage()
                    )
                }
            }
        }
    }

    private fun confirmDeleteDownload() {
        viewModelScope.launch {
            deleteDownload(bookId)
            _uiState.update {
                it.copy(
                    downloadPhase = DownloadPhase.NotDownloaded,
                    showDeleteDownloadDialog = false,
                    showDownloadSuccess = false
                )
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getBookContent(bookId)) {
                is AppResult.Success -> applyContent(result.data)
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun applyContent(content: BookContent) {
        _uiState.update {
            it.copy(
                bookTitle = content.title,
                pages = content.pages,
                currentPage = 1,
                totalPages = content.pageCount,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    private fun movePage(delta: Int) {
        val state = _uiState.value
        if (state.totalPages <= 0) return
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
