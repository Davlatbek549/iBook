package com.example.dz.presentation.onboarding.onboarding_one

sealed interface OnboardingOneEvent {
    data object NextClicked : OnboardingOneEvent
    data object SkipClicked : OnboardingOneEvent
}
