package com.example.dz.designsystem.components.organic

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicLight
import com.example.dz.designsystem.theme.OrganicShape
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlin.math.abs
import kotlin.math.sqrt

/** One tab bar destination. */
data class OrganicTab(
    val route: String,
    val icon: ImageVector,
    /** Spoken label — the route string is an identifier, not something to read aloud. */
    val label: String,
)

/** Height of the glass pill. [ORGANIC_TAB_BAR_CLEARANCE] is derived from it, so they cannot drift. */
val ORGANIC_TAB_BAR_HEIGHT: Dp = 76.dp

private val TabBarSideInset = 24.dp
private val TabBarBottomInset = 22.dp

/** The mark at rest: a circle of this diameter, or a slice's width where that is narrower. */
private val MarkMaxSize = 52.dp
private val MarkIconSize = 24.dp

/**
 * How far the mark is allowed to smear between where it is and where the finger is. Without a cap a
 * fling across the bar would stretch it into a band the length of the pill.
 */
private val MarkMaxSmear = 40.dp

/** How far the halo reaches past the mark while a drag is live. */
private val MarkHaloGrow = 7.dp

/**
 * The mark's spring, as a damped-spring acceleration: `a = stiffness * offset - damping * velocity`.
 *
 * Underdamped on purpose: the mark should arrive with a little settle rather than stopping dead,
 * and while a drag is live the lag this spring leaves behind the finger is what draws the smear.
 */
private const val MarkStiffness = 550f
private const val MarkDampingRatio = 0.78f
private val MarkDamping = 2f * MarkDampingRatio * sqrt(MarkStiffness)

/** Longest step the integrator will take, so a dropped frame cannot fling the mark across the bar. */
private const val MarkMaxStep = 1f / 30f

/** Below this, in pixels and pixels per second, the mark has arrived and the frame loop parks. */
private const val MarkAtRest = 0.5f

/**
 * The floating tab bar: a glass pill inset from both edges and lifted off the bottom, with the
 * active destination marked by a terracotta blob you can tap, or take hold of and throw.
 *
 * It floats *over* the scroll region rather than reserving space below it, which is why it carries
 * its own insets instead of sitting in a `Scaffold` bottom-bar slot. Screens underneath pay for the
 * overlap with bottom padding — see [ORGANIC_TAB_BAR_CLEARANCE].
 *
 * Geometry from `dz-all-screens.html`: inset 24, bottom 22, pill radius, `--shadow-md`.
 *
 * ## Departures from the handoff, all requested
 * - The pill is [organicGlass] rather than an opaque `--color-neutral-900` fill. Pass a null
 *   [backdrop] to get the flat fill the handoff draws.
 * - Each item owns a full fifth of the bar's width as its tap target, with the mark centred inside
 *   it. The handoff's `space-around` left the gaps between circles inert, so a tap that landed a
 *   few dp wide of an icon did nothing. Equal slices put the marks exactly where `space-around`
 *   did — the geometry is unchanged, only the dead space is gone.
 * - The bar is [ORGANIC_TAB_BAR_HEIGHT] rather than the handoff's 64, and its mark and icons grew
 *   with it. A 64dp pill was a thin ledge to aim a drag along.
 *
 * ## The mark is one object, not five backgrounds
 * Each item used to draw its own circle and cross-fade it in and out. That cannot be dragged: a
 * thing that moves has to be a thing, with one position. So the mark is drawn once, in the pill's
 * own `drawBehind`, at a position a spring carries toward whichever target is current — the route's
 * slot at rest, the finger while a drag is live. Drawing it behind the row rather than laying it
 * out means following a finger costs a draw pass and no layout at all, and the icons sit on top of
 * it for free.
 *
 * ## How it reads as liquid
 * The mark trails the finger by however far the spring is behind, and it is drawn spanning that
 * gap: the trailing edge stays where the mark is, the leading edge reaches the finger, and the
 * corner radius is always half the height. So it is a circle standing still, a capsule in motion,
 * and it thins slightly as it stretches the way a bead of water does. Nothing here is keyframed —
 * the shape is a readout of how far behind the spring is, which is why it settles by itself.
 *
 * ## The gesture
 * A tap still selects, unchanged. Holding — or moving past touch slop, whichever lands first — takes
 * hold of the mark instead: it follows the finger across the bar, ticks as it crosses into each
 * slot, and on release settles into the slot it stopped in and navigates there. Slop-or-hold rather
 * than hold-only because a bar this size is easy to aim at and waiting out a long press before the
 * mark moves feels stuck; nothing else on this surface wants a horizontal drag, so there is nobody
 * to lose the race to.
 *
 * Pointer handling lives on the pill rather than on the items, because the drag belongs to the bar
 * as a whole and an item that consumed its own taps would also fire one at the end of every drag
 * that passed over it. The items keep their own semantics, so assistive tech still sees five tabs
 * and activates them through [onTabClick] without going near the touch path.
 */
