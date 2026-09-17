package com.example.dz.data.remote.api

import com.example.dz.data.remote.dto.openlibrary.OpenLibraryRatingsDto
import com.example.dz.data.remote.dto.openlibrary.OpenLibrarySearchResponseDto
import com.example.dz.data.remote.dto.openlibrary.OpenLibraryWorkDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface OpenLibraryApi {
    suspend fun searchBooks(query: String, limit: Int = DEFAULT_LIMIT): OpenLibrarySearchResponseDto
    suspend fun searchBooksBySubject(subject: String, limit: Int = DEFAULT_LIMIT): OpenLibrarySearchResponseDto
    suspend fun getWork(workId: String): OpenLibraryWorkDto

    /**
     * The search row for one known work.
     *
     * `works/{id}.json` is the canonical record but carries none of the numbers a reader wants —
     * no rating, no page count, no publisher. Those live only on search, so a detail screen has to
     * ask twice.
     */
    suspend fun searchByWorkKey(workId: String): OpenLibrarySearchResponseDto

    /** Every score a work has been given, and how many gave each. */
    suspend fun getWorkRatings(workId: String): OpenLibraryRatingsDto

    companion object {
        const val DEFAULT_LIMIT = 20
    }
}

class KtorOpenLibraryApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://openlibrary.org"
) : OpenLibraryApi {
    override suspend fun searchBooks(query: String, limit: Int): OpenLibrarySearchResponseDto =
        client.get("$baseUrl/search.json") {
            parameter("q", query)
            parameter("limit", limit)
        }.body()

    override suspend fun searchBooksBySubject(subject: String, limit: Int): OpenLibrarySearchResponseDto =
        client.get("$baseUrl/search.json") {
            parameter("subject", subject)
            parameter("limit", limit)
        }.body()

    override suspend fun getWork(workId: String): OpenLibraryWorkDto =
        client.get("$baseUrl/works/${workId.removePrefix("/works/")}.json").body()

    override suspend fun searchByWorkKey(workId: String): OpenLibrarySearchResponseDto =
        client.get("$baseUrl/search.json") {
            parameter("q", "key:/works/${workId.removePrefix("/works/")}")
            parameter("limit", 1)
        }.body()

    override suspend fun getWorkRatings(workId: String): OpenLibraryRatingsDto =
        client.get("$baseUrl/works/${workId.removePrefix("/works/")}/ratings.json").body()
}
