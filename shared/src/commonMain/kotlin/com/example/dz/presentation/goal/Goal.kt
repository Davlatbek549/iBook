package com.example.dz.presentation.goal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkProgressBar
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.goal_edit
import dz.shared.generated.resources.goal_streak
import dz.shared.generated.resources.goal_this_week
import dz.shared.generated.resources.goal_this_year
import dz.shared.generated.resources.goal_title
import dz.shared.generated.resources.goal_yearly
import org.jetbrains.compose.resources.stringResource

private data class GoalDay(val label: String, val completion: Float)

private val weekDays = listOf(
    GoalDay("M", 1f), GoalDay("T", 1f), GoalDay("W", 1f), GoalDay("T", 1f),
    GoalDay("F", 0.6f), GoalDay("S", 0f), GoalDay("S", 0f)
)

private const val TODAY_INDEX = 4

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
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        InkTopBar(
            title = stringResource(Res.string.goal_title),
            onBackClick = onBackClick,
            right = { InkIconButton(icon = InkIcons.Settings, onClick = onSettingsClick, colors = colors) },
            colors = colors
        )

        // today ring
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 8.dp)
                .fillMaxWidth()
                .inkCard(colors)
                .padding(horizontal = 18.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 9.dp.toPx()
                    val inset = strokeWidth / 2 + 7.dp.toPx()
                    val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
                    val topLeft = Offset(inset, inset)
                    drawArc(
                        color = colors.alt,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(strokeWidth)
                    )
                    drawArc(
                        color = colors.accent,
                        startAngle = -90f,
                        sweepAngle = 360f * 26f / 30f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "26",
                        fontFamily = displayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 30.sp,
                        color = colors.ink
                    )
                    Text(
                        text = "of 30 min",
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = bodyFont,
                        fontSize = 11.sp,
                        color = colors.muted
                    )
                }
            }
            Text(
                text = "4 minutes to keep the streak alive",
                modifier = Modifier.padding(top = 14.dp),
                fontFamily = displayFont,
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp,
                color = colors.inkSoft
            )
        }

        // week
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 14.dp)
                .fillMaxWidth()
                .inkCard(colors)
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            InkLabel(text = stringResource(Res.string.goal_this_week), colors = colors)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEachIndexed { i, day ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        day.completion >= 1f -> colors.accent
                                        day.completion > 0f -> colors.accentSoft
                                        else -> colors.alt
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                day.completion >= 1f -> Icon(
                                    imageVector = InkIcons.Done,
                                    contentDescription = null,
                                    tint = colors.onAccent,
                                    modifier = Modifier.size(10.dp)
                                )
                                day.completion > 0f -> Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(colors.accent)
                                )
                            }
                        }
                        Text(
                            text = day.label,
                            fontFamily = bodyFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = if (i == TODAY_INDEX) colors.ink else colors.muted
                        )
                    }
                }
            }
        }

        // stats
        Row(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                label = stringResource(Res.string.goal_streak),
                value = "21 days",
                sub = "longest 34",
                modifier = Modifier.weight(1f),
                colors = colors
            )
            StatCard(
                label = stringResource(Res.string.goal_this_year),
                value = "18 books",
                sub = "goal 24",
                modifier = Modifier.weight(1f),
                colors = colors
            )
        }

        // yearly goal
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 14.dp)
                .fillMaxWidth()
                .inkCard(colors)
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    InkLabel(text = stringResource(Res.string.goal_yearly), colors = colors)
                }
                Text(
                    text = stringResource(Res.string.goal_edit),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.5.sp,
                    color = colors.accent
                )
            }
            InkProgressBar(
                progress = 0.75f,
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                colors = colors
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) {
                        append("18 of 24")
                    }
                    append(" books · 3 ahead of schedule")
                },
                modifier = Modifier.padding(top = 9.dp),
                fontFamily = bodyFont,
                fontSize = 11.5.sp,
                color = colors.muted
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier,
    colors: InkColors,
) {
    Column(
        modifier = modifier
            .inkCard(colors)
            .padding(horizontal = 16.dp, vertical = 15.dp)
    ) {
        InkLabel(text = label, colors = colors)
        Text(
            text = value,
            modifier = Modifier.padding(top = 10.dp),
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 21.sp,
            color = colors.ink
        )
        Text(
            text = sub,
            modifier = Modifier.padding(top = 6.dp),
            fontFamily = inkBodyFontFamily(),
            fontSize = 11.sp,
            color = colors.muted
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun GoalScreenPreview() {
    GoalScreen()
}
