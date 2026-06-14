package com.example.dz.domain.usecase.chat

import com.example.dz.domain.repository.ChatRepository

class GetMessagesUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(friendId: String) = repository.getMessages(friendId)
}
