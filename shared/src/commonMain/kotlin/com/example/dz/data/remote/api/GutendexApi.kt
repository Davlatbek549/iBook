package com.example.dz.data.remote.api

import com.example.dz.data.remote.dto.gutendex.GutendexBookDto
import com.example.dz.data.remote.dto.gutendex.GutendexBooksResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface GutendexApi {
    suspend fun getBooks(page: Int = 1): GutendexBooksResponseDto
    suspend fun searchBooks(query: String, page: Int = 1): GutendexBooksResponseDto
    suspend fun getBook(bookId: String): GutendexBookDto
}

class KtorGutendexApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://gutendex.com"
) : GutendexApi {
    override suspend fun getBooks(page: Int): GutendexBooksResponseDto =
        client.get("$baseUrl/books") {
            parameter("page", page)
        }.body()

    override suspend fun searchBooks(query: String, page: Int): GutendexBooksResponseDto =
        client.get("$baseUrl/books") {
            parameter("search", query)
            parameter("page", page)
        }.body()

    override suspend fun getBook(bookId: String): GutendexBookDto =
        client.get("$baseUrl/books/${bookId.removePrefix("gutenberg-")}").body()
}
