package com.example.dz.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val logout: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.BackClicked -> emitEffect(SettingsEffect.NavigateBack)
            SettingsEvent.SignOutClicked -> signOut()
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

    /**
     * Revoking the session on the server needs a round trip, so the button is held until it
     * returns. The local session is cleared either way — [LogoutUseCase] never reports failure.
     */
    private fun signOut() {
        if (_uiState.value.isSigningOut) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSigningOut = true) }
            logout()
            _uiState.update { it.copy(isSigningOut = false) }
            _effects.emit(SettingsEffect.NavigateToLogin)
        }
    }

    private fun emitEffect(effect: SettingsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
