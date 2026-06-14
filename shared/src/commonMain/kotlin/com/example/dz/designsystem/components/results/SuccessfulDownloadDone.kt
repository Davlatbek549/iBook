package com.example.dz.designsystem.components.results

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.download_completed_coffee_message
import dz.shared.generated.resources.download_completed_title
import org.jetbrains.compose.resources.stringResource

private data class SuccessfulDownloadDoneMetrics(
    val cardWidth: Dp,
    val cardHeight: Dp,
    val cardCorner: Dp,
    val artSize: Dp,
    val titleTopSpacing: Dp,
    val messageTopSpacing: Dp,
    val titleFontSize: TextUnit,
    val messageFontSize: TextUnit
)

@Composable
fun SuccessfulDownloadDone(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.download_completed_title),
    message: String = stringResource(Res.string.download_completed_coffee_message)
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val metrics = rememberSuccessfulDownloadDoneMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme

        SuccessfulDownloadDoneBackground()

        Column(
            modifier = Modifier
                .size(metrics.cardWidth, metrics.cardHeight)
                .clip(RoundedCornerShape(metrics.cardCorner))
                .background(colorScheme.surface)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            DownloadDoneArt(
                modifier = Modifier.size(metrics.artSize)
            )

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = title,
                color = colorScheme.onSurface.copy(alpha = 0.72f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = metrics.titleFontSize,
                    lineHeight = metrics.titleFontSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.messageTopSpacing))

            Text(
                text = message,
                color = colorScheme.onSurface.copy(alpha = 0.68f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = metrics.messageFontSize,
                    lineHeight = metrics.messageFontSize * 1.25f,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun SuccessfulDownloadDoneBackground() {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.18f
                    scaleY = 1.18f
                }
                .blur(42.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorScheme.tertiary.copy(alpha = 0.2f),
                            colorScheme.primary.copy(alpha = 0.16f),
                            colorScheme.background
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.scrim.copy(alpha = 0.16f))
        )
    }
}

@Composable
private fun DownloadDoneArt(
    modifier: Modifier = Modifier
) {
    val artBackgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)

    Canvas(modifier = modifier.fillMaxWidth()) {
        drawCircle(
            color = artBackgroundColor,
            radius = size.minDimension * 0.43f,
            center = Offset(size.width * 0.5f, size.height * 0.5f)
        )

        drawDoneConfetti()
        drawLargeCheckmark()
    }
}

private fun DrawScope.drawLargeCheckmark() {
    val checkPath = Path().apply {
        moveTo(size.width * 0.25f, size.height * 0.51f)
        lineTo(size.width * 0.44f, size.height * 0.68f)
        lineTo(size.width * 0.76f, size.height * 0.28f)
    }

    drawPath(
        path = checkPath,
        color = Color.Black.copy(alpha = 0.16f),
        style = Stroke(
            width = size.minDimension * 0.19f,
            cap = StrokeCap.Round
        )
    )

    drawPath(
        path = checkPath,
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFAADB27),
                Color(0xFFD0FF42),
                Color(0xFF99C820)
            ),
            start = Offset(size.width * 0.22f, size.height * 0.68f),
            end = Offset(size.width * 0.78f, size.height * 0.26f)
        ),
        style = Stroke(
            width = size.minDimension * 0.18f,
            cap = StrokeCap.Round
        )
    )

    drawPath(
        path = checkPath,
        color = Color.White.copy(alpha = 0.16f),
        style = Stroke(
            width = size.minDimension * 0.055f,
            cap = StrokeCap.Round
        )
    )
}

private fun DrawScope.drawDoneConfetti() {
    val violet = Color(0xFF6556FF)
    val mint = Color(0xFF62D1C8)
    val amber = Color(0xFFF7B731)
    val blue = Color(0xFF4B7BEC)
    val purple = Color(0xFFA65EEA)

    drawArcSymbol(Offset(size.width * 0.12f, size.height * 0.23f), violet, 0.07f)
    drawArcSymbol(Offset(size.width * 0.82f, size.height * 0.25f), violet, 0.045f)
    drawArcSymbol(Offset(size.width * 0.86f, size.height * 0.69f), amber, 0.075f)
    drawArcSymbol(Offset(size.width * 0.15f, size.height * 0.68f), violet, 0.035f)

    drawPlusSymbol(Offset(size.width * 0.08f, size.height * 0.49f), blue)
    drawPlusSymbol(Offset(size.width * 0.64f, size.height * 0.82f), purple)
    drawSquiggle(Offset(size.width * 0.79f, size.height * 0.39f), mint)

    drawCircle(violet, size.minDimension * 0.024f, Offset(size.width * 0.37f, size.height * 0.88f))
}

private fun DrawScope.drawArcSymbol(
    center: Offset,
    color: Color,
    scale: Float
) {
    val radius = size.minDimension * scale
    drawArc(
        color = color,
        startAngle = 120f,
        sweepAngle = 270f,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2f, radius * 2f),
        style = Stroke(width = size.minDimension * 0.022f)
    )
}

private fun DrawScope.drawPlusSymbol(
    center: Offset,
    color: Color
) {
    val length = size.minDimension * 0.035f
    val stroke = size.minDimension * 0.016f
    drawLine(color, Offset(center.x - length, center.y), Offset(center.x + length, center.y), strokeWidth = stroke)
    drawLine(color, Offset(center.x, center.y - length), Offset(center.x, center.y + length), strokeWidth = stroke)
}

private fun DrawScope.drawSquiggle(
    center: Offset,
    color: Color
) {
    val path = Path().apply {
        moveTo(center.x - size.minDimension * 0.035f, center.y)
        cubicTo(
            center.x - size.minDimension * 0.02f,
            center.y - size.minDimension * 0.03f,
            center.x + size.minDimension * 0.005f,
            center.y + size.minDimension * 0.03f,
            center.x + size.minDimension * 0.035f,
            center.y
        )
    }
    drawPath(path, color, style = Stroke(width = size.minDimension * 0.016f))
}

@Composable
private fun rememberSuccessfulDownloadDoneMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): SuccessfulDownloadDoneMetrics {
    val widthRatio = dimensionRatio(maxWidth, 390.dp, 0.84f, 1.16f)
    val heightRatio = dimensionRatio(maxHeight, 844.dp, 0.78f, 1.1f)
    val compactness = kotlin.math.min(widthRatio, heightRatio)

    return remember(maxWidth, maxHeight) {
        SuccessfulDownloadDoneMetrics(
            cardWidth = (292.dp * widthRatio).coerceIn(258.dp, 324.dp),
            cardHeight = (312.dp * compactness).coerceIn(282.dp, 348.dp),
            cardCorner = (48.dp * compactness).coerceIn(38.dp, 58.dp),
            artSize = (212.dp * compactness).coerceIn(184.dp, 238.dp),
            titleTopSpacing = (0.dp * compactness).coerceIn(0.dp, 4.dp),
            messageTopSpacing = (6.dp * compactness).coerceIn(4.dp, 8.dp),
            titleFontSize = (20f * compactness).coerceIn(18f, 23f).sp,
            messageFontSize = (15.5f * compactness).coerceIn(13.5f, 17f).sp
        )
    }
}

private fun dimensionRatio(
    value: Dp,
    base: Dp,
    min: Float,
    max: Float
): Float {
    if (!value.value.isFinite() || !base.value.isFinite() || base.value == 0f) {
        return 1f
    }

    return (value / base).coerceIn(min, max)
}

@Preview(
    name = "Successful Download Done",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun SuccessfulDownloadDonePreview() {
    DZTheme(darkTheme = false) {
        SuccessfulDownloadDone()
    }
}
