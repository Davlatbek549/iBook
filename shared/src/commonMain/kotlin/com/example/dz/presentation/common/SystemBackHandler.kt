package com.example.dz.presentation.common

import androidx.compose.runtime.Composable

/**
 * Intercepts the platform's own way back while [enabled]: the back button or gesture on Android,
 * the edge swipe on iOS. Without it those go straight to the nav host, which pops the screen
 * before it can do what its on-screen back arrow does.
 */
@Composable
expect fun SystemBackHandler(enabled: Boolean, onBack: () -> Unit)
