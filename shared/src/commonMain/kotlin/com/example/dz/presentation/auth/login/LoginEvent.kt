package com.example.dz.presentation.auth.login

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object SignInClicked : LoginEvent
    data object ForgotPasswordClicked : LoginEvent
    data object SignUpClicked : LoginEvent
    data object GoogleClicked : LoginEvent
    data object AppleClicked : LoginEvent

    /** The account picker succeeded; the server still has to prove the token. */
    data class GoogleTokenReceived(val idToken: String) : LoginEvent

    /** The picker failed. Cancelling sends null, which clears busy without an error. */
    data class GoogleSignInFailed(val message: String?) : LoginEvent
}
