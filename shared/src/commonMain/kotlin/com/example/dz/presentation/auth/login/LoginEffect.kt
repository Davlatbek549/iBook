package com.example.dz.presentation.auth.login

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data object NavigateToForgotPassword : LoginEffect
    data object NavigateToSignUp : LoginEffect
}
