package com.example.dz.presentation.onboarding.onboarding_three

sealed interface OnboardingThreeEvent {
    data object GetStartedClicked : OnboardingThreeEvent
    data object LoginClicked : OnboardingThreeEvent
}
