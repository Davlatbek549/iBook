package com.example.dz.core.time

/** Current wall-clock time in epoch milliseconds, from the platform clock. */
expect fun currentEpochMillis(): Long

/**
 * How far the reader's timezone is from UTC at [epochMillis], in milliseconds.
 *
 * The one thing the platform has to answer. Everything else about local time is derived from it in
 * [localDayKey] and [localHourOfDay], so the day boundary and the hour cannot drift apart between
 * Android and iOS.
 */
expect fun localUtcOffsetMillis(epochMillis: Long): Long
