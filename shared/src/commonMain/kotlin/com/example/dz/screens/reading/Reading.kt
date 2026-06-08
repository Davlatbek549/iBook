package com.example.dz.screens.reading

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.ColorCategoryFantasy
import com.example.dz.theme.ColorPrimary
import com.example.dz.theme.ColorTertiary
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.min

private data class ReadingMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val topButtonSize: Dp,
    val topIconSize: Dp,
    val bookTopSpacing: Dp,
    val bookHeight: Dp,
    val bookTextPadding: Dp,
    val bookTextFontSize: TextUnit,
    val bookTextLineHeight: TextUnit,
    val titleTopSpacing: Dp,
    val titleFontSize: TextUnit,
    val authorFontSize: TextUnit,
    val sheetTopSpacing: Dp,
    val sheetTopPadding: Dp,
    val sheetHorizontalPadding: Dp,
    val statCardHeight: Dp,
    val statCardCorner: Dp,
    val statTitleFontSize: TextUnit,
    val statArtSize: Dp,
    val statValueFontSize: TextUnit,
    val statUnitFontSize: TextUnit,
    val sectionTopSpacing: Dp,
    val sectionTitleFontSize: TextUnit,
    val overviewFontSize: TextUnit,
    val bottomBarHeight: Dp,
    val bottomBarCorner: Dp,
    val bottomIconSize: Dp
)

@Composable
fun ReadingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCommentsClick: () -> Unit = {},
    onKeepReadingClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberReadingMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Box(modifier = Modifier.fillMaxSize()) {
            ReadingBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(metrics.topSpacing))

                TopActions(
                    metrics = metrics,
                    onBackClick = onBackClick,
                    onMenuClick = onMenuClick,
                    modifier = Modifier.padding(horizontal = metrics.horizontalPadding)
                )

                Spacer(modifier = Modifier.height(metrics.bookTopSpacing))

                OpenBookPreview(
                    metrics = metrics,
                    modifier = Modifier.padding(horizontal = metrics.horizontalPadding)
                )

                Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

                Text(
                    text = stringResource(Res.string.reading_book_title),
                    color = MaterialTheme.colorScheme.surface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = metrics.titleFontSize,
                        lineHeight = metrics.titleFontSize * 1.24f,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(Res.string.reading_book_author),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.authorFontSize,
                        lineHeight = metrics.authorFontSize * 1.25f,
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.height(metrics.sheetTopSpacing))

                ReadingInfoSheet(
                    metrics = metrics,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            BottomReadingBar(
                metrics = metrics,
                onCommentsClick = onCommentsClick,
                onKeepReadingClick = onKeepReadingClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun ReadingBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7A2634),
                        Color(0xFF0A6372),
                        Color(0xFF10233B)
                    )
                )
            )
    )
}

@Composable
private fun TopActions(
    metrics: ReadingMetrics,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReadingCircleButton(
            onClick = onBackClick,
            metrics = metrics
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = stringResource(Res.string.cd_back),
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(metrics.topIconSize)
            )
        }

        ReadingCircleButton(
            onClick = onMenuClick,
            metrics = metrics
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_three_vertical_dots),
                contentDescription = stringResource(Res.string.cd_more_options),
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(metrics.topIconSize)
            )
        }
    }
}

