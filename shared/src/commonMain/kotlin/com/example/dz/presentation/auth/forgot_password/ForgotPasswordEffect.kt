package com.example.dz.presentation.auth.forgot_password

sealed interface ForgotPasswordEffect {
    data object NavigateToVerification : ForgotPasswordEffect
    data object NavigateBack : ForgotPasswordEffect
}
