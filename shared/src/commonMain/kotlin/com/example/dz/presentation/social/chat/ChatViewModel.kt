package com.example.dz.presentation.social.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.chat.GetMessagesUseCase
import com.example.dz.domain.usecase.chat.SendMessageUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val friendId: String,
    private val getMessages: GetMessagesUseCase,
    private val sendMessage: SendMessageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ChatUiState(friendId = friendId, friendName = friendId, isLoading = true)
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ChatEffect>()
    val effects = _effects.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            ChatEvent.BackClicked -> emitEffect(ChatEffect.NavigateBack)
            ChatEvent.SendClicked -> send()
            is ChatEvent.ComposerChanged -> _uiState.update { it.copy(composerText = event.text) }
            ChatEvent.OptionsClicked -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getMessages(friendId)) {
                is AppResult.Success -> _uiState.update { state ->
                    val messages = result.data.map { it.toChatMessageUi() }
                    state.copy(messages = messages, isLoading = false, errorMessage = null)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                }
            }
        }
    }

    private fun send() {
        val text = _uiState.value.composerText.trim()
        if (text.isBlank()) return
        _uiState.update {
            it.copy(
                messages = it.messages + ChatMessageUi(
                    id = "local-${it.messages.size}",
                    text = text,
                    time = "now",
                    fromMe = true
                ),
                composerText = ""
            )
        }
        viewModelScope.launch { sendMessage(friendId, text) }
    }

    private fun emitEffect(effect: ChatEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
