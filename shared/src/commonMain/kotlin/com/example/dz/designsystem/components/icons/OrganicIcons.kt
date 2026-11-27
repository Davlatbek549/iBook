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

    /** A star is drawn small and has five inside corners; 2.4 closes them up. */
    private const val STROKE_STAR = 1.8f

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

    /** Arrow into a tray — saving a book to the device. */
    val Download: ImageVector by lazy {
        organicIcon("OrganicDownload") {
            stroke {
                moveTo(12f, 4f)
                verticalLineToRelative(10.5f)
                moveTo(7.5f, 10.5f)
                lineTo(12f, 15f)
                lineToRelative(4.5f, -4.5f)
                moveTo(5f, 19.5f)
                horizontalLineToRelative(14f)
            }
        }
    }

    /** A large A beside a small one — the reader's type control. */
    val TextSize: ImageVector by lazy {
        organicIcon("OrganicTextSize") {
            stroke {
                // Small A
                moveTo(3f, 15.5f)
                lineTo(6.2f, 7.5f)
                lineTo(9.4f, 15.5f)
                moveTo(4.3f, 12.6f)
                horizontalLineToRelative(3.8f)
            }
            stroke {
                // Large A
                moveTo(12.6f, 19f)
                lineTo(17.3f, 5.5f)
                lineTo(22f, 19f)
                moveTo(14.4f, 14.2f)
                horizontalLineToRelative(5.8f)
            }
        }
    }

    /** Speech bubble — notes other readers left on a page. */
    val Chat: ImageVector by lazy {
        organicIcon("OrganicChat") {
            stroke {
                moveTo(4f, 17.5f)
                verticalLineTo(6.5f)
                curveToRelative(0f, -1.1f, 0.9f, -2f, 2f, -2f)
                horizontalLineToRelative(12f)
                curveToRelative(1.1f, 0f, 2f, 0.9f, 2f, 2f)
                verticalLineToRelative(7f)
                curveToRelative(0f, 1.1f, -0.9f, 2f, -2f, 2f)
                horizontalLineTo(8.5f)
                lineTo(4f, 20f)
                close()
            }
        }
    }

    /** Cross, for removing one thing from a list without leaving the screen. */
    val Close: ImageVector by lazy {
        organicIcon("OrganicClose") {
            stroke {
                moveTo(6.5f, 6.5f)
                lineToRelative(11f, 11f)
                moveTo(17.5f, 6.5f)
                lineToRelative(-11f, 11f)
            }
        }
    }

    /** Plus, for the action that makes a new thing — a shelf, a friend. */
    val Plus: ImageVector by lazy {
        organicIcon("OrganicPlus") {
            stroke {
                moveTo(12f, 5f)
                verticalLineToRelative(14f)
                moveTo(5f, 12f)
                horizontalLineToRelative(14f)
            }
        }
    }

    val Send: ImageVector by lazy {
        organicIcon("OrganicSend") {
            stroke {
                moveTo(4f, 20f)
                lineToRelative(16f, -8f)
                lineTo(4f, 4f)
                lineToRelative(3f, 8f)
                close()
            }
        }
    }

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

    /** An unearned star — the empty half of a rating. */
    val Star: ImageVector by lazy {
        organicIcon("OrganicStar") {
            stroke(STROKE_STAR) { star() }
        }
    }

    /** A given star. */
    val StarFilled: ImageVector by lazy {
        organicIcon("OrganicStarFilled") {
            path(fill = INK) { star() }
        }
    }

    /**
     * The reader's bookmark — a ribbon with a notch cut out of its foot.
     *
     * Drawn as a stroke so the empty state reads as an outline, and filled by the caller's tint
     * when a page is actually pinned; one shape covers both states, which keeps the button from
     * changing silhouette when it is pressed.
     */
    val Bookmark: ImageVector by lazy {
        organicIcon("OrganicBookmark") {
            stroke {
                moveTo(7f, 4f)
                horizontalLineToRelative(10f)
                verticalLineToRelative(16.5f)
                lineToRelative(-5f, -4f)
                lineToRelative(-5f, 4f)
                close()
            }
        }
    }

    /** The same ribbon, solid — the pinned state. */
    val BookmarkFilled: ImageVector by lazy {
        organicIcon("OrganicBookmarkFilled") {
            path(fill = INK) {
                moveTo(7f, 4f)
                horizontalLineToRelative(10f)
                verticalLineToRelative(16.5f)
                lineToRelative(-5f, -4f)
                lineToRelative(-5f, 4f)
                close()
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

    val ChevronDown: ImageVector by lazy {
        organicIcon("OrganicChevronDown") {
            stroke(STROKE_CHEVRON) {
                moveTo(5f, 9f)
                lineToRelative(7f, 7f)
                lineToRelative(7f, -7f)
            }
        }
    }

    /** The streak card's flame — a single stroked outline, waisted rather than a plain teardrop. */
    val Flame: ImageVector by lazy {
        organicIcon("OrganicFlame") {
            stroke {
                moveTo(12f, 3f)
                curveToRelative(0f, 0f, 5f, 4.2f, 5f, 9f)
                arcToRelative(5f, 5f, 0f, false, true, -10f, 0f)
                curveToRelative(0f, -1.8f, 0.8f, -3f, 0.8f, -3f)
                curveToRelative(0.3f, 1.4f, 1.2f, 2.2f, 2.2f, 2.2f)
                curveToRelative(1.6f, 0f, 2f, -2.6f, 2f, -8.2f)
                close()
            }
        }
    }

    // ── Settings rows ────────────────────────────────────────────────────────

    /** Sun — auto night mode. */
    val Sun: ImageVector by lazy {
        organicIcon("OrganicSun") {
            stroke {
                moveTo(12f, 3f)
                verticalLineToRelative(2f)
                moveTo(12f, 19f)
                verticalLineToRelative(2f)
                moveTo(5f, 12f)
                horizontalLineTo(3f)
                moveTo(21f, 12f)
                horizontalLineToRelative(-2f)
                moveTo(6f, 6f)
                lineTo(4.6f, 4.6f)
                moveTo(19.4f, 19.4f)
                lineTo(18f, 18f)
                moveTo(6f, 18f)
                lineToRelative(-1.4f, 1.4f)
                moveTo(19.4f, 4.6f)
                lineTo(18f, 6f)
            }
            stroke { circle(12f, 12f, 4f) }
        }
    }

    /** Rounded phone outline — keep screen awake. */
    val Device: ImageVector by lazy {
        organicIcon("OrganicDevice") {
            stroke {
                moveTo(10f, 2f)
                horizontalLineToRelative(4f)
                arcToRelative(3f, 3f, 0f, false, true, 3f, 3f)
                verticalLineToRelative(14f)
                arcToRelative(3f, 3f, 0f, false, true, -3f, 3f)
                horizontalLineToRelative(-4f)
                arcToRelative(3f, 3f, 0f, false, true, -3f, -3f)
                verticalLineToRelative(-14f)
                arcToRelative(3f, 3f, 0f, false, true, 3f, -3f)
                close()
                moveTo(11f, 18f)
                horizontalLineToRelative(2f)
            }
        }
    }

    /** A bell — the daily goal nudge. */
    val Bell: ImageVector by lazy {
        organicIcon("OrganicBell") {
            stroke {
                moveTo(18f, 8f)
                arcToRelative(6f, 6f, 0f, true, false, -12f, 0f)
                curveToRelative(0f, 7f, -3f, 8f, -3f, 8f)
                horizontalLineToRelative(18f)
                curveToRelative(0f, 0f, -3f, -1f, -3f, -8f)
                moveTo(10f, 21f)
                horizontalLineToRelative(4f)
            }
        }
    }

    /** An envelope — store offers by mail. */
    val Mail: ImageVector by lazy {
        organicIcon("OrganicMail") {
            stroke {
                moveTo(3f, 6f)
                horizontalLineToRelative(18f)
                verticalLineToRelative(12f)
                horizontalLineTo(3f)
                close()
            }
            stroke {
                moveTo(3f, 8f)
                lineToRelative(9f, 6f)
                lineToRelative(9f, -6f)
            }
        }
    }

    /** Three connected nodes — hand the link to the system share sheet. */
    val Share: ImageVector by lazy {
        organicIcon("OrganicShare") {
            stroke {
                circle(18f, 5f, 3f)
                circle(6f, 12f, 3f)
                circle(18f, 19f, 3f)
                moveTo(8.6f, 13.5f)
                lineToRelative(6.8f, 4f)
                moveTo(15.4f, 6.5f)
                lineToRelative(-6.8f, 4f)
            }
        }
    }

    /** A person with a plus — the empty-friends illustration badge. */
    val UserPlus: ImageVector by lazy {
        organicIcon("OrganicUserPlus") {
            stroke {
                moveTo(16f, 21f)
                verticalLineToRelative(-2f)
                arcToRelative(4f, 4f, 0f, false, false, -4f, -4f)
                horizontalLineTo(6f)
                arcToRelative(4f, 4f, 0f, false, false, -4f, 4f)
                verticalLineToRelative(2f)
                circle(9f, 7f, 4f)
                moveTo(19f, 8f)
                verticalLineToRelative(6f)
                moveTo(22f, 11f)
                horizontalLineToRelative(-6f)
            }
        }
    }

    /** A padlock — who sees the reader's shelves. */
    val Lock: ImageVector by lazy {
        organicIcon("OrganicLock") {
            stroke {
                moveTo(7f, 10f)
                horizontalLineToRelative(10f)
                arcToRelative(3f, 3f, 0f, false, true, 3f, 3f)
                verticalLineToRelative(5f)
                arcToRelative(3f, 3f, 0f, false, true, -3f, 3f)
                horizontalLineToRelative(-10f)
                arcToRelative(3f, 3f, 0f, false, true, -3f, -3f)
                verticalLineToRelative(-5f)
                arcToRelative(3f, 3f, 0f, false, true, 3f, -3f)
                close()
            }
            stroke {
                moveTo(8f, 10f)
                verticalLineToRelative(-3f)
                arcToRelative(4f, 4f, 0f, false, true, 8f, 0f)
                verticalLineToRelative(3f)
            }
        }
    }

    /** A circled "i" — help and contact. */
    val Info: ImageVector by lazy {
        organicIcon("OrganicInfo") {
            stroke { circle(12f, 12f, 9f) }
            stroke {
                moveTo(12f, 16f)
                verticalLineToRelative(-4f)
                moveTo(12f, 8f)
                horizontalLineToRelative(0.01f)
            }
        }
    }

    /** A shield — terms and privacy. */
    val Shield: ImageVector by lazy {
        organicIcon("OrganicShield") {
            stroke {
                moveTo(12f, 3f)
                lineToRelative(8f, 4f)
                verticalLineToRelative(5f)
                curveToRelative(0f, 5f, -3.5f, 8f, -8f, 9f)
                curveToRelative(-4.5f, -1f, -8f, -4f, -8f, -9f)
                verticalLineToRelative(-5f)
                close()
            }
        }
    }

    // ── Edit profile ─────────────────────────────────────────────────────────

    /** A camera body with its lens — change a photo. */
    val Camera: ImageVector by lazy {
        organicIcon("OrganicCamera") {
            stroke {
                moveTo(14.5f, 4f)
                horizontalLineToRelative(-5f)
                lineTo(7f, 7f)
                horizontalLineTo(4f)
                arcToRelative(2f, 2f, 0f, false, false, -2f, 2f)
                verticalLineToRelative(9f)
                arcToRelative(2f, 2f, 0f, false, false, 2f, 2f)
                horizontalLineToRelative(16f)
                arcToRelative(2f, 2f, 0f, false, false, 2f, -2f)
                verticalLineTo(9f)
                arcToRelative(2f, 2f, 0f, false, false, -2f, -2f)
                horizontalLineToRelative(-3f)
                close()
            }
            stroke { circle(12f, 13f, 3f) }
        }
    }

    /** A globe — the open profile, and "on your profile". */
    val Globe: ImageVector by lazy {
        organicIcon("OrganicGlobe") {
            stroke { circle(12f, 12f, 9f) }
            stroke {
                moveTo(12f, 3f)
                arcToRelative(13f, 13f, 0f, false, false, 0f, 18f)
                arcToRelative(13f, 13f, 0f, false, false, 0f, -18f)
                moveTo(3f, 12f)
                horizontalLineToRelative(18f)
            }
        }
    }

    /** A wall calendar — date of birth. */
    val Calendar: ImageVector by lazy {
        organicIcon("OrganicCalendar") {
            stroke {
                moveTo(5f, 5f)
                horizontalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                verticalLineToRelative(12f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                horizontalLineTo(5f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                verticalLineTo(7f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                close()
            }
            stroke {
                moveTo(8f, 3f)
                verticalLineToRelative(4f)
                moveTo(16f, 3f)
                verticalLineToRelative(4f)
                moveTo(3f, 10f)
                horizontalLineToRelative(18f)
            }
        }
    }

    /** A circled "!" — a username that is taken, a change that will be lost. */
    val Alert: ImageVector by lazy {
        organicIcon("OrganicAlert") {
            stroke { circle(12f, 12f, 9f) }
            stroke {
                moveTo(12f, 8f)
                verticalLineToRelative(4.5f)
                moveTo(12f, 16f)
                horizontalLineToRelative(0.01f)
            }
        }
    }

    /** A bin — remove a photo. */
    val Trash: ImageVector by lazy {
        organicIcon("OrganicTrash") {
            stroke {
                moveTo(3f, 6f)
                horizontalLineToRelative(18f)
                moveTo(19f, 6f)
                verticalLineToRelative(14f)
                curveToRelative(0f, 1f, -1f, 2f, -2f, 2f)
                horizontalLineTo(7f)
                curveToRelative(-1f, 0f, -2f, -1f, -2f, -2f)
                verticalLineTo(6f)
                moveTo(8f, 6f)
                verticalLineTo(4f)
                curveToRelative(0f, -1f, 1f, -2f, 2f, -2f)
                horizontalLineToRelative(4f)
                curveToRelative(1f, 0f, 2f, 1f, 2f, 2f)
                verticalLineToRelative(2f)
            }
        }
    }

    /** A framed picture — choose from the photo library. */
    val Image: ImageVector by lazy {
        organicIcon("OrganicImage") {
            stroke {
                moveTo(5f, 3f)
                horizontalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                verticalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                horizontalLineTo(5f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                verticalLineTo(5f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                close()
            }
            stroke {
                circle(9f, 9f, 2f)
                moveTo(21f, 15f)
                lineToRelative(-5f, -5f)
                lineTo(5f, 21f)
            }
        }
    }

    /** A briefcase — speciality, at work. */
    val Briefcase: ImageVector by lazy {
        organicIcon("OrganicBriefcase") {
            stroke {
                moveTo(4f, 7f)
                horizontalLineToRelative(16f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                verticalLineToRelative(9f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                horizontalLineTo(4f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                verticalLineTo(9f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                close()
            }
            stroke {
                moveTo(8f, 7f)
                verticalLineTo(5f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                horizontalLineToRelative(4f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                verticalLineToRelative(2f)
            }
        }
    }

    /** A mortarboard — speciality, studying. */
    val GraduationCap: ImageVector by lazy {
        organicIcon("OrganicGraduationCap") {
            stroke {
                moveTo(2f, 9.5f)
                lineTo(12f, 5f)
                lineToRelative(10f, 4.5f)
                lineTo(12f, 14f)
                close()
                moveTo(22f, 9.5f)
                verticalLineToRelative(5.5f)
            }
            stroke {
                moveTo(6f, 11.8f)
                verticalLineTo(16f)
                arcToRelative(6f, 3f, 0f, false, false, 12f, 0f)
                verticalLineToRelative(-4.2f)
            }
        }
    }

    /** The five-pointed star both rating glyphs are cut from. */
    private fun PathBuilder.star() {
        moveTo(12f, 2.6f)
        lineTo(14.9f, 8.5f)
        lineTo(21.4f, 9.4f)
        lineTo(16.7f, 14f)
        lineTo(17.8f, 20.4f)
        lineTo(12f, 17.4f)
        lineTo(6.2f, 20.4f)
        lineTo(7.3f, 14f)
        lineTo(2.6f, 9.4f)
        lineTo(9.1f, 8.5f)
        close()
    }

    private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
        moveTo(cx - r, cy)
        arcToRelative(r, r, 0f, true, false, 2 * r, 0f)
        arcToRelative(r, r, 0f, true, false, -2 * r, 0f)
        close()
    }
}
