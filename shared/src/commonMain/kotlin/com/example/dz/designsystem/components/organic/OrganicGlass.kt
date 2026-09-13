package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.theme.OrganicColors
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import dev.chrisbanes.haze.glass.GlassStyle
import dev.chrisbanes.haze.glass.hazeGlass

/**
 * The Organic glass surface: a real refracting material, not a blur with gradients on top.
 *
 * Haze does the optics — refraction, depth blur, specular highlight, Fresnel edge lift and
 * chromatic dispersion, through a runtime shader on Android and Skia elsewhere. What lives here is
 * only the Organic tuning of it, so every glass panel in the app is the same material.
 *
 * This replaced a hand-rolled version (blurred backdrop plus a tint, a sheen and a drawn rim) that
 * existed because Haze is built against Kotlin 2.4 / Compose 1.12 and the project was pinned to
 * 2.3 / 1.11. The toolchain upgrade removed that reason.
 *
 * ## How to use it
 * A panel cannot refract what it never saw, so the content behind it is registered as a source:
 *
 * ```
 * val backdrop = rememberOrganicBackdrop()
 * Box {
 *     NavHost(modifier = Modifier.organicBackdropSource(backdrop))
 *     Panel(modifier = Modifier.organicGlass(backdrop, shape))
 * }
 * ```
 *
 * The `organic…` names are kept deliberately: call sites speak the design system's vocabulary, and
 * the material underneath stays swappable.
 */

/** What the glass panels read from. One per screen stack. */
@Composable
fun rememberOrganicBackdrop(): HazeState = rememberHazeState()

/** Marks this content as what the glass panels above it refract. */
fun Modifier.organicBackdropSource(backdrop: HazeState): Modifier = hazeSource(backdrop)

/**
 * Glass tints. These sit *over* refracted content, so they are far thinner than the fills they
 * replace — the material supplies the structure the opacity used to have to fake.
 */
object OrganicGlassTint {
    /**
     * The dark pill the tab bar sits in.
     *
     * 0.72 is a balance point found by looking: thinner and a bright shelf of book covers lifts
     * the pill until its icons wash out; thicker and the refraction stops being visible at all and
     * the glass may as well be a solid fill. The icons carry the rest of the contrast by going
     * brighter on glass than the handoff's neutral-300, which was drawn for a solid ground.
     */
    val dark: Color = OrganicColors.neutral900.copy(alpha = 0.72f)

    /** A light panel, for sheets over the cream ground. */
    val light: Color = OrganicColors.neutral100.copy(alpha = 0.40f)
}

/**
 * Turns this element into Organic glass.
 *
 * Goes before any `.clip()`/`.background()` in the chain — it is the element's background.
 */
@OptIn(ExperimentalHazeApi::class)
@Composable
fun Modifier.organicGlass(
    backdrop: HazeState,
    shape: RoundedCornerShape,
    tint: Color = OrganicGlassTint.dark,
): Modifier = hazeGlass(
    input = HazeInput.Backdrop(backdrop),
    style = organicGlassStyle(shape = shape, tint = tint),
)

/**
 * The Organic glass material.
 *
 * Built on `GlassStyle.clear` rather than `regular`: clear keeps more of the page visible and
 * keeps its refraction and edge response recognisable, which is what makes the pill read as glass
 * over a warm, low-contrast ground instead of as a dark slab.
 *
 * The two places this departs from Haze's defaults:
 * - `backgroundColor` is the cream page colour, so where the captured content is transparent the
 *   panel still refracts something warm rather than going flat.
 * - `chromaticAberrationStrength` is nudged off zero. The prismatic edge split is the most
 *   recognisable part of the material; `Simple` mode is the cheap one and enough at this size.
 */
@OptIn(ExperimentalHazeApi::class)
@Composable
fun organicGlassStyle(
    shape: RoundedCornerShape,
    tint: Color = OrganicGlassTint.dark,
): GlassStyle = GlassStyle.clear.then {
    shape(shape)
    tint(tint)
    backgroundColor(OrganicColors.bg)
    chromaticAberrationStrength(0.18f)
    edgeSoftness(2.dp)
}
