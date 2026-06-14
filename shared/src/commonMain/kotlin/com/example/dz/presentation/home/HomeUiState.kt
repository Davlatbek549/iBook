package com.example.dz.presentation.home

import com.example.dz.domain.model.Book
import com.example.dz.domain.model.LibraryBook

data class HomeUiState(
    val books: List<Book> = emptyList(),
    val continueReading: LibraryBook? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
