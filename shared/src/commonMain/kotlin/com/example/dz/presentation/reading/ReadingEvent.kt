package com.example.dz.presentation.reading

sealed interface ReadingEvent {
    data object BackClicked : ReadingEvent
    data object MenuClicked : ReadingEvent
    data object CommentsClicked : ReadingEvent
    data object BookmarkToggled : ReadingEvent
    data object NextPageClicked : ReadingEvent
    data object PreviousPageClicked : ReadingEvent
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
