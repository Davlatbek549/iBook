package com.example.dz.presentation.splash

sealed interface SplashEffect {
    /** A stored session was restored, so the sign-in screens are skipped. */
    data object NavigateToHome : SplashEffect

    /**
     * A stored session whose address was never proved. The server refuses everything else until
     * it is, so opening Home would show a shell whose every request comes back refused.
     */
    data class NavigateToVerification(val email: String) : SplashEffect

    /** Onboarding hasn't been completed (or skipped) yet — start the three beats. */
    data object NavigateToOnboarding : SplashEffect

    /** Onboarding was already completed/skipped in a previous session — go straight on. */
    data object NavigateToSignUp : SplashEffect
}
