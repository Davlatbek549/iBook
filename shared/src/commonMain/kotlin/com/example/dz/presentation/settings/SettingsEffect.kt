package com.example.dz.presentation.settings

sealed interface SettingsEffect {
    data object NavigateBack : SettingsEffect
    data object NavigateToEditProfile : SettingsEffect
}
