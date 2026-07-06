package com.example.dz.presentation.collections.list

sealed interface CollectionsEvent {
    data object BackClicked : CollectionsEvent
    data object SearchClicked : CollectionsEvent
    data object NewCollectionClicked : CollectionsEvent
    data class CollectionClicked(val collectionId: String) : CollectionsEvent
}
