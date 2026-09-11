package com.example.dz.presentation.settings

data class SettingsUiState(
    val email: String = "amelia@hartwell.co",
    val appearance: String = "Light",
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
