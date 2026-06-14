package com.example.dz.data.remote.dto.openlibrary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenLibraryBookDto(
    val key: String? = null,
    val title: String? = null,
    @SerialName("author_key")
    val authorKeys: List<String> = emptyList(),
    @SerialName("author_name")
    val authorNames: List<String> = emptyList(),
    @SerialName("cover_i")
    val coverId: Int? = null,
    @SerialName("first_publish_year")
    val firstPublishYear: Int? = null,
    @SerialName("language")
    val languages: List<String> = emptyList(),
    @SerialName("number_of_pages_median")
    val pageCount: Int? = null,
    @SerialName("ratings_average")
    val rating: Double? = null,
    @SerialName("ratings_count")
    val reviewCount: Int? = null,
    @SerialName("subject")
    val subjects: List<String> = emptyList()
)
