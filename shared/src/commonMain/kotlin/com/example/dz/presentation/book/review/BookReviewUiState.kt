package com.example.dz.presentation.book.review

import org.jetbrains.compose.resources.DrawableResource

data class BookReviewUiState(
    val bookId: String = "",
    val bookTitle: String = "",
    val bookAuthor: String = "",
    val averageRating: String = "4.6",
    val ratingsCount: String = "1,284 ratings",
    val ratingBars: List<Float> = listOf(0.72f, 0.18f, 0.06f, 0.03f, 0.01f),
    val reviews: List<BookReviewItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class BookReviewItemUi(
    val id: String,
    val avatarRes: DrawableResource,
    val name: String,
    val date: String,
    val stars: Int,
    val text: String,
    val helpfulCount: Int,
    val markedHelpful: Boolean
)
