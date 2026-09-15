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
 * Icon set for the "Organic" design system — transcribed 1:1 from the design
 * handoff's inline SVGs (dz-all-screens.html), 24px viewBox, round caps/joins,
 * stroke-width 2.75 by default (the spec's "heavier round" setting). Unlike
 * [InkIcons] (baked at a fixed 1.7 stroke), every icon here accepts its own
 * stroke width so it can match the handoff exactly per use (bottom tab bar
 * icons ship at 2.4, trailing chevrons at 2.6, etc).
 */
object OrganicIcons {

    private val INK = SolidColor(Color(0xFF211C16))

    private fun ImageVector.Builder.stroke(
        width: Float = 2.75f,
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

    /** House — bottom tab bar "Home". */
    val Home: ImageVector by lazy {
        organicIcon("OrganicHome") {
            stroke(2.4f) {
                moveTo(4f, 10.8f)
                lineTo(12f, 4f)
                lineTo(20f, 10.8f)
                verticalLineTo(20f)
                horizontalLineTo(15f)
                verticalLineTo(14.5f)
                horizontalLineTo(9f)
                verticalLineTo(20f)
                horizontalLineTo(4f)
                close()
            }
        }
    }

    /** Two spines + a tilted book — bottom tab bar "Library". */
    val Library: ImageVector by lazy {
        organicIcon("OrganicLibrary") {
            stroke(2.4f) {
                moveTo(4.5f, 4.5f)
                verticalLineTo(19.5f)
                moveTo(8f, 4.5f)
                verticalLineTo(19.5f)
                moveTo(12.5f, 5.2f)
                lineTo(17.1f, 4.2f)
                lineTo(20.3f, 17.8f)
                lineTo(15.7f, 18.8f)
                close()
            }
        }
    }

    /** Shopping bag — bottom tab bar "Store". */
    val Store: ImageVector by lazy {
        organicIcon("OrganicStore") {
            stroke(2.4f) {
                moveTo(4f, 5f)
                horizontalLineTo(6f)
                lineTo(8.2f, 14.5f)
                horizontalLineTo(17.5f)
                lineTo(19f, 8f)
                horizontalLineTo(6.4f)
            }
            path(fill = null, stroke = INK, strokeLineWidth = 2.4f) { circle(10f, 19f, 1f) }
            path(fill = null, stroke = INK, strokeLineWidth = 2.4f) { circle(17f, 19f, 1f) }
        }
    }

    /** Magnifying glass — "Search" (tab bar + top-bar search circles). */
    val Search: ImageVector by lazy {
        organicIcon("OrganicSearch") {
            stroke(2.4f) {
                circle(11f, 11f, 6f)
                moveTo(15.5f, 15.5f)
                lineTo(20f, 20f)
            }
        }
    }

    /** Head + shoulders — bottom tab bar "Profile". */
    val Profile: ImageVector by lazy {
        organicIcon("OrganicProfile") {
            stroke(2.4f) {
                circle(12f, 8f, 3.6f)
                moveTo(5f, 20f)
                curveTo(6.4f, 16.4f, 9f, 14.6f, 12f, 14.6f)
                curveTo(15f, 14.6f, 17.6f, 16.4f, 19f, 20f)
            }
        }
    }

    /** Left chevron — back buttons. */
    val ChevronLeft: ImageVector by lazy {
        organicIcon("OrganicChevronLeft") {
            stroke(2.4f) {
                moveTo(15f, 4f)
                lineTo(7f, 12f)
                lineTo(15f, 20f)
            }
        }
    }

    /** Right chevron — trailing row affordance. */
    val ChevronRight: ImageVector by lazy {
        organicIcon("OrganicChevronRight") {
            stroke(2.6f) {
                moveTo(9f, 5f)
                lineTo(16f, 12f)
                lineTo(9f, 19f)
            }
        }
    }

    /** Bookmark ribbon — favorite / save-to-shelf toggle. */
    val Bookmark: ImageVector by lazy {
        organicIcon("OrganicBookmark") {
            stroke(2.4f) {
                moveTo(7f, 4f)
                horizontalLineTo(17f)
                verticalLineTo(20.5f)
                lineTo(12f, 16.5f)
                lineTo(7f, 20.5f)
                close()
            }
        }
    }

    /** Plus — new item / add. */
    val Plus: ImageVector by lazy {
        organicIcon("OrganicPlus") {
            stroke(2.75f) {
                moveTo(12f, 5f)
                verticalLineTo(19f)
                moveTo(5f, 12f)
                horizontalLineTo(19f)
            }
        }
    }

    /** Pencil — edit. */
    val Edit: ImageVector by lazy {
        organicIcon("OrganicEdit") {
            stroke(2.75f) {
                moveTo(12f, 20f)
                horizontalLineTo(21f)
                moveTo(16.5f, 3.5f)
                curveTo(17.33f, 2.67f, 18.67f, 2.67f, 19.5f, 3.5f)
                curveTo(20.33f, 4.33f, 20.33f, 5.67f, 19.5f, 6.5f)
                lineTo(7f, 19f)
                lineTo(3f, 20f)
                lineTo(4f, 16f)
                close()
            }
        }
    }

    /** Filled play triangle — Continue reading / Listen / Read next up. */
    val Play: ImageVector by lazy {
        ImageVector.Builder(
            name = "OrganicPlay",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = INK) {
                moveTo(8f, 5.5f)
                verticalLineTo(18.5f)
                lineTo(19f, 12f)
                close()
            }
        }.build()
    }

    /** Three nodes joined — share. */
    val Share: ImageVector by lazy {
        organicIcon("OrganicShare") {
            path(fill = null, stroke = INK, strokeLineWidth = 2.75f) { circle(18f, 5f, 3f) }
            path(fill = null, stroke = INK, strokeLineWidth = 2.75f) { circle(6f, 12f, 3f) }
            path(fill = null, stroke = INK, strokeLineWidth = 2.75f) { circle(18f, 19f, 3f) }
            stroke(2.75f) {
                moveTo(8.6f, 13.5f)
                lineTo(15.4f, 17.5f)
                moveTo(15.4f, 6.5f)
                lineTo(8.6f, 10.5f)
            }
        }
    }

    /** Two horizontal bars — drag handle. */
    val DragHandle: ImageVector by lazy {
        organicIcon("OrganicDragHandle") {
            stroke(2.75f) {
                moveTo(4f, 8f)
                horizontalLineTo(20f)
                moveTo(4f, 16f)
                horizontalLineTo(20f)
            }
        }
    }

    /** Single horizontal bar — remove / minus. */
    val Minus: ImageVector by lazy {
        organicIcon("OrganicMinus") {
            stroke(3f) {
                moveTo(5f, 12f)
                horizontalLineTo(19f)
            }
        }
    }

    /** Checkmark — finished / done marker. */
    val Check: ImageVector by lazy {
        organicIcon("OrganicCheck") {
            stroke(3f) {
                moveTo(5f, 12.5f)
                lineTo(9.5f, 17f)
                lineTo(19f, 7f)
            }
        }
    }

    /** "Aa" glyph — text-size / type sheet entry point. */
    val TypeSize: ImageVector by lazy {
        organicIcon("OrganicTypeSize") {
            stroke(2.4f) {
                moveTo(3.5f, 18f)
                lineTo(8f, 6f)
                lineTo(12.5f, 18f)
                moveTo(5f, 14.4f)
                horizontalLineTo(11f)
                moveTo(14.5f, 18f)
                lineTo(17.7f, 10f)
                lineTo(21f, 18f)
                moveTo(15.6f, 15.7f)
                horizontalLineTo(20.2f)
            }
        }
    }

    /** Speech bubble — comments / chat. */
    val Chat: ImageVector by lazy {
        organicIcon("OrganicChat") {
            stroke(2.4f) {
                moveTo(4.5f, 6.5f)
                curveTo(4.5f, 5.4f, 5.4f, 4.5f, 6.5f, 4.5f)
                horizontalLineTo(17.5f)
                curveTo(18.6f, 4.5f, 19.5f, 5.4f, 19.5f, 6.5f)
                verticalLineTo(13.5f)
                curveTo(19.5f, 14.6f, 18.6f, 15.5f, 17.5f, 15.5f)
                horizontalLineTo(10f)
                lineTo(6f, 19f)
                verticalLineTo(15.5f)
                horizontalLineTo(6.5f)
                curveTo(5.4f, 15.5f, 4.5f, 14.6f, 4.5f, 13.5f)
                close()
            }
        }
    }

    /** Star — rating. */
    val Star: ImageVector by lazy {
        ImageVector.Builder(
            name = "OrganicStar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = INK) {
                moveTo(12f, 4.3f)
                lineTo(14.3f, 9.1f)
                lineTo(19.6f, 9.8f)
                lineTo(15.7f, 13.5f)
                lineTo(16.7f, 18.8f)
                lineTo(12f, 16.2f)
                lineTo(7.3f, 18.8f)
                lineTo(8.3f, 13.5f)
                lineTo(4.4f, 9.8f)
                lineTo(9.7f, 9.1f)
                close()
            }
        }.build()
    }

    private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
        moveTo(cx - r, cy)
        arcToRelative(r, r, 0f, true, false, 2 * r, 0f)
        arcToRelative(r, r, 0f, true, false, -2 * r, 0f)
        close()
    }
}
