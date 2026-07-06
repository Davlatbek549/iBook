package com.example.dz.presentation.social.chat

import com.example.dz.domain.model.Message

data class ChatUiState(
    val friendId: String = "",
    val friendName: String = "Patricia Lane",
    val online: Boolean = true,
    val messages: List<ChatMessageUi> = defaultMessages,
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

val defaultMessages: List<ChatMessageUi> = listOf(
    ChatMessageUi("m1", "Finished Olive, Again last night — the last chapter undid me completely.", "14:02", fromMe = false),
    ChatMessageUi("m2", "I told you! Strout never raises her voice and it still lands harder than anything.", "14:05", fromMe = true),
    ChatMessageUi("m3", "Okay, what's next then. You always know.", "14:06", fromMe = false)
)
