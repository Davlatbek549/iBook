package com.example.dz.domain.model

data class Message(
    val id: String,
    val senderId: String,
    val text: String,
    val sentAt: String,
    val isMine: Boolean = false
)
