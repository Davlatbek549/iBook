package com.example.dz.presentation.settings

data class SettingsUiState(
    val email: String = "amelia@hartwell.co",
    /** The app follows the system's light or dark setting; there is no override of its own yet. */
    val appearance: String = "System",
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
