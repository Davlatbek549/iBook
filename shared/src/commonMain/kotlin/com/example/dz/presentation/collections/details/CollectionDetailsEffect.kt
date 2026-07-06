package com.example.dz.presentation.collections.details

sealed interface CollectionDetailsEffect {
    data object NavigateBack : CollectionDetailsEffect
    data class NavigateToEdit(val collectionId: String) : CollectionDetailsEffect
    data class NavigateToBook(val bookId: String) : CollectionDetailsEffect
}
