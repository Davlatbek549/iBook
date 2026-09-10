package com.example.dz.presentation.auth.login

data class LoginUiState(
    val email: String = "",
    /**
     * Set when the reader has just finished a password reset. The reset ends here rather than on
     * Home — the server issues no session for one — so without a word of explanation this screen
     * looks like the reset failed and dumped them back at the start.
     */
    val passwordJustReset: Boolean = false,
    val password: String = "",
    val isLoading: Boolean = false,
    /**
     * The server's verdict, shown as a screen-level alert. Kept apart from the field errors
     * because "email or password is incorrect" deliberately does not say which one was wrong.
     */
    val errorMessage: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
)
