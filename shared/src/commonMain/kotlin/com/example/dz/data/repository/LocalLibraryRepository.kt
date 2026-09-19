package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress
import com.example.dz.domain.repository.LibraryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Library persistence backed by the local SQLDelight database ([LibraryLocalDataSource]), replacing
 * the in-memory fake. The library is the user's own on-device shelf, so all reads/writes are local.
 *
 * Every query runs on [io]. The data source is synchronous and callers are view models on the
 * main thread, so without the switch each read was a disk access on the thread that draws.
 */
class LocalLibraryRepository(
    private val library: LibraryLocalDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : LibraryRepository {

    /**
     * `upsert` is INSERT OR REPLACE, so calling it on a book already on the shelf would throw away
     * how far through it the reader is and whether it is downloaded. The row is only written when
     * there is no row.
     */
    override suspend fun add(book: Book): AppResult<Unit> = withContext(io) {
        if (library.getLibraryBook(book.id) == null) {
            library.upsert(LibraryBook(book = book), addedAt = currentEpochMillis())
        }
        AppResult.Success(Unit)
    }

    override suspend fun getLibraryBooks(): AppResult<List<LibraryBook>> = withContext(io) {
        AppResult.Success(library.getLibraryBooks())
    }

    override suspend fun getContinueReading(): AppResult<LibraryBook?> = withContext(io) {
        AppResult.Success(library.getContinueReading())
    }

    override suspend fun updateReadingProgress(
        bookId: String,
        progressPercent: Int,
    ): AppResult<ReadingProgress> = withContext(io) {
        val clamped = progressPercent.coerceIn(0, 100)
        library.updateProgress(bookId, clamped)
        AppResult.Success(
            ReadingProgress(
                bookId = bookId,
                progressPercent = clamped,
                lastReadAt = currentEpochMillis().toString()
            )
        )
    }
}
