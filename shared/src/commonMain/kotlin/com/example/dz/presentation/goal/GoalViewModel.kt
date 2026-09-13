package com.example.dz.presentation.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.DayMinutes
import com.example.dz.domain.usecase.goal.GetReadingGoalUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoalViewModel(
    private val getReadingGoal: GetReadingGoalUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(GoalUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<GoalEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: GoalEvent) {
        when (event) {
            GoalEvent.BackClicked -> emitEffect(GoalEffect.NavigateBack)
            GoalEvent.SettingsClicked -> emitEffect(GoalEffect.NavigateToSettings)
            GoalEvent.EditGoalClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getReadingGoal()) {
                is AppResult.Success -> _uiState.update {
                    val goal = result.data
                    // Minutes, the week strip and the streak are now measured from reading
                    // sessions. The yearly figures still have no source — nothing counts books
                    // finished per year — so those stay as they were.
                    it.copy(
                        minutesRead = goal.minutesToday,
                        goalMinutes = goal.targetMinutes,
                        week = goal.week.toWeekUi(goal.targetMinutes),
                        todayIndex = (goal.week.size - 1).coerceAtLeast(0),
                        streakDays = goal.streakDays,
                        longestStreak = maxOf(goal.streakDays, it.longestStreak),
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    /**
     * Turns measured minutes into the week strip.
     *
     * The weekday letter comes from the day key itself: 1970-01-01 was a Thursday, so a Monday-first
     * index is `(dayKey + 3) % 7`. That keeps the strip correct across month and year boundaries
     * without a calendar library.
     */
    private fun List<DayMinutes>.toWeekUi(target: Int): List<GoalDayUi> =
        map { day ->
            GoalDayUi(
                label = WEEKDAY_LETTERS[((day.dayKey + 3) % 7 + 7) % 7],
                completion = if (target <= 0) 0f else (day.minutes.toFloat() / target).coerceIn(0f, 1f)
            )
        }

    private fun emitEffect(effect: GoalEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    private companion object {
        /** Monday-first, matching the design's week strip. */
        val WEEKDAY_LETTERS = listOf("M", "T", "W", "T", "F", "S", "S")
    }
}
