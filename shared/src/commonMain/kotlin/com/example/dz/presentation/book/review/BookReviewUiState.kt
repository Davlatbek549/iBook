package com.example.dz.presentation.book.review

import com.example.dz.domain.model.BookRatings

/**
 * Ratings, and the reader's own review.
 *
 * There is no third thing here on purpose. Other readers' reviews would have to come from a server,
 * and no catalogue the app reads carries review text — so rather than a list of invented strangers,
 * this screen shows the scores OpenLibrary actually holds and whatever the reader said themselves.
 */
data class BookReviewUiState(
    val bookId: String = "",
    val bookTitle: String = "",
    val bookAuthor: String = "",
    /** `null` when nobody has rated this book, which is every Project Gutenberg title. */
    val ratings: BookRatings? = null,
    val myReview: MyReviewUi? = null,
    /** Non-null while the write sheet is open. */
    val editor: ReviewEditorUiState? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

data class MyReviewUi(
    val stars: Int,
    val note: String,
    /** The day it was last written, already in words. */
    val writtenOn: String,
)

/** The write sheet's own state — what is in it, not what has been saved. */
data class ReviewEditorUiState(
    val stars: Int = 0,
    val note: String = "",
    /** True when this is a review being changed rather than a first one. */
    val isEditing: Boolean = false,
) {
    /** A score is the review; the note is optional. Nothing is saved without one. */
    val canSave: Boolean get() = stars > 0
}
