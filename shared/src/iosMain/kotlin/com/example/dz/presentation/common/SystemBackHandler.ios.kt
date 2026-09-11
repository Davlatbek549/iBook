package com.example.dz.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler

/**
 * The edge swipe reaches the same dispatcher the nav host listens on, and this handler, added
 * after the nav host's, is asked first. The compat handler is deprecated in favour of
 * NavigationEventHandler, which is not on this target's classpath yet.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Suppress("DEPRECATION")
@Composable
actual fun SystemBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}
