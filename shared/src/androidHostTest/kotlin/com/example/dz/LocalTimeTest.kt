package com.example.dz

import com.example.dz.core.time.TimeOfDay
import com.example.dz.core.time.localDayKey
import com.example.dz.core.time.localHourOfDay
import com.example.dz.core.time.localUtcOffsetMillis
import com.example.dz.core.time.timeOfDay
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The greeting and the reading day both hang off the local hour, so the boundaries are worth
 * pinning down.
 *
 * Times are built from the machine's own offset rather than assumed to be UTC — the test asks for
 * "9am local" and gets it wherever it runs.
 */
class LocalTimeTest {

    /** Epoch millis for [hour] o'clock local time, on a day well clear of the epoch. */
    private fun atLocalHour(hour: Int): Long {
        val approximate = DAY_IN_2026 * MILLIS_PER_DAY + hour * MILLIS_PER_HOUR
        return approximate - localUtcOffsetMillis(approximate)
    }

    @Test
    fun hourIsReadInTheReadersOwnTimezone() {
        assertEquals(9, localHourOfDay(atLocalHour(9)))
        assertEquals(23, localHourOfDay(atLocalHour(23)))
        assertEquals(0, localHourOfDay(atLocalHour(0)))
    }

    @Test
    fun morningRunsFromFiveUntilNoon() {
        assertEquals(TimeOfDay.MORNING, timeOfDay(atLocalHour(5)))
        assertEquals(TimeOfDay.MORNING, timeOfDay(atLocalHour(9)))
        assertEquals(TimeOfDay.MORNING, timeOfDay(atLocalHour(11)))
    }

    @Test
    fun afternoonRunsFromNoonUntilSix() {
        assertEquals(TimeOfDay.AFTERNOON, timeOfDay(atLocalHour(12)))
        assertEquals(TimeOfDay.AFTERNOON, timeOfDay(atLocalHour(17)))
    }

    @Test
    fun eveningCoversTheEveningAndTheSmallHours() {
        assertEquals(TimeOfDay.EVENING, timeOfDay(atLocalHour(18)))
        assertEquals(TimeOfDay.EVENING, timeOfDay(atLocalHour(23)))
        // The one that used to be wrong: 1am is not morning.
        assertEquals(TimeOfDay.EVENING, timeOfDay(atLocalHour(1)))
        assertEquals(TimeOfDay.EVENING, timeOfDay(atLocalHour(4)))
    }

    @Test
    fun everyHourOfTheDayGetsAGreeting() {
        val greeted = (0..23).map { timeOfDay(atLocalHour(it)) }
        assertEquals(24, greeted.size)
        assertEquals(setOf(TimeOfDay.MORNING, TimeOfDay.AFTERNOON, TimeOfDay.EVENING), greeted.toSet())
    }

    @Test
    fun theDayTurnsOverAtLocalMidnightNotAtUtcMidnight() {
        val lateEvening = atLocalHour(23)
        val justAfterMidnight = lateEvening + 2 * MILLIS_PER_HOUR

        assertEquals(DAY_IN_2026, localDayKey(lateEvening))
        assertEquals(DAY_IN_2026 + 1, localDayKey(justAfterMidnight))
    }

    private companion object {
        const val MILLIS_PER_HOUR = 3_600_000L
        const val MILLIS_PER_DAY = 86_400_000L

        /** An arbitrary day far from the epoch and from any DST edge. */
        const val DAY_IN_2026 = 20_500
    }
}
