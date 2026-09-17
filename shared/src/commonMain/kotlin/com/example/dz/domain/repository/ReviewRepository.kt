package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.BookReview

/** The reader's own reviews. Everyone else's would need a server, and there is not one. */
interface ReviewRepository {
    suspend fun getReview(bookId: String): AppResult<BookReview?>

    /** Writes [stars] and [note] for a book, replacing whatever was there. */
    suspend fun saveReview(bookId: String, stars: Int, note: String): AppResult<BookReview>

    suspend fun deleteReview(bookId: String): AppResult<Unit>
}
