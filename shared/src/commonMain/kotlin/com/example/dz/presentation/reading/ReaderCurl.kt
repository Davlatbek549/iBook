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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.components.organic.OrganicPageColors
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.pow

/**
 * Pages that turn like paper.
 *
 * A sheet is taken by its right edge and dragged. The paper folds along the perpendicular bisector
 * of the line from where it was taken to where the finger is now, which is the whole of the trick:
 * take it by the middle of the edge and the fold is vertical and sweeps across; take it by a corner
 * and the fold lies diagonally and the corner peels. One rule, and the sheet behaves both ways.
 *
 * Everything past the fold is the back of the sheet, so its words come out mirrored and show
 * through the paper, exactly as they do when a real page is half over. The fold itself is a roll
 * rather than a crease: paper bends around something, and the width of what it bends around is
 * most of what tells your eye it is paper.
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
    /** How far the page number has taken the chrome's place, as the chrome fades out. */
    numberAlpha: Float,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val sheet = rememberGraphicsLayer()
    val recorded = remember { Recorded() }
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
                PageOfText(pageText(underPage), style, textPadding, underPage + 1, numberAlpha)
            }
        }

        // The sheet on top is recorded once and then drawn twice: the part still lying down, and
        // the part folded back showing its own reverse.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    val fold = live?.foldOrNull(finger, size.width)
                    if (fold == null) {
                        // Lying flat there is nothing to draw twice, so there is no reason to
                        // keep a copy of it either.
                        recorded.turn = null
                        drawContent()
                        return@drawWithContent
                    }
                    // A page is taken once and drawn from all the way over. What is on it cannot
                    // change while it is in the air — the faces were fixed when the turn began —
                    // so the copy is taken on the first frame of the turn and used for the rest.
                    // Taken every frame instead, every frame pays to set a whole page of type,
                    // and that is a page of type per frame of a swipe.
                    if (recorded.turn !== live ||
                        recorded.width != size.width ||
                        recorded.height != size.height
                    ) {
                        sheet.record { this@drawWithContent.drawContent() }
                        recorded.turn = live
                        recorded.width = size.width
                        recorded.height = size.height
                    }
                    drawFoldedSheet(sheet, fold, page)
                }
                // After the recording, not before: the ground has to be part of what the sheet
                // is, or it paints over the page underneath and the sheet's own layer comes out
                // transparent.
                .background(page.ground)
        ) {
            if (topPage in 0 until pageCount) {
                PageOfText(pageText(topPage), style, textPadding, topPage + 1, numberAlpha)
            }
        }
    }
}

private enum class Direction { FORWARD, BACK }

/** Which turn the top sheet's copy was taken for, so it is not taken again for the same one. */
private class Recorded {
    var turn: Turn? = null
    var width = 0f
    var height = 0f
}

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
 * Draws the top sheet: the part still lying down, the roll it bends around, and the back of it.
 *
 * Paper does not crease when a page is turned — it rolls. The sheet leaves the page along the fold
 * line, wraps a half turn around a cylinder lying on that line, and comes back flat on top of
 * itself face down. That roll is the whole look of a real page going over, and it is what a plain
 * reflection cannot give you: a reflection is the same thing with a cylinder of no width at all.
 *
 * So the back of the sheet is drawn through a map from paper to screen rather than through one
 * mirror. Measuring along the paper from the fold line, material at distance `s` lands at
 *
 *     s <= PI*r : out = -r * sin(s / r)   — on the roll, bulging past the fold over the new page
 *     s >  PI*r : out = s - PI*r          — flat again, the far side of the sheet
 *
 * where `out` is distance from the fold line towards the reader's hand. The two agree in value and
 * in slope where they meet, so the roll runs smoothly into the flat part with no seam.
 *
 * Only the second half of the roll is ever drawn. The first half is face up but lies under the
 * second — the cylinder is between them — so it is hidden everywhere, and skipping it means the map
 * above never doubles back on itself and the bands can simply be painted in order.
 */
