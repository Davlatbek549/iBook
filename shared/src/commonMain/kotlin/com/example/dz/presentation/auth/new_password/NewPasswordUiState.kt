package com.example.dz.presentation.auth.new_password

data class NewPasswordUiState(
    /** Whose password is being reset; carried through from the code screen. */
    val email: String = "",
    val password: String = "",
    val confirmation: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    /** Sits under the confirmation box — both rules it can break belong to that field. */
    val confirmationError: String? = null,
    /**
     * Set once the server has taken the new password. The screen confirms in place rather than
     * navigating on success, matching the Forgot password screen: the reader is told what
     * happened before they are moved, and the button becomes the way on.
     */
    val isSaved: Boolean = false,
)
