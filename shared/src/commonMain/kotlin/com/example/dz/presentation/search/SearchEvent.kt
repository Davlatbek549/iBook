package com.example.dz.presentation.search

sealed interface SearchEvent {
    data class QueryChanged(val value: String) : SearchEvent
    data object SearchClicked : SearchEvent
    data class BookClicked(val bookId: String) : SearchEvent
    data class AuthorClicked(val authorId: String) : SearchEvent
    data class CategoryClicked(val categoryId: String) : SearchEvent
}
