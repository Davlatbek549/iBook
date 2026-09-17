package com.example.dz.domain.model

/**
 * What this reader thought of a book.
 *
 * The score is required and the note is not: plenty of people will give four stars and have nothing
 * to add, and making them write something to be counted would leave the score unrecorded.
 */
data class BookReview(
    val bookId: String,
    val stars: Int,
    val note: String,
    val writtenAt: Long,
) {
    companion object {
        const val MIN_STARS = 1
        const val MAX_STARS = 5
    }
}