private fun DrawScope.drawFoldedSheet(sheet: GraphicsLayer, fold: Fold, page: OrganicPageColors) {
    val middle = fold.middle
    val direction = fold.across.normalised()
    // How much paper is over the fold: the fold line sits half way between the held point and
    // where it has been dragged to, so the held point is this far back along the sheet from it.
    val reach = hypot(fold.across.x, fold.across.y) / 2f
    // A half turn eats PI * r of paper, and a sheet only just lifted has not got that much to
    // give. So the roll starts as tight as the paper allows and opens out as the page comes up,
    // which is also how it looks: a page just picked up turns on a sharp edge, one half over on
    // a fat one.
    val radius = minOf(ROLL_RADIUS.toPx(), reach / PI.toFloat())
    val crease = fold.creaseDegrees
    // Far enough to pass any corner of the sheet, whichever way the fold lies across it.
    val span = size.width + size.height

    // Every clip below is taken in the fold's own frame — the world turned until the fold lies
    // flat — where "how far over the fold is this" is a difference in y and nothing more. What it
    // replaces was the screen's four corners cut against the fold line into a polygon, walked into
    // a path and handed over as a shaped clip, for every band, on every frame. A straight-sided
    // clip is a rectangle the whole way down; a shaped one has to be drawn into a mask first.

    // The part of the sheet still lying down is the far side of the fold from the held point.
    withTransform({ rotate(crease, middle) }) {
        clipRect(
            left = middle.x - span,
            top = middle.y - span,
            right = middle.x + span,
            bottom = middle.y,
        ) {
            withTransform({ rotate(-crease, middle) }) { drawLayer(sheet) }
        }
    }

    // The shadow the raised sheet throws, cast from the roll's outer lip rather than from the
    // fold line — the lip is the part standing over the new page, so that is where the light
    // stops.
    drawFoldShadow(fold, page, standOff = radius, span = span)

    /**
     * One band of the back of the sheet, from [fromArc] to [toArc] along the paper, put down
     * between [fromOut] and [toOut] on the screen.
     *
     * Both are strips between lines parallel to the fold, so what carries one onto the other is a
     * squash across the fold and a shove along it — a reflection first, since this is the back.
     * The turn onto the fold's frame is left until last so that the clip in between, which is the
     * one that says which paper this band holds, is a plain rectangle.
     *
     * [toArc] may be infinite: that is the flat part of the sheet, which runs to its edge.
     */
    fun band(fromArc: Float, toArc: Float, fromOut: Float, toOut: Float, fromTone: Color, toTone: Color) {
        val ends = toArc.isFinite()
        val squash = if (ends) (toOut - fromOut) / (toArc - fromArc) else 1f
        withTransform({
            // Read backwards: the last issued is the first the paper meets.
            rotate(crease, middle)
            translate(0f, squash * fromArc - fromOut)
            scale(1f, squash, middle)
            scale(1f, -1f, middle)
        }) {
            clipRect(
                left = middle.x - span,
                top = middle.y + fromArc,
                right = middle.x + span,
                bottom = middle.y + if (ends) toArc else span,
            ) {
                withTransform({ rotate(-crease, middle) }) {
                    drawLayer(sheet)
                    // Paper is not glass: the words on the far face come through it, not off it.
                    drawRect(color = page.ground, alpha = PAPER_OPACITY)
                    // The light on the roll, as a ramp between the band's two edges rather than
                    // one tone for the whole band. Flat tones make a cylinder look like a folded
                    // map: bands you can count.
                    if (ends && (fromTone.alpha > 0f || toTone.alpha > 0f)) {
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(fromTone, toTone),
                                start = middle - direction * fromArc,
                                end = middle - direction * toArc,
                            )
                        )
                    }
                }
            }
        }
    }

    if (radius > MIN_ROLL_PX) {
        // Only as many bands as the roll is wide enough to show. A page just picked up turns on a
        // roll a few pixels across, and cutting that into ten leaves nine of them thinner than the
        // line that would draw them: all of the cost and none of the curve. Most of a quick swipe
        // is spent at exactly that end.
        val bands = (radius / MIN_BAND_PX).toInt().coerceIn(1, ROLL_BANDS)
        // Banded by how much screen each covers, not how much paper: near the lip the paper is
        // almost edge on, and bands cut evenly along the paper would put most of them in the first
        // pixel and squash each one to nothing.
        for (i in 0 until bands) {
            val from = i / bands.toFloat()
            val to = (i + 1) / bands.toFloat()
            band(
                fromArc = radius * rollAngle(from),
                toArc = radius * rollAngle(to),
                fromOut = -radius * (1f - from),
                toOut = -radius * (1f - to),
                fromTone = rollTone(from, page),
                toTone = rollTone(to, page),
            )
        }
    }

    // And everything past the roll, which is flat: the same reflection as ever, moved back by the
    // paper the roll has taken up.
    band(
        fromArc = PI.toFloat() * radius,
        toArc = Float.POSITIVE_INFINITY,
        fromOut = 0f,
        toOut = 0f,
        fromTone = Color.Transparent,
        toTone = Color.Transparent,
    )
}

/**
 * How far around the roll a band lies, in radians, for [part] of the way across the visible half.
 *
 * The visible half runs from edge on at the lip to flat at the fold line. Asking for it by screen
 * position rather than by angle is what keeps the bands even, and inverting `out = -r sin(angle)`
 * is all that takes.
 */
private fun rollAngle(part: Float): Float = PI.toFloat() - asin((1f - part).coerceIn(0f, 1f))

