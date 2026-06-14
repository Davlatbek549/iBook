package com.example.dz.data.remote.dto.openlibrary

data class OpenLibrarySearchResponseDto(
    val numFound: Int? = null,
    val docs: List<OpenLibraryBookDto> = emptyList()
)
