package com.example.dz.core.time

import platform.Foundation.NSDate
import platform.Foundation.NSTimeZone
import platform.Foundation.localTimeZone
import platform.Foundation.secondsFromGMT
import platform.Foundation.timeIntervalSince1970

actual fun currentEpochMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

/**
 * The zone's current offset rather than the offset at [epochMillis]: every caller asks about now,
 * so the two agree, and this avoids constructing an NSDate per call.
 */
actual fun localUtcOffsetMillis(epochMillis: Long): Long =
    NSTimeZone.localTimeZone.secondsFromGMT * 1_000L
