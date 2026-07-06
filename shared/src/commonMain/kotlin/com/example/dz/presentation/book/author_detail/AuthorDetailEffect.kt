package com.example.dz.presentation.book.author_detail

sealed interface AuthorDetailEffect {
    data object NavigateBack : AuthorDetailEffect
    data class NavigateToBook(val bookId: String) : AuthorDetailEffect
}
