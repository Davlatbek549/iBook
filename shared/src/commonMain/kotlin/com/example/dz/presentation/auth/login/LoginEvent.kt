package com.example.dz.presentation.auth.login

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object SignInClicked : LoginEvent
    data object ForgotPasswordClicked : LoginEvent
    data object SignUpClicked : LoginEvent
    data object GoogleClicked : LoginEvent
    data object AppleClicked : LoginEvent
}
