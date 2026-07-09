package com.example.dz.presentation.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.user.GetProfileUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoalViewModel(
    private val getProfile: GetProfileUseCase
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
            when (val result = getProfile()) {
                is AppResult.Success -> _uiState.update {
                    // Only the daily goal maps cleanly from the profile; the weekly rings,
                    // streak, and yearly figures have no domain source yet and stay as UI state.
                    it.copy(
                        goalMinutes = result.data.currentGoalMinutes ?: it.goalMinutes,
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

    private fun emitEffect(effect: GoalEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
