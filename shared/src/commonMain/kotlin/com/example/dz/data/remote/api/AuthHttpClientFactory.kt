package com.example.dz.data.remote.api

import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.dto.auth.AuthResponseDto
import com.example.dz.data.remote.dto.auth.RefreshRequestDto
import com.example.dz.data.remote.mock.createMockAuthEngine
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

private const val BEARER_PREFIX = "Bearer "

/** Marks the refresh call itself, so a 401 from it cannot trigger another refresh. */
private val IsRefreshRequest = AttributeKey<Unit>("DzIsRefreshRequest")

/**
 * Builds the HTTP client for the app's own (authenticated) backend. It injects the stored bearer
 * token on every request and fails fast on non-2xx responses so [runRemote] can map status codes.
 *
 * Access tokens live 15 minutes, so a session that outlasts one has to be renewed: a 401 is
 * retried once against `/auth/refresh` before it ever reaches the caller (see [SessionRefresher]).
 *
 * While [ApiConfig.useMockBackend] is true the client talks to an in-memory mock engine; flip the
 * flag and set a real [ApiConfig.baseUrl] to talk to a live server with no other changes.
 */
fun createAuthHttpClient(
    local: LocalDataSource,
    config: ApiConfig = ApiConfig(),
    /**
     * Left null in production, where the engine follows from [config]. Tests pass a stub
     * server so the retry path runs against the real client wiring rather than a copy of it.
     */
    engineOverride: HttpClientEngine? = null
): HttpClient {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val configure: HttpClientConfig<*>.() -> Unit = {
        expectSuccess = true
        install(ContentNegotiation) { json(json) }
        defaultRequest {
            local.getToken()?.let { token ->
                header(HttpHeaders.Authorization, "$BEARER_PREFIX$token")
            }
        }
    }

    val engine = engineOverride ?: if (config.useMockBackend) createMockAuthEngine(json) else null
    val client = if (engine != null) HttpClient(engine) { configure() } else HttpClient { configure() }

    return client.withTokenRefresh(SessionRefresher(local, config.baseUrl))
}

/**
 * Retries a request once with a renewed access token when the server rejects the current one.
 *
 * The retry happens below `expectSuccess`, so callers never see the transient 401 — a screen that
 * loads after the token expired just works, one round trip slower.
 */
private fun HttpClient.withTokenRefresh(refresher: SessionRefresher): HttpClient = apply {
    plugin(HttpSend).intercept { request ->
        val call = execute(request)
        if (call.response.status != HttpStatusCode.Unauthorized) return@intercept call
        if (request.attributes.contains(IsRefreshRequest)) return@intercept call

        val staleToken = request.headers[HttpHeaders.Authorization]?.removePrefix(BEARER_PREFIX)
        val freshToken = refresher.refresh(this@apply, staleToken) ?: return@intercept call

        // execute() goes straight to the engine, so defaultRequest does not re-run and the
        // stale header has to be swapped out by hand.
        request.headers.remove(HttpHeaders.Authorization)
        request.header(HttpHeaders.Authorization, "$BEARER_PREFIX$freshToken")
        execute(request)
    }
}

/**
 * Trades the stored refresh token for a new access token.
 *
 * The server rotates the refresh token on every use — the old one dies the moment it is spent — so
 * several requests expiring at once must not each spend it. [mutex] funnels them into one refresh
 * and the losers reuse whatever it stored.
 */
private class SessionRefresher(
    private val local: LocalDataSource,
    private val baseUrl: String
) {
    private val mutex = Mutex()

    suspend fun refresh(client: HttpClient, staleToken: String?): String? = mutex.withLock {
        // Someone may have refreshed while this call waited for the lock.
        val current = local.getToken()
        if (current != null && current != staleToken) return@withLock current

        val refreshToken = local.getRefreshToken() ?: return@withLock null

        val attempt = runCatching {
            client.post("$baseUrl/auth/refresh") {
                attributes.put(IsRefreshRequest, Unit)
                contentType(ContentType.Application.Json)
                setBody(RefreshRequestDto(refreshToken))
            }.body<AuthResponseDto>()
        }

        val session = attempt.getOrNull()
        if (session == null) {
            // Only a refusal means the token is gone for good. A timeout or a 5xx says nothing
            // about its validity, and clearing the session there would sign the user out
            // every time the network hiccups.
            if (attempt.exceptionOrNull().isCredentialRejection()) local.clearSession()
            return@withLock null
        }

        local.saveTokens(session.token, session.refreshToken)
        session.token
    }

    private fun Throwable?.isCredentialRejection(): Boolean =
        this is ResponseException && response.status.value in 400..499
}
