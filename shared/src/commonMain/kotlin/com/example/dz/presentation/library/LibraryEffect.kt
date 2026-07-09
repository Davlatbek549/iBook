package com.example.dz.presentation.library

sealed interface LibraryEffect {
    data class NavigateToBook(val bookId: String) : LibraryEffect
    data object NavigateToGoal : LibraryEffect
    data object OpenSort : LibraryEffect
}
