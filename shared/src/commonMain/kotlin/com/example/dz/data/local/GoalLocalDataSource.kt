package com.example.dz.data.local

import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.DayMinutes

/**
 * Local persistence for reading sessions, backed by SQLDelight. Sessions are stored in seconds and
 * only rounded to minutes when something asks a question in minutes, so a run of short sessions
 * does not round away to nothing.
 */
class GoalLocalDataSource(database: DzDatabase) {
    private val queries = database.readingSessionQueries

    fun recordSession(bookId: String, dayKey: Int, startedAt: Long, seconds: Long) {
        queries.insert(
            book_id = bookId,
            day_key = dayKey.toLong(),
            started_at = startedAt,
            seconds = seconds
        )
    }

    fun secondsOnDay(dayKey: Int): Long =
        queries.secondsOnDay(dayKey.toLong()).executeAsOne()

    /** Minutes per day from [fromDayKey] onward, oldest first. Days with no reading are absent. */
    fun minutesPerDaySince(fromDayKey: Int): List<DayMinutes> =
        queries.secondsPerDaySince(fromDayKey.toLong()).executeAsList().map { row ->
            DayMinutes(
                dayKey = row.day_key.toInt(),
                minutes = (row.seconds / SECONDS_PER_MINUTE).toInt()
            )
        }

    /** Every day that has any reading on it, most recent first. */
    fun daysWithReading(): List<Int> =
        queries.daysWithReading().executeAsList().map { it.toInt() }

    fun clear() {
        queries.deleteAll()
    }

    private companion object {
        const val SECONDS_PER_MINUTE = 60L
    }
}
