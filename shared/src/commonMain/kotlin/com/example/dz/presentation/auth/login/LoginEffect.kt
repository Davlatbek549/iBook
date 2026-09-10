package com.example.dz.presentation.auth.login

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect

    /**
     * Signed in, but to an account that has never proved its address. The server refuses such an
     * account everything else, and the splash sends it to the code screen on every launch — so it
     * goes there now, rather than to a Home it would be turned away from on the next launch.
     */
    data class NavigateToVerification(val email: String) : LoginEffect
    /** Carries whatever address has been typed, so the recovery screen does not ask for it again. */
    data class NavigateToForgotPassword(val email: String) : LoginEffect
    data object NavigateToSignUp : LoginEffect
}
