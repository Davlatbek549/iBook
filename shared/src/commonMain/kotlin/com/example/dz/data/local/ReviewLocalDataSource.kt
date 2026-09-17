package com.example.dz.data.local

import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.BookReview

/** The reader's own reviews, one per book, in the device database. */
class ReviewLocalDataSource(database: DzDatabase) {
    private val queries = database.bookReviewQueries

    fun getReview(bookId: String): BookReview? =
        queries.selectForBook(bookId).executeAsOneOrNull()?.let { row ->
            BookReview(
                bookId = row.book_id,
                stars = row.stars.toInt(),
                note = row.note,
                writtenAt = row.written_at,
            )
        }

    fun save(review: BookReview) {
        queries.upsert(
            book_id = review.bookId,
            stars = review.stars.toLong(),
            note = review.note,
            written_at = review.writtenAt,
        )
    }

    fun delete(bookId: String) {
        queries.deleteForBook(bookId)
    }

    fun clear() {
        queries.deleteAll()
    }
}
