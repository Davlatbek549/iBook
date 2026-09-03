package com.example.dz.presentation.splash

sealed interface SplashEffect {
    /** A stored session was restored, so the sign-in screens are skipped. */
    data object NavigateToHome : SplashEffect

    /** Onboarding hasn't been completed (or skipped) yet — start the three beats. */
    data object NavigateToOnboarding : SplashEffect

    /** Onboarding was already completed/skipped in a previous session — go straight on. */
    data object NavigateToSignUp : SplashEffect

    /** Returning reader tapped "Sign in". */
    data object NavigateToSignIn : SplashEffect
}
