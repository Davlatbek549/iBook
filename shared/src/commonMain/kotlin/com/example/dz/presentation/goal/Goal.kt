package com.example.dz.presentation.goal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

@Composable
fun GoalScreen(
    uiState: GoalUiState = GoalUiState(),
    onEvent: (GoalEvent) -> Unit = {},
    modifier: Modifier = Modifier
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
            onBackClick = { onEvent(GoalEvent.BackClicked) },
            right = { InkIconButton(icon = InkIcons.Settings, onClick = { onEvent(GoalEvent.SettingsClicked) }, colors = colors) },
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
                        sweepAngle = 360f * uiState.dailyProgress,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.minutesRead.toString(),
                        fontFamily = displayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 30.sp,
                        color = colors.ink
                    )
                    Text(
                        text = "of ${uiState.goalMinutes} min",
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = bodyFont,
                        fontSize = 11.sp,
                        color = colors.muted
                    )
                }
            }
            Text(
                text = "${uiState.minutesRemaining} minutes to keep the streak alive",
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
                uiState.week.forEachIndexed { i, day ->
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
                            color = if (i == uiState.todayIndex) colors.ink else colors.muted
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
                value = "${uiState.streakDays} days",
                sub = "longest ${uiState.longestStreak}",
                modifier = Modifier.weight(1f),
                colors = colors
            )
            StatCard(
                label = stringResource(Res.string.goal_this_year),
                value = "${uiState.booksThisYear} books",
                sub = "goal ${uiState.yearlyGoalBooks}",
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
                    modifier = Modifier
                        .clickable { onEvent(GoalEvent.EditGoalClicked) }
                        .padding(4.dp),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.5.sp,
                    color = colors.accent
                )
            }
            InkProgressBar(
                progress = uiState.yearlyProgress,
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                colors = colors
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) {
                        append("${uiState.booksThisYear} of ${uiState.yearlyGoalBooks}")
                    }
                    append(" books · ${uiState.booksAheadOfSchedule} ahead of schedule")
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
