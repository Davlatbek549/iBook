package com.example.dz.presentation.auth.forgot_password

sealed interface ForgotPasswordEffect {
    /** Carries the address so the code screen can name it instead of saying "your email". */
    data class NavigateToVerification(val email: String) : ForgotPasswordEffect
    data object NavigateBack : ForgotPasswordEffect
}
