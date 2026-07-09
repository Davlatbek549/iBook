package com.example.dz.presentation.auth.verification

sealed interface VerificationEffect {
    data object NavigateToHome : VerificationEffect
    data object NavigateBack : VerificationEffect
}
