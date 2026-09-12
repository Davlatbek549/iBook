package com.example.dz.core.time

actual fun currentEpochMillis(): Long = System.currentTimeMillis()

actual fun localDayKey(epochMillis: Long): Int {
    val offset = java.util.TimeZone.getDefault().getOffset(epochMillis)
    return Math.floorDiv(epochMillis + offset, MILLIS_PER_DAY).toInt()
}

private const val MILLIS_PER_DAY = 86_400_000L
