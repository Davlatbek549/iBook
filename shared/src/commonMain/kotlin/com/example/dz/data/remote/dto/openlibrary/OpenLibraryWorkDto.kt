package com.example.dz.data.remote.dto.openlibrary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class OpenLibraryWorkDto(
    val key: String? = null,
    val title: String? = null,
    val description: JsonElement? = null,
    val subjects: List<String> = emptyList(),
    val covers: List<Int> = emptyList(),
    val authors: List<OpenLibraryWorkAuthorDto> = emptyList(),
    @SerialName("first_publish_date")
    val firstPublishDate: String? = null
)

@Serializable
data class OpenLibraryWorkAuthorDto(
    val author: OpenLibraryAuthorRefDto? = null
)

@Serializable
data class OpenLibraryAuthorRefDto(
    val key: String? = null
)
