package com.example.dz.data.remote.api

import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.mock.createMockAuthEngine
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Builds the HTTP client for the app's own (authenticated) backend. It injects the stored bearer
 * token on every request and fails fast on non-2xx responses so [runRemote] can map status codes.
 *
 * While [ApiConfig.useMockBackend] is true the client talks to an in-memory mock engine; flip the
 * flag and set a real [ApiConfig.baseUrl] to talk to a live server with no other changes.
 */
fun createAuthHttpClient(
    local: LocalDataSource,
    config: ApiConfig = ApiConfig()
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
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }

    return if (config.useMockBackend) {
        HttpClient(createMockAuthEngine(json)) { configure() }
    } else {
        HttpClient { configure() }
    }
}
