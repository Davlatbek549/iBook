package com.example.dz.data.remote.dto.openlibrary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenLibrarySearchResponseDto(
    @SerialName("numFound")
    val numFound: Int? = null,
    val docs: List<OpenLibraryBookDto> = emptyList()
)
