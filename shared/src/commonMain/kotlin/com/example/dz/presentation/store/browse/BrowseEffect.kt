package com.example.dz.presentation.store.browse

sealed interface BrowseEffect {
    data object NavigateBack : BrowseEffect
    data object NavigateToSearch : BrowseEffect
    data class NavigateToCategory(val categoryId: String) : BrowseEffect
}
