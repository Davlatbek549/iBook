package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.Chat
import com.example.dz.domain.model.Message
import com.example.dz.domain.repository.ChatRepository
import kotlin.random.Random

class ChatRepositoryImpl(private val local: LocalDataSource) : ChatRepository {

    override suspend fun getChats(): AppResult<List<Chat>> {
        val chats = FakeData.friends.map { friend ->
            Chat(
                id = "chat-${friend.id}",
                friend = friend,
                lastMessage = FakeData.messages.lastOrNull(),
                unreadCount = local.getSetting("unread_${friend.id}", "0").toIntOrNull() ?: 0
            )
        }
        return AppResult.Success(chats)
    }

    override suspend fun getMessages(friendId: String): AppResult<List<Message>> =
        AppResult.Success(FakeData.messages)

    override suspend fun sendMessage(friendId: String, text: String): AppResult<Message> {
        val senderId = local.getUserId() ?: FakeData.user.id
        val message = Message(
            id = "msg_${Random.nextInt(100_000)}",
            senderId = senderId,
            text = text,
            sentAt = "Now",
            isMine = true
        )
        local.removeSetting("unread_$friendId")
        return AppResult.Success(message)
    }
}
