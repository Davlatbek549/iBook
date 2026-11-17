package com.example.dz.presentation.settings

sealed interface SettingsEvent {
    data object BackClicked : SettingsEvent
    data object EditProfileClicked : SettingsEvent
    data object TypeAndThemeClicked : SettingsEvent
    data class AutoNightModeToggled(val enabled: Boolean) : SettingsEvent
    data class KeepScreenAwakeToggled(val enabled: Boolean) : SettingsEvent
    data object DownloadsClicked : SettingsEvent
    data class DailyGoalNudgeToggled(val enabled: Boolean) : SettingsEvent
    data class FriendActivityToggled(val enabled: Boolean) : SettingsEvent
    data class StoreOffersToggled(val enabled: Boolean) : SettingsEvent
    data object ShelvesPrivacyClicked : SettingsEvent
    data object HelpClicked : SettingsEvent
    data object TermsClicked : SettingsEvent
    data object SignOutClicked : SettingsEvent
    data object DeleteAccountClicked : SettingsEvent
    data object DeleteAccountConfirmed : SettingsEvent
    data object DeleteAccountDismissed : SettingsEvent

    /** Edit Profile saved and came back here: reload the account card and say so. */
    data object ProfileUpdated : SettingsEvent
    data object ProfileUpdatedShown : SettingsEvent
}
