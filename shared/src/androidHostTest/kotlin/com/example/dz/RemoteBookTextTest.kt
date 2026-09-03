package com.example.dz

import com.example.dz.core.result.AppResult
import com.example.dz.data.remote.api.KtorGutendexApi
import com.example.dz.data.remote.api.KtorOpenLibraryApi
import com.example.dz.data.repository.RemoteBookRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

class RemoteBookTextTest {

    private fun repositoryWith(engine: MockEngine): RemoteBookRepository {
        // Redirects must be handled by getBookText itself, exactly as in production where
        // Ktor refuses the https -> http downgrade Gutenberg answers with.
        val client = HttpClient(engine) { followRedirects = false }
        return RemoteBookRepository(
            openLibraryApi = KtorOpenLibraryApi(client),
            gutendexApi = KtorGutendexApi(client),
            httpClient = client
        )
    }

    @Test
    fun followsHttpDowngradeRedirectByUpgradingToHttps() = runBlocking {
        val engine = MockEngine { request ->
            when (request.url.toString()) {
                "https://www.gutenberg.org/ebooks/2701.txt.utf-8" -> respond(
                    content = "<html>302 Found</html>",
                    status = HttpStatusCode.Found,
                    headers = headersOf(
                        HttpHeaders.Location,
                        "http://www.gutenberg.org/cache/epub/2701/pg2701.txt"
                    )
                )
                // The redirect target is http://, but we must only ever request it over https.
                "https://www.gutenberg.org/cache/epub/2701/pg2701.txt" -> respond(
                    content = "Call me Ishmael."
                )
                else -> error("unexpected request: ${request.url}")
            }
        }

        val result = repositoryWith(engine)
            .getBookText("https://www.gutenberg.org/ebooks/2701.txt.utf-8")

        assertEquals(AppResult.Success("Call me Ishmael."), result)
    }

    @Test
    fun upgradesAPlainHttpTextUrlBeforeRequesting() = runBlocking {
        val engine = MockEngine { request ->
            assertEquals("https", request.url.protocol.name)
            respond(content = "body over https")
        }

        val result = repositoryWith(engine).getBookText("http://example.org/book.txt")

        assertEquals(AppResult.Success("body over https"), result)
    }

    @Test
    fun redirectLoopFailsInsteadOfHanging() = runBlocking {
        val engine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.Found,
                headers = headersOf(HttpHeaders.Location, request.url.toString())
            )
        }

        val result = repositoryWith(engine).getBookText("https://example.org/loop.txt")

        assertTrue(result is AppResult.Error)
    }

    @Test
    fun errorStatusIsReportedAsErrorNotAsBookText() = runBlocking {
        val engine = MockEngine {
            respond(content = "<html>404</html>", status = HttpStatusCode.NotFound)
        }

        val result = repositoryWith(engine).getBookText("https://example.org/missing.txt")

        assertTrue(result is AppResult.Error)
    }
}
