package com.example.dz.app_components.goal_selected_dialog

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.goal_selected_subtitle
import dz.shared.generated.resources.goal_selected_title
import org.jetbrains.compose.resources.stringResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val StopwatchYellow = Color(0xFFFFD84D)
private val StopwatchRimPink = Color(0xFFFF304F)
private val StopwatchPurple = Color(0xFF6254FF)
private val StopwatchButtonPurple = Color(0xFF5B56F6)
private val StopwatchButtonDark = Color(0xFF4945D9)
private val StopwatchButtonEdge = Color(0xFF3F3BCB)
private val StopwatchPink = Color(0xFFFF4FA0)
private val StopwatchTurquoise = Color(0xFF64D3D0)
private val StopwatchYellowOrange = Color(0xFFFFB333)
private val StopwatchLightBlue = Color(0xFF6FA8FF)

private data class GoalSelectedDialogMetrics(
    val cardWidth: Dp,
    val cardCorner: Dp,
    val cardHorizontalPadding: Dp,
    val cardVerticalPadding: Dp,
    val illustrationSize: Dp,
    val illustrationToTitleSpacing: Dp,
    val titleSize: TextUnit,
    val titleLineHeight: TextUnit,
    val subtitleTopSpacing: Dp,
    val subtitleSize: TextUnit,
    val subtitleLineHeight: TextUnit
)

