package com.example.dz.designsystem.components.organic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily

/**
 * The moment something has worked, held briefly over the screen that did it.
 *
 * It confirms and then gets out of the way on its own — there is no button, because by the time
 * this appears there is nothing left to decide. Whoever shows it is responsible for moving on;
 * this only says so.
 *
 * The entrance is staggered the way the splash is: the card settles, then the ring opens out of
 * the roundel, then the tick lands. Everything is tuned to finish well inside the shortest dwell
 * a caller would sensibly use, so it is never cut off mid-animation.
 */
@Composable
fun OrganicSuccessOverlay(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    val cardScale = remember { Animatable(0.86f) }
    val cardAlpha = remember { Animatable(0f) }
    val tickScale = remember { Animatable(0f) }
    val ringProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        cardAlpha.animateTo(1f, animationSpec = tween(durationMillis = 180))
    }
    LaunchedEffect(Unit) {
        cardScale.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )
    }
    LaunchedEffect(Unit) {
        // Behind the tick rather than with it: the ring reads as the roundel opening out, which
        // only works if it starts before the tick arrives to draw the eye.
        ringProgress.animateTo(1f, animationSpec = tween(durationMillis = 620, easing = LinearEasing))
    }
    LaunchedEffect(Unit) {
        tickScale.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        )
    }

    // Every animated value below is read inside a draw or layer lambda, never in composition. Read
    // in composition, each one recomposed the overlay on every frame it animated; read here, a
    // frame only redraws.
    Box(
        modifier = modifier
            .fillMaxSize()
            // Same scrim as the legal sheet, so a covered screen always dims by the same amount.
            .drawBehind {
                drawRect(OrganicColors.neutral900, alpha = 0.42f * cardAlpha.value)
            }
            // The scrim swallows everything, drags included. The form underneath is still
            // mounted, and a stray tap landing in a field the reader can no longer see would
            // be a keyboard rising behind the confirmation.
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent().changes.forEach { it.consume() }
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .graphicsLayer {
                    scaleX = cardScale.value
                    scaleY = cardScale.value
                }
                .shadow(elevation = 18.dp, shape = RoundedCornerShape(OrganicShape.radiusLg))
                .clip(RoundedCornerShape(OrganicShape.radiusLg))
                .background(OrganicColors.bg)
                .padding(horizontal = 28.dp, vertical = 32.dp)
                // One announcement for the whole moment, rather than three fragments as each
                // piece animates in.
                .semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = "$title. $body"
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Box(
                modifier = Modifier.size(96.dp),
                contentAlignment = Alignment.Center,
            ) {
                // The ring opens out of the roundel and fades as it goes, so the roundel is what
                // is left rather than the ring being something that vanished.
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val progress = ringProgress.value
                    if (progress <= 0f || progress >= 1f) return@Canvas
                    val stroke = 3.dp.toPx()
                    val start = size.minDimension / 2f - stroke
                    val radius = start + (size.minDimension / 2f) * progress
                    drawCircle(
                        color = OrganicColors.accent2_400.copy(alpha = (1f - progress) * 0.9f),
                        radius = radius,
                        style = Stroke(width = stroke),
                    )
                }

                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(OrganicColors.accent2_200),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = InkIcons.Done,
                        contentDescription = null,
                        tint = OrganicColors.accent2_900,
                        modifier = Modifier
                            .size(38.dp)
                            .graphicsLayer {
                                scaleX = tickScale.value
                                scaleY = tickScale.value
                            },
                    )
                }
            }

            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = organicHeadingFontFamily(),
                fontSize = 24.sp,
                lineHeight = 28.sp,
                color = OrganicColors.text,
                textAlign = TextAlign.Center,
            )

            Text(
                text = body,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = organicBodyFontFamily(),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = OrganicColors.neutral700,
                textAlign = TextAlign.Center,
            )
        }
    }
}