/**
 * The light on the roll, [part] of the way from its outer lip to the fold line.
 *
 * Two things in one ramp. At the lip the page is nearly shut against the one below it and almost
 * no light gets in, so it goes dark and opens out from there. Past that the roll turns to face
 * upwards and catches a sheen, strongest a little before it flattens. By the fold line it is
 * ordinary paper again and the ramp has to be gone, or the flat part beyond would start on a step.
 *
 * Shaded by how far round the roll a point is rather than by the angle it makes with the light.
 * That angle opens almost at once — it is a sine — which crams the whole ramp into the first pixel
 * or two and leaves the rest of the roll as flat as the crease it was meant to replace.
 */
private fun rollTone(part: Float, page: OrganicPageColors): Color =
    if (part < ROLL_TONE_CROSS) {
        page.ink.copy(alpha = ROLL_SHADE * (1f - part / ROLL_TONE_CROSS).pow(ROLL_FALLOFF))
    } else {
        val round = (part - ROLL_TONE_CROSS) / (1f - ROLL_TONE_CROSS)
        Color.White.copy(
            alpha = ROLL_GLOSS * (1f - abs(round - GLOSS_AT) / GLOSS_WIDTH).coerceAtLeast(0f)
        )
    }

/**
 * A soft edge of shade on the revealed page, hugging the paper standing over it.
 *
 * [standOff] is the roll's radius. The paper does not stand on the fold line but a roll's width
 * past it, and a shadow cast from the fold line would fall under the sheet throwing it.
 */
private fun DrawScope.drawFoldShadow(
    fold: Fold,
    page: OrganicPageColors,
    standOff: Float,
    span: Float,
) {
    val middle = fold.middle
    val crease = fold.creaseDegrees
    val direction = fold.across.normalised()
    val lip = middle - direction * standOff
    withTransform({ rotate(crease, middle) }) {
        clipRect(
            left = middle.x - span,
            top = middle.y + standOff,
            right = middle.x + span,
            bottom = middle.y + span,
        ) {
            withTransform({ rotate(-crease, middle) }) {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(page.ink.copy(alpha = FOLD_SHADOW), Color.Transparent),
                        start = lip,
                        end = lip - direction * FOLD_SHADOW_PX,
                    )
                )
            }
        }
    }
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
    //
    // Every one of these lands back on the hinge's own line rather than on whatever height the
    // hand happened to let go at, and that matters more than it looks. A turn back finishes with
    // the corner at the hinge's x, so the only thing left in the line from the hinge to the corner
    // is the height the hand drifted by — a straight up-and-down line, which makes the fold
    // perpendicular to it lie flat ACROSS the page. The sheet never reaches its hinge, and the
    // last thing you see of a page going back is a crease swinging round to horizontal through the
    // middle of it with the words upside down above. Landing on the hinge's height closes the fold
    // to nothing instead, and on the way there the page straightens as it falls, which is what
    // paper does.
    val target = when {
        direction == Direction.FORWARD && goesOver -> Offset(-width, turn.grab.y)
        direction == Direction.FORWARD -> turn.origin
        goesOver -> Offset(turn.grab.x + width, turn.grab.y)
        else -> turn.grab
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

/**
 * The shade the raised sheet throws on the page it uncovers.
 *
 * Kept under the roll's own shading. The roll is the thing to look at; a shadow heavier than the
 * paper casting it reads as the edge of a hole rather than the edge of a page.
 */
private const val FOLD_SHADOW = 0.26f
private const val FOLD_SHADOW_PX = 52f

/**
 * How fat the roll gets.
 *
 * It is the stiffness of the paper, really: a newspaper turns on a tight roll and a magazine cover
 * on a wide one. This is about a book's.
 */
private val ROLL_RADIUS = 20.dp

/** Under this the roll is thinner than the line that would draw it, and the fold is just a fold. */
private const val MIN_ROLL_PX = 1.5f

/**
 * How many strips the roll is painted in.
 *
 * Each is a straight piece of a curve, so this is how round the roll comes out. It only has to
 * carry the paper: the light across it is a ramp drawn through the strips, not one tone each, so
 * the count does not have to be high enough to hide steps in the shading — only high enough that
 * the mirrored words bend rather than kink. Each one is also a pass over the whole sheet, which
 * is what makes this the number to keep honest.
 */
private const val ROLL_BANDS = 6

/** Thinner than this and a band is narrower than the line drawing it, so it is not worth one. */
private const val MIN_BAND_PX = 2.5f

/** The shading around the roll: how dark the lip goes, and how fast it opens out. */
private const val ROLL_SHADE = 0.34f
private const val ROLL_FALLOFF = 1.6f

/** Where the shade has run out and the sheen has not started, as a part of the way round. */
private const val ROLL_TONE_CROSS = 0.55f

/** The sheen off the curve: how bright, how far past the crossing it sits, and how wide it is. */
private const val ROLL_GLOSS = 0.16f
private const val GLOSS_AT = 0.6f
private const val GLOSS_WIDTH = 0.38f
