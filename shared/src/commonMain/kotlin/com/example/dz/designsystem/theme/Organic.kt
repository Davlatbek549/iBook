package com.example.dz.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
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
import dz.shared.generated.resources.newsreader_medium
import dz.shared.generated.resources.newsreader_regular
import org.jetbrains.compose.resources.Font

/**
 * "Organic" design tokens — warm cream ground, terracotta actions, pill buttons, filled surfaces
 * with no hairlines. Ported from the handoff's `_ds/organic-…/styles.css`, which the bundle names
 * as the source of truth.
 *
 * Kept beside [InkColors] rather than replacing it: the redesign lands screen by screen (splash,
 * onboarding and auth so far), and the screens still on the old direction have to keep rendering
 * while it does.
 *
 * There are two of these, [OrganicLight] and [OrganicDark], and screens name neither: they read
 * [OrganicColors], which is whichever one [DZTheme] put in place for the system's appearance.
 */
@Immutable
data class OrganicPalette(
    // Core roles
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

    // Accent (terracotta) ramp
    val accent100: Color,
    val accent200: Color,
    val accent300: Color,
    val accent400: Color,
    val accent500: Color,
    val accent600: Color,
    val accent700: Color,
    val accent800: Color,
    val accent900: Color,

    // Accent-2 (sage) ramp
    val accent2_100: Color,
    val accent2_200: Color,
    val accent2_300: Color,
    val accent2_400: Color,
    val accent2_500: Color,
    val accent2_600: Color,
    val accent2_700: Color,
    val accent2_800: Color,
    val accent2_900: Color,

    // Shadow tint (used as the ambient/spot color for elevated surfaces)
    val shadow: Color,

    /**
     * Not a token in the handoff either: what a screen dims to under a sheet or a dialog, at
     * whatever alpha the caller gives it. In daylight it is the neutral-900 the scrims always
     * used; it is its own token because a dimmed screen gets darker in both appearances, while
     * neutral-900 turns light in the dark one.
     */
    val scrim: Color,

    /**
     * The tab bar's pill, which is dark on the cream page by design. In the dark palette it stays
     * dark and is lifted off the ground instead — inverted, it would be a bright bar under a dark
     * screen.
     */
    val pill: Color,
) {
    /**
     * Not a token in the handoff — it carries no failure colour. The strong end of the terracotta
     * ramp is the nearest thing it does carry, so errors read as errors without introducing a hue
     * the palette never sanctioned.
     */
    val danger: Color get() = accent700
}

/** The palette as the handoff draws it. */
val OrganicLight = OrganicPalette(
    bg = Color(0xFFF5EAD8),
    surface = Color(0xFFEBDDC5),
    text = Color(0xFF201E1D),
    accent = Color(0xFFC67139),
    accent2 = Color(0xFF7A8A5E),

    neutral100 = Color(0xFFF9F4ED),
    neutral200 = Color(0xFFEEE7DB),
    neutral300 = Color(0xFFDCD3C4),
    neutral400 = Color(0xFFC0B6A5),
    neutral500 = Color(0xFFA19786),
    neutral600 = Color(0xFF82796A),
    neutral700 = Color(0xFF645C50),
    neutral800 = Color(0xFF474238),
    neutral900 = Color(0xFF2E2B25),

    accent100 = Color(0xFFFFF2EB),
    accent200 = Color(0xFFFFE1D0),
    accent300 = Color(0xFFFFC6A5),
    accent400 = Color(0xFFF6A06B),
    accent500 = Color(0xFFD67F48),
    accent600 = Color(0xFFB2622D),
    accent700 = Color(0xFF8C491A),
    accent800 = Color(0xFF643312),
    accent900 = Color(0xFF402310),

    accent2_100 = Color(0xFFF0FAE1),
    accent2_200 = Color(0xFFE1EECC),
    accent2_300 = Color(0xFFCCDBB2),
    accent2_400 = Color(0xFFAEBF92),
    accent2_500 = Color(0xFF8FA073),
    accent2_600 = Color(0xFF728157),
    accent2_700 = Color(0xFF56633F),
    accent2_800 = Color(0xFF3D472B),
    accent2_900 = Color(0xFF272E1B),

    shadow = Color(0xFF2E2B25),
    scrim = Color(0xFF2E2B25),
    pill = Color(0xFF2E2B25),
)

