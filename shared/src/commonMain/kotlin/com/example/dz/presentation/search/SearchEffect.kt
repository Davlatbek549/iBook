package com.example.dz.presentation.search

sealed interface SearchEffect {
    data class NavigateToBook(val bookId: String) : SearchEffect
    data class NavigateToAuthor(val authorId: String) : SearchEffect
    data class NavigateToCategory(val categoryId: String) : SearchEffect
}
