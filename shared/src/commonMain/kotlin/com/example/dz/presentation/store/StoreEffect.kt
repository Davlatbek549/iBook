package com.example.dz.presentation.store

sealed interface StoreEffect {
    data class NavigateToBook(val bookId: String) : StoreEffect
    data class NavigateToCategory(val categoryId: String) : StoreEffect
    data object NavigateToMembership : StoreEffect
}
