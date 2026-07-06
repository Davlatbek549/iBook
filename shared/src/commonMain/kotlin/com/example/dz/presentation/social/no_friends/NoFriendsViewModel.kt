package com.example.dz.presentation.social.no_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NoFriendsViewModel : ViewModel() {
    private val _effects = MutableSharedFlow<NoFriendsEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: NoFriendsEvent) {
        when (event) {
            NoFriendsEvent.BackClicked -> emitEffect(NoFriendsEffect.NavigateBack)
            NoFriendsEvent.FindFriendsClicked -> emitEffect(NoFriendsEffect.NavigateToInvite)
            NoFriendsEvent.InviteContactsClicked -> emitEffect(NoFriendsEffect.NavigateToInvite)
        }
    }

    private fun emitEffect(effect: NoFriendsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
