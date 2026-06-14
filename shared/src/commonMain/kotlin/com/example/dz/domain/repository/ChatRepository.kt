package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Chat
import com.example.dz.domain.model.Message

interface ChatRepository {
    suspend fun getChats(): AppResult<List<Chat>>
    suspend fun getMessages(friendId: String): AppResult<List<Message>>
    suspend fun sendMessage(friendId: String, text: String): AppResult<Message>
}
