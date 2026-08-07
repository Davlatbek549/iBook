package com.example.dz.data.remote.api

import com.example.dz.core.error.AppError
import com.example.dz.data.remote.AuthBackendException
import com.example.dz.data.remote.dto.ApiErrorDto
import com.example.dz.data.remote.dto.auth.AuthResponseDto
import com.example.dz.data.remote.dto.auth.LoginRequestDto
import com.example.dz.data.remote.dto.auth.SignUpRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface AuthApi {
    suspend fun login(request: LoginRequestDto): AuthResponseDto
    suspend fun signUp(request: SignUpRequestDto): AuthResponseDto
    suspend fun logout()
}

/**
 * Talks to our own backend (`dz-server`). [baseUrl] carries the `/api/v1`
 * prefix, so the paths below are appended to it as-is.
 */
class KtorAuthApi(
    private val client: HttpClient,
    private val baseUrl: String
) : AuthApi {

    override suspend fun login(request: LoginRequestDto): AuthResponseDto = withAuthErrors {
        client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun signUp(request: SignUpRequestDto): AuthResponseDto = withAuthErrors {
        client.post("$baseUrl/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun logout() {
        withAuthErrors { client.post("$baseUrl/auth/logout") }
    }

    /**
     * Turns the server's error body into an [AuthBackendException] so the UI can
     * tell a duplicate email from a wrong password. Without this every failure
     * collapses into a generic 401 and the screens can only say "unauthorized".
     *
     * Anything unrecognised is rethrown untouched for `runRemote` to map by
     * status code, which is the behaviour we had before.
     */
    private suspend fun <T> withAuthErrors(block: suspend () -> T): T =
        try {
            block()
        } catch (error: ResponseException) {
            throw error.toAuthReason()?.let(::AuthBackendException) ?: error
        }

    private suspend fun ResponseException.toAuthReason(): AppError.AuthReason? {
        val code = runCatching { response.body<ApiErrorDto>().code }.getOrNull() ?: return null
        return AppError.AuthReason.entries.firstOrNull { it.name == code }
    }
}
