package com.example.dz.presentation.book

sealed interface BookEvent {
    data class BookSelected(val bookId: String) : BookEvent
    data class AuthorSelected(val authorId: String) : BookEvent
    data object BackClicked : BookEvent
    data object PurchaseClicked : BookEvent
    data object StartReadingClicked : BookEvent
}
