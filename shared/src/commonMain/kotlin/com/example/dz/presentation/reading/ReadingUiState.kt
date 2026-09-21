package com.example.dz.presentation.reading

import com.example.dz.domain.model.ReaderPreferences

data class ReadingUiState(
    val bookId: String = "",
    val bookTitle: String = "",
    /** The book's whole body. The reader cuts it into pages itself. */
    val text: String = "",
    /**
     * Where the reader is, as a character offset into [text].
     *
     * Not a page number: a page is only as long as the type size makes it, so the same number means
     * a different place after the size changes. An offset is the word they stopped on.
     */
    val offset: Int = 0,
    /** The offset pinned in this book, or `null` when nothing is pinned. */
    val bookmarkOffset: Int? = null,
    val preferences: ReaderPreferences = ReaderPreferences(),
    /**
     * The reader opens on the book, not on its controls.
     *
     * Somebody who has just tapped a book wants to read it; the back button and the tools are for
     * a moment that has not arrived yet, and a tap on the page brings them when it does.
     */
    val chromeVisible: Boolean = false,
    val showDisplaySheet: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val downloadPhase: DownloadPhase = DownloadPhase.NotDownloaded,
    val showDownloadSuccess: Boolean = false,
    val showDeleteDownloadDialog: Boolean = false,
    val downloadErrorMessage: String? = null
) {
    val isDownloaded: Boolean get() = downloadPhase == DownloadPhase.Downloaded
    val isDownloading: Boolean get() = downloadPhase == DownloadPhase.Downloading

    val hasText: Boolean get() = text.isNotEmpty()

    /**
     * How far through the book, as a fraction of its text.
     *
     * Measured in characters rather than pages so that changing the type size moves the bar by
     * nothing at all — the reader is exactly as far through the book as they were a moment ago.
     */
    val progress: Float
        get() = if (text.isEmpty()) 0f else (offset.toFloat() / text.length).coerceIn(0f, 1f)

    /**
     * The same fraction as a whole number, for the shelf.
     *
     * Rounded up off zero rather than down onto it: eight pages into a novel is a fraction of a
     * percent, and a book someone has started belongs under "Reading" rather than "To read".
     */
    val progressPercent: Int
        get() = when {
            text.isEmpty() || offset <= 0 -> 0
            else -> (offset.toLong() * 100 / text.length).toInt().coerceAtLeast(1)
        }

    /** Where the pin sits along the progress bar, so a reader can see what they are scrubbing to. */
    val bookmarkProgress: Float?
        get() = bookmarkOffset
            ?.takeIf { text.isNotEmpty() }
            ?.let { (it.toFloat() / text.length).coerceIn(0f, 1f) }
}

/** Offline-download state for the book currently open in the reader. */
enum class DownloadPhase { NotDownloaded, Downloading, Downloaded }
