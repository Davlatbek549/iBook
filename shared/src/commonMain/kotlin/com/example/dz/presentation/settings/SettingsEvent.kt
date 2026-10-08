package com.example.dz.presentation.settings

import com.example.dz.domain.model.Appearance

sealed interface SettingsEvent {
    data object BackClicked : SettingsEvent
    data object EditProfileClicked : SettingsEvent
    data object EmailClicked : SettingsEvent
    data object PasswordClicked : SettingsEvent
    data object AppearanceClicked : SettingsEvent
    data class AppearanceChosen(val appearance: Appearance) : SettingsEvent
    data object AppearancePickerDismissed : SettingsEvent
    data object TextSizeClicked : SettingsEvent
    data object DailyGoalClicked : SettingsEvent
    data object HelpClicked : SettingsEvent
    data object TermsClicked : SettingsEvent
    data object PrivacyClicked : SettingsEvent
    data object SignOutClicked : SettingsEvent
    data object DeleteAccountClicked : SettingsEvent
    data object DeleteAccountConfirmed : SettingsEvent
    data object DeleteAccountDismissed : SettingsEvent
    data class ReadingRemindersToggled(val enabled: Boolean) : SettingsEvent
    data class MessagesToggled(val enabled: Boolean) : SettingsEvent
    data class PriceDropsToggled(val enabled: Boolean) : SettingsEvent
}
