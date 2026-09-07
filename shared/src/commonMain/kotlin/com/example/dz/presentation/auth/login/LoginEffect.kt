package com.example.dz.presentation.auth.login

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    /** Carries whatever address has been typed, so the recovery screen does not ask for it again. */
    data class NavigateToForgotPassword(val email: String) : LoginEffect
    data object NavigateToSignUp : LoginEffect
}
