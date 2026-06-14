package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.Chat
import com.example.dz.domain.model.Message
import com.example.dz.domain.repository.ChatRepository

class FakeChatRepository : ChatRepository {
    override suspend fun getChats(): AppResult<List<Chat>> =
        AppResult.Success(
            FakeData.friends.map { friend ->
                Chat(
                    id = "chat-${friend.id}",
                    friend = friend,
                    lastMessage = FakeData.messages.lastOrNull(),
                    unreadCount = if (friend.isOnline) 1 else 0
                )
            }
        )

    override suspend fun getMessages(friendId: String): AppResult<List<Message>> =
        AppResult.Success(FakeData.messages)

    override suspend fun sendMessage(friendId: String, text: String): AppResult<Message> =
        AppResult.Success(
            Message(
                id = "message-${text.hashCode()}",
                senderId = FakeData.user.id,
                text = text,
                sentAt = "Now",
                isMine = true
            )
        )
}
