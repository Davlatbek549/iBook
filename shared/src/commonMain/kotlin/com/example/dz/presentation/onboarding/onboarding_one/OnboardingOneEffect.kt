package com.example.dz.presentation.onboarding.onboarding_one

sealed interface OnboardingOneEffect {
    data object NavigateToNext : OnboardingOneEffect
    data object NavigateToLogin : OnboardingOneEffect
}
