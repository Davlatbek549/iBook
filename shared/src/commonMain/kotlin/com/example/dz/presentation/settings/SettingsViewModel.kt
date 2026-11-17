package com.example.dz.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.repository.ReaderPreferencesRepository
import com.example.dz.domain.usecase.account.DeleteAccountUseCase
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.domain.usecase.user.GetProfileUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getProfile: GetProfileUseCase,
    private val readerPreferences: ReaderPreferencesRepository,
    private val logout: LogoutUseCase,
    private val deleteAccount: DeleteAccountUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.BackClicked -> emitEffect(SettingsEffect.NavigateBack)
            SettingsEvent.EditProfileClicked -> emitEffect(SettingsEffect.NavigateToEditProfile)
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
            is SettingsEvent.AutoNightModeToggled -> _uiState.update { it.copy(isAutoNightMode = event.enabled) }
            is SettingsEvent.KeepScreenAwakeToggled -> _uiState.update { it.copy(isKeepScreenAwake = event.enabled) }
            is SettingsEvent.DailyGoalNudgeToggled -> _uiState.update { it.copy(isDailyGoalNudgeEnabled = event.enabled) }
            is SettingsEvent.FriendActivityToggled -> _uiState.update { it.copy(isFriendActivityEnabled = event.enabled) }
            is SettingsEvent.StoreOffersToggled -> _uiState.update { it.copy(isStoreOffersEnabled = event.enabled) }
            SettingsEvent.ProfileUpdated -> {
                _uiState.update { it.copy(showProfileUpdated = true) }
                load()
            }
            SettingsEvent.ProfileUpdatedShown -> _uiState.update { it.copy(showProfileUpdated = false) }
            SettingsEvent.TypeAndThemeClicked,
            SettingsEvent.DownloadsClicked,
            SettingsEvent.ShelvesPrivacyClicked,
            SettingsEvent.HelpClicked,
            SettingsEvent.TermsClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            // Device-side and synchronous — no reason to wait for the profile round trip first.
            val prefs = readerPreferences.get()
            when (val result = getProfile()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        name = result.data.user.name,
                        email = result.data.user.email.orEmpty(),
                        membershipLabel = result.data.membershipPlan?.title ?: "Free plan",
                        useSerif = prefs.useSerif,
                        pageTheme = prefs.pageTheme,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(
                        useSerif = prefs.useSerif,
                        pageTheme = prefs.pageTheme,
                        isLoading = false,
                        errorMessage = result.error.toPresentationMessage()
                    )
                }
            }
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
