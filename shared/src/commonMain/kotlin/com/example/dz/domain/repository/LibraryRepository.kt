package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress

interface LibraryRepository {
    suspend fun getLibraryBooks(): AppResult<List<LibraryBook>>
    suspend fun getContinueReading(): AppResult<LibraryBook?>
    suspend fun updateReadingProgress(bookId: String, progressPercent: Int): AppResult<ReadingProgress>
}
