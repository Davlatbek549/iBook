package com.example.dz.domain.model

@kotlinx.serialization.Serializable
data class Author(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val bio: String? = null,
    val booksCount: Int? = null
)
