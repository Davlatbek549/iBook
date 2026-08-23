package com.example.dz.presentation.auth.forgot_password

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailError: String? = null,
    /**
     * The address a code was requested for. Non-null switches the screen to its confirmed state,
     * which is shown in place rather than by navigating: leaving the screen on tap means a typo
     * can only be found by going back, and the reader never sees where the code went.
     */
    val sentTo: String? = null,
)
