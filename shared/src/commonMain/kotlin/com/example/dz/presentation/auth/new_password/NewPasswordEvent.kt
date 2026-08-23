package com.example.dz.presentation.auth.new_password

sealed interface NewPasswordEvent {
    data class PasswordChanged(val password: String) : NewPasswordEvent
    data class ConfirmationChanged(val confirmation: String) : NewPasswordEvent
    data object SaveClicked : NewPasswordEvent
    data object BackClicked : NewPasswordEvent
}
