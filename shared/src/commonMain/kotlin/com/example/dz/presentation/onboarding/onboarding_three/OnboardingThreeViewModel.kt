package com.example.dz.presentation.onboarding.onboarding_three

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingThreeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingThreeUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<OnboardingThreeEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: OnboardingThreeEvent) {
        when (event) {
            OnboardingThreeEvent.GetStartedClicked -> emitEffect(OnboardingThreeEffect.NavigateToSignUp)
            OnboardingThreeEvent.LoginClicked -> emitEffect(OnboardingThreeEffect.NavigateToLogin)
        }
    }

    private fun emitEffect(effect: OnboardingThreeEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
