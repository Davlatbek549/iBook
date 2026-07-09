package com.example.dz.presentation.onboarding.onboarding_two

sealed interface OnboardingTwoEffect {
    data object NavigateToNext : OnboardingTwoEffect
    data object NavigateToLogin : OnboardingTwoEffect
}
