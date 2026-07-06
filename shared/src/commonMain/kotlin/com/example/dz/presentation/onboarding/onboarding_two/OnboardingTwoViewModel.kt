package com.example.dz.presentation.onboarding.onboarding_two

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingTwoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingTwoUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<OnboardingTwoEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: OnboardingTwoEvent) {
        when (event) {
            OnboardingTwoEvent.NextClicked -> emitEffect(OnboardingTwoEffect.NavigateToNext)
            OnboardingTwoEvent.SkipClicked -> emitEffect(OnboardingTwoEffect.NavigateToLogin)
        }
    }

    private fun emitEffect(effect: OnboardingTwoEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
