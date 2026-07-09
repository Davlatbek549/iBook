package com.example.dz.data.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class SignUpRequestDto(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponseDto(
    val token: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String? = null,
    val avatarUrl: String? = null
)
