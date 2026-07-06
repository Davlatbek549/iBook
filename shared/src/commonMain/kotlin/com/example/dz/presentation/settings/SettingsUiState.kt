package com.example.dz.presentation.settings

data class SettingsUiState(
    val email: String = "amelia@hartwell.co",
    val appearance: String = "Light",
    val textSize: String = "Medium",
    val dailyGoal: String = "30 min",
    val readingRemindersEnabled: Boolean = true,
    val messagesEnabled: Boolean = true,
    val priceDropsEnabled: Boolean = false
)
