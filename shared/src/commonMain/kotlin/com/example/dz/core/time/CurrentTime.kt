package com.example.dz.core.time

/** Current wall-clock time in epoch milliseconds, from the platform clock. */
expect fun currentEpochMillis(): Long

/**
 * The reader's local calendar day, as days since the epoch.
 *
 * Reading totals are per-day in the reader's own timezone, and epoch millis alone cannot say when
 * their midnight was. Stamping this at write time keeps timezone maths out of SQL, and out of every
 * query that later asks about "today".
 */
expect fun localDayKey(epochMillis: Long): Int
