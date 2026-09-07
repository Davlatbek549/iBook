package com.example.dz.presentation.onboarding

sealed interface OnboardingEvent {
    /**
     * The only way out, from the last page. There is no Skip: this is the one screen a reader
     * sees before they know what DZ is, and offering to skip it is the app saying it is not worth
     * the three swipes. The pager still lets anyone move as fast as they like.
     */
    data object StartClicked : OnboardingEvent
}
