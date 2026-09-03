package com.example.dz.presentation.social.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.social.GetFriendsUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendListViewModel(
    private val getFriends: GetFriendsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendListUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<FriendListEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: FriendListEvent) {
        when (event) {
            FriendListEvent.BackClicked -> emitEffect(FriendListEffect.NavigateBack)
            FriendListEvent.AddFriendClicked -> emitEffect(FriendListEffect.NavigateToInvite)
            is FriendListEvent.FriendClicked -> emitEffect(FriendListEffect.NavigateToFriendDetail(event.friendId))
            is FriendListEvent.QueryChanged -> _uiState.update { it.copy(query = event.query) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            // The curated roster carries handles/avatars the domain model can't express yet;
            // the use case drives the loading/error state so this stays backend-ready.
            when (val result = getFriends()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        friends = result.data.map { friend -> friend.toFriendUi() },
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

    private fun emitEffect(effect: FriendListEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
