package com.example.ibook.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = MainColor,
    onPrimary = White,
    primaryContainer = MainColor.copy(alpha = 0.12f),
    onPrimaryContainer = TextColor,

    secondary = Romantic,
    onSecondary = White,
    secondaryContainer = Romantic.copy(alpha = 0.10f),
    onSecondaryContainer = TextColor,

    tertiary = Horror,
    onTertiary = White,
    tertiaryContainer = Horror.copy(alpha = 0.10f),
    onTertiaryContainer = TextColor,

    background = SecondaryColor,
    onBackground = TextColor,
    surface = White,
    onSurface = TextColor,
    surfaceVariant = Semi,
    onSurfaceVariant = TextColor,
    surfaceTint = MainColor,

    inverseSurface = Semi_Dark,
    inverseOnSurface = White,

    error = Color(0xFFB00020),
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410001),

    outline = Semi.copy(alpha = 0.9f),
    outlineVariant = Semi.copy(alpha = 0.5f),
    scrim = Color(0x66000000),

    surfaceBright = SecondaryColor,
    surfaceDim = Semi.copy(alpha = 0.6f),
    surfaceContainer = SecondaryColor.copy(alpha = 0.85f),
    surfaceContainerHigh = White,
    surfaceContainerHighest = White,
    surfaceContainerLow = SecondaryColor.copy(alpha = 0.6f),
    surfaceContainerLowest = SecondaryColor.copy(alpha = 0.4f),
)

private val DarkColorScheme = darkColorScheme(
    primary = MainColor,
    onPrimary = Color(0xFF0F0F0F),
    primaryContainer = MainColor.copy(alpha = 0.18f),
    onPrimaryContainer = White,

    secondary = Romantic,
    onSecondary = Color(0xFF0F0F0F),
    secondaryContainer = Romantic.copy(alpha = 0.16f),
    onSecondaryContainer = White,

    tertiary = Horror,
    onTertiary = Color(0xFF0F0F0F),
    tertiaryContainer = Horror.copy(alpha = 0.16f),
    onTertiaryContainer = White,

    background = Dark,
    onBackground = White,
    surface = Semi_Dark,
    onSurface = White,
    surfaceVariant = Color(0xFF2E2E2E),
    onSurfaceVariant = White,
    surfaceTint = MainColor,

    inverseSurface = SecondaryColor,
    inverseOnSurface = TextColor,

    error = Color(0xFFCF6679),
    onError = Color(0xFF201A1B),
    errorContainer = Color(0xFFB3261E),
    onErrorContainer = White,

    outline = Color(0x66FFFFFF),
    outlineVariant = Color(0x33FFFFFF),
    scrim = Color(0x99000000),

    surfaceBright = Semi_Dark,
    surfaceDim = Color(0x22000000),
    surfaceContainer = Color(0xFF1F1F1F),
    surfaceContainerHigh = Color(0xFF262626),
    surfaceContainerHighest = Color(0xFF2A2A2A),
    surfaceContainerLow = Color(0xFF141414),
    surfaceContainerLowest = Color(0xFF0E0E0E),
)

@Composable
fun IBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}