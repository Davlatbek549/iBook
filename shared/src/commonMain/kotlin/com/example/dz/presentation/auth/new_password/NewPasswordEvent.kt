package com.example.dz.presentation.auth.new_password

sealed interface NewPasswordEvent {
    data class PasswordChanged(val password: String) : NewPasswordEvent
    data class ConfirmationChanged(val confirmation: String) : NewPasswordEvent
    data object SaveClicked : NewPasswordEvent

    /** Leaves for the sign-in screen, once the password has actually changed. */
    data object SignInClicked : NewPasswordEvent
    data object BackClicked : NewPasswordEvent
}
