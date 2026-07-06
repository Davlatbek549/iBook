package com.example.dz.presentation.social.friend_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.social.GetFriendDetailsUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendDetailViewModel(
    private val friendId: String,
    private val getFriendDetails: GetFriendDetailsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FriendDetailUiState(friendId = friendId, isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<FriendDetailEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: FriendDetailEvent) {
        when (event) {
            FriendDetailEvent.BackClicked -> emitEffect(FriendDetailEffect.NavigateBack)
            FriendDetailEvent.MessageClicked -> emitEffect(FriendDetailEffect.NavigateToChat(friendId))
            FriendDetailEvent.OptionsClicked,
            FriendDetailEvent.SeeAllClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getFriendDetails(friendId)) {
                is AppResult.Success -> _uiState.update {
                    result.data.toFriendDetailUiState().copy(isLoading = false, errorMessage = null)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun emitEffect(effect: FriendDetailEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
