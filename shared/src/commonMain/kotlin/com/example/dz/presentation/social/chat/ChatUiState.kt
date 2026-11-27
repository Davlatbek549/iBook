package com.example.dz.presentation.social.chat

import com.example.dz.domain.model.Message

data class ChatUiState(
    val friendId: String = "",
    val friendName: String = "",
    val friendCurrentBookTitle: String? = null,
    val messages: List<ChatMessageUi> = emptyList(),
    val composerText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val canSend: Boolean get() = composerText.isNotBlank()
    val firstName: String get() = friendName.substringBefore(' ').ifBlank { friendName }
}

data class ChatMessageUi(
    val id: String,
    val text: String,
    val fromMe: Boolean
)

fun Message.toChatMessageUi(): ChatMessageUi =
    ChatMessageUi(id = id, text = text, fromMe = isMine)
