package com.example.dz.presentation.auth.verification

sealed interface VerificationEffect {
    data object NavigateToHome : VerificationEffect

    /** Reset only: the code is spent, now the password has to actually change. */
    data class NavigateToNewPassword(val email: String) : VerificationEffect
    data object NavigateBack : VerificationEffect
}
