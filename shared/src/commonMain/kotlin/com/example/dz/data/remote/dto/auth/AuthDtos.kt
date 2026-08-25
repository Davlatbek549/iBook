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

/** The signed assertion from Google. The server proves it; the app only carries it. */
@Serializable
data class GoogleSignInRequestDto(
    val idToken: String,
)

@Serializable
data class AuthResponseDto(
    val token: String,
    /**
     * Outlives the short-lived access token and buys a new one from `/auth/refresh`.
     * The server rotates it on every use, so the value here replaces the stored one.
     *
     * `dz-server` always issues one. It stays nullable so a response that omits it still
     * parses — such a session simply cannot refresh and ends with the access token.
     */
    val refreshToken: String? = null,
    val user: UserDto
)

/**
 * Spending a code. The address travels with it because this is sent while signed out — after
 * sign-up the session exists but is not yet trusted, and a reset has none at all.
 */
@Serializable
data class VerifyEmailRequestDto(
    val email: String,
    val code: String,
)

/** Asking for another code. The server answers the same way whether or not the address exists. */
@Serializable
data class ResendVerificationRequestDto(
    val email: String,
)

@Serializable
data class RefreshRequestDto(
    val refreshToken: String
)

/** Sending the refresh token revokes just this session; omitting it revokes every session. */
@Serializable
data class LogoutRequestDto(
    val refreshToken: String? = null
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String? = null,
    val avatarUrl: String? = null
)
