package com.example.dz.presentation.library

sealed interface LibraryEffect {
    data class NavigateToBook(val bookId: String) : LibraryEffect
    data class NavigateToCollection(val collectionId: String) : LibraryEffect
    data object NavigateToCollections : LibraryEffect
    data object NavigateToSearch : LibraryEffect
    data object NavigateToGoal : LibraryEffect
}
