package com.example.dz.presentation.browse

sealed interface BrowseEvent {
    data object BackClicked : BrowseEvent
    data object SearchClicked : BrowseEvent
    data class MoodClicked(val index: Int) : BrowseEvent
    data object SortClicked : BrowseEvent
    data class CategoryClicked(val categoryId: String) : BrowseEvent
}
