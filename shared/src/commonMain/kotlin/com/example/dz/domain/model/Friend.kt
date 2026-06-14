package com.example.dz.domain.model

data class Friend(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val currentBook: Book? = null
)