/**
 * The same palette for a dark appearance. The handoff draws no dark mode, so this one is derived
 * rather than ported: every token keeps its hue and its job, and only its lightness moves (in
 * OKLCH, so the steps stay even to the eye).
 *
 * - The ground is a warm near-black. The neutral ramp climbs from just above it — 100 and 200, the
 *   fills cards, chips and fields sit on — to the cream the type is set in at 900.
 * - The terracotta and sage ramps run the same way: their pale tints become dark washes and their
 *   deep ends become the light ink that reads on those washes, so every tile keeps its pairing and
 *   its contrast.
 * - [OrganicPalette.accent] and [OrganicPalette.accent2] do not move. They are the brand's two
 *   voices, both hold their contrast on the dark ground, and white type on the accent stays exactly
 *   as legible as it is in daylight.
 */
val OrganicDark = OrganicPalette(
    bg = Color(0xFF1C1913),
    surface = Color(0xFF2A261E),
    text = Color(0xFFF2E8D8),
    accent = Color(0xFFC67139),
    accent2 = Color(0xFF7A8A5E),

    neutral100 = Color(0xFF26221D),
    neutral200 = Color(0xFF302B21),
    neutral300 = Color(0xFF3F382C),
    neutral400 = Color(0xFF574F40),
    neutral500 = Color(0xFF756C5C),
    neutral600 = Color(0xFF938979),
    neutral700 = Color(0xFFB1A89A),
    neutral800 = Color(0xFFD0CABE),
    neutral900 = Color(0xFFEAE6DE),

    accent100 = Color(0xFF2A211B),
    accent200 = Color(0xFF39271C),
    accent300 = Color(0xFF4F311E),
    accent400 = Color(0xFF74401E),
    accent500 = Color(0xFFA65316),
    accent600 = Color(0xFFC27443),
    accent700 = Color(0xFFD89973),
    accent800 = Color(0xFFE8C1AB),
    accent900 = Color(0xFFF4E2D8),

    accent2_100 = Color(0xFF22241E),
    accent2_200 = Color(0xFF292D23),
    accent2_300 = Color(0xFF363C2C),
    accent2_400 = Color(0xFF4A5439),
    accent2_500 = Color(0xFF647449),
    accent2_600 = Color(0xFF839168),
    accent2_700 = Color(0xFFA2AF8E),
    accent2_800 = Color(0xFFC6CEB9),
    accent2_900 = Color(0xFFE4E8DD),

    shadow = Color(0xFF000000),
    scrim = Color(0xFF000000),
    pill = Color(0xFF3F382C),
)

private val LocalOrganicPalette = staticCompositionLocalOf { OrganicLight }

/**
 * The Organic palette for the current appearance: [OrganicLight] or [OrganicDark], as [DZTheme]
 * provided it.
 *
 * It is read in composition. A draw lambda — `drawBehind`, `Canvas`, a card's `decoration` — runs
 * outside it, so a colour it needs is read into a `val` beside the lambda and used from there.
 */
val OrganicColors: OrganicPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalOrganicPalette.current

/**
 * Draws [content] in [palette], whatever the appearance.
 *
 * For the few things whose colours are not the appearance's to switch: a reader's page, which is
 * set on the ground they chose, and the surfaces that are dark in daylight already and so have no
 * dark version of their own.
 */
@Composable
fun ProvideOrganicPalette(
    palette: OrganicPalette,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalOrganicPalette provides palette, content = content)
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

/**
 * The reader's alternative face. The handoff offers "Figtree or the serif" on the display sheet;
 * Newsreader is the serif the app already bundles, and it is a text face rather than a display
 * one, which is what a page of prose needs.
 */
@Composable
fun organicSerifFontFamily(): FontFamily = FontFamily(
    Font(Res.font.newsreader_regular, FontWeight.Normal),
    Font(Res.font.newsreader_medium, FontWeight.Medium),
)

/** Body/UI face — Figtree at 400 / 600 / 700. */
@Composable
fun organicBodyFontFamily(): FontFamily = FontFamily(
    Font(Res.font.figtree_regular, FontWeight.Normal),
    Font(Res.font.figtree_semibold, FontWeight.SemiBold),
    Font(Res.font.figtree_bold, FontWeight.Bold),
)
