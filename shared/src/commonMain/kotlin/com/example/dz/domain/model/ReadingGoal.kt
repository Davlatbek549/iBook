package com.example.dz.domain.model

/**
 * The daily reading goal and how the reader is doing against it.
 *
 * Every figure here is measured from reading sessions recorded on the device, not estimated: the
 * reader opened the book, and the time they held it open is the time that counts.
 */
data class ReadingGoal(
    /** The reader's daily target. Falls back to [DEFAULT_TARGET_MINUTES] when they have not set one. */
    val targetMinutes: Int = DEFAULT_TARGET_MINUTES,
    val minutesToday: Int = 0,
    /** Consecutive days ending today, or ending yesterday when today has not started yet. */
    val streakDays: Int = 0,
    /** The last seven days, oldest first, so a week strip can be drawn without another query. */
    val week: List<DayMinutes> = emptyList(),
) {
    /** How far through today's goal, clamped so an over-long day does not overflow a ring. */
    val progress: Float
        get() = if (targetMinutes <= 0) 0f else (minutesToday.toFloat() / targetMinutes).coerceIn(0f, 1f)

    val minutesRemaining: Int
        get() = (targetMinutes - minutesToday).coerceAtLeast(0)

    val isMet: Boolean
        get() = minutesToday >= targetMinutes

    companion object {
        /** What the onboarding beat previews, and what a reader who never chose a target gets. */
        const val DEFAULT_TARGET_MINUTES = 20
    }
}

/** Minutes read on one local calendar day. [dayKey] is days since the epoch, local time. */
data class DayMinutes(val dayKey: Int, val minutes: Int)
