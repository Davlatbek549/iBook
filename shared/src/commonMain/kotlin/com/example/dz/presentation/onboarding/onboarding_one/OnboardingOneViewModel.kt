package com.example.dz.presentation.onboarding.onboarding_one

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingOneViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingOneUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<OnboardingOneEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: OnboardingOneEvent) {
        when (event) {
            OnboardingOneEvent.NextClicked -> emitEffect(OnboardingOneEffect.NavigateToNext)
            OnboardingOneEvent.SkipClicked -> emitEffect(OnboardingOneEffect.NavigateToLogin)
        }
    }

    private fun emitEffect(effect: OnboardingOneEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
