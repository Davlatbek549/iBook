package com.example.dz.presentation.home

sealed interface HomeEvent {
    data class BookClicked(val bookId: String) : HomeEvent
    data object KeepReadingClicked : HomeEvent
    data object PresenceClicked : HomeEvent
    data object ProfileClicked : HomeEvent
}
