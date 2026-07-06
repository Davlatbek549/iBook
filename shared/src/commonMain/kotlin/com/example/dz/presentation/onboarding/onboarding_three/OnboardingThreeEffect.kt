package com.example.dz.presentation.onboarding.onboarding_three

sealed interface OnboardingThreeEffect {
    data object NavigateToSignUp : OnboardingThreeEffect
    data object NavigateToLogin : OnboardingThreeEffect
}
