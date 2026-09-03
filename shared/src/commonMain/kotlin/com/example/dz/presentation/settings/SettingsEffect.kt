package com.example.dz.presentation.settings

sealed interface SettingsEffect {
    data object NavigateBack : SettingsEffect
    data object NavigateToEditProfile : SettingsEffect

    /** The session is gone by the time this is emitted, so the back stack must go with it. */
    data object NavigateToLogin : SettingsEffect
}
