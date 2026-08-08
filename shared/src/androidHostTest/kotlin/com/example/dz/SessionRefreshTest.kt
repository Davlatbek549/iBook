package com.example.dz

import com.example.dz.data.remote.api.ApiConfig
import com.example.dz.data.remote.api.createAuthHttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

/**
 * Access tokens live 15 minutes while sessions are meant to last, so the client has to renew one
 * mid-flight. These tests pin that behaviour: a screen loading with an expired token must see a
 * normal success, not the 401 underneath it.
 */
class SessionRefreshTest {

    private val baseUrl = ApiConfig.DEPLOYED_BASE_URL
    private val jsonHeaders =
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    private fun refreshedSession(token: String, refreshToken: String) = """
        {
          "token":"$token",
          "refreshToken":"$refreshToken",
          "expiresIn":900,
          "user":{"id":"u-1","name":"Ada Lovelace","email":"ada@example.com","avatarUrl":null}
        }
    """.trimIndent()

    private fun signedInLocal() = FakeLocalDataSource().apply {
        saveUserSession(
            userId = "u-1",
            name = "Ada Lovelace",
            email = "ada@example.com",
            token = "expired-token",
            refreshToken = "refresh-1"
        )
    }

    /**
     * Stands in for dz-server: it rejects the expired token, honours the refresh token once, and
     * accepts whatever token that refresh handed out.
     */
    private class StubServer(
        private val jsonHeaders: io.ktor.http.Headers,
        private val refreshResponse: (attempt: Int) -> Pair<HttpStatusCode, String>
    ) {
        var refreshCalls = 0
            private set
        var protectedCalls = 0
            private set

        fun engine(): MockEngine = MockEngine { request ->
            val bearer = request.headers[HttpHeaders.Authorization]?.removePrefix("Bearer ")
            when {
                request.url.encodedPath.endsWith("/auth/refresh") -> {
                    refreshCalls++
                    val (status, body) = refreshResponse(refreshCalls)
                    respond(body, status, jsonHeaders)
                }

                bearer == "expired-token" || bearer == null ->
                    respond(
                        """{"code":"Unauthorized","message":"token expired"}""",
                        HttpStatusCode.Unauthorized,
                        jsonHeaders
                    )

                else -> {
                    protectedCalls++
                    respond("""{"ok":true}""", HttpStatusCode.OK, jsonHeaders)
                }
            }
        }
    }

    @Test
    fun `an expired token is refreshed and the original request retried`() = runBlocking {
        val local = signedInLocal()
        val server = StubServer(jsonHeaders) { HttpStatusCode.OK to refreshedSession("fresh-token", "refresh-2") }
        val client = createAuthHttpClient(local, ApiConfig(), server.engine())

        val response: HttpResponse = client.get("$baseUrl/library/books")

        assertEquals(HttpStatusCode.OK, response.status, "the 401 should never reach the caller")
        assertEquals(1, server.refreshCalls)
        assertEquals("fresh-token", local.getToken())
        assertEquals("refresh-2", local.getRefreshToken(), "the rotated refresh token must replace the spent one")
        assertTrue(local.isLoggedIn())
    }

    @Test
    fun `a rejected refresh token ends the session`() = runBlocking {
        val local = signedInLocal()
        val server = StubServer(jsonHeaders) {
            HttpStatusCode.Unauthorized to """{"code":"InvalidCredentials","message":"refresh token spent"}"""
        }
        val client = createAuthHttpClient(local, ApiConfig(), server.engine())

        // The caller still sees the failure; it just is not recoverable.
        val failed = runCatching { client.get("$baseUrl/library/books") }
        assertTrue(failed.isFailure)
        assertTrue(!local.isLoggedIn(), "a spent refresh token should send the user back to sign-in")
        assertNull(local.getToken())
    }

    @Test
    fun `a server outage during refresh keeps the session`() = runBlocking {
        val local = signedInLocal()
        val server = StubServer(jsonHeaders) { HttpStatusCode.InternalServerError to "" }
        val client = createAuthHttpClient(local, ApiConfig(), server.engine())

        val failed = runCatching { client.get("$baseUrl/library/books") }

        assertTrue(failed.isFailure)
        assertTrue(local.isLoggedIn(), "a 5xx says nothing about the refresh token's validity")
        assertEquals("refresh-1", local.getRefreshToken())
    }

    @Test
    fun `parallel requests share a single refresh`() = runBlocking {
        val local = signedInLocal()
        val server = StubServer(jsonHeaders) { attempt ->
            // The server rotates on use: a second spend of "refresh-1" would be rejected.
            if (attempt == 1) HttpStatusCode.OK to refreshedSession("fresh-token", "refresh-2")
            else HttpStatusCode.Unauthorized to """{"code":"InvalidCredentials","message":"already spent"}"""
        }
        val client = createAuthHttpClient(local, ApiConfig(), server.engine())

        val responses = (1..5)
            .map { async { client.get("$baseUrl/library/books") } }
            .awaitAll()

        assertTrue(responses.all { it.status == HttpStatusCode.OK }, "every caller should get the renewed session")
        assertEquals(1, server.refreshCalls, "the refresh token is rotated on use and must be spent once")
        assertEquals(5, server.protectedCalls)
    }
}
