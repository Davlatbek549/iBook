package com.example.dz.presentation.reading

import com.example.dz.domain.model.PageTheme
import com.example.dz.domain.model.PageTurn

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

    /** How one page should give way to the next. */
    data class PageTurnChanged(val pageTurn: PageTurn) : ReadingEvent
    data object CommentsClicked : ReadingEvent

    /** Pins the place being read, or unpins it when it is already the pinned one. */
    data class BookmarkToggled(val pinned: Boolean) : ReadingEvent

    /**
     * The reader came to rest on a page starting at [offset].
     *
     * Sent by the pager after a swipe, a scrub, or the first page being restored — every way the
     * place can change is the same event, because the place is the only thing that matters.
     */
    data class PageSettled(val offset: Int) : ReadingEvent

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
