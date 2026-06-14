package com.example.dz.data.remote.api

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
}
