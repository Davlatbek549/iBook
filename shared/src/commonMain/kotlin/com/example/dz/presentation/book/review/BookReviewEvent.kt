package com.example.dz.presentation.book.review

sealed interface BookReviewEvent {
    data object BackClicked : BookReviewEvent
    data object RetryClicked : BookReviewEvent

    /** Opens the write sheet, holding whatever the reader said last time. */
    data object WriteReviewClicked : BookReviewEvent
    data class EditorStarsChanged(val stars: Int) : BookReviewEvent
    data class EditorNoteChanged(val note: String) : BookReviewEvent
    data object EditorDismissed : BookReviewEvent
    data object EditorSaved : BookReviewEvent
    data object DeleteReviewClicked : BookReviewEvent
}
