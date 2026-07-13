package com.example.dz.presentation.reading

sealed interface ReadingEvent {
    data object BackClicked : ReadingEvent
    data object MenuClicked : ReadingEvent
    data object CommentsClicked : ReadingEvent
    data object BookmarkToggled : ReadingEvent
    data object NextPageClicked : ReadingEvent
    data object PreviousPageClicked : ReadingEvent
    data object RetryClicked : ReadingEvent
}
