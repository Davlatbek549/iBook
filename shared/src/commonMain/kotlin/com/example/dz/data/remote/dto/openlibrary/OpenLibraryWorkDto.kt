package com.example.dz.data.remote.dto.openlibrary

data class OpenLibraryWorkDto(
    val key: String? = null,
    val title: String? = null,
    val description: String? = null,
    val subjects: List<String> = emptyList()
)
