package com.example.ibook.app_components.results

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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.IBookTheme

private data class SuccessfulDownloadCoffeeMetrics(
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
fun SuccessfulDownloadCoffee(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.download_completed_title),
    message: String = stringResource(R.string.download_completed_coffee_message)
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val metrics = rememberSuccessfulDownloadCoffeeMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme

        SuccessfulDownloadBackground()

        Column(
            modifier = Modifier
                .size(metrics.cardWidth, metrics.cardHeight)
                .clip(RoundedCornerShape(metrics.cardCorner))
                .background(colorScheme.surface)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(38.dp))

            CoffeeCelebrationArt(
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
private fun SuccessfulDownloadBackground() {
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
                            colorScheme.tertiary.copy(alpha = 0.22f),
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
private fun CoffeeCelebrationArt(
    modifier: Modifier = Modifier
) {
    val artBackgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)

    Canvas(modifier = modifier.fillMaxWidth()) {
        drawCircle(
            color = artBackgroundColor,
            radius = size.minDimension * 0.43f,
            center = Offset(size.width * 0.5f, size.height * 0.49f)
        )

        drawConfetti()
        drawCoffeeMug()
    }
}

private fun DrawScope.drawCoffeeMug() {
    val cupTop = size.height * 0.28f
    val cupLeft = size.width * 0.27f
    val cupWidth = size.width * 0.47f
    val cupHeight = size.height * 0.5f
    val cupCenterX = cupLeft + cupWidth * 0.5f
    val topOvalHeight = size.height * 0.16f

    val shadow = Path().apply {
        moveTo(cupLeft + cupWidth * 0.1f, cupTop + cupHeight * 0.1f)
        cubicTo(cupLeft + cupWidth * 0.16f, cupTop + cupHeight, cupLeft + cupWidth * 0.84f, cupTop + cupHeight, cupLeft + cupWidth * 0.9f, cupTop + cupHeight * 0.1f)
        close()
    }
    drawPath(
        path = shadow,
        color = Color(0xFFE6B427).copy(alpha = 0.2f)
    )

    val body = Path().apply {
        moveTo(cupLeft, cupTop + topOvalHeight * 0.45f)
        cubicTo(cupLeft + cupWidth * 0.06f, cupTop + cupHeight * 0.93f, cupLeft + cupWidth * 0.94f, cupTop + cupHeight * 0.93f, cupLeft + cupWidth, cupTop + topOvalHeight * 0.45f)
        cubicTo(cupLeft + cupWidth * 0.88f, cupTop + topOvalHeight, cupLeft + cupWidth * 0.12f, cupTop + topOvalHeight, cupLeft, cupTop + topOvalHeight * 0.45f)
        close()
    }
    drawPath(
        path = body,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFFFD342), Color(0xFFE7AE24)),
            start = Offset(cupLeft, cupTop),
            end = Offset(cupLeft + cupWidth, cupTop + cupHeight)
        )
    )

    drawOval(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFE14D), Color(0xFFE8B623))
        ),
        topLeft = Offset(cupLeft, cupTop),
        size = Size(cupWidth, topOvalHeight)
    )
    drawOval(
        color = Color(0xFFC9184E),
        topLeft = Offset(cupLeft + cupWidth * 0.08f, cupTop + topOvalHeight * 0.16f),
        size = Size(cupWidth * 0.84f, topOvalHeight * 0.68f)
    )
    drawOval(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF161E1A), Color(0xFF4C361D))
        ),
        topLeft = Offset(cupLeft + cupWidth * 0.14f, cupTop + topOvalHeight * 0.26f),
        size = Size(cupWidth * 0.72f, topOvalHeight * 0.48f)
    )

    drawArc(
        color = Color(0xFF3365FF),
        startAngle = -78f,
        sweepAngle = 230f,
        useCenter = false,
        topLeft = Offset(cupLeft + cupWidth * 0.78f, cupTop + cupHeight * 0.33f),
        size = Size(cupWidth * 0.42f, cupHeight * 0.52f),
        style = Stroke(width = size.minDimension * 0.07f)
    )
    drawArc(
        color = Color(0xFFFFD342),
        startAngle = 35f,
        sweepAngle = 110f,
        useCenter = false,
        topLeft = Offset(cupLeft + cupWidth * 0.78f, cupTop + cupHeight * 0.33f),
        size = Size(cupWidth * 0.42f, cupHeight * 0.52f),
        style = Stroke(width = size.minDimension * 0.07f)
    )

    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF406AFF), Color(0xFF2456F8))
        ),
        topLeft = Offset(cupLeft + cupWidth * 0.17f, cupTop + cupHeight * 0.72f),
        size = Size(cupWidth * 0.66f, cupHeight * 0.18f),
        cornerRadius = CornerRadius(size.minDimension * 0.07f)
    )

    val line = Path().apply {
        moveTo(cupLeft + cupWidth * 0.13f, cupTop + cupHeight * 0.48f)
        cubicTo(
            cupLeft + cupWidth * 0.34f,
            cupTop + cupHeight * 0.42f,
            cupLeft + cupWidth * 0.48f,
            cupTop + cupHeight * 0.74f,
            cupLeft + cupWidth * 0.92f,
            cupTop + cupHeight * 0.54f
        )
    }
    drawPath(
        path = line,
        color = Color(0xFFF8448F),
        style = Stroke(width = size.minDimension * 0.009f)
    )

    drawOval(
        color = Color.Black.copy(alpha = 0.11f),
        topLeft = Offset(cupCenterX - cupWidth * 0.38f, cupTop + cupHeight * 0.84f),
        size = Size(cupWidth * 0.76f, cupHeight * 0.13f)
    )
}

