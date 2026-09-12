package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.ReadingGoal

interface GoalRepository {
    /**
     * Reading measured against [targetMinutes]. The target is the caller's because it belongs to
     * the profile, not to the session log this repository owns.
     */
    suspend fun getGoal(targetMinutes: Int): AppResult<ReadingGoal>

    /** Records one stretch of reading. Stretches under a few seconds are dropped as noise. */
    suspend fun recordSession(bookId: String, seconds: Long): AppResult<Unit>
}
