package com.example.dz.presentation.settings

import com.example.dz.domain.model.Appearance

data class SettingsUiState(
    val email: String = "amelia@hartwell.co",
    /** How the app is drawn: following the device, or held light or dark. */
    val appearance: Appearance = Appearance.SYSTEM,
    val isAppearancePickerVisible: Boolean = false,
    val textSize: String = "Medium",
    val dailyGoal: String = "30 min",
    val isSigningOut: Boolean = false,
    val isDeleteConfirmationVisible: Boolean = false,
    val isDeletingAccount: Boolean = false,

    /** Why the last attempt to delete failed. Whenever this is set, the account still exists. */
    val deleteAccountError: String? = null,
    val readingRemindersEnabled: Boolean = true,
    val messagesEnabled: Boolean = true,
    val priceDropsEnabled: Boolean = false
)
