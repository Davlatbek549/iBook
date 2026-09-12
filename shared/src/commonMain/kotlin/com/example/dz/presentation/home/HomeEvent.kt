package com.example.dz.presentation.home

sealed interface HomeEvent {
    data class BookClicked(val bookId: String) : HomeEvent
    data object KeepReadingClicked : HomeEvent
    data object PresenceClicked : HomeEvent
    data object GoalClicked : HomeEvent

    /**
     * Home came back to the foreground. Only the on-device figures are re-read — minutes, the
     * shelf, the book in progress — because those are what reading changes. The catalogue is not
     * refetched on every return to the tab.
     */
    data object Resumed : HomeEvent
    data object ProfileClicked : HomeEvent
}
