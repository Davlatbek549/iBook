package com.example.dz.presentation.reading

data class ReadingUiState(
    val bookId: String = "",
    val bookTitle: String = "",
    val pages: List<String> = emptyList(),
    val currentPage: Int = 1,
    val totalPages: Int = 0,
    val bookmarked: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val downloadPhase: DownloadPhase = DownloadPhase.NotDownloaded,
    val showDownloadSuccess: Boolean = false,
    val showDeleteDownloadDialog: Boolean = false,
    val downloadErrorMessage: String? = null
) {
    val isDownloaded: Boolean get() = downloadPhase == DownloadPhase.Downloaded
    val isDownloading: Boolean get() = downloadPhase == DownloadPhase.Downloading

    val currentPageParagraphs: List<String>
        get() = pages.getOrNull(currentPage - 1)
            ?.split(PARAGRAPH_BREAK)
            ?.filter { it.isNotBlank() }
            .orEmpty()

    val progress: Float
        get() = if (totalPages <= 0) 0f else (currentPage.toFloat() / totalPages).coerceIn(0f, 1f)

    val progressPercent: Int
        get() = if (totalPages <= 0) 0 else currentPage * 100 / totalPages

    val canGoToPreviousPage: Boolean get() = currentPage > 1
    val canGoToNextPage: Boolean get() = currentPage < totalPages

    private companion object {
        val PARAGRAPH_BREAK = Regex("\\n\\s*\\n")
    }
}

/** Offline-download state for the book currently open in the reader. */
enum class DownloadPhase { NotDownloaded, Downloading, Downloaded }
