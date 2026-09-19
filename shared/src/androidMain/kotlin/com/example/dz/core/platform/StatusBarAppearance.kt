package com.example.dz.core.platform

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Sets the window's status-bar appearance, and puts it back on the way out so the screen underneath
 * is not left with the reader's settings.
 */
@Composable
actual fun StatusBarAppearance(darkBackground: Boolean) {
    val view = LocalView.current
    DisposableEffect(view, darkBackground) {
        val window = (view.context as? Activity)?.window
        if (window == null) {
            onDispose { }
        } else {
            val controller = WindowCompat.getInsetsController(window, view)
            val previous = controller.isAppearanceLightStatusBars
            controller.isAppearanceLightStatusBars = !darkBackground
            onDispose { controller.isAppearanceLightStatusBars = previous }
        }
    }
}
