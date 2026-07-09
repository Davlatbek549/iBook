package com.example.dz.presentation.book.review

sealed interface BookReviewEvent {
    data object BackClicked : BookReviewEvent
    data object ShareClicked : BookReviewEvent
    data object WriteReviewClicked : BookReviewEvent
    data object StartReadingClicked : BookReviewEvent
    data class HelpfulToggled(val reviewId: String) : BookReviewEvent
}
