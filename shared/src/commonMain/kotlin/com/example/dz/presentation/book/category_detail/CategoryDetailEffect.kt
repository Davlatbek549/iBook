package com.example.dz.presentation.book.category_detail

sealed interface CategoryDetailEffect {
    data object NavigateBack : CategoryDetailEffect
    data class NavigateToBook(val bookId: String) : CategoryDetailEffect
}
