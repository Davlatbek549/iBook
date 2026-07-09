package com.example.dz.presentation.collections.edit

sealed interface CollectionsEditEvent {
    data object BackClicked : CollectionsEditEvent
    data object SaveClicked : CollectionsEditEvent
    data object DeleteClicked : CollectionsEditEvent
    data class NameChanged(val value: String) : CollectionsEditEvent
    data class DescriptionChanged(val value: String) : CollectionsEditEvent
    data class VisibilityChanged(val value: Boolean) : CollectionsEditEvent
    data class BookRemoved(val bookId: String) : CollectionsEditEvent
}
