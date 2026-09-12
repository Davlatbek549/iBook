package com.example.dz.domain.usecase.goal

import com.example.dz.core.result.AppResult
import com.example.dz.domain.repository.GoalRepository

/** Records a stretch of reading once the reader leaves the page. */
class RecordReadingSessionUseCase(
    private val goals: GoalRepository,
) {
    suspend operator fun invoke(bookId: String, seconds: Long): AppResult<Unit> =
        goals.recordSession(bookId, seconds)
}
