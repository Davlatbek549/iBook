package com.example.dz.presentation.reading

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import com.example.dz.designsystem.components.organic.OrganicPageColors
import kotlinx.coroutines.launch
import kotlin.math.hypot

/**
 * Pages that turn like paper.
 *
 * A sheet is taken by its right edge and dragged. The paper folds along the perpendicular bisector
 * of the line from where it was taken to where the finger is now, which is the whole of the trick:
 * take it by the middle of the edge and the fold is vertical and sweeps across; take it by a corner
 * and the fold lies diagonally and the corner peels. One rule, and the sheet behaves both ways.
 *
 * Everything past the fold is the back of the sheet — the same page reflected, so its words come
 * out mirrored and show through the paper, exactly as they do when a real page is half over.
 *
 * The pager is still the authority on which page is being read even though it draws nothing here;
 * a completed turn tells it to move, so the progress bar, the count and the pin all go on working.
 */
@Composable
internal fun CurlingPages(
    pagerState: PagerState,
    pageText: (Int) -> String,
    style: TextStyle,
    page: OrganicPageColors,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val sheet = rememberGraphicsLayer()
    val finger = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    var turn by remember { mutableStateOf<Turn?>(null) }

    val current = pagerState.currentPage
    val live = turn
    // Which sheet is on top, and which is being uncovered beneath it.
    val topPage = if (live?.direction == Direction.BACK) current - 1 else current
    val underPage = if (live?.direction == Direction.BACK) current else current + 1

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { onTap() } }
            .pointerInput(pagerState) {
                detectDragGestures(
                    onDragStart = { start ->
                        // The sheet is always hinged at the right edge; where along it decides
                        // whether the fold comes out vertical or diagonal.
                        turn = Turn(
                            origin = Offset(size.width.toFloat(), start.y),
                            direction = null,
                        )
                        scope.launch { finger.snapTo(start) }
                    },
                    onDrag = { change, delta ->
                        change.consume()
                        val started = turn ?: return@detectDragGestures
                        // The first real movement says which way the book is going.
                        val direction = started.direction ?: when {
                            delta.x < 0f && current < pagerState.pageCount - 1 -> Direction.FORWARD
                            delta.x > 0f && current > 0 -> Direction.BACK
                            else -> null
                        }
                        turn = started.copy(direction = direction)
                        if (direction != null) scope.launch { finger.snapTo(change.position) }
                    },
                    onDragEnd = {
                        val ending = turn ?: return@detectDragGestures
                        scope.launch {
                            finishTurn(ending, finger, size.width.toFloat(), pagerState)
                            turn = null
                        }
                    },
                    onDragCancel = {
                        val ending = turn ?: return@detectDragGestures
                        scope.launch {
                            finger.animateTo(ending.origin, tween(SETTLE_MILLIS))
                            turn = null
                        }
                    },
                )
            }
    ) {
        // The page being uncovered lies underneath, plain.
        if (underPage in 0 until pagerState.pageCount) {
            PageOfText(pageText(underPage), style)
        }

        // The sheet on top is recorded once and then drawn twice: the part still lying down, and
        // the part folded back showing its own reverse.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    sheet.record { this@drawWithContent.drawContent() }
                    val fold = live?.foldOrNull(finger.value)
                    if (fold == null) drawLayer(sheet) else drawFoldedSheet(sheet, fold, page)
                }
        ) {
            if (topPage in 0 until pagerState.pageCount) {
                PageOfText(pageText(topPage), style)
            }
        }
    }
}

private enum class Direction { FORWARD, BACK }

private data class Turn(val origin: Offset, val direction: Direction?) {
    /** No direction yet, or a finger still on the hinge, means the sheet is simply lying flat. */
    fun foldOrNull(finger: Offset): Fold? {
        if (direction == null) return null
        val axis = finger - origin
        if (hypot(axis.x, axis.y) < MIN_FOLD_PX) return null
        return Fold(origin = origin, finger = finger)
    }
}

/**
 * Where the paper bends.
 *
 * [origin] is the point the sheet is held by and [finger] is where that point has been dragged to.
 * The crease is the perpendicular bisector between them, which is what makes the held corner land
 * exactly under the finger however the sheet is pulled.
 */
private data class Fold(val origin: Offset, val finger: Offset) {
    val middle: Offset get() = Offset((origin.x + finger.x) / 2f, (origin.y + finger.y) / 2f)

    /** From the held point towards the finger: the side of the crease the sheet lifts off.  */
    val across: Offset get() = finger - origin

    /** Along the crease itself. */
    val along: Offset get() = Offset(-across.y, across.x)
}

/**
 * Draws the top sheet in its two halves.
 *
 * The half still lying down is drawn as it is. The half that has lifted is drawn through the
 * reflection, which puts its words down mirrored on the other side of the crease — and because the
 * reflection is applied before the clip, the clipped region travels with it.
 */
private fun DrawScope.drawFoldedSheet(sheet: GraphicsLayer, fold: Fold, page: OrganicPageColors) {
    val bounds = listOf(
        Offset.Zero,
        Offset(size.width, 0f),
        Offset(size.width, size.height),
        Offset(0f, size.height),
    )

    // The part of the sheet still lying down is the far side of the crease from the held point.
    val lyingDown = bounds.keepSideOf(fold.middle, fold.across, keepPositive = true)
    if (lyingDown.isNotEmpty()) {
        clipPath(lyingDown.toPath()) { drawLayer(sheet) }
    }

    // The shadow the raised sheet throws across the page it is uncovering.
    drawFoldShadow(fold, page)

    // The raised half, reflected: the back of the paper.
    val raised = bounds.keepSideOf(fold.middle, fold.across, keepPositive = false)
    if (raised.isEmpty()) return
    val path = raised.toPath()
    withTransform({ transform(reflectionAcross(fold)) }) {
        clipPath(path) {
            drawLayer(sheet)
            // Paper is not glass: the words on the far face come through it, not off it.
            drawRect(color = page.ground, alpha = PAPER_OPACITY)
            // And it is lit along the crease, where the sheet is still bending.
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(page.ink.copy(alpha = CREASE_SHADE), Color.Transparent),
                    start = fold.middle,
                    end = fold.middle - fold.across.normalised() * CREASE_SHADE_PX,
                )
            )
        }
    }
}

