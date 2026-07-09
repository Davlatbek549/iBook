package com.example.dz.presentation.goal

data class GoalUiState(
    val minutesRead: Int = 26,
    val goalMinutes: Int = 30,
    val week: List<GoalDayUi> = defaultWeek,
    val todayIndex: Int = 4,
    val streakDays: Int = 21,
    val longestStreak: Int = 34,
    val booksThisYear: Int = 18,
    val yearlyGoalBooks: Int = 24,
    val booksAheadOfSchedule: Int = 3,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /** Fraction of today's daily goal completed, clamped to [0, 1]. */
    val dailyProgress: Float
        get() = if (goalMinutes <= 0) 0f else (minutesRead.toFloat() / goalMinutes).coerceIn(0f, 1f)

    /** Minutes still needed to reach today's goal, never negative. */
    val minutesRemaining: Int
        get() = (goalMinutes - minutesRead).coerceAtLeast(0)

    /** Fraction of the yearly book goal completed, clamped to [0, 1]. */
    val yearlyProgress: Float
        get() = if (yearlyGoalBooks <= 0) 0f else (booksThisYear.toFloat() / yearlyGoalBooks).coerceIn(0f, 1f)
}

data class GoalDayUi(val label: String, val completion: Float)

val defaultWeek: List<GoalDayUi> = listOf(
    GoalDayUi("M", 1f), GoalDayUi("T", 1f), GoalDayUi("W", 1f), GoalDayUi("T", 1f),
    GoalDayUi("F", 0.6f), GoalDayUi("S", 0f), GoalDayUi("S", 0f)
)
