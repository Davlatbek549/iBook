package com.example.dz.presentation.goal

sealed interface GoalEvent {
    data object BackClicked : GoalEvent
    data object EditGoalClicked : GoalEvent
    data object FinishItClicked : GoalEvent
}
