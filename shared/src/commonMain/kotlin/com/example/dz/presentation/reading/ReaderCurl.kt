package com.example.dz.presentation.reading

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import kotlin.math.PI
import kotlin.math.atan2
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
 * There is no pager behind this one, so it does not borrow a pager's page index: it is told which
 * page is being read and says when that has changed. A pager whose layout is not composed cannot
 * be scrolled — asking it to waits for a layout that never comes — which is exactly the shape of
 * the bug this replaced, a turn that finished on screen and then never committed.
 */
@Composable
internal fun CurlingPages(
    index: Int,
    pageCount: Int,
    onIndexChange: (Int) -> Unit,
    pageText: (Int) -> String,
    style: TextStyle,
    page: OrganicPageColors,
    /** How far the page area sits from the top and foot of the screen this sheet covers. */
    textPadding: PageInset,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val sheet = rememberGraphicsLayer()
    // The finger is plain state, written straight from the gesture. It used to be an Animatable
    // snapped to from a coroutine launched per drag event, which is a race for every frame of a
    // swipe; the animation on release is the only part that needs to be driven over time.
    var finger by remember { mutableStateOf(Offset.Zero) }
    var turn by remember { mutableStateOf<Turn?>(null) }

    // Read through the gesture rather than keyed into it. The page count climbs as the book is
    // cut — a page at a time — and the page being read changes at the end of every turn; keying
    // the gesture on either tears it down and builds it again mid-swipe, which strands the sheet
    // half turned with nothing left running to finish or let go of it.
    val liveIndex by rememberUpdatedState(index)
    val liveCount by rememberUpdatedState(pageCount)

    val live = turn
    // Fixed when the turn starts, not read off [index] each frame: the page moves the moment a
    // turn completes, and a sheet that swapped its own face at that moment would flash.
    val topPage = live?.topPage ?: index
    val underPage = live?.underPage ?: (index + 1)

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { onTap() } }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { start ->
                        // The sheet is always hinged at the right edge; where along it decides
                        // whether the fold comes out vertical or diagonal.
                        turn = Turn(origin = Offset(size.width.toFloat(), start.y), grab = start)
                        finger = start
                    },
                    onDrag = { change, delta ->
                        change.consume()
                        val started = turn ?: return@detectDragGestures
                        // The first real movement says which way the book is going, and fixes
                        // which two pages this turn is between.
                        if (started.direction == null) {
                            turn = when {
                                delta.x < 0f && liveIndex < liveCount - 1 -> started.copy(
                                    direction = Direction.FORWARD,
                                    topPage = liveIndex,
                                    underPage = liveIndex + 1,
                                )

                                delta.x > 0f && liveIndex > 0 -> started.copy(
                                    direction = Direction.BACK,
                                    topPage = liveIndex - 1,
                                    underPage = liveIndex,
                                )

                                else -> started
                            }
                        }
                        finger = change.position
                    },
                    onDragEnd = {
                        val ending = turn ?: return@detectDragGestures
                        scope.launch {
                            finishTurn(
                                turn = ending,
                                from = finger,
                                width = size.width.toFloat(),
                                pageCount = liveCount,
                                onFinger = { finger = it },
                                onLanded = onIndexChange,
                                onDone = { turn = null },
                            )
                        }
                    },
                    onDragCancel = {
                        val ending = turn ?: return@detectDragGestures
                        val from = finger
                        scope.launch {
                            animate(Offset.VectorConverter, from, ending.origin, animationSpec = tween(SETTLE_MILLIS)) { value, _ ->
                                finger = value
                            }
                            turn = null
                        }
                    },
                )
            }
    ) {
        // The page being uncovered lies underneath, plain. Both sheets carry their own ground:
        // paper is not transparent, and without it the page beneath reads straight through the
        // one on top of it.
        if (underPage in 0 until pageCount) {
            Box(Modifier.fillMaxSize().background(page.ground)) {
                PageOfText(pageText(underPage), style, textPadding)
            }
        }

        // The sheet on top is recorded once and then drawn twice: the part still lying down, and
        // the part folded back showing its own reverse.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    sheet.record { this@drawWithContent.drawContent() }
                    val fold = live?.foldOrNull(finger, size.width)
                    if (fold == null) drawLayer(sheet) else drawFoldedSheet(sheet, fold, page)
                }
                // After the recording, not before: the ground has to be part of what the sheet
                // is, or it paints over the page underneath and the sheet's own layer comes out
                // transparent.
                .background(page.ground)
        ) {
            if (topPage in 0 until pageCount) {
                PageOfText(pageText(topPage), style, textPadding)
            }
        }
    }
}

