package com.example.dz.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.hanken_regular
import dz.shared.generated.resources.hanken_semibold
import dz.shared.generated.resources.newsreader_italic
import dz.shared.generated.resources.newsreader_medium
import dz.shared.generated.resources.newsreader_regular
import org.jetbrains.compose.resources.Font

/**
 * "Ink & Paper" design direction — editorial, serif-forward, hairline rules.
 * Token source: design/explorations/theme-data.js (DZDIR.ink).
 */
@Immutable
data class InkColors(
    val paper: Color,
    val surface: Color,
    val alt: Color,
    val ink: Color,
    val inkSoft: Color,
    val muted: Color,
    val line: Color,
    val accent: Color,
    val accentSoft: Color,
    val onAccent: Color,
    val danger: Color,
)

val InkLight = InkColors(
    paper      = Color(0xFFF2ECE1),
    surface    = Color(0xFFFBF7EF),
    alt        = Color(0xFFECE3D4),
    ink        = Color(0xFF211C16),
    inkSoft    = Color(0xFF4C453A),
    muted      = Color(0xFF8C8270),
    line       = Color(0xFFDFD5C5),
    accent     = Color(0xFFB5532E),
    accentSoft = Color(0xFFEBDBCC),
    onAccent   = Color(0xFFFBF7EF),
    danger     = Color(0xFFB3402A),
)

val InkDark = InkColors(
    paper      = Color(0xFF17130D),
    surface    = Color(0xFF201B14),
    alt        = Color(0xFF2A241B),
    ink        = Color(0xFFEFE7D8),
    inkSoft    = Color(0xFFC7BEAC),
    muted      = Color(0xFF988E7C),
    line       = Color(0xFF332D24),
    accent     = Color(0xFFD98A5E),
    accentSoft = Color(0xFF3A2A20),
    onAccent   = Color(0xFF17130D),
    danger     = Color(0xFFD96A55),
)

@Composable
fun inkColors(darkTheme: Boolean = isSystemInDarkTheme()): InkColors =
    if (darkTheme) InkDark else InkLight

object InkShape {
    val radius: Dp = 12.dp
    val radiusSm: Dp = 8.dp
    val cover: Dp = 6.dp
}

/** Display + accent serif (Newsreader). Italic style is the "accent" voice. */
@Composable
fun inkDisplayFontFamily(): FontFamily = FontFamily(
    Font(Res.font.newsreader_regular, FontWeight.Normal),
    Font(Res.font.newsreader_medium, FontWeight.Medium),
    Font(Res.font.newsreader_italic, FontWeight.Normal, FontStyle.Italic),
)

/** Body sans (Hanken Grotesk). */
@Composable
fun inkBodyFontFamily(): FontFamily = FontFamily(
    Font(Res.font.hanken_regular, FontWeight.Normal),
    Font(Res.font.hanken_semibold, FontWeight.SemiBold),
)
