package com.example.dz.presentation.settings

sealed interface SettingsEvent {
    data object BackClicked : SettingsEvent
    data object EditProfileClicked : SettingsEvent
    data object EmailClicked : SettingsEvent
    data object PasswordClicked : SettingsEvent
    data object AppearanceClicked : SettingsEvent
    data object TextSizeClicked : SettingsEvent
    data object DailyGoalClicked : SettingsEvent
    data object HelpClicked : SettingsEvent
    data object TermsClicked : SettingsEvent
    data object PrivacyClicked : SettingsEvent
    data object SignOutClicked : SettingsEvent
    data class ReadingRemindersToggled(val enabled: Boolean) : SettingsEvent
    data class MessagesToggled(val enabled: Boolean) : SettingsEvent
    data class PriceDropsToggled(val enabled: Boolean) : SettingsEvent
}
