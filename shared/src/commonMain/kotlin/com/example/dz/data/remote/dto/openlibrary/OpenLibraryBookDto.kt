package com.example.dz.data.remote.dto.openlibrary

data class OpenLibraryBookDto(
    val key: String? = null,
    val title: String? = null,
    val authorName: List<String> = emptyList(),
    val firstPublishYear: Int? = null,
    val coverId: Int? = null
)