@Composable
fun OrganicTabBar(
    tabs: List<OrganicTab>,
    currentRoute: String?,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: HazeState? = null,
) {
    val shape = RoundedCornerShape(OrganicShape.pill)
    val haptics = LocalHapticFeedback.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = TabBarSideInset, end = TabBarSideInset, bottom = TabBarBottomInset)
    ) {
        // The design's circle assumes a 390dp screen. Narrower phones do not leave room for five of
        // them, so the mark gives way rather than colliding with the pill's edge — the tap target
        // stays a full slice either way.
        val markSize = (maxWidth / tabs.size - 6.dp).coerceAtMost(MarkMaxSize)
        val slotWidth = constraints.maxWidth.toFloat() / tabs.size

        // -1 on every screen that shows the bar without being a tab — Collections, Settings, a book.
        // The mark is not drawn there, but a drag still picks it up, which is a way out of a pushed
        // screen the old bar did not have.
        val selectedIndex = tabs.indexOfFirst { it.route == currentRoute }

        fun slotAt(x: Float): Int = (x / slotWidth).toInt().coerceIn(0, tabs.lastIndex)
        fun centerOf(index: Int): Float = slotWidth * (index + 0.5f)

        /** Where the mark is drawn. The frame loop below moves it; nothing else writes it. */
        val markX = remember { mutableFloatStateOf(0f) }

        /** Where the mark is headed — a slot centre at rest, the finger during a drag. */
        val markTarget = remember { mutableFloatStateOf(0f) }

        var dragging by remember { mutableStateOf(false) }

        /** Set when the next move should not be animated: first placement, and picking up a hidden mark. */
        var placeWithoutAnimating by remember { mutableStateOf(true) }

        val markAlpha = animateFloatAsState(
            targetValue = if (selectedIndex >= 0 || dragging) 1f else 0f,
            label = "tabMarkAlpha"
        )
        val markLift = animateFloatAsState(
            targetValue = if (dragging) 1f else 0f,
            label = "tabMarkLift"
        )

        // The lit icon is whichever one the mark is over, not whichever one the route names. At rest
        // those agree; mid-drag the mark is the thing that is arriving, so it should be the thing
        // that decides. Derived, so the five items recompose when it crosses a slot and not once a
        // frame while it travels.
        val litIndex by remember(slotWidth, tabs.size, selectedIndex) {
            derivedStateOf {
                if (selectedIndex >= 0 || dragging) slotAt(markX.floatValue) else -1
            }
        }

        // The route can also change from elsewhere — Home's avatar opens the Profile tab — and the
        // mark should travel for that too, on the same spring.
        LaunchedEffect(selectedIndex, slotWidth) {
            if (selectedIndex >= 0 && !dragging) markTarget.floatValue = centerOf(selectedIndex)
        }

        // The spring is integrated here, a frame at a time, rather than handed to an `Animatable`
        // as a series of targets. That was the first version of this and it does not work: a finger
        // produces at least one new target per frame, every one of them cancels the animation in
        // flight, and an animation cancelled before it is ever given a frame never advances its
        // value at all. The mark stood still at the slot it started in while the finger ran away
        // from it, and only landed when the events stopped and the last animation was left alone to
        // finish. Integrating it directly means nothing restarts and the mark tracks whatever the
        // target is doing right now.
        LaunchedEffect(Unit) {
            var velocity = 0f

            while (true) {
                // Park until the mark has somewhere to be. This bar is on screen for most of the
                // app's life and still for nearly all of it, and a frame callback it does not need
                // is a cost paid on every screen that shows it.
                snapshotFlow { markTarget.floatValue }
                    .filter { destination ->
                        destination > 0f && (
                            placeWithoutAnimating ||
                                abs(destination - markX.floatValue) > MarkAtRest
                            )
                    }
                    .first()

                var previousFrame = 0L
                var moving = true
                while (moving) {
                    withFrameNanos { now ->
                        val destination = markTarget.floatValue
                        when {
                            placeWithoutAnimating -> {
                                placeWithoutAnimating = false
                                markX.floatValue = destination
                                velocity = 0f
                            }

                            previousFrame != 0L -> {
                                val step = ((now - previousFrame) / 1e9f).coerceAtMost(MarkMaxStep)
                                val pull = MarkStiffness * (destination - markX.floatValue)
                                velocity += (pull - MarkDamping * velocity) * step
                                markX.floatValue += velocity * step
                            }
                        }
                        previousFrame = now
                        moving = dragging ||
                            abs(destination - markX.floatValue) > MarkAtRest ||
                            abs(velocity) > MarkAtRest
                    }
                }
                velocity = 0f
            }
        }

        // The handoff's neutral-300 is drawn against a solid neutral-900 pill. Glass is lighter
        // and, worse, varies with whatever scrolls behind it, so on glass the inactive icons go
        // brighter to hold their contrast over a pale shelf of book covers. The pill is dark in
        // both appearances, so its icons take their colour from the light palette in both.
        val inactiveTint = if (backdrop != null) {
            Color.White.copy(alpha = 0.78f)
        } else {
            OrganicLight.neutral300
        }
        // Read here rather than in the draw below, which runs outside composition.
        val markColor = OrganicColors.accent

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(ORGANIC_TAB_BAR_HEIGHT)
                .shadow(
                    elevation = 10.dp,
                    shape = shape,
                    ambientColor = OrganicColors.shadow.copy(alpha = 0.16f),
                    spotColor = OrganicColors.shadow.copy(alpha = 0.16f)
                )
                .then(
                    if (backdrop != null) {
                        Modifier.organicGlass(backdrop = backdrop, shape = shape)
                    } else {
                        Modifier.background(OrganicColors.pill, shape)
                    }
                )
                // After the glass, which is this element's background and documented to go first,
                // and before the mark, which is the thing that needs clipping: at full stretch it
                // is wider than a slice and would otherwise run out past the pill's rounded ends.
                .clip(shape)
                .drawBehind {
                    val alpha = markAlpha.value
                    if (alpha <= 0.01f) return@drawBehind

                    // Read in the draw scope, not in composition: this is the whole reason
                    // following a finger does not recompose anything.
                    val from = markX.floatValue
                    val smearLimit = MarkMaxSmear.toPx()
                    val smear = (markTarget.floatValue - from).coerceIn(-smearLimit, smearLimit)
                    val reach = abs(smear)

                    val base = markSize.toPx()
                    val width = base + reach
                    val height = base - reach * 0.12f
                    val centerX = from + smear / 2f
                    val centerY = size.height / 2f

                    val lift = markLift.value
                    if (lift > 0.01f) {
                        val grow = MarkHaloGrow.toPx() * lift
                        drawRoundRect(
                            color = markColor,
                            alpha = 0.22f * lift * alpha,
                            topLeft = Offset(centerX - width / 2f - grow, centerY - height / 2f - grow),
                            size = Size(width + grow * 2f, height + grow * 2f),
                            cornerRadius = CornerRadius((height + grow * 2f) / 2f)
                        )
                    }
                    drawRoundRect(
                        color = markColor,
                        alpha = alpha,
                        topLeft = Offset(centerX - width / 2f, centerY - height / 2f),
                        size = Size(width, height),
                        cornerRadius = CornerRadius(height / 2f)
                    )
                }
                .pointerInput(tabs, slotWidth) {
                    val firstCenter = centerOf(0)
                    val lastCenter = centerOf(tabs.lastIndex)

                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val startX = down.position.x
                        var takeHold = false
                        var liftedEarly = false

                        // Whichever comes first decides what this gesture is: a lift (tap), slop
                        // crossed (drag), or the clock running out with the finger still down
                        // (hold, also a drag).
                        val decided = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id }
                                if (change == null || !change.pressed) {
                                    liftedEarly = true
                                    break
                                }
                                if (abs(change.position.x - startX) > viewConfiguration.touchSlop) {
                                    takeHold = true
                                    break
                                }
                            }
                        }
                        if (decided == null) takeHold = true

                        if (!takeHold) {
                            if (liftedEarly) onTabClick(tabs[slotAt(startX)].route)
                            return@awaitEachGesture
                        }

                        // On a screen with no current tab the mark is invisible and parked
                        // wherever it last was, so it appears under the finger rather than
                        // sliding in from a stale slot.
                        if (markAlpha.value <= 0.01f) placeWithoutAnimating = true
                        dragging = true
                        haptics.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)

                        var slot = slotAt(startX)
                        markTarget.floatValue = startX.coerceIn(firstCenter, lastCenter)

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            val stillDown = change.pressed
                            change.consume()
                            if (!stillDown) break

                            markTarget.floatValue = change.position.x.coerceIn(firstCenter, lastCenter)
                            val crossed = slotAt(change.position.x)
                            if (crossed != slot) {
                                slot = crossed
                                haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                            }
                        }

                        // Settle explicitly rather than waiting for the route to come back: it has
                        // to land in the slot even when that slot was already the current one and
                        // no navigation follows.
                        markTarget.floatValue = centerOf(slot)
                        dragging = false
                        haptics.performHapticFeedback(HapticFeedbackType.GestureEnd)
                        onTabClick(tabs[slot].route)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                OrganicTabItem(
                    tab = tab,
                    lit = index == litIndex,
                    markSize = markSize,
                    inactiveTint = inactiveTint,
                    onClick = { onTabClick(tab.route) }
                )
            }
        }
    }
}

/**
 * One destination: a full slice of the bar holding a centred icon, and nothing else.
 *
 * It draws no background of its own — the mark belongs to the bar — and claims no pointers, so the
 * drag can cross it without being interrupted. What it does keep is its semantics: a name, the tab
 * role, whether it is the selected one, and an action for assistive tech to fire, which reaches
 * [onClick] directly rather than through the gesture the mark is driven by.
 */
@Composable
private fun RowScope.OrganicTabItem(
    tab: OrganicTab,
    lit: Boolean,
    markSize: Dp,
    inactiveTint: Color,
    onClick: () -> Unit,
) {
    val tint by animateColorAsState(
        targetValue = if (lit) Color.White else inactiveTint,
        label = "tabTint"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .semantics(mergeDescendants = true) {
                contentDescription = tab.label
                role = Role.Tab
                selected = lit
                onClick(label = tab.label) {
                    onClick()
                    true
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = tab.icon,
            // The slice describes itself, and it is the slice that carries the role, the selected
            // state and the action. A second description on the glyph inside it is a duplicate.
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(if (markSize < MarkMaxSize) markSize * 0.46f else MarkIconSize)
        )
    }
}
