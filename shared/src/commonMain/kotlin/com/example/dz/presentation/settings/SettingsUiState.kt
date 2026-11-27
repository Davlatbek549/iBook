package com.example.dz.presentation.settings

import com.example.dz.domain.model.PageTheme

data class SettingsUiState(
    val name: String = "",
    val email: String = "",
    val membershipLabel: String = "Free plan",
    val useSerif: Boolean = false,
    val pageTheme: PageTheme = PageTheme.CREAM,
    val isAutoNightMode: Boolean = false,
    val isKeepScreenAwake: Boolean = false,
    // Neither a download-network preference nor a shelf-visibility scope is stored anywhere yet —
    // these two carry a default rather than nothing, the same way the Ink screen showed something
    // rather than a blank row.
    val isWifiOnlyDownloads: Boolean = true,
    val isShelvesFriendsOnly: Boolean = true,
    val isSigningOut: Boolean = false,
    val isDeleteConfirmationVisible: Boolean = false,
    val isDeletingAccount: Boolean = false,
    /** Why the last attempt to delete failed. Whenever this is set, the account still exists. */
    val deleteAccountError: String? = null,
    val isDailyGoalNudgeEnabled: Boolean = true,
    val isFriendActivityEnabled: Boolean = true,
    val isStoreOffersEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    /** Just back from a saved Edit Profile: show "Profile updated" over the list. */
    val showProfileUpdated: Boolean = false,
) {
    /** The header avatar's letter, standing in for a photo. */
    val avatarInitial: String get() = name.trim().firstOrNull()?.uppercase() ?: "?"
}
