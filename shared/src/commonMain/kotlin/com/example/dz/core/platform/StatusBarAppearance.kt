package com.example.dz.core.platform

import androidx.compose.runtime.Composable

/**
 * Asks the system to draw its status-bar content light or dark for as long as this is composed.
 *
 * The reader's night ground is the only place in the app where the screen behind the status bar is
 * dark, and the clock and battery are drawn by the system, not by us — left alone they stay dark
 * ink on a dark page and become unreadable.
 *
 * [darkBackground] describes the *page*, not the icons: pass `true` when the content behind the
 * bar is dark and its icons therefore need to be light.
 */
@Composable
expect fun StatusBarAppearance(darkBackground: Boolean)
