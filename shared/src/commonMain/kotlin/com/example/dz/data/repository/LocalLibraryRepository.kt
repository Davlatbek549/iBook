package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress
import com.example.dz.domain.repository.LibraryRepository

/**
 * Library persistence backed by the local SQLDelight database ([LibraryLocalDataSource]), replacing
 * the in-memory fake. The library is the user's own on-device shelf, so all reads/writes are local.
 */
class LocalLibraryRepository(
    private val library: LibraryLocalDataSource
) : LibraryRepository {

    override suspend fun getLibraryBooks(): AppResult<List<LibraryBook>> =
        AppResult.Success(library.getLibraryBooks())

    override suspend fun getContinueReading(): AppResult<LibraryBook?> =
        AppResult.Success(library.getContinueReading())

    override suspend fun updateReadingProgress(bookId: String, progressPercent: Int): AppResult<ReadingProgress> {
        val clamped = progressPercent.coerceIn(0, 100)
        library.updateProgress(bookId, clamped)
        return AppResult.Success(
            ReadingProgress(
                bookId = bookId,
                progressPercent = clamped,
                lastReadAt = currentEpochMillis().toString()
            )
        )
    }
}
