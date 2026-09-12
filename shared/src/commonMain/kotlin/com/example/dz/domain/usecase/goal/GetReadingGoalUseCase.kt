package com.example.dz.domain.usecase.goal

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.ReadingGoal
import com.example.dz.domain.repository.GoalRepository
import com.example.dz.domain.repository.UserRepository

/**
 * Today's reading against the reader's target.
 *
 * The two halves live apart on purpose: the target is a profile setting that will sync, and the
 * minutes are a device measurement that does not. This joins them at the point of use rather than
 * making either repository know about the other.
 */
class GetReadingGoalUseCase(
    private val goals: GoalRepository,
    private val users: UserRepository,
) {
    suspend operator fun invoke(): AppResult<ReadingGoal> {
        val target = (users.getProfile() as? AppResult.Success)?.data?.currentGoalMinutes
            ?.takeIf { it > 0 }
            ?: ReadingGoal.DEFAULT_TARGET_MINUTES
        return goals.getGoal(target)
    }
}
