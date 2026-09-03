package com.example.dz.presentation.onboarding

import androidx.compose.runtime.Composable

/** iOS has no hardware/gesture back button to intercept for this flow — no-op. */
@Composable
actual fun OnboardingBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // Intentionally empty.
}
