package com.example.dz.presentation.search

sealed interface SearchEvent {
    data class QueryChanged(val value: String) : SearchEvent

    /** Sent on every keystroke: searches once typing pauses. */
    data object SearchClicked : SearchEvent

    /** The keyboard's Search key: searches now, and keeps the query as a recent one. */
    data object SearchSubmitted : SearchEvent
    data class RecentSearchClicked(val query: String) : SearchEvent
    data class BookClicked(val bookId: String) : SearchEvent
    data object BackClicked : SearchEvent

    /** Back on screen, perhaps from a book that has since been added to the shelf. */
    data object Resumed : SearchEvent
}
