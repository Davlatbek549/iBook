package com.example.dz.presentation.book

import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Review

data class BookUiState(
    val book: Book? = null,
    val relatedBooks: List<Book> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
