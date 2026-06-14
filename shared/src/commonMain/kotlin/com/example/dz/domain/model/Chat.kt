package com.example.dz.domain.model

data class Chat(
    val id: String,
    val friend: Friend,
    val lastMessage: Message? = null,
    val unreadCount: Int = 0
)
