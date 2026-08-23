package com.example.dz.presentation.splash

sealed interface SplashEffect {
    /** Onboarding hasn't been completed (or skipped) yet — start the three beats. */
    data object NavigateToOnboarding : SplashEffect

    /** Onboarding was already completed/skipped in a previous session — go straight on. */
    data object NavigateToSignUp : SplashEffect

    /** Returning reader tapped "Sign in". */
    data object NavigateToSignIn : SplashEffect
}
