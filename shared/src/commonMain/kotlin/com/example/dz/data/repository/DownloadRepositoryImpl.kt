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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Coordinates offline downloads across the network ([BookRepository]), on-device files
 * ([FileStorage]), and the library database ([LibraryLocalDataSource]).
 *
 * Everything here runs on [io]. The heaviest of it is whole files — a downloaded book is written
 * and read back in one piece, and reading one is what opening a downloaded book waits on.
 */
class DownloadRepositoryImpl(
    private val bookRepository: BookRepository,
    private val fileStorage: FileStorage,
    private val library: LibraryLocalDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : DownloadRepository {

    override suspend fun downloadBook(bookId: String): AppResult<Unit> = withContext(io) {
        val book = when (val details = bookRepository.getBookDetails(bookId)) {
            is AppResult.Success -> details.data
            is AppResult.Error -> return@withContext details
        }
        val textUrl = book.textUrl ?: return@withContext AppResult.Error(AppError.NotFound)

        val text = when (val remote = bookRepository.getBookText(textUrl)) {
            is AppResult.Success -> remote.data
            is AppResult.Error -> return@withContext remote
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
        AppResult.Success(Unit)
    }

    override suspend fun deleteDownload(bookId: String): AppResult<Unit> = withContext(io) {
        library.getDownloadPath(bookId)?.let { fileStorage.delete(it) }
        library.clearDownload(bookId)
        AppResult.Success(Unit)
    }

    override suspend fun getDownloadedContent(bookId: String): DownloadedContent? =
        withContext(io) {
            val entry = library.getLibraryBook(bookId) ?: return@withContext null
            if (!entry.isDownloaded) return@withContext null
            val path = library.getDownloadPath(bookId) ?: return@withContext null
            val text = fileStorage.read(path) ?: return@withContext null
            DownloadedContent(bookId = bookId, title = entry.book.title, text = text)
        }

    override suspend fun isDownloaded(bookId: String): Boolean = withContext(io) {
        val entry = library.getLibraryBook(bookId) ?: return@withContext false
        entry.isDownloaded && library.getDownloadPath(bookId) != null
    }
}
