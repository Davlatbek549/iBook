package com.example.dz.domain.usecase.chat

import com.example.dz.domain.repository.ChatRepository

class SendMessageUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(friendId: String, text: String) = repository.sendMessage(friendId, text)
}
