package com.example.dz.domain.model

data class Book(
    val id: String,
    val title: String,
    val authors: List<Author> = emptyList(),
    val coverUrl: String? = null,
    val description: String? = null,
    val categories: List<Category> = emptyList(),
    val rating: Double? = null,
    val reviewCount: Int? = null,
    val firstPublishYear: Int? = null,
    val pageCount: Int? = null,
    val language: String? = null,
    val price: String? = null,
    val isFree: Boolean = false
)
