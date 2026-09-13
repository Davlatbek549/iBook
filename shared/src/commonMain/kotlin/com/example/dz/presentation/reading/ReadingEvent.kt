package com.example.dz.presentation.reading

import com.example.dz.domain.model.PageTheme

sealed interface ReadingEvent {
    data object BackClicked : ReadingEvent
    /** Opens the display sheet — text size, page colour, face. */
    data object MenuClicked : ReadingEvent
    data object DisplaySheetDismissed : ReadingEvent
    /** A tap on the page itself, which shows or hides everything around it. */
    data object PageTapped : ReadingEvent
    /** The text-size slider moving; the page follows it live. */
    data class FontScaleChanged(val scale: Float) : ReadingEvent

    /** The finger came off the text-size slider — the size it landed on is the one to keep. */
    data object FontScaleCommitted : ReadingEvent
    data class PageThemeChanged(val theme: PageTheme) : ReadingEvent
    data class SerifChanged(val useSerif: Boolean) : ReadingEvent
    data object CommentsClicked : ReadingEvent
    data object BookmarkToggled : ReadingEvent
    data object NextPageClicked : ReadingEvent
    data object PreviousPageClicked : ReadingEvent

    /** Dragging the progress bar — [fraction] is 0 at the first page and 1 at the last. */
    data class ProgressScrubbed(val fraction: Float) : ReadingEvent

    /** The finger came off the progress bar; the page it landed on is the one to keep. */
    data object ProgressScrubFinished : ReadingEvent
    data object RetryClicked : ReadingEvent

    /** Download the current book for offline reading. */
    data object DownloadClicked : ReadingEvent

    /** Dismiss the "download complete" confirmation. */
    data object DownloadSuccessDismissed : ReadingEvent

    /** Dismiss the download-failure message. */
    data object DownloadErrorDismissed : ReadingEvent

    /** Open the delete-download confirmation popup. */
    data object DeleteDownloadClicked : ReadingEvent

    /** Confirm and remove the offline download. */
    data object ConfirmDeleteDownload : ReadingEvent

    /** Close the delete-download confirmation without deleting. */
    data object DismissDeleteDownloadDialog : ReadingEvent
}
