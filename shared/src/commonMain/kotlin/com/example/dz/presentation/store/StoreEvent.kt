package com.example.dz.presentation.store

sealed interface StoreEvent {
    data class CategoryClicked(val categoryId: String) : StoreEvent
    data class BookClicked(val bookId: String) : StoreEvent
    data object ViewMoreClicked : StoreEvent
}
