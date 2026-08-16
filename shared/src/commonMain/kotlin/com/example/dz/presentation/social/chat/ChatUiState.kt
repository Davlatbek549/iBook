package com.example.dz.presentation.social.chat

import com.example.dz.domain.model.Message

data class ChatUiState(
    val friendId: String = "",
    val friendName: String = "",
    val online: Boolean = false,
    val messages: List<ChatMessageUi> = emptyList(),
    val composerText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val canSend: Boolean get() = composerText.isNotBlank()
}

data class ChatMessageUi(
    val id: String,
    val text: String,
    val time: String,
    val fromMe: Boolean
)

fun Message.toChatMessageUi(): ChatMessageUi =
    ChatMessageUi(id = id, text = text, time = sentAt, fromMe = isMine)
