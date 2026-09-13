package com.example.dz.core.time

import java.util.TimeZone

actual fun currentEpochMillis(): Long = System.currentTimeMillis()

/** `getOffset` accounts for daylight saving at that instant, not just the zone's base offset. */
actual fun localUtcOffsetMillis(epochMillis: Long): Long =
    TimeZone.getDefault().getOffset(epochMillis).toLong()
