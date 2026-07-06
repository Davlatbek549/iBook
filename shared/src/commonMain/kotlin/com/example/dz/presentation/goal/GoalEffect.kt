package com.example.dz.presentation.goal

sealed interface GoalEffect {
    data object NavigateBack : GoalEffect
    data object NavigateToSettings : GoalEffect
}
