package com.example.dz.presentation.reading

sealed interface ReadingEffect {
    data object NavigateBack : ReadingEffect
    data object NavigateToSettings : ReadingEffect
    data class NavigateToComments(val bookId: String) : ReadingEffect
}
