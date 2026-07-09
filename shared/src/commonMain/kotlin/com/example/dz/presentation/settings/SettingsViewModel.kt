package com.example.dz.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.BackClicked -> emitEffect(SettingsEffect.NavigateBack)
            SettingsEvent.SignOutClicked -> emitEffect(SettingsEffect.NavigateBack)
            SettingsEvent.EditProfileClicked -> emitEffect(SettingsEffect.NavigateToEditProfile)
            is SettingsEvent.ReadingRemindersToggled -> _uiState.update { it.copy(readingRemindersEnabled = event.enabled) }
            is SettingsEvent.MessagesToggled -> _uiState.update { it.copy(messagesEnabled = event.enabled) }
            is SettingsEvent.PriceDropsToggled -> _uiState.update { it.copy(priceDropsEnabled = event.enabled) }
            SettingsEvent.EmailClicked,
            SettingsEvent.PasswordClicked,
            SettingsEvent.AppearanceClicked,
            SettingsEvent.TextSizeClicked,
            SettingsEvent.DailyGoalClicked,
            SettingsEvent.HelpClicked,
            SettingsEvent.TermsClicked,
            SettingsEvent.PrivacyClicked -> Unit
        }
    }

    private fun emitEffect(effect: SettingsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
