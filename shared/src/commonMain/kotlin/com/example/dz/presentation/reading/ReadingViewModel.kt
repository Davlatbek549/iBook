package com.example.dz.presentation.reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.domain.model.BookContent
import com.example.dz.domain.model.ReaderPreferences
import com.example.dz.domain.repository.DownloadRepository
import com.example.dz.domain.repository.ReaderPreferencesRepository
import com.example.dz.domain.repository.ReadingPositionRepository
import com.example.dz.domain.usecase.book.DeleteDownloadUseCase
import com.example.dz.domain.usecase.book.DownloadBookUseCase
import com.example.dz.domain.usecase.book.GetBookContentUseCase
import com.example.dz.domain.usecase.goal.RecordReadingSessionUseCase
import com.example.dz.domain.usecase.library.UpdateReadingProgressUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReadingViewModel(
    private val bookId: String,
    private val getBookContent: GetBookContentUseCase,
    private val updateReadingProgress: UpdateReadingProgressUseCase,
    private val downloadBook: DownloadBookUseCase,
    private val deleteDownload: DeleteDownloadUseCase,
    private val downloadRepository: DownloadRepository,
    private val recordReadingSession: RecordReadingSessionUseCase,
    private val readerPreferences: ReaderPreferencesRepository,
    private val readingPosition: ReadingPositionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadingUiState(bookId = bookId, isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ReadingEffect>()
    val effects = _effects.asSharedFlow()

    /** Start of the stretch of reading not yet written down. */
    private var unrecordedSince = currentEpochMillis()

    init {
        // Read before the first frame: a page that waited for its own type size would show at the
        // default size and then jump.
        _uiState.update { it.copy(preferences = readerPreferences.get()) }
        load()
        refreshDownloadState()
        trackReadingTime()
    }

    fun onEvent(event: ReadingEvent) {
        when (event) {
            ReadingEvent.BackClicked -> {
                // Write down the part-minute before leaving; the ticker has the rest.
                viewModelScope.launch { flushReadingTime() }
                emitEffect(ReadingEffect.NavigateBack)
            }
            // The display sheet lives over the page rather than on its own screen: the handoff
            // dismisses it by tapping outside, and the page stays visible behind it.
            ReadingEvent.MenuClicked -> _uiState.update { it.copy(showDisplaySheet = true) }
            ReadingEvent.DisplaySheetDismissed -> _uiState.update { it.copy(showDisplaySheet = false) }
            ReadingEvent.PageTapped -> _uiState.update { it.copy(chromeVisible = !it.chromeVisible) }
            // The size is shown while the slider moves and written when it stops: a drag across
            // the scale is one write to the store rather than one per pixel it passes.
            is ReadingEvent.FontScaleChanged -> _uiState.update { state ->
                state.copy(
                    preferences = state.preferences.copy(
                        fontScale = event.scale.coerceIn(
                            ReaderPreferences.MIN_FONT_SCALE,
                            ReaderPreferences.MAX_FONT_SCALE,
                        )
                    )
                )
            }

            ReadingEvent.FontScaleCommitted -> readerPreferences.save(_uiState.value.preferences)
            is ReadingEvent.PageThemeChanged -> updatePreferences { it.copy(pageTheme = event.theme) }
            is ReadingEvent.SerifChanged -> updatePreferences { it.copy(useSerif = event.useSerif) }
            ReadingEvent.CommentsClicked -> emitEffect(ReadingEffect.NavigateToComments(bookId))
            ReadingEvent.BookmarkToggled -> toggleBookmark()
            ReadingEvent.NextPageClicked -> movePage(1)
            ReadingEvent.PreviousPageClicked -> movePage(-1)
            is ReadingEvent.ProgressScrubbed -> scrubTo(event.fraction)
            ReadingEvent.ProgressScrubFinished -> persistProgress()
            ReadingEvent.RetryClicked -> load()
            ReadingEvent.DownloadClicked -> download()
            ReadingEvent.DownloadSuccessDismissed ->
                _uiState.update { it.copy(showDownloadSuccess = false) }
            ReadingEvent.DownloadErrorDismissed ->
                _uiState.update { it.copy(downloadErrorMessage = null) }
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
                        // A missing text source means this book has no downloadable full text
                        // (e.g. OpenLibrary titles), which is clearer than a generic "not found".
                        downloadErrorMessage = when (result.error) {
                            AppError.NotFound -> "This book isn't available for offline download."
                            else -> result.error.toPresentationMessage()
                        }
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
                    it.copy(
                        isLoading = false,
                        // "Not found" here never means a missing book — the reader was opened from
                        // one. It means this title has no full text behind it, which is the case
                        // for everything that came from OpenLibrary rather than Gutenberg.
                        errorMessage = when (result.error) {
                            AppError.NotFound -> NO_TEXT_MESSAGE
                            else -> result.error.toPresentationMessage()
                        }
                    )
                }
            }
        }
    }

    /**
     * Opens the book where it was left rather than at page one.
     *
     * Pages are cut on a fixed number of characters, so a page number means the same thing in every
     * session and at every type size — which is what makes storing one worth doing.
     */
    private fun applyContent(content: BookContent) {
        val resumed = readingPosition.lastPage(bookId).coerceIn(1, maxOf(content.pageCount, 1))
        _uiState.update {
            it.copy(
                bookTitle = content.title,
                pages = content.pages,
                currentPage = resumed,
                totalPages = content.pageCount,
                bookmarkedPage = readingPosition.bookmark(bookId)
                    ?.takeIf { page -> page <= content.pageCount },
                isLoading = false,
                errorMessage = null
            )
        }
    }

    /** Pins the page being read, or unpins it when it is already the pinned one. */
    private fun toggleBookmark() {
        _uiState.update { state ->
            val pinned = if (state.bookmarked) null else state.currentPage
            readingPosition.saveBookmark(bookId, pinned)
            state.copy(bookmarkedPage = pinned)
        }
    }

    /**
     * Dragging the progress bar.
     *
     * The page follows the finger, but only the local position is written while it moves; the
     * library's percentage is written once on release, so a drag across a six-hundred-page book is
     * one database write rather than six hundred.
     */
    private fun scrubTo(fraction: Float) {
        val state = _uiState.value
        if (state.totalPages <= 0) return
        val target = (fraction.coerceIn(0f, 1f) * state.totalPages)
            .toInt()
            .coerceIn(0, state.totalPages - 1) + 1
        if (target == state.currentPage) return
        readingPosition.saveLastPage(bookId, target)
        _uiState.update { it.copy(currentPage = target) }
    }

    private fun movePage(delta: Int) {
        val state = _uiState.value
        if (state.totalPages <= 0) return
        val target = (state.currentPage + delta).coerceIn(1, state.totalPages)
        if (target == state.currentPage) return
        readingPosition.saveLastPage(bookId, target)
        _uiState.update { it.copy(currentPage = target) }
        persistProgress()
    }

    private fun persistProgress() {
        viewModelScope.launch {
            updateReadingProgress(bookId, _uiState.value.progressPercent)
        }
    }

    /**
     * Writes the reading down as it happens rather than once at the end.
     *
     * `onCleared` is not a safe place for this — `viewModelScope` is already cancelled by then, and
     * a process killed with the book open would take the whole session with it. Flushing on a
     * ticker costs one small insert a minute and loses at most the last minute.
     *
     * Known limitation: this counts time while the reader screen is the current destination, which
     * includes time the app spends in the background with the book still open. Fixing that needs
     * platform lifecycle observation rather than a timer.
     */
    /** Preferences are written as they change — there is no Save on the display sheet. */
    private fun updatePreferences(change: (ReaderPreferences) -> ReaderPreferences) {
        _uiState.update { state ->
            val updated = change(state.preferences)
            readerPreferences.save(updated)
            state.copy(preferences = updated)
        }
    }

    private fun trackReadingTime() {
        viewModelScope.launch {
            while (true) {
                delay(SESSION_FLUSH_MILLIS)
                flushReadingTime()
            }
        }
    }

    private suspend fun flushReadingTime() {
        val now = currentEpochMillis()
        val seconds = (now - unrecordedSince) / MILLIS_PER_SECOND
        if (seconds <= 0) return
        unrecordedSince = now
        recordReadingSession(bookId, seconds)
    }

    private fun emitEffect(effect: ReadingEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    private companion object {
        const val NO_TEXT_MESSAGE = "This book doesn't have a readable copy yet."

        /** One small insert a minute; at most a minute is lost if the process dies. */
        const val SESSION_FLUSH_MILLIS = 60_000L
        const val MILLIS_PER_SECOND = 1_000L
    }
}
