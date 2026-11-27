package com.example.dz.domain.model

@kotlinx.serialization.Serializable
data class Category(
    val id: String,
    val name: String,
    val imageUrl: String? = null
)
