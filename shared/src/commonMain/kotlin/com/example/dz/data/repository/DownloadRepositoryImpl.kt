package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.local.file.FileStorage
import com.example.dz.domain.model.DownloadedContent
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.DownloadRepository

/**
 * Coordinates offline downloads across the network ([BookRepository]), on-device files
 * ([FileStorage]), and the library database ([LibraryLocalDataSource]).
 */
class DownloadRepositoryImpl(
    private val bookRepository: BookRepository,
    private val fileStorage: FileStorage,
    private val library: LibraryLocalDataSource
) : DownloadRepository {

    override suspend fun downloadBook(bookId: String): AppResult<Unit> {
        val book = when (val details = bookRepository.getBookDetails(bookId)) {
            is AppResult.Success -> details.data
            is AppResult.Error -> return details
        }
        val textUrl = book.textUrl ?: return AppResult.Error(AppError.NotFound)

        val text = when (val remote = bookRepository.getBookText(textUrl)) {
            is AppResult.Success -> remote.data
            is AppResult.Error -> return remote
        }

        val path = fileStorage.save(bookId, text)

        // Ensure a library row exists (without clobbering an existing one), then record the download.
        if (library.getLibraryBook(bookId) == null) {
            library.upsert(
                LibraryBook(book = book, isDownloaded = true),
                addedAt = currentEpochMillis()
            )
        }
        library.setDownload(bookId, downloaded = true, path = path)
        return AppResult.Success(Unit)
    }

    override suspend fun deleteDownload(bookId: String): AppResult<Unit> {
        library.getDownloadPath(bookId)?.let { fileStorage.delete(it) }
        library.clearDownload(bookId)
        return AppResult.Success(Unit)
    }

    override suspend fun getDownloadedContent(bookId: String): DownloadedContent? {
        val entry = library.getLibraryBook(bookId) ?: return null
        if (!entry.isDownloaded) return null
        val path = library.getDownloadPath(bookId) ?: return null
        val text = fileStorage.read(path) ?: return null
        return DownloadedContent(bookId = bookId, title = entry.book.title, text = text)
    }

    override suspend fun isDownloaded(bookId: String): Boolean {
        val entry = library.getLibraryBook(bookId) ?: return false
        return entry.isDownloaded && library.getDownloadPath(bookId) != null
    }
}
