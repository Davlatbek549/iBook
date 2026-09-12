package com.example.dz.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.account.DeleteAccountUseCase
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val logout: LogoutUseCase,
    private val deleteAccount: DeleteAccountUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.BackClicked -> emitEffect(SettingsEffect.NavigateBack)
            SettingsEvent.SignOutClicked -> signOut()
            SettingsEvent.DeleteAccountClicked -> _uiState.update {
                if (it.isSigningOut) it
                else it.copy(isDeleteConfirmationVisible = true, deleteAccountError = null)
            }
            SettingsEvent.DeleteAccountDismissed -> _uiState.update {
                if (it.isDeletingAccount) it
                else it.copy(isDeleteConfirmationVisible = false, deleteAccountError = null)
            }
            SettingsEvent.DeleteAccountConfirmed -> confirmDeletion()
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
        if (_uiState.value.isSigningOut || _uiState.value.isDeletingAccount) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSigningOut = true) }
            logout()
            _uiState.update { it.copy(isSigningOut = false) }
            _effects.emit(SettingsEffect.NavigateToLogin)
        }
    }

    /**
     * The dialog stays up until the server answers — closing it sooner would suggest the account
     * is gone when it may not be. A failure keeps it open with the reason, because the account and
     * everything on the device are still there to try again with.
     *
     * Success leaves the dialog busy on purpose: the screen is about to be torn down, and the
     * dialog should go with it rather than hand back the settings of an account that is gone.
     */
    private fun confirmDeletion() {
        if (_uiState.value.isDeletingAccount) return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeletingAccount = true, deleteAccountError = null) }
            when (val result = deleteAccount()) {
                is AppResult.Success -> _effects.emit(SettingsEffect.NavigateToLogin)
                is AppResult.Error -> _uiState.update {
                    it.copy(isDeletingAccount = false, deleteAccountError = result.error.deletionMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: SettingsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}

/**
 * A refused session has already been cleared on this device by the time the error arrives, so
 * the only way forward is signing in again; the generic "sign in to continue" would not say why.
 */
private fun AppError.deletionMessage(): String =
    if (this == AppError.Unauthorized) {
        "Your session has ended. Sign in again to delete your account."
    } else {
        toPresentationMessage()
    }
