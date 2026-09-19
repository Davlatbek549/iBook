package com.example.dz.core.time

/**
 * Local time, derived from the platform's UTC offset.
 *
 * Kept in common code rather than per platform so both targets answer identically, and so the
 * boundaries are testable without a device.
 */

/**
 * The reader's local calendar day, as days since the epoch.
 *
 * Reading totals are per-day in the reader's own timezone, and epoch millis alone cannot say when
 * their midnight was. Stamping this at write time keeps timezone maths out of SQL, and out of every
 * query that later asks about "today".
 */
fun localDayKey(epochMillis: Long): Int =
    floorDiv(epochMillis + localUtcOffsetMillis(epochMillis), MILLIS_PER_DAY).toInt()

/** The reader's local hour, 0–23. */
fun localHourOfDay(epochMillis: Long): Int =
    (floorMod(epochMillis + localUtcOffsetMillis(epochMillis), MILLIS_PER_DAY) / MILLIS_PER_HOUR).toInt()

/** Which part of the reader's day it is, for anything that greets them. */
enum class TimeOfDay { MORNING, AFTERNOON, EVENING }

/**
 * Morning from five until noon, afternoon until six, evening after that.
 *
 * The small hours count as evening rather than earning a fourth greeting: someone reading at 1am is
 * still up from the night before as far as a greeting is concerned.
 */
fun timeOfDay(epochMillis: Long): TimeOfDay = when (localHourOfDay(epochMillis)) {
    in MORNING_FROM until AFTERNOON_FROM -> TimeOfDay.MORNING
    in AFTERNOON_FROM until EVENING_FROM -> TimeOfDay.AFTERNOON
    else -> TimeOfDay.EVENING
}

/** A calendar date in the reader's own timezone. */
data class LocalDate(val year: Int, val month: Int, val day: Int)

/**
 * The calendar date an instant falls on, where the reader is.
 *
 * Derived from [localDayKey] by the usual civil-from-days arithmetic, so a date and a day key can
 * never disagree about which day something happened on.
 */
fun localDate(epochMillis: Long): LocalDate {
    // Shift the epoch to 0000-03-01, which puts the leap day at the end of the year and makes the
    // month lengths repeat on a fixed pattern.
    val shifted = localDayKey(epochMillis).toLong() + DAYS_FROM_YEAR_ZERO
    val era = floorDiv(shifted, DAYS_PER_ERA)
    val dayOfEra = shifted - era * DAYS_PER_ERA
    val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val monthIndex = (5 * dayOfYear + 2) / 153
    val day = dayOfYear - (153 * monthIndex + 2) / 5 + 1
    val month = if (monthIndex < 10) monthIndex + 3 else monthIndex - 9
    val year = yearOfEra + era * 400 + if (month <= 2) 1 else 0
    return LocalDate(year = year.toInt(), month = month.toInt(), day = day.toInt())
}

/** Floor division, so an instant before the epoch still lands on the day that contains it. */
private fun floorDiv(value: Long, divisor: Long): Long {
    val quotient = value / divisor
    return if (value % divisor != 0L && (value xor divisor) < 0L) quotient - 1 else quotient
}

private fun floorMod(value: Long, divisor: Long): Long = value - floorDiv(value, divisor) * divisor

/** 1970-01-01 as days since 0000-03-01, the origin the civil-date arithmetic counts from. */
private const val DAYS_FROM_YEAR_ZERO = 719_468L
private const val DAYS_PER_ERA = 146_097L

private const val MILLIS_PER_HOUR = 3_600_000L
private const val MILLIS_PER_DAY = 86_400_000L
private const val MORNING_FROM = 5
private const val AFTERNOON_FROM = 12
private const val EVENING_FROM = 18
