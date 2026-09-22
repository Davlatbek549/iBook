package com.example.dz

import com.example.dz.core.result.AppResult
import com.example.dz.data.remote.api.KtorGutendexApi
import com.example.dz.data.remote.api.KtorOpenLibraryApi
import com.example.dz.data.repository.RemoteBookRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** The number on a Browse tile: Open Library's `numFound` for the genre, asked for as cheaply as it can be. */
class RemoteCategoryCountTest {

    private val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    private fun repositoryWith(engine: MockEngine): RemoteBookRepository {
        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; isLenient = true })
            }
        }
        return RemoteBookRepository(
            openLibraryApi = KtorOpenLibraryApi(client),
            gutendexApi = KtorGutendexApi(client),
            httpClient = client
        )
    }

    @Test
    fun readsTheSubjectTotalAndFetchesOneBookToGetIt() = runBlocking {
        val engine = MockEngine { request ->
            assertEquals("/search.json", request.url.encodedPath)
            assertEquals("gothic-fiction", request.url.parameters["subject"])
            assertEquals("1", request.url.parameters["limit"], "the count needs no page of books")
            respond("""{"numFound":2140,"docs":[{"key":"/works/OL1W","title":"Carmilla"}]}""", HttpStatusCode.OK, jsonHeaders)
        }

        assertEquals(AppResult.Success(2140), repositoryWith(engine).getCategoryBookCount("gothic-fiction"))
    }

    @Test
    fun aResponseWithoutATotalCountsAsNone() = runBlocking {
        val engine = MockEngine { respond("""{"docs":[]}""", HttpStatusCode.OK, jsonHeaders) }

        assertEquals(AppResult.Success(0), repositoryWith(engine).getCategoryBookCount("poetry"))
    }

    @Test
    fun aFailedRequestIsAnErrorNotAZero() = runBlocking {
        val engine = MockEngine { respond("", HttpStatusCode.InternalServerError) }
        val client = HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }
        val repository = RemoteBookRepository(
            openLibraryApi = KtorOpenLibraryApi(client),
            gutendexApi = KtorGutendexApi(client),
            httpClient = client
        )

        assertTrue(repository.getCategoryBookCount("poetry") is AppResult.Error)
    }
}
