package com.example.dz.presentation.library

sealed interface LibraryEvent {
    data class BookClicked(val bookId: String) : LibraryEvent
    data class ProgressChanged(val bookId: String, val progressPercent: Int) : LibraryEvent
    data object GoalClicked : LibraryEvent
    data object SortClicked : LibraryEvent
}
