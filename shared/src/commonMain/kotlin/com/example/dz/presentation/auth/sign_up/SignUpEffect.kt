package com.example.dz.presentation.auth.sign_up

sealed interface SignUpEffect {
    data object NavigateToVerification : SignUpEffect
    data object NavigateToLogin : SignUpEffect
}
