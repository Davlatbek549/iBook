package com.example.dz.core.platform

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import com.example.dz.domain.model.Appearance

/**
 * Applies edge-to-edge again with bar styles for the app's appearance instead of the device's.
 *
 * `MainActivity` turns edge-to-edge on before anything is composed, and the styles it picks then
 * follow the device. This repeats the call whenever [darkTheme] changes, with the same transparent
 * status bar and the same navigation-bar scrims the default call uses — chosen for the app's light
 * or dark rather than the device's.
 *
 * The reader's [StatusBarAppearance] is composed inside the app and so runs after this: while the
 * reader is open, its page still decides the status bar.
 */
@Composable
actual fun PlatformAppearance(appearance: Appearance, darkTheme: Boolean) {
    val view = LocalView.current
    DisposableEffect(view, darkTheme) {
        (view.context as? ComponentActivity)?.enableEdgeToEdge(
            statusBarStyle = if (darkTheme) {
                SystemBarStyle.dark(Color.TRANSPARENT)
            } else {
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
            },
            navigationBarStyle = if (darkTheme) {
                SystemBarStyle.dark(DARK_SCRIM)
            } else {
                SystemBarStyle.light(LIGHT_SCRIM, DARK_SCRIM)
            },
        )
        onDispose { }
    }
}

/** The scrims `enableEdgeToEdge` puts behind three-button navigation by default. */
private val LIGHT_SCRIM = Color.argb(0xE6, 0xFF, 0xFF, 0xFF)
private val DARK_SCRIM = Color.argb(0x80, 0x1B, 0x1B, 0x1B)
