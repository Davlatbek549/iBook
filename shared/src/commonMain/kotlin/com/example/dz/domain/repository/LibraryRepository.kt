package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.ReadingProgress

interface LibraryRepository {
    /**
     * Puts [book] on the shelf, and leaves it alone if it is already there.
     *
     * Everything else here updates a row that has to exist first — [updateReadingProgress] writes
     * to nothing at all if the book was never shelved — so this is what opens a book's account.
     */
    suspend fun add(book: Book): AppResult<Unit>

    suspend fun getLibraryBooks(): AppResult<List<LibraryBook>>
    suspend fun getContinueReading(): AppResult<LibraryBook?>
    suspend fun updateReadingProgress(bookId: String, progressPercent: Int): AppResult<ReadingProgress>
}
