package com.example.dz.presentation.search

sealed interface SearchEffect {
    data object NavigateBack : SearchEffect
    data class NavigateToBook(val bookId: String) : SearchEffect
}
