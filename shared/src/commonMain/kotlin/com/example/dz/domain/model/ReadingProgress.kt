package com.example.dz.domain.model

data class ReadingProgress(
    val bookId: String,
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val progressPercent: Int = 0,
    val lastReadAt: String? = null
)
