package com.example.dz

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.core.time.localDayKey
import com.example.dz.data.local.GoalLocalDataSource
import com.example.dz.data.repository.LocalGoalRepository
import com.example.dz.database.DzDatabase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

/**
 * Exercises reading-goal measurement against an in-memory database: the day the minutes land on,
 * the week the strip is filled from, and the streak rules, which are the part with edge cases.
 *
 * Days are addressed relative to the real today, because that is what the repository asks the
 * clock for. Sessions are inserted through the data source so a test can name the day directly.
 */
class ReadingGoalTest {

    private fun newDatabase(): DzDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DzDatabase.Schema.create(driver)
        return DzDatabase(driver)
    }

    private fun today() = localDayKey(currentEpochMillis())

    private fun GoalLocalDataSource.read(dayKey: Int, minutes: Int) {
        recordSession(
            bookId = "b1",
            dayKey = dayKey,
            startedAt = currentEpochMillis(),
            seconds = minutes * 60L
        )
    }

    private suspend fun goalOf(sessions: GoalLocalDataSource, target: Int = 20) =
        (LocalGoalRepository(sessions).getGoal(target) as AppResult.Success).data

    @Test
    fun sumsTodaysMinutesAndLeavesOtherDaysOut() = runBlocking {
        val sessions = GoalLocalDataSource(newDatabase())
        sessions.read(today(), minutes = 12)
        sessions.read(today(), minutes = 8)
        sessions.read(today() - 1, minutes = 30)

        val goal = goalOf(sessions)

        assertEquals(20, goal.minutesToday)
        assertEquals(1f, goal.progress)
        assertEquals(0, goal.minutesRemaining)
    }

    @Test
    fun weekIsSevenDaysOldestFirstWithQuietDaysAsZero() = runBlocking {
        val sessions = GoalLocalDataSource(newDatabase())
        sessions.read(today(), minutes = 10)
        sessions.read(today() - 3, minutes = 25)

        val week = goalOf(sessions).week

        assertEquals(7, week.size)
        assertEquals(today() - 6, week.first().dayKey)
        assertEquals(today(), week.last().dayKey)
        assertEquals(10, week.last().minutes)
        assertEquals(25, week[3].minutes)
        // A day with no reading is still a day on the strip, not a gap.
        assertEquals(0, week[4].minutes)
    }

    @Test
    fun streakCountsConsecutiveDaysEndingToday() = runBlocking {
        val sessions = GoalLocalDataSource(newDatabase())
        listOf(0, 1, 2, 3).forEach { back -> sessions.read(today() - back, minutes = 5) }

        assertEquals(4, goalOf(sessions).streakDays)
    }

    /**
     * The rule that matters most: someone who has not opened a book yet this morning still has
     * whatever streak they went to bed with.
     */
    @Test
    fun streakSurvivesADayThatHasNotStartedYet() = runBlocking {
        val sessions = GoalLocalDataSource(newDatabase())
        listOf(1, 2, 3).forEach { back -> sessions.read(today() - back, minutes = 5) }

        assertEquals(3, goalOf(sessions).streakDays)
    }

    @Test
    fun streakStopsAtTheFirstMissedDay() = runBlocking {
        val sessions = GoalLocalDataSource(newDatabase())
        sessions.read(today(), minutes = 5)
        sessions.read(today() - 1, minutes = 5)
        // Nothing on today-2.
        sessions.read(today() - 3, minutes = 5)

        assertEquals(2, goalOf(sessions).streakDays)
    }

    @Test
    fun streakIsZeroWhenTheLastReadingIsOlderThanYesterday() = runBlocking {
        val sessions = GoalLocalDataSource(newDatabase())
        sessions.read(today() - 2, minutes = 40)

        assertEquals(0, goalOf(sessions).streakDays)
    }

    @Test
    fun openingABookAndLeavingIsNotReading() = runBlocking {
        val database = newDatabase()
        val sessions = GoalLocalDataSource(database)
        val repository = LocalGoalRepository(sessions)

        repository.recordSession(bookId = "b1", seconds = 3)
        assertEquals(0, goalOf(sessions).minutesToday)

        repository.recordSession(bookId = "b1", seconds = 120)
        assertEquals(2, goalOf(sessions).minutesToday)
    }

    @Test
    fun noReadingAtAllIsAnEmptyGoalRatherThanAnError() = runBlocking {
        val goal = goalOf(GoalLocalDataSource(newDatabase()))

        assertEquals(0, goal.minutesToday)
        assertEquals(0, goal.streakDays)
        assertEquals(0f, goal.progress)
        assertEquals(7, goal.week.size)
    }
}
