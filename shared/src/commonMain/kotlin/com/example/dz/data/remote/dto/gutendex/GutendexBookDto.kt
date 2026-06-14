package com.example.dz.data.remote.dto.gutendex

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GutendexBookDto(
    val id: Int? = null,
    val title: String? = null,
    val authors: List<GutendexAuthorDto> = emptyList(),
    val summaries: List<String> = emptyList(),
    val subjects: List<String> = emptyList(),
    val formats: Map<String, String> = emptyMap(),
    val languages: List<String> = emptyList(),
    @SerialName("download_count")
    val downloadCount: Int? = null
)
