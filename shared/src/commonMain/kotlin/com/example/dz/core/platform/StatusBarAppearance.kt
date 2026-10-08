package com.example.dz.core.platform

import androidx.compose.runtime.Composable

/**
 * Asks the system to draw its status-bar content light or dark for as long as this is composed.
 *
 * Everywhere else the app follows the system's appearance, and the system draws its clock and
 * battery to match. The reader is the exception: its page is the ground the reader chose, so a
 * night page can sit under a daylight status bar, or a cream one under a dark one — and left alone
 * the system's ink is then the same tone as the page and becomes unreadable.
 *
 * [darkBackground] describes the *page*, not the icons: pass `true` when the content behind the
 * bar is dark and its icons therefore need to be light.
 */
@Composable
expect fun StatusBarAppearance(darkBackground: Boolean)
