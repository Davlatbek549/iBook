package com.example.dz.data.remote.dto.gutendex

import kotlinx.serialization.Serializable

@Serializable
data class GutendexBooksResponseDto(
    val count: Int? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: List<GutendexBookDto> = emptyList()
)
