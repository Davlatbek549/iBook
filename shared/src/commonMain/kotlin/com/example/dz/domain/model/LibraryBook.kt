package com.example.dz.domain.model

@kotlinx.serialization.Serializable
data class LibraryBook(
    val book: Book,
    val progressPercent: Int = 0,
    val isDownloaded: Boolean = false,
    val isFavorite: Boolean = false
)