@Composable
private fun OpenBookPreview(
    metrics: ReadingMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.bookHeight)
            .shadow(16.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFECECEC))
            .border(7.dp, Color.Black, RoundedCornerShape(18.dp)),
        verticalAlignment = Alignment.Top
    ) {
        BookPage(
            text = stringResource(Res.string.reading_left_page),
            metrics = metrics,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .width(2.dp)
                .height(metrics.bookHeight)
                .background(Color.Black.copy(alpha = 0.9f))
        )

        BookPage(
            text = stringResource(Res.string.reading_right_page),
            metrics = metrics,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BookPage(
    text: String,
    metrics: ReadingMetrics,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFF5F5F5),
                        Color(0xFFE6E6E6)
                    )
                )
            )
            .padding(metrics.bookTextPadding)
    ) {
        Text(
            text = text,
            color = Color(0xFF777777),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = metrics.bookTextFontSize,
                lineHeight = metrics.bookTextLineHeight,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Composable
private fun ReadingInfoSheet(
    metrics: ReadingMetrics,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFEFF2F7),
        shape = RoundedCornerShape(topStart = 46.dp, topEnd = 46.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(
                    start = metrics.sheetHorizontalPadding,
                    top = metrics.sheetTopPadding,
                    end = metrics.sheetHorizontalPadding,
                    bottom = metrics.bottomBarHeight + 36.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(metrics.sheetHorizontalPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReadingStatCard(
                    title = stringResource(Res.string.reading_today_title),
                    value = stringResource(Res.string.reading_today_value),
                    unit = stringResource(Res.string.reading_today_unit),
                    valueColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    metrics = metrics,
                    art = { ReadingStopwatchArt(modifier = Modifier.size(metrics.statArtSize)) },
                    modifier = Modifier.weight(1f)
                )

                ReadingStatCard(
                    title = stringResource(Res.string.reading_streak_title),
                    value = stringResource(Res.string.reading_streak_value),
                    unit = stringResource(Res.string.reading_streak_unit),
                    valueColor = MaterialTheme.colorScheme.primary,
                    metrics = metrics,
                    art = { ReadingTrophyArt(modifier = Modifier.size(metrics.statArtSize)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(metrics.sectionTopSpacing))

            Text(
                text = stringResource(Res.string.book_review_categories),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = metrics.sectionTitleFontSize,
                    lineHeight = metrics.sectionTitleFontSize * 1.2f,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CategoryChip(
                    text = stringResource(Res.string.category_horror),
                    color = ColorTertiary
                )
                CategoryChip(
                    text = stringResource(Res.string.category_fantasy),
                    color = ColorCategoryFantasy
                )
            }

            Spacer(modifier = Modifier.height(metrics.sectionTopSpacing))

            Text(
                text = stringResource(Res.string.book_review_overview),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = metrics.sectionTitleFontSize,
                    lineHeight = metrics.sectionTitleFontSize * 1.2f,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.reading_overview_body),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = metrics.overviewFontSize,
                    lineHeight = metrics.overviewFontSize * 1.45f
                )
            )
        }
    }
}

@Composable
private fun ReadingStatCard(
    title: String,
    value: String,
    unit: String,
    valueColor: Color,
    metrics: ReadingMetrics,
    art: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(metrics.statCardHeight)
            .clip(RoundedCornerShape(metrics.statCardCorner))
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = metrics.statTitleFontSize,
                lineHeight = metrics.statTitleFontSize * 1.18f,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            art()
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                color = valueColor,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.statValueFontSize,
                    lineHeight = metrics.statValueFontSize,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = unit,
                color = valueColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = metrics.statUnitFontSize,
                    lineHeight = metrics.statUnitFontSize * 1.25f,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    color: Color
) {
    Text(
        text = text,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelLarge.copy(
            fontSize = 14.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
private fun BottomReadingBar(
    metrics: ReadingMetrics,
    onCommentsClick: () -> Unit,
    onKeepReadingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.bottomBarHeight)
            .navigationBarsPadding()
            .shadow(12.dp, RoundedCornerShape(topStart = metrics.bottomBarCorner))
            .clip(RoundedCornerShape(topStart = metrics.bottomBarCorner))
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(0.78f)
                .fillMaxSize()
                .clickable(onClick = onCommentsClick)
                .padding(start = 44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_message),
                contentDescription = stringResource(Res.string.cd_message),
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(metrics.bottomIconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.book_review_review_value),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Row(
            modifier = Modifier
                .weight(1.72f)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = metrics.bottomBarCorner))
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = onKeepReadingClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_book_open),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(metrics.bottomIconSize)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(Res.string.reading_keep_reading),
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun ReadingCircleButton(
    onClick: () -> Unit,
    metrics: ReadingMetrics,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(metrics.topButtonSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.18f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun ReadingStopwatchArt(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawConfetti()

        val center = Offset(size.width * 0.5f, size.height * 0.56f)
        val radius = size.minDimension * 0.33f

        drawCircle(Color(0xFFFF4049), radius + size.minDimension * 0.035f, center)
        drawCircle(Color(0xFFFFD159), radius, center)

        drawRoundRect(
            color = ColorPrimary,
            topLeft = Offset(size.width * 0.42f, size.height * 0.08f),
            size = Size(size.width * 0.16f, size.height * 0.08f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )
        drawLine(
            color = ColorPrimary,
            start = Offset(size.width * 0.5f, size.height * 0.16f),
            end = Offset(size.width * 0.5f, size.height * 0.23f),
            strokeWidth = size.minDimension * 0.05f,
            cap = StrokeCap.Round
        )

        drawLine(
            color = Color(0xFFFF4049),
            start = center,
            end = Offset(center.x, center.y - radius * 0.72f),
            strokeWidth = size.minDimension * 0.03f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = ColorPrimary,
            start = center,
            end = Offset(center.x - radius * 0.02f, center.y + radius * 0.68f),
            strokeWidth = size.minDimension * 0.018f,
            cap = StrokeCap.Round
        )

        repeat(8) { index ->
            val angle = ((index * 45 - 90).toDouble() * kotlin.math.PI) / 180.0
            val start = Offset(
                center.x + kotlin.math.cos(angle).toFloat() * radius * 0.78f,
                center.y + kotlin.math.sin(angle).toFloat() * radius * 0.78f
            )
            val end = Offset(
                center.x + kotlin.math.cos(angle).toFloat() * radius * 0.9f,
                center.y + kotlin.math.sin(angle).toFloat() * radius * 0.9f
            )
            drawLine(Color.White, start, end, strokeWidth = size.minDimension * 0.018f)
        }
    }
}

@Composable
fun ReadingTrophyArt(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawConfetti()

        drawRoundRect(
            color = Color(0xFF3551E8),
            topLeft = Offset(size.width * 0.36f, size.height * 0.72f),
            size = Size(size.width * 0.28f, size.height * 0.16f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f, 5f)
        )
        drawRoundRect(
            color = Color(0xFFFFC64C),
            topLeft = Offset(size.width * 0.31f, size.height * 0.84f),
            size = Size(size.width * 0.38f, size.height * 0.1f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )

        drawLine(
            color = ColorPrimary,
            start = Offset(size.width * 0.5f, size.height * 0.61f),
            end = Offset(size.width * 0.5f, size.height * 0.75f),
            strokeWidth = size.minDimension * 0.13f,
            cap = StrokeCap.Round
        )

        drawCupHandle(left = true)
        drawCupHandle(left = false)

        val cup = Path().apply {
            moveTo(size.width * 0.25f, size.height * 0.17f)
            cubicTo(size.width * 0.36f, size.height * 0.1f, size.width * 0.62f, size.height * 0.1f, size.width * 0.75f, size.height * 0.17f)
            lineTo(size.width * 0.66f, size.height * 0.55f)
            cubicTo(size.width * 0.61f, size.height * 0.66f, size.width * 0.39f, size.height * 0.66f, size.width * 0.34f, size.height * 0.55f)
            close()
        }
        drawPath(cup, Color(0xFFFF3348))

        val star = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.28f)
            lineTo(size.width * 0.54f, size.height * 0.38f)
            lineTo(size.width * 0.65f, size.height * 0.38f)
            lineTo(size.width * 0.56f, size.height * 0.45f)
            lineTo(size.width * 0.6f, size.height * 0.56f)
            lineTo(size.width * 0.5f, size.height * 0.49f)
            lineTo(size.width * 0.4f, size.height * 0.56f)
            lineTo(size.width * 0.44f, size.height * 0.45f)
            lineTo(size.width * 0.35f, size.height * 0.38f)
            lineTo(size.width * 0.46f, size.height * 0.38f)
            close()
        }
        drawPath(
            path = star,
            color = Color(0xFFFFE25C),
            style = Stroke(width = size.minDimension * 0.035f, cap = StrokeCap.Round)
        )
    }
}

private fun DrawScope.drawCupHandle(left: Boolean) {
    val x = if (left) size.width * 0.28f else size.width * 0.72f
    val start = if (left) 84f else 276f
    drawArc(
        color = Color(0xFFFF3348),
        startAngle = start,
        sweepAngle = 190f,
        useCenter = false,
        topLeft = Offset(x - size.width * 0.13f, size.height * 0.25f),
        size = Size(size.width * 0.26f, size.height * 0.26f),
        style = Stroke(width = size.minDimension * 0.06f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawConfetti() {
    val colors = listOf(ColorPrimary, ColorCategoryFantasy, ColorTertiary, Color(0xFF62D1C8))
    val points = listOf(
        Offset(size.width * 0.08f, size.height * 0.58f),
        Offset(size.width * 0.17f, size.height * 0.22f),
        Offset(size.width * 0.86f, size.height * 0.24f),
        Offset(size.width * 0.82f, size.height * 0.78f),
        Offset(size.width * 0.24f, size.height * 0.84f),
        Offset(size.width * 0.94f, size.height * 0.55f)
    )
    points.forEachIndexed { index, point ->
        drawCircle(
            color = colors[index % colors.size],
            radius = size.minDimension * 0.018f,
            center = point,
            style = Stroke(width = size.minDimension * 0.012f)
        )
    }
}

@Composable
private fun rememberReadingMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): ReadingMetrics {
    val widthRatio = (maxWidth / 390.dp).coerceIn(0.84f, 1.12f)
    val heightRatio = (maxHeight / 844.dp).coerceIn(0.8f, 1.08f)
    val compactness = min(widthRatio, heightRatio)
    val compactHeight = maxHeight < 760.dp
    val verticalScale = if (compactHeight) 0.72f else 0.88f

    return remember(maxWidth, maxHeight) {
        ReadingMetrics(
            horizontalPadding = (34.dp * widthRatio).coerceIn(28.dp, 40.dp),
            topSpacing = (24.dp * verticalScale).coerceIn(16.dp, 28.dp),
            topButtonSize = (38.dp * compactness).coerceIn(34.dp, 42.dp),
            topIconSize = (23.dp * compactness).coerceIn(20.dp, 25.dp),
            bookTopSpacing = (20.dp * verticalScale).coerceIn(12.dp, 22.dp),
            bookHeight = (212.dp * compactness).coerceIn(178.dp, 226.dp),
            bookTextPadding = (10.dp * compactness).coerceIn(8.dp, 12.dp),
            bookTextFontSize = (9.5f * compactness).coerceIn(8.5f, 10f).sp,
            bookTextLineHeight = (12f * compactness).coerceIn(10.5f, 12.5f).sp,
            titleTopSpacing = (24.dp * verticalScale).coerceIn(16.dp, 26.dp),
            titleFontSize = (19f * compactness).coerceIn(17f, 21f).sp,
            authorFontSize = (15f * compactness).coerceIn(14f, 17f).sp,
            sheetTopSpacing = (28.dp * verticalScale).coerceIn(20.dp, 32.dp),
            sheetTopPadding = (32.dp * verticalScale).coerceIn(22.dp, 36.dp),
            sheetHorizontalPadding = (26.dp * widthRatio).coerceIn(20.dp, 30.dp),
            statCardHeight = (196.dp * compactness).coerceIn(170.dp, 208.dp),
            statCardCorner = (24.dp * compactness).coerceIn(19.dp, 26.dp),
            statTitleFontSize = (18f * compactness).coerceIn(15f, 20f).sp,
            statArtSize = (78.dp * compactness).coerceIn(64.dp, 86.dp),
            statValueFontSize = (27f * compactness).coerceIn(22f, 30f).sp,
            statUnitFontSize = (10.5f * compactness).coerceIn(9f, 12f).sp,
            sectionTopSpacing = (28.dp * verticalScale).coerceIn(18.dp, 32.dp),
            sectionTitleFontSize = (20f * compactness).coerceIn(17f, 22f).sp,
            overviewFontSize = (13f * compactness).coerceIn(12f, 14f).sp,
            bottomBarHeight = (78.dp * compactness).coerceIn(68.dp, 86.dp),
            bottomBarCorner = (46.dp * compactness).coerceIn(38.dp, 52.dp),
            bottomIconSize = (22.dp * compactness).coerceIn(18.dp, 24.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ReadingScreenPreview() {
    DZTheme(darkTheme = false) {
        ReadingScreen()
    }
}