private fun DrawScope.drawConfetti() {
    val violet = Color(0xFF6556FF)
    val pink = Color(0xFFF8448F)
    val mint = Color(0xFF62D1C8)
    val amber = Color(0xFFF7B731)
    val blue = Color(0xFF4B7BEC)

    drawArcSymbol(Offset(size.width * 0.13f, size.height * 0.21f), violet, 0.07f)
    drawArcSymbol(Offset(size.width * 0.87f, size.height * 0.24f), violet, 0.085f)
    drawArcSymbol(Offset(size.width * 0.08f, size.height * 0.71f), amber, 0.08f)

    drawPlusSymbol(Offset(size.width * 0.89f, size.height * 0.47f), blue)
    drawPlusSymbol(Offset(size.width * 0.25f, size.height * 0.83f), violet)
    drawSquiggle(Offset(size.width * 0.12f, size.height * 0.39f), mint)
    drawSquiggle(Offset(size.width * 0.78f, size.height * 0.41f), pink)

    drawCircle(pink, size.minDimension * 0.017f, Offset(size.width * 0.31f, size.height * 0.19f))
    drawCircle(violet, size.minDimension * 0.024f, Offset(size.width * 0.58f, size.height * 0.9f))
    drawCircle(violet, size.minDimension * 0.012f, Offset(size.width * 0.85f, size.height * 0.72f))
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
private fun rememberSuccessfulDownloadCoffeeMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): SuccessfulDownloadCoffeeMetrics {
    val widthRatio = dimensionRatio(maxWidth, 390.dp, 0.84f, 1.16f)
    val heightRatio = dimensionRatio(maxHeight, 844.dp, 0.78f, 1.1f)
    val compactness = kotlin.math.min(widthRatio, heightRatio)

    return remember(maxWidth, maxHeight) {
        SuccessfulDownloadCoffeeMetrics(
            cardWidth = (292.dp * widthRatio).coerceIn(258.dp, 324.dp),
            cardHeight = (338.dp * compactness).coerceIn(292.dp, 374.dp),
            cardCorner = (48.dp * compactness).coerceIn(38.dp, 58.dp),
            artSize = (228.dp * compactness).coerceIn(194.dp, 252.dp),
            titleTopSpacing = (10.dp * compactness).coerceIn(6.dp, 12.dp),
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
    name = "Successful Download Coffee",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun SuccessfulDownloadCoffeePreview() {
    IBookTheme(darkTheme = false) {
        SuccessfulDownloadCoffee()
    }
}
