package com.example.dz.data.remote.api

import com.example.dz.data.remote.dto.auth.AuthResponseDto
import com.example.dz.data.remote.dto.auth.LoginRequestDto
import com.example.dz.data.remote.dto.auth.SignUpRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface AuthApi {
    suspend fun login(request: LoginRequestDto): AuthResponseDto
    suspend fun signUp(request: SignUpRequestDto): AuthResponseDto
    suspend fun logout()
}

class KtorAuthApi(
    private val client: HttpClient,
    private val baseUrl: String
) : AuthApi {
    override suspend fun login(request: LoginRequestDto): AuthResponseDto =
        client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    override suspend fun signUp(request: SignUpRequestDto): AuthResponseDto =
        client.post("$baseUrl/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    override suspend fun logout() {
        client.post("$baseUrl/auth/logout")
    }
}
