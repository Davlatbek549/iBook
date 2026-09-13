package com.example.dz.designsystem.components.organic

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape

/**
 * Progress, in the three shapes the design uses it: the filled donut on Home and in Library rows,
 * the stroked ring on Reading goal, and the bar in the reader.
 *
 * All three animate from their previous value on mount — the handoff asks for ~400ms ease-out —
 * so a percentage that changed reads as movement rather than as a different screen.
 */
private const val PROGRESS_ANIMATION_MILLIS = 400

/**
 * The filled donut: a conic wedge of accent over a track, with a smaller circle punched out of the
 * middle for the label. 58dp on Home's Keep going card (white track, 44dp centre), 44dp in Library
 * rows (neutral-200 track, 34dp centre).
 *
 * Built as two stacked circles rather than a stroked arc because that is what the design is — a
 * `conic-gradient` disc with a disc on top — and the two differ at the seam where the wedge closes.
 */
@Composable
fun OrganicProgressDonut(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 58.dp,
    innerSize: Dp = 44.dp,
    progressColor: Color = OrganicColors.accent,
    trackColor: Color = Color.White.copy(alpha = 0.55f),
    innerColor: Color = Color.Transparent,
    label: @Composable (() -> Unit)? = null,
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(PROGRESS_ANIMATION_MILLIS, easing = EaseOut),
        label = "donutProgress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(color = trackColor)
            drawArc(
                color = progressColor,
                // -90° so the wedge starts at twelve o'clock, as the design draws it.
                startAngle = -90f,
                sweepAngle = animated * 360f,
                useCenter = true
            )
        }
        Box(
            modifier = Modifier
                .size(innerSize)
                .clip(CircleShape)
                .background(innerColor),
            contentAlignment = Alignment.Center
        ) {
            label?.invoke()
        }
    }
}

/**
 * The stroked ring on Reading goal: 132dp across, a 14dp band, round cap, drawn from twelve
 * o'clock. Track is accent-2-300 against the sage card it sits on.
 */
@Composable
fun OrganicProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 132.dp,
    strokeWidth: Dp = 14.dp,
    progressColor: Color = OrganicColors.accent,
    trackColor: Color = OrganicColors.accent2_300,
    content: @Composable (() -> Unit)? = null,
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(PROGRESS_ANIMATION_MILLIS, easing = EaseOut),
        label = "ringProgress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = strokeWidth.toPx()
            val inset = stroke / 2f
            val arcSize = Size(
                width = this.size.width - stroke,
                height = this.size.height - stroke
            )
            val topLeft = Offset(inset, inset)

            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke)
            )
            if (animated > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = animated * 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
        }
        content?.invoke()
    }
}

/** The reader's progress bar, and the week strip on Reading goal. */
@Composable
fun OrganicProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    progressColor: Color = OrganicColors.accent,
    trackColor: Color = OrganicColors.neutral300,
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(PROGRESS_ANIMATION_MILLIS, easing = EaseOut),
        label = "barProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animated)
                .fillMaxHeight()
                .clip(RoundedCornerShape(OrganicShape.pill))
                .background(progressColor)
        )
    }
}
