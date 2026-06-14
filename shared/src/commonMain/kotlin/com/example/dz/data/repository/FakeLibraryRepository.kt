package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress
import com.example.dz.domain.repository.LibraryRepository

class FakeLibraryRepository : LibraryRepository {
    override suspend fun getLibraryBooks(): AppResult<List<LibraryBook>> =
        AppResult.Success(FakeData.libraryBooks)

    override suspend fun getContinueReading(): AppResult<LibraryBook?> =
        AppResult.Success(FakeData.libraryBooks.maxByOrNull { it.progressPercent })

    override suspend fun updateReadingProgress(bookId: String, progressPercent: Int): AppResult<ReadingProgress> =
        AppResult.Success(
            ReadingProgress(
                bookId = bookId,
                progressPercent = progressPercent.coerceIn(0, 100),
                lastReadAt = "Now"
            )
        )
}
