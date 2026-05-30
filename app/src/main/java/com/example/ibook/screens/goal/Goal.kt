package com.example.ibook.screens.goal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.IBookTheme
import kotlin.math.cos
import kotlin.math.sin

private val GoalMainOrange = Color(0xFFFF6F45)
private val GoalInnerOrange = Color(0xFFFF7048)
private val GoalRingOrange = Color(0xFFFF3F1F)
private val GoalProgressPeach = Color(0xFFFFA38B)

private data class GoalMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val topButtonSize: Dp,
    val topIconSize: Dp,
    val trackerTopSpacing: Dp,
    val calendarTopSpacing: Dp,
    val weekLabelSize: TextUnit,
    val calendarDateSize: TextUnit,
    val calendarRowHeight: Dp,
    val selectedDateSize: Dp,
    val streakTopSpacing: Dp,
    val streakIconContainerSize: Dp,
    val streakFireSize: Dp,
    val streakNumberSize: TextUnit,
    val streakLabelSize: TextUnit,
    val shareTopSpacing: Dp,
    val shareButtonHeight: Dp,
    val shareButtonCorner: Dp,
    val shareTextSize: TextUnit,
    val bottomSpacing: Dp
)

private data class GoalCalendarDate(
    val label: String,
    val state: GoalCalendarDateState
)

private enum class GoalCalendarDateState {
    PastMuted,
    Normal,
    Selected,
    FutureMuted
}

private val goalWeekDays = listOf("S", "M", "T", "W", "T", "F", "S")

private val goalCalendarDates = listOf(
    GoalCalendarDate("29", GoalCalendarDateState.PastMuted),
    GoalCalendarDate("30", GoalCalendarDateState.PastMuted),
    GoalCalendarDate("1", GoalCalendarDateState.Normal),
    GoalCalendarDate("2", GoalCalendarDateState.Normal),
    GoalCalendarDate("3", GoalCalendarDateState.Normal),
    GoalCalendarDate("4", GoalCalendarDateState.Normal),
    GoalCalendarDate("5", GoalCalendarDateState.Normal),
    GoalCalendarDate("6", GoalCalendarDateState.Normal),
    GoalCalendarDate("7", GoalCalendarDateState.Normal),
    GoalCalendarDate("8", GoalCalendarDateState.Normal),
    GoalCalendarDate("9", GoalCalendarDateState.Normal),
    GoalCalendarDate("10", GoalCalendarDateState.Normal),
    GoalCalendarDate("11", GoalCalendarDateState.Normal),
    GoalCalendarDate("12", GoalCalendarDateState.Selected),
    GoalCalendarDate("13", GoalCalendarDateState.Selected),
    GoalCalendarDate("14", GoalCalendarDateState.Selected),
    GoalCalendarDate("15", GoalCalendarDateState.Selected),
    GoalCalendarDate("16", GoalCalendarDateState.Selected),
    GoalCalendarDate("17", GoalCalendarDateState.Selected),
    GoalCalendarDate("18", GoalCalendarDateState.Selected),
    GoalCalendarDate("19", GoalCalendarDateState.Selected),
    GoalCalendarDate("20", GoalCalendarDateState.Selected),
    GoalCalendarDate("21", GoalCalendarDateState.Selected),
    GoalCalendarDate("22", GoalCalendarDateState.Selected),
    GoalCalendarDate("23", GoalCalendarDateState.Selected),
    GoalCalendarDate("24", GoalCalendarDateState.Selected),
    GoalCalendarDate("25", GoalCalendarDateState.Selected),
    GoalCalendarDate("26", GoalCalendarDateState.Selected),
    GoalCalendarDate("27", GoalCalendarDateState.Selected),
    GoalCalendarDate("28", GoalCalendarDateState.Normal),
    GoalCalendarDate("28", GoalCalendarDateState.Normal),
    GoalCalendarDate("30", GoalCalendarDateState.Normal),
    GoalCalendarDate("31", GoalCalendarDateState.Normal),
    GoalCalendarDate("1", GoalCalendarDateState.FutureMuted),
    GoalCalendarDate("2", GoalCalendarDateState.FutureMuted)
)

