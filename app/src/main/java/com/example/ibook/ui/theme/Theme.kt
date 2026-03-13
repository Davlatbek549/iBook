package com.example.ibook.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorSurfaceLight,
    secondary = ColorSecondary,
    onSecondary = ColorSurfaceLight,
    tertiary = ColorTertiary,
    onTertiary = ColorSurfaceLight,

    // Backgrounds & surfaces
    background = ColorBackgroundLight,
    onBackground = ColorText,
    surface = ColorSurfaceLight,
    onSurface = ColorText,

    // Status colors
    error = ColorError,
    onError = ColorOnError,

    // Borders / outlines
    outline = ColorOutline
)


private val DarkColorScheme = darkColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorBackgroundDark,
    secondary = ColorSecondary,
    onSecondary = ColorBackgroundDark,
    tertiary = ColorTertiary,
    onTertiary = ColorBackgroundDark,

    background = ColorBackgroundDark,
    onBackground = ColorSurfaceLight,
    surface = ColorSurfaceDark,
    onSurface = ColorSurfaceLight,

    error = ColorError,
    onError = ColorOnError,

    outline = ColorOutline.copy(alpha = 0.6f)
)


@Composable
fun IBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
