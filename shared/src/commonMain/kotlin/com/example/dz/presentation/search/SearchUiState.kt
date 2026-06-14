package com.example.dz.presentation.search

import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category

data class SearchUiState(
    val query: String = "",
    val books: List<Book> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
