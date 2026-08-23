package com.example.dz.presentation.auth.login

data class LoginUiState(
    val email: String = "",
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
