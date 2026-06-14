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
import kotlin.math.min

private data class SuccessfulDownloadLampMetrics(
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
fun SuccessfulDownloadLamp(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.download_completed_title),
    message: String = stringResource(Res.string.download_completed_coffee_message)
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val metrics = rememberSuccessfulDownloadLampMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme

        SuccessfulDownloadLampBackground()

        Column(
            modifier = Modifier
                .size(metrics.cardWidth, metrics.cardHeight)
                .clip(RoundedCornerShape(metrics.cardCorner))
                .background(colorScheme.surface)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            LampCelebrationArt(
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
private fun SuccessfulDownloadLampBackground() {
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
private fun LampCelebrationArt(
    modifier: Modifier = Modifier
) {
    val artBackgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)

    Canvas(modifier = modifier.fillMaxWidth()) {
        drawCircle(
            color = artBackgroundColor,
            radius = size.minDimension * 0.43f,
            center = Offset(size.width * 0.5f, size.height * 0.5f)
        )

        drawLampConfetti()
        drawLightBulb()
    }
}

private fun DrawScope.drawLightBulb() {
    val bulbCenter = Offset(size.width * 0.5f, size.height * 0.35f)
    val bulbRadius = size.minDimension * 0.25f
    val neckTop = size.height * 0.57f
    val neckLeft = size.width * 0.39f
    val neckWidth = size.width * 0.22f
    val neckHeight = size.height * 0.18f

    val bulbPath = Path().apply {
        moveTo(bulbCenter.x - bulbRadius * 0.98f, bulbCenter.y + bulbRadius * 0.1f)
        cubicTo(
            bulbCenter.x - bulbRadius * 1.05f,
            bulbCenter.y - bulbRadius * 0.72f,
            bulbCenter.x - bulbRadius * 0.42f,
            bulbCenter.y - bulbRadius * 1.18f,
            bulbCenter.x,
            bulbCenter.y - bulbRadius * 1.1f
        )
        cubicTo(
            bulbCenter.x + bulbRadius * 0.74f,
            bulbCenter.y - bulbRadius * 1.08f,
            bulbCenter.x + bulbRadius * 1.05f,
            bulbCenter.y - bulbRadius * 0.48f,
            bulbCenter.x + bulbRadius * 0.96f,
            bulbCenter.y + bulbRadius * 0.12f
        )
        cubicTo(
            bulbCenter.x + bulbRadius * 0.88f,
            bulbCenter.y + bulbRadius * 0.72f,
            bulbCenter.x + bulbRadius * 0.38f,
            bulbCenter.y + bulbRadius * 0.84f,
            bulbCenter.x + bulbRadius * 0.24f,
            neckTop
        )
        lineTo(bulbCenter.x - bulbRadius * 0.24f, neckTop)
        cubicTo(
            bulbCenter.x - bulbRadius * 0.38f,
            bulbCenter.y + bulbRadius * 0.84f,
            bulbCenter.x - bulbRadius * 0.86f,
            bulbCenter.y + bulbRadius * 0.7f,
            bulbCenter.x - bulbRadius * 0.98f,
            bulbCenter.y + bulbRadius * 0.1f
        )
        close()
    }

    drawPath(
        path = bulbPath,
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFDF66), Color(0xFFF8C33E), Color(0xFFE7A82A)),
            center = Offset(bulbCenter.x - bulbRadius * 0.28f, bulbCenter.y - bulbRadius * 0.42f),
            radius = bulbRadius * 1.55f
        )
    )

    drawCircle(
        color = Color.White.copy(alpha = 0.22f),
        radius = bulbRadius * 0.38f,
        center = Offset(bulbCenter.x - bulbRadius * 0.38f, bulbCenter.y - bulbRadius * 0.42f)
    )

    val neckPath = Path().apply {
        moveTo(neckLeft, neckTop)
        lineTo(neckLeft + neckWidth, neckTop)
        cubicTo(neckLeft + neckWidth * 0.94f, neckTop + neckHeight, neckLeft + neckWidth * 0.06f, neckTop + neckHeight, neckLeft, neckTop)
        close()
    }
    drawPath(
        path = neckPath,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF2F74F6), Color(0xFF063A9C)),
            startY = neckTop,
            endY = neckTop + neckHeight
        )
    )

    repeat(5) { index ->
        val y = neckTop + neckHeight * (0.12f + index * 0.17f)
        drawLine(
            color = if (index % 2 == 0) Color(0xFF4E94FF) else Color(0xFF0B4ECC),
            start = Offset(neckLeft - size.width * 0.015f, y),
            end = Offset(neckLeft + neckWidth + size.width * 0.015f, y + size.height * 0.018f),
            strokeWidth = size.minDimension * 0.018f,
            cap = StrokeCap.Round
        )
    }

    drawOvalShadow(
        center = Offset(size.width * 0.5f, neckTop + neckHeight * 1.02f),
        width = neckWidth * 0.9f,
        height = size.height * 0.05f
    )
}

