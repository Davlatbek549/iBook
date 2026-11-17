package com.example.dz.presentation.search

sealed interface SearchEvent {
    data class QueryChanged(val value: String) : SearchEvent
    data object SearchClicked : SearchEvent
    data object BackClicked : SearchEvent
    data class BookClicked(val bookId: String) : SearchEvent
    data class RecentSearchClicked(val query: String) : SearchEvent
    /** The screen regained focus — a book opened from these results may now be on the shelf. */
    data object Resumed : SearchEvent
}
