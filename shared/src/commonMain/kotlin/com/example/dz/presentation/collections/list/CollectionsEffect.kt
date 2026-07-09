package com.example.dz.presentation.collections.list

sealed interface CollectionsEffect {
    data object NavigateBack : CollectionsEffect
    data class NavigateToDetail(val collectionId: String) : CollectionsEffect
    data class NavigateToEdit(val collectionId: String) : CollectionsEffect
}
