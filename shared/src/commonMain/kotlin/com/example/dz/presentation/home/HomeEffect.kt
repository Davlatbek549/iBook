package com.example.dz.presentation.home

sealed interface HomeEffect {
    data class NavigateToBook(val bookId: String) : HomeEffect
    data class NavigateToAuthor(val authorId: String) : HomeEffect
    data class NavigateToReading(val bookId: String) : HomeEffect
    data object NavigateToNotifications : HomeEffect
    data object NavigateToProfile : HomeEffect
}
