package com.example.dz.presentation.store.browse

sealed interface BrowseEvent {
    data object BackClicked : BrowseEvent
    data object SearchClicked : BrowseEvent

    /** Choosing the mood that is already chosen clears it, so the grid can go back to every genre. */
    data class MoodClicked(val mood: BrowseMood) : BrowseEvent
    data object SortClicked : BrowseEvent
    data class CategoryClicked(val categoryId: String) : BrowseEvent
}
