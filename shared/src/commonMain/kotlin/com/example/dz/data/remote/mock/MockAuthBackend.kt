package com.example.dz.data.remote.mock

import com.example.dz.data.remote.dto.auth.AuthResponseDto
import com.example.dz.data.remote.dto.auth.LoginRequestDto
import com.example.dz.data.remote.dto.auth.SignUpRequestDto
import com.example.dz.data.remote.dto.auth.UserDto
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * In-memory stand-in for the auth backend, wired while there is no real server. It mirrors the
 * lenient demo behaviour (any non-blank credentials succeed) but exercises the full networking
 * path: JSON (de)serialization, status codes, and error mapping. Swap this engine for a real
 * platform engine once a server exists (see [com.example.dz.data.remote.api.ApiConfig]).
 */
fun createMockAuthEngine(json: Json): MockEngine = MockEngine { request ->
    val path = request.url.encodedPath
    val rawBody = request.body.toByteArray().decodeToString()
    when {
        path.endsWith("/auth/login") -> {
            val req = runCatching { json.decodeFromString<LoginRequestDto>(rawBody) }.getOrNull()
            if (req == null || req.email.isBlank() || req.password.isBlank()) {
                respond("Invalid credentials", HttpStatusCode.Unauthorized)
            } else {
                respondAuth(json, email = req.email, name = req.email.substringBefore("@"))
            }
        }

        path.endsWith("/auth/signup") -> {
            val req = runCatching { json.decodeFromString<SignUpRequestDto>(rawBody) }.getOrNull()
            if (req == null || req.email.isBlank() || req.password.isBlank()) {
                respond("Invalid signup", HttpStatusCode.BadRequest)
            } else {
                respondAuth(json, email = req.email, name = req.name)
            }
        }

        path.endsWith("/auth/logout") -> respond("", HttpStatusCode.OK)

        else -> respond("Not found", HttpStatusCode.NotFound)
    }
}

private fun MockRequestHandleScope.respondAuth(json: Json, email: String, name: String): HttpResponseData {
    val payload = AuthResponseDto(
        token = "mock_${email.hashCode()}_token",
        user = UserDto(
            id = email.hashCode().toString(),
            name = name.ifBlank { email.substringBefore("@") },
            email = email
        )
    )
    return respond(
        content = json.encodeToString(payload),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )
}
