package com.example.dz.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.caprasimo_regular
import dz.shared.generated.resources.figtree_bold
import dz.shared.generated.resources.figtree_regular
import dz.shared.generated.resources.figtree_semibold
import org.jetbrains.compose.resources.Font

/**
 * "Organic" design direction — warm cream ground, terracotta actions, pill buttons, filled
 * surfaces with no hairlines. Ported from the handoff's `_ds/organic-…/styles.css`, which the
 * bundle names as the source of truth.
 *
 * Kept beside [InkColors] rather than replacing it: the redesign lands screen by screen, and the
 * screens still on the old direction have to keep rendering while it does.
 */
@Immutable
data class OrganicColors(
    val bg: Color,
    val surface: Color,
    val text: Color,
    val accent: Color,
    val accent2: Color,
    // Neutral ramp
    val neutral100: Color,
    val neutral200: Color,
    val neutral300: Color,
    val neutral400: Color,
    val neutral500: Color,
    val neutral600: Color,
    val neutral700: Color,
    val neutral800: Color,
    val neutral900: Color,
    // Terracotta ramp
    val accent100: Color,
    val accent200: Color,
    val accent700: Color,
    val accent900: Color,
    // Sage ramp
    val accent2200: Color,
    val accent2900: Color,
    val danger: Color,
)

/**
 * The handoff specifies one palette. It is defined here in full rather than only inside a theme
 * branch, so the screens render correctly whatever the host is doing.
 */
val Organic = OrganicColors(
    bg         = Color(0xFFF5EAD8),
    surface    = Color(0xFFEBDDC5),
    text       = Color(0xFF201E1D),
    accent     = Color(0xFFC67139),
    accent2    = Color(0xFF7A8A5E),
    neutral100 = Color(0xFFF9F4ED),
    neutral200 = Color(0xFFEEE7DB),
    neutral300 = Color(0xFFDCD3C4),
    neutral400 = Color(0xFFC0B6A5),
    neutral500 = Color(0xFFA19786),
    neutral600 = Color(0xFF82796A),
    neutral700 = Color(0xFF645C50),
    neutral800 = Color(0xFF474238),
    neutral900 = Color(0xFF2E2B25),
    accent100  = Color(0xFFFFF2EB),
    accent200  = Color(0xFFFFE1D0),
    accent700  = Color(0xFF8C491A),
    accent900  = Color(0xFF402310),
    accent2200 = Color(0xFFE1EECC),
    accent2900 = Color(0xFF272E1B),
    // Not in the handoff's ramp — the nearest thing the design carries for a failure, taken from
    // the terracotta ramp's dark end so it reads as an error without introducing a new hue.
    danger     = Color(0xFF8C491A),
)

@Composable
fun organicColors(): OrganicColors = Organic

/** Radii from the handoff: sm 8 · md 16 · lg 28 · pill 999 · frame 38. */
object OrganicShape {
    val sm: Dp = 8.dp
    val md: Dp = 16.dp
    val lg: Dp = 28.dp
    val pill: Dp = 999.dp
}

/**
 * Heights the handoff fixes rather than derives, so screens read them instead of repeating
 * magic numbers.
 */
object OrganicSize {
    /** Full-width primary action. */
    val buttonHeight: Dp = 58.dp
    val socialButtonHeight: Dp = 54.dp
    val fieldHeight: Dp = 56.dp
    val codeBoxHeight: Dp = 64.dp
    val backButton: Dp = 42.dp
    /** Auth screens use a wider gutter than the 24dp the rest of the app uses. */
    val authGutter: Dp = 28.dp
}

/** Display face — Caprasimo, 400 only. Headings and the code digits. */
@Composable
fun organicHeadingFontFamily(): FontFamily = FontFamily(
    Font(Res.font.caprasimo_regular, FontWeight.Normal),
)

/** Body/UI face — Figtree at 400 / 600 / 700. */
@Composable
fun organicBodyFontFamily(): FontFamily = FontFamily(
    Font(Res.font.figtree_regular, FontWeight.Normal),
    Font(Res.font.figtree_semibold, FontWeight.SemiBold),
    Font(Res.font.figtree_bold, FontWeight.Bold),
)
