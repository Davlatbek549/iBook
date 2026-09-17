package com.example.dz.presentation.book.review

sealed interface BookReviewEffect {
    data object NavigateBack : BookReviewEffect
}
