package com.example.dz.presentation.onboarding

sealed interface OnboardingEffect {
    /** Skip or Start was pressed — onboarding is complete, hand off to account creation. */
    data object NavigateToSignUp : OnboardingEffect
}
