package com.example.dz.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.uikit.LocalUIViewController
import com.example.dz.domain.model.Appearance
// overrideUserInterfaceStyle is declared in a UIView category, which Kotlin/Native can hand over
// as a member or as an extension; the star import reaches it either way.
import platform.UIKit.*

/**
 * Sets the window's interface style. iOS takes the status bar's style from it, and every UIKit
 * control's — and Compose reads the system theme from it too, so that stays in step.
 *
 * [Appearance.SYSTEM] clears the override rather than setting one, which hands the choice back to
 * the device.
 *
 * The window exists once the view is on screen, and a drawn frame means it is, so the style is set
 * after the next frame rather than straight away.
 */
@Composable
actual fun PlatformAppearance(appearance: Appearance, darkTheme: Boolean) {
    val controller = LocalUIViewController.current
    LaunchedEffect(controller, appearance) {
        withFrameNanos { }
        controller.view.window?.overrideUserInterfaceStyle = when (appearance) {
            Appearance.SYSTEM -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
            Appearance.LIGHT -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
            Appearance.DARK -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
        }
    }
}
