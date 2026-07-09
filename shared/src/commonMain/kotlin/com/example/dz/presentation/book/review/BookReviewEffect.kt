package com.example.dz.presentation.book.review

sealed interface BookReviewEffect {
    data object NavigateBack : BookReviewEffect
    data class NavigateToReading(val bookId: String) : BookReviewEffect
}
