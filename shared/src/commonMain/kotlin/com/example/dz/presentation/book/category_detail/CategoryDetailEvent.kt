package com.example.dz.presentation.book.category_detail

sealed interface CategoryDetailEvent {
    data object BackClicked : CategoryDetailEvent
    data object SearchClicked : CategoryDetailEvent
    data object SortClicked : CategoryDetailEvent
    data class BookClicked(val bookId: String) : CategoryDetailEvent
    data class BookmarkClicked(val bookId: String) : CategoryDetailEvent
}
