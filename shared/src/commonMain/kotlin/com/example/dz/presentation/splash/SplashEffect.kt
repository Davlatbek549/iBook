package com.example.dz.presentation.splash

sealed interface SplashEffect {
    /** A stored session was restored, so the sign-in screens are skipped. */
    data object NavigateToHome : SplashEffect
    data object NavigateToOnboarding : SplashEffect
}
