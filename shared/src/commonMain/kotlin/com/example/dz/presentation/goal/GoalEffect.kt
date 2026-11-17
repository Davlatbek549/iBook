package com.example.dz.presentation.goal

sealed interface GoalEffect {
    data object NavigateBack : GoalEffect
    data class NavigateToReading(val bookId: String) : GoalEffect
}
