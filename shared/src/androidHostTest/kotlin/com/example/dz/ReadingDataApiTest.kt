package com.example.dz

import com.example.dz.data.remote.api.*
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.*
import io.ktor.http.content.TextContent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import kotlin.test.*

class ReadingDataApiTest {
    @Test fun wireContractMatchesServerAndExcludesDeviceOnlyData() = runBlocking {
        val requests = mutableListOf<Pair<String, JsonObject>>()
        val local = FakeLocalDataSource().apply {
            saveUserSession("alice", "Alice", "a@example.com", "access", "refresh", true)
        }
        val engine = MockEngine { request ->
            assertEquals("Bearer access", request.headers[HttpHeaders.Authorization])
            val response = when {
                request.method == HttpMethod.Get && request.url.encodedPath.endsWith("/library/books") ->
                    """{"books":[{"bookId":"gutenberg-1","title":"Book","author":"Author","coverUrl":null,"description":null,"textUrl":"https://example.com/text","isFree":true,"isFavorite":false,"progressPercent":42,"addedAt":"2026-10-22T00:00:00Z","lastReadAt":null}]}"""
                request.method == HttpMethod.Get ->
                    """{"collections":[{"id":"shelf","title":"Shelf","description":null,"books":[{"bookId":"gutenberg-1","title":"Book","author":null,"coverUrl":null}],"createdAt":"2026-10-22T00:00:00Z"}]}"""
                request.method == HttpMethod.Delete -> return@MockEngine respond("", HttpStatusCode.NotFound)
                else -> {
                    requests += request.url.encodedPath to Json.parseToJsonElement((request.body as TextContent).text).jsonObject
                    "{}"
                }
            }
            respond(response, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        createAuthHttpClient(local, ApiConfig("https://example.com/api/v1"), engine).use { client ->
            val api = KtorReadingDataApi(client, "https://example.com/api/v1")
            assertEquals(42, api.library().single().progressPercent)
            assertEquals("gutenberg-1", api.collections().single().books.single().id)
            val book = Book("gutenberg-1", "Book", isFree = true)
            api.saveBook(LibraryBook(book, 25, isDownloaded = true))
            api.progress(book.id, 51)
            api.saveCollection(Collection("shelf", "Shelf", books = listOf(book), colorIndex = 3, isShared = true))
            api.deleteCollection("already-removed")
        }
        assertEquals("/api/v1/library/books/gutenberg-1", requests[0].first)
        assertFalse("isDownloaded" in requests[0].second)
        assertFalse("downloadPath" in requests[0].second)
        assertEquals(25, requests[0].second.getValue("progressPercent").jsonPrimitive.int)
        assertEquals(setOf("progressPercent"), requests[1].second.keys)
        assertFalse("colorIndex" in requests[2].second)
        assertFalse("isShared" in requests[2].second)
        assertEquals("gutenberg-1", requests[2].second.getValue("books").jsonArray.single().jsonObject.getValue("bookId").jsonPrimitive.content)
    }
}