private fun DrawScope.drawOvalShadow(
    center: Offset,
    width: Float,
    height: Float
) {
    drawOval(
        color = Color.Black.copy(alpha = 0.12f),
        topLeft = Offset(center.x - width / 2f, center.y - height / 2f),
        size = Size(width, height)
    )
}

private fun DrawScope.drawLampConfetti() {
    val violet = Color(0xFF6556FF)
    val pink = Color(0xFFF8448F)
    val mint = Color(0xFF62D1C8)
    val amber = Color(0xFFF7B731)
    val blue = Color(0xFF4B7BEC)
    val purple = Color(0xFFA65EEA)

    drawArcSymbol(Offset(size.width * 0.14f, size.height * 0.24f), violet, 0.04f)
    drawArcSymbol(Offset(size.width * 0.84f, size.height * 0.26f), violet, 0.065f)
    drawArcSymbol(Offset(size.width * 0.12f, size.height * 0.68f), amber, 0.052f)
    drawArcSymbol(Offset(size.width * 0.82f, size.height * 0.68f), violet, 0.025f)

    drawPlusSymbol(Offset(size.width * 0.72f, size.height * 0.83f), purple)
    drawPlusSymbol(Offset(size.width * 0.89f, size.height * 0.47f), blue)
    drawSquiggle(Offset(size.width * 0.12f, size.height * 0.38f), mint)
    drawSquiggle(Offset(size.width * 0.74f, size.height * 0.4f), pink)

    drawCircle(pink, size.minDimension * 0.014f, Offset(size.width * 0.28f, size.height * 0.22f))
    drawCircle(violet, size.minDimension * 0.019f, Offset(size.width * 0.5f, size.height * 0.85f))
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
        style = Stroke(width = size.minDimension * 0.018f)
    )
}

private fun DrawScope.drawPlusSymbol(
    center: Offset,
    color: Color
) {
    val length = size.minDimension * 0.028f
    val stroke = size.minDimension * 0.013f
    drawLine(color, Offset(center.x - length, center.y), Offset(center.x + length, center.y), strokeWidth = stroke)
    drawLine(color, Offset(center.x, center.y - length), Offset(center.x, center.y + length), strokeWidth = stroke)
}

private fun DrawScope.drawSquiggle(
    center: Offset,
    color: Color
) {
    val path = Path().apply {
        moveTo(center.x - size.minDimension * 0.028f, center.y)
        cubicTo(
            center.x - size.minDimension * 0.016f,
            center.y - size.minDimension * 0.024f,
            center.x + size.minDimension * 0.004f,
            center.y + size.minDimension * 0.024f,
            center.x + size.minDimension * 0.028f,
            center.y
        )
    }
    drawPath(path, color, style = Stroke(width = size.minDimension * 0.013f))
}

@Composable
private fun rememberSuccessfulDownloadLampMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): SuccessfulDownloadLampMetrics {
    val widthRatio = dimensionRatio(maxWidth, 390.dp, 0.84f, 1.16f)
    val heightRatio = dimensionRatio(maxHeight, 844.dp, 0.78f, 1.1f)
    val compactness = min(widthRatio, heightRatio)

    return remember(maxWidth, maxHeight) {
        SuccessfulDownloadLampMetrics(
            cardWidth = (292.dp * widthRatio).coerceIn(258.dp, 324.dp),
            cardHeight = (314.dp * compactness).coerceIn(282.dp, 348.dp),
            cardCorner = (48.dp * compactness).coerceIn(38.dp, 58.dp),
            artSize = (214.dp * compactness).coerceIn(184.dp, 238.dp),
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
    name = "Successful Download Lamp",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun SuccessfulDownloadLampPreview() {
    DZTheme(darkTheme = false) {
        SuccessfulDownloadLamp()
    }
}
