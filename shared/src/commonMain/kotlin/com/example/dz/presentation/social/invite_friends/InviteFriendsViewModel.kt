package com.example.dz.presentation.social.invite_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.social.InviteFriendUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InviteFriendsViewModel(
    private val inviteFriend: InviteFriendUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(InviteFriendsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<InviteFriendsEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: InviteFriendsEvent) {
        when (event) {
            InviteFriendsEvent.BackClicked -> emitEffect(InviteFriendsEffect.NavigateBack)
            InviteFriendsEvent.ShareClicked -> Unit
            is InviteFriendsEvent.ContactActionClicked -> inviteContact(event.contactId)
        }
    }

    private fun inviteContact(contactId: String) {
        viewModelScope.launch {
            when (inviteFriend(contactId)) {
                is AppResult.Success -> _uiState.update { state ->
                    state.copy(
                        contacts = state.contacts.map { contact ->
                            if (contact.id == contactId) contact.copy(invited = true) else contact
                        }
                    )
                }
                is AppResult.Error -> Unit
            }
        }
    }

    private fun emitEffect(effect: InviteFriendsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
