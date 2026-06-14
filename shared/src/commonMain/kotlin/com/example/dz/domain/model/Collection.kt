package com.example.dz.domain.model

data class Collection(
    val id: String,
    val title: String,
    val description: String? = null,
    val books: List<Book> = emptyList()
)
