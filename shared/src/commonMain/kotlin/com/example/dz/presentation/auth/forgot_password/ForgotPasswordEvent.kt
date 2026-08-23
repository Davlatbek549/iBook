package com.example.dz.presentation.auth.forgot_password

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val email: String) : ForgotPasswordEvent
    data object SendLinkClicked : ForgotPasswordEvent

    /** Moves on to code entry, once a code has actually been asked for. */
    data object ContinueClicked : ForgotPasswordEvent
    data object BackClicked : ForgotPasswordEvent
}
