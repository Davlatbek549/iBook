package com.example.dz.domain.usecase.chat

import com.example.dz.domain.repository.ChatRepository

class GetChatsUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() = repository.getChats()
}
