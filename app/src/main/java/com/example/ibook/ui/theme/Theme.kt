package com.example.ibook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorSurfaceLight,
    secondary = ColorSecondary,
    onSecondary = ColorSurfaceLight,
    tertiary = ColorTertiary,
    onTertiary = ColorSurfaceLight,

    // Backgrounds & surfaces
    background = ColorBackgroundLight,
    onBackground = ColorTextStrongLight,
    surface = ColorSurfaceLight,
    onSurface = ColorTextStrongLight,

    // Status colors
    error = ColorError,
    onError = ColorOnError,

    // Borders / outlines
    outline = ColorOutline
)


private val DarkColorScheme = darkColorScheme(
    primary = ColorPrimary,
    onPrimary = ColorSurfaceLight,
    secondary = ColorSecondary,
    onSecondary = ColorSurfaceLight,
    tertiary = ColorTertiary,
    onTertiary = ColorSurfaceLight,

    background = ColorBackgroundDark,
    onBackground = ColorTextStrongDark,
    surface = ColorSurfaceDark,
    onSurface = ColorTextStrongDark,

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