@Composable
fun Goal(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    GoalScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onSettingsClick = onSettingsClick,
        onShareClick = onShareClick
    )
}

@Composable
fun GoalScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberGoalMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = metrics.horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            GoalTopActions(
                metrics = metrics,
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )

            Spacer(modifier = Modifier.height(metrics.trackerTopSpacing))

            ReadingGoalTracker(
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(metrics.calendarTopSpacing))

            GoalCalendar(
                metrics = metrics
            )

            Spacer(modifier = Modifier.height(metrics.streakTopSpacing))

            GoalStreak(
                metrics = metrics
            )

            Spacer(modifier = Modifier.height(metrics.shareTopSpacing))

            GoalShareButton(
                metrics = metrics,
                onClick = onShareClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(metrics.bottomSpacing))
        }
    }
}

@Composable
private fun GoalTopActions(
    metrics: GoalMetrics,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GoalCircleButton(
            metrics = metrics,
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.topIconSize * 0.52f)
            )
        }

        GoalCircleButton(
            metrics = metrics,
            onClick = onSettingsClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.topIconSize * 0.72f)
            )
        }
    }
}

@Composable
private fun GoalCircleButton(
    metrics: GoalMetrics,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(metrics.topButtonSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.16f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun ReadingGoalTracker(
    modifier: Modifier = Modifier,
    timeText: String = "10:03",
    goalText: String = "of your 40-minute goal",
    progress: Float = 0.28f
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val trackerSize = (maxWidth * 0.68f).coerceIn(248.dp, 286.dp)
        val glowSize = trackerSize * 1.18f
        val mainTimeSize = (trackerSize.value * 0.22f).sp
        val goalSize = (trackerSize.value * 0.07f).sp

        Box(
            modifier = Modifier.size(glowSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GoalMainOrange.copy(alpha = 0.28f),
                            GoalMainOrange.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.58f, size.height * 0.62f),
                        radius = size.minDimension * 0.48f
                    ),
                    radius = size.minDimension * 0.52f,
                    center = Offset(size.width * 0.58f, size.height * 0.62f)
                )
            }

            Box(
                modifier = Modifier.size(trackerSize),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val diameter = size.minDimension
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val strokeWidth = diameter * 0.08f
                    val arcInset = strokeWidth * 1.15f
                    val arcSize = Size(
                        width = diameter - arcInset * 2f,
                        height = diameter - arcInset * 2f
                    )
                    val arcTopLeft = Offset(
                        x = center.x - arcSize.width / 2f,
                        y = center.y - arcSize.height / 2f
                    )
                    val sweepAngle = progress.coerceIn(0f, 1f) * 375f

                    drawCircle(
                        color = GoalMainOrange,
                        radius = diameter / 2f,
                        center = center
                    )

                    drawCircle(
                        color = GoalInnerOrange,
                        radius = diameter * 0.405f,
                        center = center
                    )

                    drawArc(
                        color = GoalRingOrange,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )

                    drawArc(
                        color = GoalProgressPeach,
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )

                    val endAngle = Math.toRadians((-90f + sweepAngle).toDouble())
                    val endRadius = arcSize.width / 2f
                    val capCenter = Offset(
                        x = center.x + cos(endAngle).toFloat() * endRadius,
                        y = center.y + sin(endAngle).toFloat() * endRadius
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.96f),
                        radius = strokeWidth * 0.52f,
                        center = capCenter
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = timeText,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = mainTimeSize,
                            lineHeight = mainTimeSize * 1.04f,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(trackerSize * 0.035f))

                    Text(
                        text = goalText,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = goalSize,
                            lineHeight = goalSize * 1.2f,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun GoalCalendar(
    metrics: GoalMetrics
) {
    val rows = remember { goalCalendarDates.chunked(goalWeekDays.size) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            goalWeekDays.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = metrics.weekLabelSize,
                            lineHeight = metrics.weekLabelSize * 1.1f,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        )
                    )
                }
            }
        }

        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(metrics.calendarRowHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { date ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        GoalCalendarDateCell(
                            date = date,
                            metrics = metrics
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalCalendarDateCell(
    date: GoalCalendarDate,
    metrics: GoalMetrics
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val colorScheme = MaterialTheme.colorScheme
        val textColor = when (date.state) {
            GoalCalendarDateState.PastMuted,
            GoalCalendarDateState.FutureMuted -> colorScheme.onSurface.copy(alpha = 0.28f)
            GoalCalendarDateState.Normal -> colorScheme.onSurface.copy(alpha = 0.68f)
            GoalCalendarDateState.Selected -> colorScheme.onPrimary
        }

        Box(
            modifier = Modifier
                .size(metrics.selectedDateSize)
                .clip(CircleShape)
                .background(
                    if (date.state == GoalCalendarDateState.Selected) {
                        colorScheme.primary
                    } else {
                        Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.label,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.calendarDateSize,
                    lineHeight = metrics.calendarDateSize * 1.1f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun GoalStreak(
    metrics: GoalMetrics
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(metrics.streakIconContainerSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.img_fire),
                    contentDescription = "Reading streak",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(metrics.streakFireSize)
                )
            }
        }

        Column(
            modifier = Modifier.padding(start = metrics.streakIconContainerSize * 0.18f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "16 days",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.streakNumberSize,
                    lineHeight = metrics.streakNumberSize * 1.04f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Text(
                text = "Longest Reading Streak",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.streakLabelSize,
                    lineHeight = metrics.streakLabelSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun GoalShareButton(
    metrics: GoalMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(metrics.shareButtonHeight)
            .clip(RoundedCornerShape(metrics.shareButtonCorner))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Share",
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.shareTextSize,
                lineHeight = metrics.shareTextSize * 1.18f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberGoalMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): GoalMetrics {
    return remember(maxWidth, maxHeight) {
        val width = maxWidth.value
        val height = maxHeight.value
        val selectedDateSize = (maxWidth * 0.082f).coerceIn(34.dp, 44.dp)
        val topButtonSize = (maxWidth * 0.105f).coerceIn(46.dp, 56.dp)
        val shareTextSize = (width * 0.043f).coerceIn(18f, 22f).sp

        GoalMetrics(
            horizontalPadding = (maxWidth * 0.075f).coerceIn(28.dp, 36.dp),
            topSpacing = (maxHeight * 0.055f).coerceIn(44.dp, 58.dp),
            topButtonSize = topButtonSize,
            topIconSize = topButtonSize * 0.6f,
            trackerTopSpacing = (maxHeight * 0.005f).coerceIn(2.dp, 8.dp),
            calendarTopSpacing = (maxHeight * 0.025f).coerceIn(18.dp, 30.dp),
            weekLabelSize = (width * 0.04f).coerceIn(17f, 21f).sp,
            calendarDateSize = (width * 0.039f).coerceIn(16f, 20f).sp,
            calendarRowHeight = (maxHeight * 0.063f).coerceIn(48.dp, 60.dp),
            selectedDateSize = selectedDateSize,
            streakTopSpacing = (maxHeight * 0.018f).coerceIn(12.dp, 24.dp),
            streakIconContainerSize = (maxWidth * 0.112f).coerceIn(48.dp, 60.dp),
            streakFireSize = (maxWidth * 0.066f).coerceIn(28.dp, 36.dp),
            streakNumberSize = (width * 0.082f).coerceIn(34f, 44f).sp,
            streakLabelSize = (width * 0.032f).coerceIn(14f, 17f).sp,
            shareTopSpacing = (maxHeight * 0.047f).coerceIn(34.dp, 52.dp),
            shareButtonHeight = (maxHeight * 0.079f).coerceIn(66.dp, 82.dp),
            shareButtonCorner = (maxWidth * 0.052f).coerceIn(22.dp, 30.dp),
            shareTextSize = shareTextSize,
            bottomSpacing = (height * 0.024f).coerceIn(18f, 30f).dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalScreenPreview() {
    IBookTheme {
        GoalScreen()
    }
}
