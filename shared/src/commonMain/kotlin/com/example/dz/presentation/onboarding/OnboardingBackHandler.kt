package com.example.dz.presentation.onboarding

import androidx.compose.runtime.Composable

/**
 * Intercepts the platform back gesture/button while enabled. On Android this
 * is the hardware/gesture back action; other platforms have no equivalent
 * hardware control, so the actual is a no-op there.
 */
@Composable
expect fun OnboardingBackHandler(enabled: Boolean, onBack: () -> Unit)