@Composable
fun GoalSelectedDialog(
    modifier: Modifier = Modifier,
    goalMinutes: Int = 40
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        val metrics = rememberGoalSelectedDialogMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme
        val cardShape = RoundedCornerShape(metrics.cardCorner)

        Column(
            modifier = Modifier
                .width(metrics.cardWidth)
                .shadow(
                    elevation = 10.dp,
                    shape = cardShape,
                    ambientColor = Color.Black.copy(alpha = 0.08f),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                )
                .clip(cardShape)
                .background(colorScheme.surface)
                .padding(
                    horizontal = metrics.cardHorizontalPadding,
                    vertical = metrics.cardVerticalPadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            StopwatchIllustration(
                modifier = Modifier.size(metrics.illustrationSize)
            )

            Spacer(modifier = Modifier.height(metrics.illustrationToTitleSpacing))

            Text(
                text = stringResource(Res.string.goal_selected_title, goalMinutes),
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleLineHeight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.subtitleTopSpacing))

            Text(
                text = stringResource(Res.string.goal_selected_subtitle),
                color = colorScheme.onSurface.copy(alpha = 0.68f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.subtitleSize,
                    lineHeight = metrics.subtitleLineHeight,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
fun StopwatchIllustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawCircle(
            color = Color(0xFFF5F5F5),
            radius = size.minDimension * 0.48f,
            center = Offset(size.width / 2f, size.height / 2f)
        )

        drawStopwatchConfetti()
        drawStopwatch()
    }
}

private fun DrawScope.drawStopwatch() {
    val minDimension = size.minDimension
    val center = Offset(size.width / 2f, size.height * 0.54f)
    val stopwatchRadius = minDimension * 0.28f
    val rimWidth = minDimension * 0.045f

    drawStopwatchTopButton(center, stopwatchRadius)
    drawStopwatchSideButton(
        center = Offset(center.x - stopwatchRadius * 0.78f, center.y - stopwatchRadius * 0.82f),
        rotation = -45f
    )
    drawStopwatchSideButton(
        center = Offset(center.x + stopwatchRadius * 0.78f, center.y - stopwatchRadius * 0.82f),
        rotation = 45f
    )

    drawCircle(
        color = StopwatchYellow,
        radius = stopwatchRadius,
        center = center
    )

    drawCircle(
        color = StopwatchRimPink,
        radius = stopwatchRadius,
        center = center,
        style = Stroke(width = rimWidth)
    )

    drawStopwatchTicks(center = center, radius = stopwatchRadius)
    drawStopwatchHand(center = center, radius = stopwatchRadius)

    drawCircle(
        color = StopwatchButtonPurple,
        radius = minDimension * 0.018f,
        center = center
    )
}

private fun DrawScope.drawStopwatchTopButton(
    center: Offset,
    stopwatchRadius: Float
) {
    val minDimension = size.minDimension
    val buttonWidth = minDimension * 0.22f
    val buttonHeight = minDimension * 0.10f
    val topLeft = Offset(
        x = center.x - buttonWidth / 2f,
        y = center.y - stopwatchRadius - buttonHeight * 1.18f
    )

    drawRoundRect(
        color = StopwatchButtonPurple,
        topLeft = topLeft,
        size = Size(buttonWidth, buttonHeight),
        cornerRadius = CornerRadius(buttonHeight * 0.22f, buttonHeight * 0.22f)
    )

    repeat(5) { index ->
        val stripeX = topLeft.x + buttonWidth * (0.2f + index * 0.13f)
        drawLine(
            color = StopwatchButtonDark.copy(alpha = 0.62f),
            start = Offset(stripeX, topLeft.y + buttonHeight * 0.12f),
            end = Offset(stripeX, topLeft.y + buttonHeight * 0.88f),
            strokeWidth = minDimension * 0.006f,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawStopwatchSideButton(
    center: Offset,
    rotation: Float
) {
    val minDimension = size.minDimension
    val buttonWidth = minDimension * 0.15f
    val buttonHeight = minDimension * 0.065f

    rotate(degrees = rotation, pivot = center) {
        drawRoundRect(
            color = StopwatchButtonEdge,
            topLeft = Offset(center.x - buttonWidth / 2f, center.y - buttonHeight / 2f + buttonHeight * 0.18f),
            size = Size(buttonWidth, buttonHeight),
            cornerRadius = CornerRadius(buttonHeight * 0.3f, buttonHeight * 0.3f)
        )
        drawRoundRect(
            color = StopwatchButtonPurple,
            topLeft = Offset(center.x - buttonWidth / 2f, center.y - buttonHeight / 2f),
            size = Size(buttonWidth, buttonHeight),
            cornerRadius = CornerRadius(buttonHeight * 0.3f, buttonHeight * 0.3f)
        )
    }
}

private fun DrawScope.drawStopwatchTicks(
    center: Offset,
    radius: Float
) {
    val minDimension = size.minDimension
    repeat(12) { index ->
        val angle = (-90.0 + index * 30.0) * PI / 180.0
        val longer = index % 3 == 0
        val outerRadius = radius * 0.82f
        val innerRadius = if (longer) radius * 0.67f else radius * 0.72f
        val start = Offset(
            x = center.x + cos(angle).toFloat() * innerRadius,
            y = center.y + sin(angle).toFloat() * innerRadius
        )
        val end = Offset(
            x = center.x + cos(angle).toFloat() * outerRadius,
            y = center.y + sin(angle).toFloat() * outerRadius
        )

        drawLine(
            color = Color.White,
            start = start,
            end = end,
            strokeWidth = minDimension * 0.012f,
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawStopwatchHand(
    center: Offset,
    radius: Float
) {
    val minDimension = size.minDimension
    val hand = Path().apply {
        moveTo(center.x, center.y - radius * 0.7f)
        lineTo(center.x - minDimension * 0.026f, center.y - minDimension * 0.02f)
        lineTo(center.x - minDimension * 0.014f, center.y + radius * 0.32f)
        lineTo(center.x + minDimension * 0.014f, center.y + radius * 0.32f)
        lineTo(center.x + minDimension * 0.026f, center.y - minDimension * 0.02f)
        close()
    }

    drawPath(
        path = hand,
        color = StopwatchRimPink
    )
}

private fun DrawScope.drawStopwatchConfetti() {
    val minDimension = size.minDimension

    drawConfettiRing(
        center = Offset(size.width * 0.18f, size.height * 0.25f),
        color = StopwatchPurple,
        radius = minDimension * 0.028f
    )
    drawCircle(
        color = StopwatchPink,
        radius = minDimension * 0.014f,
        center = Offset(size.width * 0.34f, size.height * 0.22f)
    )
    drawConfettiRing(
        center = Offset(size.width * 0.82f, size.height * 0.28f),
        color = StopwatchPurple,
        radius = minDimension * 0.04f
    )
    drawCircle(
        color = StopwatchPink,
        radius = minDimension * 0.011f,
        center = Offset(size.width * 0.76f, size.height * 0.56f)
    )
    drawConfettiPlus(
        center = Offset(size.width * 0.86f, size.height * 0.64f),
        color = StopwatchLightBlue,
        length = minDimension * 0.022f
    )
    drawCircle(
        color = StopwatchPurple,
        radius = minDimension * 0.018f,
        center = Offset(size.width * 0.61f, size.height * 0.88f)
    )
    drawConfettiPlus(
        center = Offset(size.width * 0.27f, size.height * 0.82f),
        color = Color(0xFFA65EEA),
        length = minDimension * 0.022f
    )
    drawConfettiDash(
        start = Offset(size.width * 0.14f, size.height * 0.53f),
        color = StopwatchTurquoise,
        length = minDimension * 0.032f
    )
    drawConfettiRing(
        center = Offset(size.width * 0.13f, size.height * 0.73f),
        color = StopwatchYellowOrange,
        radius = minDimension * 0.034f
    )
    drawConfettiRing(
        center = Offset(size.width * 0.78f, size.height * 0.73f),
        color = StopwatchPurple,
        radius = minDimension * 0.018f
    )
}

private fun DrawScope.drawConfettiRing(
    center: Offset,
    color: Color,
    radius: Float
) {
    drawArc(
        color = color,
        startAngle = 35f,
        sweepAngle = 285f,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2f, radius * 2f),
        style = Stroke(width = radius * 0.38f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawConfettiPlus(
    center: Offset,
    color: Color,
    length: Float
) {
    drawLine(
        color = color,
        start = Offset(center.x - length, center.y),
        end = Offset(center.x + length, center.y),
        strokeWidth = length * 0.48f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(center.x, center.y - length),
        end = Offset(center.x, center.y + length),
        strokeWidth = length * 0.48f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawConfettiDash(
    start: Offset,
    color: Color,
    length: Float
) {
    drawLine(
        color = color,
        start = start,
        end = Offset(start.x + length * 0.75f, start.y),
        strokeWidth = length * 0.42f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(start.x + length * 0.75f, start.y),
        end = Offset(start.x + length * 0.75f, start.y + length * 0.45f),
        strokeWidth = length * 0.42f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(start.x + length * 0.75f, start.y + length * 0.45f),
        end = Offset(start.x + length * 1.24f, start.y + length * 0.45f),
        strokeWidth = length * 0.42f,
        cap = StrokeCap.Round
    )
}

@Composable
private fun rememberGoalSelectedDialogMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): GoalSelectedDialogMetrics {
    return remember(maxWidth, maxHeight) {
        val cardWidth = (maxWidth * 0.75f).coerceIn(280.dp, 330.dp)
        val titleSize = (cardWidth.value * 0.062f).coerceIn(18f, 21f).sp
        val subtitleSize = (cardWidth.value * 0.048f).coerceIn(14f, 16f).sp

        GoalSelectedDialogMetrics(
            cardWidth = cardWidth,
            cardCorner = (cardWidth * 0.145f).coerceIn(38.dp, 48.dp),
            cardHorizontalPadding = (cardWidth * 0.082f).coerceIn(22.dp, 28.dp),
            cardVerticalPadding = (maxHeight * 0.028f).coerceIn(28.dp, 36.dp),
            illustrationSize = (cardWidth * 0.65f).coerceIn(166.dp, 210.dp),
            illustrationToTitleSpacing = (maxHeight * 0.02f).coerceIn(18.dp, 24.dp),
            titleSize = titleSize,
            titleLineHeight = titleSize * 1.18f,
            subtitleTopSpacing = 8.dp,
            subtitleSize = subtitleSize,
            subtitleLineHeight = subtitleSize * 1.2f
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalSelectedDialogPreview() {
    DZTheme {
        GoalSelectedDialog()
    }
}
