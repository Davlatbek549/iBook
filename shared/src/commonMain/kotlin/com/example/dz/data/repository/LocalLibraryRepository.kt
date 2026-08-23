package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress
import com.example.dz.domain.repository.LibraryRepository

/** Persists the user's library via SQLDelight, so it survives an app restart. */
class LocalLibraryRepository(
    private val local: LibraryLocalDataSource
) : LibraryRepository {
    override suspend fun getLibraryBooks(): AppResult<List<LibraryBook>> =
        AppResult.Success(local.getLibraryBooks())

    override suspend fun getContinueReading(): AppResult<LibraryBook?> =
        AppResult.Success(local.getContinueReading())

    override suspend fun updateReadingProgress(bookId: String, progressPercent: Int): AppResult<ReadingProgress> {
        val clamped = progressPercent.coerceIn(0, 100)
        local.updateProgress(bookId, clamped)
        return AppResult.Success(
            ReadingProgress(
                bookId = bookId,
                progressPercent = clamped,
                lastReadAt = currentEpochMillis().toString()
            )
        )
    }
}
