package com.example.dz.presentation.store

import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category

data class StoreUiState(
    val featuredBooks: List<Book> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
