package com.example.dz.designsystem.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Organic icon set — 24px grid, round caps and joins, drawn at the heavier weight the Organic
 * system asks for (stroke 2.4 for interface icons, 2.6 for the trailing chevron, matching
 * `dz-all-screens.html`).
 *
 * The handoff names Lucide as the source and asks for the codebase's Lucide package. There isn't
 * one — no icon dependency is declared for Compose Multiplatform here — so these follow the
 * established pattern of [InkIcons] instead: a hand-built [ImageVector] set on the same geometry.
 * Kept apart from [InkIcons] because that set is drawn at 1.7 and reads too thin against the
 * Organic ground; the weight is the point.
 *
 * Tint via `Icon(tint = ...)`.
 */
object OrganicIcons {

    /** Interface icons — nav, headers, row affordances. */
    private const val STROKE = 2.4f

    /** The trailing chevron, drawn a touch heavier because it renders at 15px. */
    private const val STROKE_CHEVRON = 2.6f

    /** The tick on a filled badge, where the heavier weight reads against the fill. */
    private const val STROKE_CHECK = 3f

    private val INK = SolidColor(Color(0xFF201E1D))

    private fun ImageVector.Builder.stroke(
        width: Float = STROKE,
        pathBuilder: PathBuilder.() -> Unit,
    ) {
        path(
            fill = null,
            stroke = INK,
            strokeLineWidth = width,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathBuilder = pathBuilder,
        )
    }

    private fun organicIcon(name: String, content: ImageVector.Builder.() -> Unit): ImageVector =
        ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply(content).build()

    // ── Tab bar ───────────────────────────────────────────────────────────────

    /** House with a doorway. Tab 1. */
    val Home: ImageVector by lazy {
        organicIcon("OrganicHome") {
            stroke {
                moveTo(4f, 10.8f)
                lineTo(12f, 4f)
                lineToRelative(8f, 6.8f)
                verticalLineTo(20f)
                horizontalLineToRelative(-5f)
                verticalLineToRelative(-5.5f)
                horizontalLineTo(9f)
                verticalLineTo(20f)
                horizontalLineTo(4f)
                close()
            }
        }
    }

    /** Two upright spines plus one leaning book. Tab 2. */
    val Library: ImageVector by lazy {
        organicIcon("OrganicLibrary") {
            stroke {
                moveTo(4.5f, 4.5f)
                verticalLineToRelative(15f)
                moveTo(8f, 4.5f)
                verticalLineToRelative(15f)
                moveTo(12.5f, 5.2f)
                lineToRelative(4.6f, -1f)
                lineToRelative(3.2f, 13.6f)
                lineToRelative(-4.6f, 1f)
                close()
            }
        }
    }

    /** Shopping cart. Tab 3. */
    val Store: ImageVector by lazy {
        organicIcon("OrganicStore") {
            stroke {
                moveTo(4f, 5f)
                horizontalLineToRelative(2f)
                lineToRelative(2.2f, 9.5f)
                horizontalLineToRelative(9.3f)
                lineTo(19f, 8f)
                horizontalLineTo(6.4f)
            }
            // Wheels. Stroked rather than filled, as the design draws them — at this weight a
            // 1-unit circle reads as a solid dot anyway.
            stroke {
                circle(10f, 19f, 1f)
                circle(17f, 19f, 1f)
            }
        }
    }

    /** Magnifier. The search circle on Home and Library — no longer a tab; see [Friends]. */
    val Search: ImageVector by lazy {
        organicIcon("OrganicSearch") {
            stroke {
                circle(11f, 11f, 6f)
                moveTo(15.5f, 15.5f)
                lineToRelative(4.5f, 4.5f)
            }
        }
    }

    /**
     * Two people, the second smaller and set back. Tab 4.
     *
     * The only tab icon with no counterpart in the handoff: its frames draw a magnifier in this
     * slot, while the written spec puts Friends here. It took three tries to make legible at 20dp.
     * Equal-sized figures merged into one blob with two eyes, and two plain overlapping discs read
     * as abstract rings next to four literal objects. What works is Lucide's trick: the figure
     * behind is smaller, higher, and drawn as a head and a single shoulder stub, so it sits clear
     * of the front one instead of fusing with it.
     */
    val Friends: ImageVector by lazy {
        organicIcon("OrganicFriends") {
            stroke {
                circle(8.8f, 10.2f, 3.1f)
                moveTo(3.2f, 20.4f)
                curveToRelative(0.8f, -2.8f, 2.6f, -4.2f, 5.6f, -4.2f)
                reflectiveCurveToRelative(4.8f, 1.4f, 5.6f, 4.2f)
            }
            stroke {
                circle(18f, 6.4f, 2.3f)
                moveTo(18.2f, 12.4f)
                curveToRelative(1.8f, 0.5f, 3.0f, 2.0f, 3.5f, 4.2f)
            }
        }
    }

    /** Head and shoulders. Tab 5. */
    val Profile: ImageVector by lazy {
        organicIcon("OrganicProfile") {
            stroke {
                circle(12f, 8f, 3.6f)
                moveTo(5f, 20f)
                curveToRelative(1.4f, -3.6f, 4f, -5.4f, 7f, -5.4f)
                reflectiveCurveToRelative(5.6f, 1.8f, 7f, 5.4f)
            }
        }
    }

    // ── Row and header affordances ────────────────────────────────────────────

    /** Tick, for the badge on a finished book. Drawn at 3 — it sits inside a filled circle. */
    val Check: ImageVector by lazy {
        organicIcon("OrganicCheck") {
            stroke(STROKE_CHECK) {
                moveTo(5f, 12.5f)
                lineToRelative(4.5f, 4.5f)
                lineTo(19f, 7f)
            }
        }
    }

    /** Trailing chevron on every list row and tappable card. */
    val ChevronRight: ImageVector by lazy {
        organicIcon("OrganicChevronRight") {
            stroke(STROKE_CHEVRON) {
                moveTo(9f, 5f)
                lineToRelative(7f, 7f)
                lineToRelative(-7f, 7f)
            }
        }
    }

    /** Back chevron — the mirror of [ChevronRight]. */
    val ChevronLeft: ImageVector by lazy {
        organicIcon("OrganicChevronLeft") {
            stroke(STROKE_CHEVRON) {
                moveTo(15f, 5f)
                lineToRelative(-7f, 7f)
                lineToRelative(7f, 7f)
            }
        }
    }

    private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
        moveTo(cx - r, cy)
        arcToRelative(r, r, 0f, true, false, 2 * r, 0f)
        arcToRelative(r, r, 0f, true, false, -2 * r, 0f)
        close()
    }
}
