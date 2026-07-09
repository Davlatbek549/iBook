package com.example.dz.presentation.collections.details

sealed interface CollectionDetailsEvent {
    data object BackClicked : CollectionDetailsEvent
    data object AddBooksClicked : CollectionDetailsEvent
    data object EditClicked : CollectionDetailsEvent
    data object ShareClicked : CollectionDetailsEvent
    data class BookClicked(val bookId: String) : CollectionDetailsEvent
    data class BookOptionsClicked(val bookId: String) : CollectionDetailsEvent
}
