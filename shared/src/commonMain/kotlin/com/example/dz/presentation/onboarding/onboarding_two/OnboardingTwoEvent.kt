package com.example.dz.presentation.onboarding.onboarding_two

sealed interface OnboardingTwoEvent {
    data object NextClicked : OnboardingTwoEvent
    data object SkipClicked : OnboardingTwoEvent
}
