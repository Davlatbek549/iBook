package com.example.dz.data.remote.dto.gutendex

data class GutendexBookDto(
    val id: Int? = null,
    val title: String? = null,
    val authors: List<GutendexAuthorDto> = emptyList(),
    val languages: List<String> = emptyList(),
    val downloadCount: Int? = null
)
