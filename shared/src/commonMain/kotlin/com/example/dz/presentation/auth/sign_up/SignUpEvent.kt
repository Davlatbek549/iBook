package com.example.dz.presentation.auth.sign_up

sealed interface SignUpEvent {
    data class FullNameChanged(val fullName: String) : SignUpEvent
    data class UsernameChanged(val username: String) : SignUpEvent
    data class EmailChanged(val email: String) : SignUpEvent
    data class PasswordChanged(val password: String) : SignUpEvent
    data object CreateAccountClicked : SignUpEvent
    data object SignInClicked : SignUpEvent
    data object GoogleClicked : SignUpEvent
    data object AppleClicked : SignUpEvent
}
