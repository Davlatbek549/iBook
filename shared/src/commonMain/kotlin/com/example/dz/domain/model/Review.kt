package com.example.dz.domain.model

data class Review(
    val id: String,
    val userName: String,
    val userImageUrl: String? = null,
    val rating: Int,
    val text: String,
    val date: String? = null
)
