package com.example.dz.presentation.auth.verification

sealed interface VerificationEffect {
    data object NavigateToHome : VerificationEffect

    /**
     * Reset only. The code travels on rather than being spent here: the server allows a fixed
     * number of guesses against it, and `/auth/password/reset` needs one of them for the change
     * itself — see [VerificationViewModel].
     */
    data class NavigateToNewPassword(val email: String, val code: String) : VerificationEffect
    data object NavigateBack : VerificationEffect
}
