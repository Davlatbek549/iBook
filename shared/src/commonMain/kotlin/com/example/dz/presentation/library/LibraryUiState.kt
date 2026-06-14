package com.example.dz.presentation.library

import com.example.dz.domain.model.LibraryBook

data class LibraryUiState(
    val books: List<LibraryBook> = emptyList(),
    val continueReading: LibraryBook? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
