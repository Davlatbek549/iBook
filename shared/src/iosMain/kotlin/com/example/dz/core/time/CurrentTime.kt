package com.example.dz.core.time

import platform.Foundation.NSDate
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.secondsFromGMT
import platform.Foundation.timeIntervalSince1970

actual fun currentEpochMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

actual fun localDayKey(epochMillis: Long): Int {
    // The zone's current offset rather than the offset at [epochMillis]: every caller asks about
    // now, so the two agree, and this avoids constructing an NSDate per session write.
    val offsetMillis = NSTimeZone.localTimeZone.secondsFromGMT * MILLIS_PER_SECOND
    return (epochMillis + offsetMillis).floorDivDay().toInt()
}

/** Negative epochs are not a real case here, but floor division keeps the day boundary honest. */
private fun Long.floorDivDay(): Long {
    val quotient = this / MILLIS_PER_DAY
    return if (this % MILLIS_PER_DAY != 0L && this < 0) quotient - 1 else quotient
}

private const val MILLIS_PER_SECOND = 1_000L
private const val MILLIS_PER_DAY = 86_400_000L
