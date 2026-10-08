package com.example.dz.core.platform

import androidx.compose.runtime.Composable
import com.example.dz.domain.model.Appearance

/**
 * Tells the platform which appearance the app is drawn in, so what the system draws around it
 * agrees: the status and navigation bars on Android; on iOS the window, and with it the status bar
 * and every UIKit control the app shows.
 *
 * Left alone the system draws for the device's appearance, and once the app can be held light or
 * dark against the device, the two part ways — a dark clock over a dark screen.
 *
 * [darkTheme] is what [appearance] resolved to, [Appearance.SYSTEM] included, so a platform that
 * only needs to know light or dark does not have to ask the device itself.
 */
@Composable
expect fun PlatformAppearance(appearance: Appearance, darkTheme: Boolean)
