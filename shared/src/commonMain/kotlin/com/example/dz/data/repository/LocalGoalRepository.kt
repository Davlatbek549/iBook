package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.core.time.localDayKey
import com.example.dz.data.local.GoalLocalDataSource
import com.example.dz.domain.model.DayMinutes
import com.example.dz.domain.model.ReadingGoal
import com.example.dz.domain.repository.GoalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/**
 * Reading goals from the on-device session log.
 *
 * Local because that is where the truth is: the app knows how long the reader held a book open, and
 * no server watched them do it. When there is a sync endpoint these rows are what it would send.
 *
 * Every query runs on [io] — the data source is synchronous and its callers are view models on the
 * thread that draws.
 */
class LocalGoalRepository(
    private val sessions: GoalLocalDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : GoalRepository {

    override suspend fun getGoal(targetMinutes: Int): AppResult<ReadingGoal> = withContext(io) {
        val today = localDayKey(currentEpochMillis())
        val secondsToday = sessions.secondsOnDay(today)
        val recorded = sessions.minutesPerDaySince(today - DAYS_IN_WEEK + 1).associateBy { it.dayKey }

        AppResult.Success(
            ReadingGoal(
                targetMinutes = targetMinutes,
                minutesToday = (secondsToday / SECONDS_PER_MINUTE).toInt(),
                streakDays = streakEndingAt(today),
                // Days with no reading are absent from the query, so the strip is filled in here
                // rather than leaving gaps a chart would have to guess at.
                week = (DAYS_IN_WEEK - 1 downTo 0).map { back ->
                    val key = today - back
                    recorded[key] ?: DayMinutes(dayKey = key, minutes = 0)
                }
            )
        )
    }

    override suspend fun recordSession(bookId: String, seconds: Long): AppResult<Unit> =
        withContext(io) {
            if (seconds < MINIMUM_SESSION_SECONDS) {
                // Opening a book and immediately leaving is not reading. Without this floor every
                // accidental tap would add a row and inflate a streak.
                return@withContext AppResult.Success(Unit)
            }
            val now = currentEpochMillis()
            sessions.recordSession(
                bookId = bookId,
                dayKey = localDayKey(now),
                startedAt = now - seconds * MILLIS_PER_SECOND,
                seconds = seconds
            )
            AppResult.Success(Unit)
        }

    /**
     * Consecutive days of reading ending today, or ending yesterday when today has not started.
     *
     * Counting from yesterday matters: a reader with a thirty-day streak who has not opened a book
     * yet this morning still has a thirty-day streak, and telling them it is zero is both wrong and
     * discouraging.
     */
    private fun streakEndingAt(today: Int): Int {
        val days = sessions.daysWithReading().toHashSet()
        if (days.isEmpty()) return 0

        var cursor = when {
            days.contains(today) -> today
            days.contains(today - 1) -> today - 1
            else -> return 0
        }
        var streak = 0
        while (days.contains(cursor)) {
            streak++
            cursor--
        }
        return streak
    }

    private companion object {
        const val SECONDS_PER_MINUTE = 60L
        const val MILLIS_PER_SECOND = 1_000L
        const val DAYS_IN_WEEK = 7
        const val MINIMUM_SESSION_SECONDS = 20L
    }
}
