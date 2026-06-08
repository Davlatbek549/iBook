package com.example.dz.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    // ── Primary (purple #6556FF) ──────────────────────────────────────────
    primary            = ColorPrimary,
    onPrimary          = ColorSurfaceLight,
    primaryContainer   = Color(0xFFE8E5FF),   // very light purple
    onPrimaryContainer = Color(0xFF1A0060),   // very dark purple

    // ── Secondary (pink #F8448F) ──────────────────────────────────────────
    secondary            = ColorSecondary,
    onSecondary          = ColorSurfaceLight,
    secondaryContainer   = Color(0xFFFFD8ED), // very light pink
    onSecondaryContainer = Color(0xFF56002A), // very dark pink

    // ── Tertiary (lavender #A65EEA) ───────────────────────────────────────
    tertiary            = ColorTertiary,
    onTertiary          = ColorSurfaceLight,
    tertiaryContainer   = Color(0xFFF3E6FF),  // very light lavender
    onTertiaryContainer = Color(0xFF300055),  // very dark purple

    // ── Backgrounds & surfaces ────────────────────────────────────────────
    background      = ColorBackgroundLight,
    onBackground    = ColorTextStrongLight,
    surface         = ColorSurfaceLight,
    onSurface       = ColorTextStrongLight,
    // surfaceVariant is used by inputs, chips, and tab indicators.
    // Matches the search-bar fill colour already used across the app.
    surfaceVariant   = Color(0xFFF0F2F6),
    onSurfaceVariant = ColorText,

    // ── Inverse surfaces (snackbars, tooltips) ────────────────────────────
    inverseSurface   = ColorTextStrongLight,
    inverseOnSurface = ColorSurfaceLight,
    inversePrimary   = Color(0xFFBEB6FF),

    // ── Status ────────────────────────────────────────────────────────────
    error   = ColorError,
    onError = ColorOnError,

    // ── Borders ───────────────────────────────────────────────────────────
    outline        = ColorOutline,
    outlineVariant = ColorOutline.copy(alpha = 0.50f),

    // ── KEY FIX: disable the Material 3 elevation tint ───────────────────
    // Without this, Material 3 overlays the primary colour onto every
    // elevated Surface, making white cards/sheets appear purple-tinted
    // instead of the exact colours declared in Color.kt.
    surfaceTint = Color.Transparent,
)

private val DarkColorScheme = darkColorScheme(
    // ── Primary ───────────────────────────────────────────────────────────
    primary            = ColorPrimary,
    onPrimary          = ColorSurfaceLight,
    primaryContainer   = Color(0xFF3D2FA6),   // mid-dark purple
    onPrimaryContainer = Color(0xFFE8E5FF),   // very light purple

    // ── Secondary ─────────────────────────────────────────────────────────
    secondary            = ColorSecondary,
    onSecondary          = ColorSurfaceLight,
    secondaryContainer   = Color(0xFF7C1140), // dark pink
    onSecondaryContainer = Color(0xFFFFD8ED), // very light pink

    // ── Tertiary ──────────────────────────────────────────────────────────
    tertiary            = ColorTertiary,
    onTertiary          = ColorSurfaceLight,
    tertiaryContainer   = Color(0xFF54247F),  // dark lavender
    onTertiaryContainer = Color(0xFFF3E6FF),  // very light lavender

    // ── Backgrounds & surfaces ────────────────────────────────────────────
    background      = ColorBackgroundDark,
    onBackground    = ColorTextStrongDark,
    surface         = ColorSurfaceDark,
    onSurface       = ColorTextStrongDark,
    surfaceVariant   = Color(0xFF3A3A3A),
    onSurfaceVariant = ColorTextStrongDark.copy(alpha = 0.70f),

    // ── Inverse surfaces ──────────────────────────────────────────────────
    inverseSurface   = ColorTextStrongDark,
    inverseOnSurface = ColorTextStrongLight,
    inversePrimary   = Color(0xFF6556FF),

    // ── Status ────────────────────────────────────────────────────────────
    error   = ColorError,
    onError = ColorOnError,

    // ── Borders ───────────────────────────────────────────────────────────
    outline        = ColorOutline.copy(alpha = 0.60f),
    outlineVariant = ColorOutline.copy(alpha = 0.30f),

    // ── Disable elevation tint in dark mode too ───────────────────────────
    surfaceTint = Color.Transparent,
)

@Composable
fun DZTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
