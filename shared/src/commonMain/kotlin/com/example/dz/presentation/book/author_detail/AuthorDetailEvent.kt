package com.example.dz.presentation.book.author_detail

sealed interface AuthorDetailEvent {
    data object BackClicked : AuthorDetailEvent
    data object ShareClicked : AuthorDetailEvent
    data object FollowClicked : AuthorDetailEvent
    data object MessageClicked : AuthorDetailEvent
    data object SeeAllClicked : AuthorDetailEvent
    data class BookClicked(val bookId: String) : AuthorDetailEvent
}
