package com.example.dz.designsystem.theme

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
import androidx.compose.runtime.Composable

/**
 * "Organic" design tokens — the warm cream / terracotta system used by the
 * Splash screen and the Onboarding flow (design handoff: dz-all-screens.html,
 * section `_ds/organic-.../styles.css`). Scoped to those two flows only; the
 * rest of the app keeps the existing "Ink & Paper" theme.
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
}

object OrganicShape {
    val radiusSm: Dp = 8.dp
    val radiusMd: Dp = 16.dp
    val radiusLg: Dp = 28.dp
    val pill: Dp = 999.dp
    val frame: Dp = 38.dp
}

object OrganicElevation {
    val sm: Dp = 1.dp
    val md: Dp = 4.dp
    val lg: Dp = 14.dp
}

/** Caprasimo — the single display voice used for headings and the wordmark. */
@Composable
fun organicDisplayFontFamily(): FontFamily = FontFamily(
    Font(Res.font.caprasimo_regular, FontWeight.Normal),
)

/** Figtree — body copy and UI labels. */
@Composable
fun organicBodyFontFamily(): FontFamily = FontFamily(
    Font(Res.font.figtree_regular, FontWeight.Normal),
    Font(Res.font.figtree_semibold, FontWeight.SemiBold),
    Font(Res.font.figtree_bold, FontWeight.Bold),
)
