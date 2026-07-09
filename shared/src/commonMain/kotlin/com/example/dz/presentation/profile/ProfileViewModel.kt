package com.example.dz.presentation.profile

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

class ProfileViewModel(
    private val getProfile: GetProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ProfileEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.BackClicked -> emitEffect(ProfileEffect.NavigateBack)
            ProfileEvent.NotificationsClicked -> emitEffect(ProfileEffect.NavigateToNotifications)
            ProfileEvent.FriendsClicked -> emitEffect(ProfileEffect.NavigateToFriends)
            ProfileEvent.GoalsClicked -> emitEffect(ProfileEffect.NavigateToGoals)
            ProfileEvent.CollectionsClicked -> emitEffect(ProfileEffect.NavigateToCollections)
            ProfileEvent.PurchasesClicked -> emitEffect(ProfileEffect.NavigateToPurchases)
            ProfileEvent.MembershipClicked -> emitEffect(ProfileEffect.NavigateToMembership)
            ProfileEvent.SettingsClicked -> emitEffect(ProfileEffect.NavigateToSettings)
            ProfileEvent.EditClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getProfile()) {
                is AppResult.Success -> _uiState.update {
                    result.data.toProfileUiState().copy(isLoading = false, errorMessage = null)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: ProfileEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
