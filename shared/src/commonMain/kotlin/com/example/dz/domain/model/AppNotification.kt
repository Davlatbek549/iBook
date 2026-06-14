package com.example.dz.domain.model

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val date: String? = null,
    val isRead: Boolean = false
)
