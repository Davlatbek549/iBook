package com.example.dz.presentation.auth.sign_up

sealed interface SignUpEvent {
    data class FullNameChanged(val fullName: String) : SignUpEvent
    data class EmailChanged(val email: String) : SignUpEvent
    data class PasswordChanged(val password: String) : SignUpEvent
    data class TermsToggled(val accepted: Boolean) : SignUpEvent
    data object CreateAccountClicked : SignUpEvent
    data object SignInClicked : SignUpEvent
    /** Opens the document in a sheet; agreeing there ticks the box. */
    data object TermsClicked : SignUpEvent
    data object PrivacyClicked : SignUpEvent
    data object DocumentDismissed : SignUpEvent
    data object DocumentAgreed : SignUpEvent
    data object GoogleClicked : SignUpEvent
    data object AppleClicked : SignUpEvent

    /** The account picker succeeded; the server still has to prove the token. */
    data class GoogleTokenReceived(val idToken: String) : SignUpEvent

    /** The picker failed. Cancelling sends null, which clears busy without an error. */
    data class GoogleSignInFailed(val message: String?) : SignUpEvent
}
