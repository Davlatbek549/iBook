package com.example.dz.presentation.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicProgressBar
import com.example.dz.designsystem.components.organic.OrganicProgressRing
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.goal_avg_minutes
import dz.shared.generated.resources.goal_best_runs
import dz.shared.generated.resources.goal_best_week
import dz.shared.generated.resources.goal_continue_book
import dz.shared.generated.resources.goal_day_streak
import dz.shared.generated.resources.goal_days_suffix
import dz.shared.generated.resources.goal_edit_action
import dz.shared.generated.resources.goal_finish_it
import dz.shared.generated.resources.goal_header_title
import dz.shared.generated.resources.goal_hours_minutes
import dz.shared.generated.resources.goal_hours_this_week
import dz.shared.generated.resources.goal_longest_streak
import dz.shared.generated.resources.goal_met
import dz.shared.generated.resources.goal_minutes_remaining
import dz.shared.generated.resources.goal_no_current_book
import dz.shared.generated.resources.goal_this_week
import dz.shared.generated.resources.goal_yearly_card_title
import dz.shared.generated.resources.goal_yearly_done
import dz.shared.generated.resources.goal_yearly_pace
import dz.shared.generated.resources.nav_back
import org.jetbrains.compose.resources.stringResource

/**
 * Reading goal — today's ring, the real week behind it, and the two sections (yearly pace, best
 * runs) neither the client nor the server has a source for yet, carried over from the Ink screen
 * at the same not-wired defaults rather than invented fresh for this pass.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun GoalScreen(
    uiState: GoalUiState = GoalUiState(),
    onEvent: (GoalEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = stringResource(Res.string.goal_header_title),
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = { onEvent(GoalEvent.BackClicked) },
                            contentDescription = stringResource(Res.string.nav_back),
                            size = 38.dp,
                            iconSize = 18.dp,
                        )
                    },
                    trailing = {
                        Text(
                            text = stringResource(Res.string.goal_edit_action),
                            modifier = Modifier.clickable(role = Role.Button) { onEvent(GoalEvent.EditGoalClicked) },
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = OrganicColors.accent700
                        )
                    },
                )
            }

            item(key = "ring") {
                TodayRingCard(uiState = uiState, onFinishIt = { onEvent(GoalEvent.FinishItClicked) })
            }

            item(key = "week") { WeekStrip(uiState.week, uiState.averageMinutesThisWeek) }

            item(key = "stats") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        value = uiState.streakDays.toString(),
                        label = stringResource(Res.string.goal_day_streak),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = stringResource(
                            Res.string.goal_hours_minutes,
                            uiState.totalMinutesThisWeek / 60,
                            uiState.totalMinutesThisWeek % 60
                        ),
                        label = stringResource(Res.string.goal_hours_this_week),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item(key = "yearly") { YearlyPaceCard(uiState) }

            item(key = "best-runs") {
                Column(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    OrganicKicker(text = stringResource(Res.string.goal_best_runs), color = OrganicColors.neutral600)
                    BestRunRow(
                        position = 1,
                        positionBackground = OrganicColors.accent200,
                        positionColor = OrganicColors.accent800,
                        label = stringResource(Res.string.goal_longest_streak),
                        value = stringResource(Res.string.goal_days_suffix, uiState.longestStreakDays),
                    )
                    BestRunRow(
                        position = 2,
                        positionBackground = OrganicColors.accent2_200,
                        positionColor = OrganicColors.accent2_900,
                        label = stringResource(Res.string.goal_best_week),
                        value = stringResource(
                            Res.string.goal_hours_minutes,
                            uiState.bestWeekMinutes / 60,
                            uiState.bestWeekMinutes % 60
                        ),
                    )
                }
            }
        }
    }
}

/** The sage hero: today's ring, how far to go, and the one-tap way to close the gap. */
@Composable
private fun TodayRingCard(uiState: GoalUiState, onFinishIt: () -> Unit) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = ORGANIC_GUTTER),
        background = OrganicColors.accent2_200,
        contentPadding = PaddingValues(22.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically) {
            OrganicProgressRing(
                progress = uiState.progress,
                trackColor = OrganicColors.accent2_300,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.minutesToday.toString(),
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 38.sp,
                        lineHeight = 38.sp,
                        color = OrganicColors.accent2_900
                    )
                    Text(
                        text = "OF ${uiState.targetMinutes} MIN",
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 0.7.sp,
                        color = OrganicColors.accent2_900.copy(alpha = 0.75f)
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (uiState.isGoalMet) {
                        stringResource(Res.string.goal_met)
                    } else {
                        stringResource(Res.string.goal_minutes_remaining, uiState.minutesRemaining)
                    },
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    lineHeight = 24.2.sp,
                    color = OrganicColors.accent2_900
                )
                Text(
                    text = uiState.continueReadingTitle?.let {
                        stringResource(Res.string.goal_continue_book, it)
                    } ?: stringResource(Res.string.goal_no_current_book),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = OrganicColors.accent2_900.copy(alpha = 0.85f)
                )
                if (uiState.continueReadingBookId != null) {
                    Row(
                        modifier = Modifier
                            .height(44.dp)
                            .clip(RoundedCornerShape(OrganicShape.pill))
                            .background(OrganicColors.accent)
                            .clickable(role = Role.Button, onClick = onFinishIt)
                            .padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = OrganicIcons.ChevronRight,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = stringResource(Res.string.goal_finish_it),
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/** Seven bars, real minutes each — today drawn dashed so it reads as still in progress. */
@Composable
private fun WeekStrip(week: List<GoalDayUi>, averageMinutes: Int) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = ORGANIC_GUTTER),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OrganicKicker(text = stringResource(Res.string.goal_this_week), color = OrganicColors.neutral600)
            Text(
                text = stringResource(Res.string.goal_avg_minutes, averageMinutes),
                fontFamily = organicBodyFontFamily(),
                fontSize = 13.sp,
                color = OrganicColors.neutral700
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().height(112.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            week.forEach { day ->
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(day.heightFraction.coerceAtLeast(0.06f))
                            .clip(
                                if (day.isToday) {
                                    RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
                                } else {
                                    RoundedCornerShape(12.dp)
                                }
                            )
                            .background(
                                when {
                                    day.isToday -> OrganicColors.accent2_400
                                    day.metTarget -> OrganicColors.accent
                                    day.minutes > 0 -> OrganicColors.accent300
                                    else -> OrganicColors.neutral300
                                }
                            )
                    )
                    Text(
                        text = day.label,
                        modifier = Modifier.padding(top = 7.dp),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = if (day.isToday) FontWeight.ExtraBold else FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (day.isToday) OrganicColors.accent2_900 else OrganicColors.neutral600
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    OrganicCard(modifier = modifier, elevated = true, contentPadding = PaddingValues(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = value,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 30.sp,
                lineHeight = 30.sp,
                color = OrganicColors.accent700
            )
            Text(
                text = label,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700
            )
        }
    }
}

/** Dashed, sage — the one card on the screen with nothing behind its numbers yet. */
@Composable
private fun YearlyPaceCard(uiState: GoalUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ORGANIC_GUTTER)
            .clip(RoundedCornerShape(OrganicShape.radiusLg))
            .background(OrganicColors.accent2_100)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val inset = strokeWidth / 2f
                drawRoundRect(
                    color = OrganicColors.accent2_400,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - strokeWidth, size.height - strokeWidth),
                    cornerRadius = CornerRadius(OrganicShape.radiusLg.toPx() - inset),
                    style = Stroke(width = strokeWidth, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f)))
                )
            }
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(Res.string.goal_yearly_card_title, uiState.yearlyGoalBooks),
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 19.sp,
                color = OrganicColors.accent2_900
            )
            Text(
                text = stringResource(Res.string.goal_yearly_done, uiState.booksThisYear),
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = OrganicColors.accent2_900
            )
        }
        OrganicProgressBar(
            progress = uiState.yearlyProgress,
            trackColor = OrganicColors.accent2_300,
            progressColor = OrganicColors.accent2_700,
        )
        Text(
            text = stringResource(Res.string.goal_yearly_pace, uiState.booksAheadOfSchedule),
            fontFamily = organicBodyFontFamily(),
            fontSize = 13.sp,
            color = OrganicColors.accent2_900.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun BestRunRow(
    position: Int,
    positionBackground: Color,
    positionColor: Color,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral100)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(OrganicShape.pill)).background(positionBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = position.toString(),
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = positionColor
            )
        }
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontFamily = organicBodyFontFamily(),
            fontSize = 14.sp,
            color = OrganicColors.text
        )
        Text(
            text = value,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = OrganicColors.accent700
        )
    }
}

@Preview
@Composable
private fun GoalScreenPreview() {
    GoalScreen()
}
