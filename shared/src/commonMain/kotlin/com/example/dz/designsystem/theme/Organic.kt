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
 * "Organic" design tokens — warm cream ground, terracotta actions, pill buttons, filled surfaces
 * with no hairlines. Ported from the handoff's `_ds/organic-…/styles.css`, which the bundle names
 * as the source of truth.
 *
 * Kept beside [InkColors] rather than replacing it: the redesign lands screen by screen (splash,
 * onboarding and auth so far), and the screens still on the old direction have to keep rendering
 * while it does.
 */
@Immutable
object OrganicColors {
    // Core roles
    val bg = Color(0xFFF5EAD8)
    val surface = Color(0xFFEBDDC5)
    val text = Color(0xFF201E1D)
    val accent = Color(0xFFC67139)
    val accent2 = Color(0xFF7A8A5E)

    // Neutral ramp
    val neutral100 = Color(0xFFF9F4ED)
    val neutral200 = Color(0xFFEEE7DB)
    val neutral300 = Color(0xFFDCD3C4)
    val neutral400 = Color(0xFFC0B6A5)
    val neutral500 = Color(0xFFA19786)
    val neutral600 = Color(0xFF82796A)
    val neutral700 = Color(0xFF645C50)
    val neutral800 = Color(0xFF474238)
    val neutral900 = Color(0xFF2E2B25)

    // Accent (terracotta) ramp
    val accent100 = Color(0xFFFFF2EB)
    val accent200 = Color(0xFFFFE1D0)
    val accent300 = Color(0xFFFFC6A5)
    val accent400 = Color(0xFFF6A06B)
    val accent500 = Color(0xFFD67F48)
    val accent600 = Color(0xFFB2622D)
    val accent700 = Color(0xFF8C491A)
    val accent800 = Color(0xFF643312)
    val accent900 = Color(0xFF402310)

    // Accent-2 (sage) ramp
    val accent2_100 = Color(0xFFF0FAE1)
    val accent2_200 = Color(0xFFE1EECC)
    val accent2_300 = Color(0xFFCCDBB2)
    val accent2_400 = Color(0xFFAEBF92)
    val accent2_500 = Color(0xFF8FA073)
    val accent2_600 = Color(0xFF728157)
    val accent2_700 = Color(0xFF56633F)
    val accent2_800 = Color(0xFF3D472B)
    val accent2_900 = Color(0xFF272E1B)

    // Shadow tint (used as the ambient/spot color for elevated surfaces)
    val shadow = Color(0xFF2E2B25)

    /**
     * Not a token in the handoff — it carries no failure colour. The dark end of the terracotta
     * ramp is the nearest thing it does carry, so errors read as errors without introducing a hue
     * the palette never sanctioned.
     */
    val danger = accent700
}

/** Radii from the handoff: sm 8 · md 16 · lg 28 · pill 999 · frame 38. */
object OrganicShape {
    val radiusSm: Dp = 8.dp
    val radiusMd: Dp = 16.dp
    val radiusLg: Dp = 28.dp
    val pill: Dp = 999.dp
    val frame: Dp = 38.dp
}

/** The handoff's three shadows, as the elevations that approximate them. */
object OrganicElevation {
    val sm: Dp = 1.dp
    val md: Dp = 4.dp
    val lg: Dp = 14.dp
}

/**
 * Heights the handoff fixes rather than derives, so screens read them instead of repeating magic
 * numbers.
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

/** Display face — Caprasimo, 400 only. Headings, the wordmark and the code digits. */
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
