package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.DownloadedContent

/**
 * Manages offline book downloads: fetching and storing book text on device, reading it back for
 * offline reading, and removing it. Backed by local file storage plus the library database.
 */
interface DownloadRepository {
    /** Downloads the book's text, stores it on device, and records the file in the library. */
    suspend fun downloadBook(bookId: String): AppResult<Unit>

    /** Deletes the downloaded file and clears its download record. */
    suspend fun deleteDownload(bookId: String): AppResult<Unit>

    /** Returns the locally stored content for a downloaded book, or null if not downloaded. */
    suspend fun getDownloadedContent(bookId: String): DownloadedContent?

    /** Whether the book is currently downloaded and available offline. */
    suspend fun isDownloaded(bookId: String): Boolean
}
