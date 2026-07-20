package com.example.dz.data.remote.dto.auth

import kotlinx.serialization.Serializable

/** Body for `accounts:signUp` and `accounts:signInWithPassword`. */
@Serializable
data class FirebaseCredentialsRequestDto(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true
)

/** Body for `accounts:update` — used to set the display name right after sign-up. */
@Serializable
data class FirebaseUpdateProfileRequestDto(
    val idToken: String,
    val displayName: String
)

/**
 * Session returned by the Identity Toolkit sign-up / sign-in / update endpoints. Only the fields
 * the app needs are declared; the rest are ignored via `ignoreUnknownKeys`.
 */
@Serializable
data class FirebaseSessionDto(
    val idToken: String = "",
    val localId: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val refreshToken: String? = null,
    val expiresIn: String? = null
)

/** Error envelope Firebase returns with non-2xx responses. */
@Serializable
data class FirebaseErrorResponseDto(
    val error: FirebaseErrorDto = FirebaseErrorDto()
)

@Serializable
data class FirebaseErrorDto(
    val code: Int = 0,
    val message: String = ""
)
