package com.example.dz.presentation.home

sealed interface HomeEvent {
    data class BookClicked(val bookId: String) : HomeEvent
    data class AuthorClicked(val authorId: String) : HomeEvent
    data object KeepReadingClicked : HomeEvent
    data object GoalsKeepReadingClicked : HomeEvent
    data object NotificationsClicked : HomeEvent
    data object ProfileClicked : HomeEvent
}