/** A soft edge of shade on the revealed page, hugging the crease and falling away from it. */
private fun DrawScope.drawFoldShadow(fold: Fold, page: OrganicPageColors) {
    val direction = fold.across.normalised()
    clipPath(
        listOf(
            Offset.Zero,
            Offset(size.width, 0f),
            Offset(size.width, size.height),
            Offset(0f, size.height),
        ).keepSideOf(fold.middle, fold.across, keepPositive = false).toPath()
    ) {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(page.ink.copy(alpha = FOLD_SHADOW), Color.Transparent),
                start = fold.middle,
                end = fold.middle - direction * FOLD_SHADOW_PX,
            )
        )
    }
}

/**
 * Reflection across the crease.
 *
 * Written straight into the matrix rather than built from a translate-rotate-scale-rotate-translate
 * sandwich: the sandwich is four chances to get an order wrong, and this is the same thing said
 * once.
 */
private fun reflectionAcross(fold: Fold): Matrix {
    val along = fold.along.normalised()
    val a = along.x * along.x - along.y * along.y
    val b = 2f * along.x * along.y
    val middle = fold.middle

    return Matrix().apply {
        this[0, 0] = a
        this[1, 0] = b
        this[0, 1] = b
        this[1, 1] = -a
        // Put the crease back through the middle point after reflecting about the origin.
        this[0, 3] = middle.x - (a * middle.x + b * middle.y)
        this[1, 3] = middle.y - (b * middle.x - a * middle.y)
    }
}

/**
 * The part of a polygon on one side of a line, by the usual corner-walk: keep the corners that are
 * inside, and where an edge crosses the line, keep the crossing point too.
 */
private fun List<Offset>.keepSideOf(
    through: Offset,
    normal: Offset,
    keepPositive: Boolean,
): List<Offset> {
    fun side(point: Offset): Float {
        val d = (point.x - through.x) * normal.x + (point.y - through.y) * normal.y
        return if (keepPositive) d else -d
    }

    val kept = mutableListOf<Offset>()
    for (i in indices) {
        val from = this[i]
        val to = this[(i + 1) % size]
        val fromSide = side(from)
        val toSide = side(to)
        if (fromSide >= 0f) kept += from
        if ((fromSide >= 0f) != (toSide >= 0f)) {
            val t = fromSide / (fromSide - toSide)
            kept += Offset(from.x + (to.x - from.x) * t, from.y + (to.y - from.y) * t)
        }
    }
    return kept
}

private fun List<Offset>.toPath(): Path = Path().apply {
    moveTo(this@toPath[0].x, this@toPath[0].y)
    for (i in 1 until this@toPath.size) lineTo(this@toPath[i].x, this@toPath[i].y)
    close()
}

private fun Offset.normalised(): Offset {
    val length = hypot(x, y)
    return if (length < 0.0001f) Offset.Zero else Offset(x / length, y / length)
}

private operator fun Offset.times(scale: Float) = Offset(x * scale, y * scale)

/**
 * Lets go of the sheet.
 *
 * Past the middle of the page it goes over; short of it, it falls back where it came from. Either
 * way the finger is animated to where the sheet would be if it had been carried there, so the
 * paper finishes the movement rather than snapping.
 */
private suspend fun finishTurn(
    turn: Turn,
    finger: Animatable<Offset, *>,
    width: Float,
    pagerState: PagerState,
) {
    val direction = turn.direction ?: return
    val carried = turn.origin.x - finger.value.x
    val goesOver = when (direction) {
        Direction.FORWARD -> carried > width * TURN_THRESHOLD
        Direction.BACK -> carried < width * (1f - TURN_THRESHOLD)
    }

    val target = when {
        // Off the left edge entirely, so the sheet is fully over before the page index moves.
        goesOver && direction == Direction.FORWARD -> Offset(-width, finger.value.y)
        goesOver && direction == Direction.BACK -> turn.origin
        direction == Direction.FORWARD -> turn.origin
        else -> Offset(-width, finger.value.y)
    }
    finger.animateTo(target, tween(SETTLE_MILLIS))

    if (!goesOver) return
    val landing = when (direction) {
        Direction.FORWARD -> pagerState.currentPage + 1
        Direction.BACK -> pagerState.currentPage - 1
    }
    if (landing in 0 until pagerState.pageCount) pagerState.scrollToPage(landing)
}

/** Below this the fold is too small to be a fold, and the sheet is simply lying down. */
private const val MIN_FOLD_PX = 2f

/** How far across the page the sheet has to be carried before letting go turns it. */
private const val TURN_THRESHOLD = 0.35f

private const val SETTLE_MILLIS = 320

/** How much of the front comes through the back of the sheet. */
private const val PAPER_OPACITY = 0.62f

/** The shading along the crease, on the paper and on the page it uncovers. */
private const val CREASE_SHADE = 0.16f
private const val CREASE_SHADE_PX = 90f
private const val FOLD_SHADOW = 0.3f
private const val FOLD_SHADOW_PX = 60f
