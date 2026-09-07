package com.example.dz.presentation.auth.forgot_password

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val email: String) : ForgotPasswordEvent
    data object SendLinkClicked : ForgotPasswordEvent
    data object BackClicked : ForgotPasswordEvent
}
