package com.example.dz.data.remote.dto.gutendex

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GutendexAuthorDto(
    val name: String? = null,
    @SerialName("birth_year")
    val birthYear: Int? = null,
    @SerialName("death_year")
    val deathYear: Int? = null
)
