package com.example.dz.presentation.goal

import com.example.dz.domain.model.DayMinutes
import com.example.dz.domain.model.ReadingGoal

data class GoalUiState(
    val minutesToday: Int = 0,
    val targetMinutes: Int = ReadingGoal.DEFAULT_TARGET_MINUTES,
    val week: List<GoalDayUi> = emptyList(),
    val streakDays: Int = 0,
    val continueReadingBookId: String? = null,
    val continueReadingTitle: String? = null,
    // Nothing counts books finished per year or keeps a lifetime best-run record, so these four
    // carry the same not-yet-wired defaults the Ink screen already showed.
    val booksThisYear: Int = 18,
    val yearlyGoalBooks: Int = 24,
    val booksAheadOfSchedule: Int = 3,
    val longestStreakDays: Int = 34,
    val bestWeekMinutes: Int = 252,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val progress: Float
        get() = if (targetMinutes <= 0) 0f else (minutesToday.toFloat() / targetMinutes).coerceIn(0f, 1f)

    val minutesRemaining: Int
        get() = (targetMinutes - minutesToday).coerceAtLeast(0)

    val isGoalMet: Boolean
        get() = minutesToday >= targetMinutes

    val totalMinutesThisWeek: Int
        get() = week.sumOf { it.minutes }

    /** "avg X min" over the real week, not a lifetime figure — there is no lifetime one to show. */
    val averageMinutesThisWeek: Int
        get() = if (week.isEmpty()) 0 else totalMinutesThisWeek / week.size

    val yearlyProgress: Float
        get() = if (yearlyGoalBooks <= 0) 0f else (booksThisYear.toFloat() / yearlyGoalBooks).coerceIn(0f, 1f)
}

/**
 * One bar in the week strip. [heightFraction] is the day's minutes against the tallest thing on
 * the strip — the target or the week's own peak, whichever is bigger — so a big day never clips
 * and a quiet week still shows something.
 */
data class GoalDayUi(
    val label: String,
    val minutes: Int,
    val heightFraction: Float,
    val isToday: Boolean,
    val metTarget: Boolean,
)

/**
 * Monday-first, matching the design's week strip. 1970-01-01 was a Thursday, so a Monday-first
 * index is `(dayKey + 3) % 7` — correct across month and year boundaries with no calendar library.
 */
private val WEEKDAY_LETTERS = listOf("M", "T", "W", "T", "F", "S", "S")

fun List<DayMinutes>.toGoalWeekUi(targetMinutes: Int): List<GoalDayUi> {
    val peak = maxOf(targetMinutes, maxOfOrNull { it.minutes } ?: 0, 1)
    return mapIndexed { index, day ->
        GoalDayUi(
            label = WEEKDAY_LETTERS[((day.dayKey + 3) % 7 + 7) % 7],
            minutes = day.minutes,
            heightFraction = (day.minutes.toFloat() / peak).coerceIn(0f, 1f),
            // The last entry is today — see GoalViewModel and the week strip it is built from.
            isToday = index == lastIndex,
            metTarget = day.minutes >= targetMinutes
        )
    }
}
