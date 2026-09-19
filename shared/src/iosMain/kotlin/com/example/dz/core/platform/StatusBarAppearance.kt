package com.example.dz.core.platform

import androidx.compose.runtime.Composable

/**
 * No-op on iOS.
 *
 * The status-bar style there belongs to the view controller hosting Compose, which is created in
 * `iosApp` and cannot be reached from a composable. Wiring it needs a hook on that controller;
 * until there is one, this does nothing rather than pretending to.
 */
@Composable
actual fun StatusBarAppearance(darkBackground: Boolean) = Unit