private enum class Direction { FORWARD, BACK }

private data class Turn(
    val origin: Offset,
    /** Where the finger went down, which is what a turn back is measured from. */
    val grab: Offset,
    val direction: Direction? = null,
    /** Fixed when the direction is, so the page moving underneath cannot change the faces. */
    val topPage: Int = 0,
    val underPage: Int = 0,
) {
    /**
     * Where the held corner of the sheet is.
     *
     * Going forward the corner is under the finger, because that is where it was taken from: the
     * sheet's right edge is where the finger came down on it.
     *
     * Going back it is not. The sheet being pulled back is already folded away off the left side,
     * and its corner is out there — nowhere near the finger. Following the finger's position would
     * drop a crease into the middle of the page the instant it was touched, so the corner is moved
     * by how far the hand has travelled instead, starting from where the sheet is lying: no jump
     * on touch, and the page unfolds out of the left edge as it is pulled over.
     *
     * At twice the hand's pace, because a crease sits halfway between the corner and its hinge:
     * carrying a corner from one edge to the other moves the crease only half a page. Matched one
     * for one, a hand crossing the whole screen would leave the sheet standing upright in the
     * middle of the book — which is the turn back feeling wrong. At twice, a hand crossing the
     * screen lays the page flat, which is what a hand crossing the screen ought to do.
     */
    fun corner(finger: Offset, width: Float): Offset = when (direction) {
        Direction.BACK -> Offset(-width + (finger.x - grab.x) * 2f, finger.y)
        else -> finger
    }

    /** How far the sheet has been carried, as a fraction of the page, whichever way it is going. */
    fun carried(finger: Offset, width: Float): Float = when (direction) {
        Direction.BACK -> (finger.x - grab.x) / width
        else -> (origin.x - finger.x) / width
    }

    /** No direction yet, or a corner still on the hinge, means the sheet is simply lying flat. */
    fun foldOrNull(finger: Offset, width: Float): Fold? {
        if (direction == null) return null
        val corner = corner(finger, width)
        val axis = corner - origin
        if (hypot(axis.x, axis.y) < MIN_FOLD_PX) return null
        return Fold(origin = origin, finger = corner)
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

    /** The crease's own angle, which is how far the world is turned to lay it flat. */
    val creaseDegrees: Float get() = atan2(along.y, along.x) * 180f / PI.toFloat()
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
    // Reflection across the crease, said with the canvas's own moves: turn the world until the
    // crease lies flat, flip over it, turn the world back. The calls read backwards because the
    // last one issued is the first one the geometry meets.
    val crease = fold.creaseDegrees
    withTransform({
        rotate(crease, fold.middle)
        scale(1f, -1f, fold.middle)
        rotate(-crease, fold.middle)
    }) {
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
    from: Offset,
    width: Float,
    pageCount: Int,
    onFinger: (Offset) -> Unit,
    onLanded: (Int) -> Unit,
    onDone: () -> Unit,
) {
    val direction = turn.direction
    if (direction == null) {
        onDone()
        return
    }
    // The same measure either way: how much of a page the sheet has been carried across.
    val goesOver = turn.carried(from, width) > TURN_THRESHOLD

    // Animated in finger terms, since that is what the corner is derived from. Laid flat means
    // the finger has carried a whole page; folded away means it has carried none.
    val target = when {
        direction == Direction.FORWARD && goesOver -> Offset(-width, from.y)
        direction == Direction.FORWARD -> turn.origin
        goesOver -> Offset(turn.grab.x + width, from.y)
        else -> Offset(turn.grab.x, from.y)
    }
    animate(Offset.VectorConverter, from, target, animationSpec = tween(SETTLE_MILLIS)) { value, _ ->
        onFinger(value)
    }

    if (goesOver) {
        val landing = if (direction == Direction.FORWARD) turn.underPage else turn.topPage
        if (landing in 0 until pageCount) onLanded(landing)
    }
    onDone()
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
