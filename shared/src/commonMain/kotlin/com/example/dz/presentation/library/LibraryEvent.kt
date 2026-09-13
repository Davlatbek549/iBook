package com.example.dz.presentation.library

sealed interface LibraryEvent {
    data class BookClicked(val bookId: String) : LibraryEvent
    data class ProgressChanged(val bookId: String, val progressPercent: Int) : LibraryEvent
    data class FilterSelected(val filter: LibraryFilter) : LibraryEvent
    data class CollectionClicked(val collectionId: String) : LibraryEvent
    data object CollectionsClicked : LibraryEvent
    data object SearchClicked : LibraryEvent
    data object GoalClicked : LibraryEvent
    /** Library came back to the foreground; reading elsewhere may have moved a book between shelves. */
    data object Resumed : LibraryEvent
}
