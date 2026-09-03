package com.example.dz.presentation.onboarding

sealed interface OnboardingEvent {
    /** Available on every page — jumps straight past onboarding. */
    data object SkipClicked : OnboardingEvent

    /** Only available on the last page (page index 2) — finishes onboarding. */
    data object StartClicked : OnboardingEvent
}
