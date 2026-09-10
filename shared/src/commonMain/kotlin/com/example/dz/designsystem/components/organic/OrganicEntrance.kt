package com.example.dz.designsystem.components.organic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

/**
 * Staggered entrances, shared by the screens that play themselves in.
 *
 * Both return progress from 0 before the element starts to 1 once it has settled; a caller reads
 * it in a `graphicsLayer` to drive alpha, offset or scale. Staggering by [delayMillis] rather than
 * moving everything at once is what makes a screen look assembled rather than faded up.
 *
 * The entrance runs when the element is first composed, and runs again if it is composed again —
 * which is what makes an onboarding page replay as the pager brings it back, without the page
 * having to track whether it is the current one.
 */
@Composable
fun rememberEntranceProgress(
    delayMillis: Int = 0,
    durationMillis: Int = 440,
): State<Float> {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis, delayMillis, FastOutSlowInEasing),
        )
    }
    return progress.asState()
}

/**
 * The same, on a spring: it carries an element a shade past its resting place and settles back,
 * which is what makes a thing read as *placed* rather than faded in. For anything that should
 * look like it has weight — a book going onto a shelf, a card landing on a pile.
 *
 * Progress can exceed 1 mid-flight, which is the overshoot. Drive an offset or rotation with it
 * directly, but coerce before using it as an alpha.
 */
@Composable
fun rememberSettleProgress(
    delayMillis: Int = 0,
    dampingRatio: Float = 0.7f,
): State<Float> {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        progress.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = dampingRatio, stiffness = Spring.StiffnessMediumLow),
        )
    }
    return progress.asState()
}
