package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.ReviewLocalDataSource
import com.example.dz.domain.model.BookReview
import com.example.dz.domain.repository.ReviewRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Reviews in the device database, on [io] like every other query here — the data source is
 * synchronous and its callers draw the screen.
 */
class LocalReviewRepository(
    private val reviews: ReviewLocalDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : ReviewRepository {

    override suspend fun getReview(bookId: String): AppResult<BookReview?> = withContext(io) {
        AppResult.Success(reviews.getReview(bookId))
    }

    override suspend fun saveReview(
        bookId: String,
        stars: Int,
        note: String,
    ): AppResult<BookReview> = withContext(io) {
        val review = BookReview(
            bookId = bookId,
            stars = stars.coerceIn(BookReview.MIN_STARS, BookReview.MAX_STARS),
            note = note.trim(),
            // Edited is dated: the screen shows when the reader last said this, not when they
            // first said something about this book.
            writtenAt = currentEpochMillis(),
        )
        reviews.save(review)
        AppResult.Success(review)
    }

    override suspend fun deleteReview(bookId: String): AppResult<Unit> = withContext(io) {
        reviews.delete(bookId)
        AppResult.Success(Unit)
    }
}
