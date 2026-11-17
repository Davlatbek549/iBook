package com.example.dz.presentation.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.goal.GetReadingGoalUseCase
import com.example.dz.domain.usecase.library.GetContinueReadingUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoalViewModel(
    private val getReadingGoal: GetReadingGoalUseCase,
    private val getContinueReading: GetContinueReadingUseCase,
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
            // No target-editing flow exists yet — the Ink screen's own Edit was the same no-op.
            GoalEvent.EditGoalClicked -> Unit
            GoalEvent.FinishItClicked -> {
                _uiState.value.continueReadingBookId?.let {
                    emitEffect(GoalEffect.NavigateToReading(it))
                }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            coroutineScope {
                val goalAsync = async { getReadingGoal() }
                val continueReadingAsync = async { getContinueReading() }

                val goalResult = goalAsync.await()
                val continueReading = (continueReadingAsync.await() as? AppResult.Success)?.data

                when (goalResult) {
                    is AppResult.Success -> {
                        val goal = goalResult.data
                        _uiState.update {
                            it.copy(
                                minutesToday = goal.minutesToday,
                                targetMinutes = goal.targetMinutes,
                                week = goal.week.toGoalWeekUi(goal.targetMinutes),
                                streakDays = goal.streakDays,
                                longestStreakDays = maxOf(goal.streakDays, it.longestStreakDays),
                                continueReadingBookId = continueReading?.book?.id,
                                continueReadingTitle = continueReading?.book?.title,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is AppResult.Error -> _uiState.update {
                        it.copy(isLoading = false, errorMessage = goalResult.error.toPresentationMessage())
                    }
                }
            }
        }
    }

    private fun emitEffect(effect: GoalEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
