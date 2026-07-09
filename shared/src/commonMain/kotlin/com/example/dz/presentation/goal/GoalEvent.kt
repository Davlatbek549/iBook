package com.example.dz.presentation.goal

sealed interface GoalEvent {
    data object BackClicked : GoalEvent
    data object SettingsClicked : GoalEvent
    data object EditGoalClicked : GoalEvent
}
