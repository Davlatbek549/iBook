package com.example.dz.data.remote.dto.openlibrary

import kotlinx.serialization.Serializable

/**
 * `works/{id}/ratings.json`.
 *
 * A work nobody has rated still answers, with a count of zero and an average of zero rather than a
 * 404 — so an empty histogram has to be read off the count, not off a missing response.
 */
@Serializable
data class OpenLibraryRatingsDto(
    val summary: OpenLibraryRatingsSummaryDto? = null,
    val counts: Map<String, Int> = emptyMap()
)

@Serializable
data class OpenLibraryRatingsSummaryDto(
    val average: Double? = null,
    val count: Int = 0
)
