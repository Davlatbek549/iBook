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

/**
 * Asking for a reset code. The server answers alike whether or not the address is registered,
 * so success here says nothing about who has an account.
 */
@Serializable
data class ForgotPasswordRequestDto(
    val email: String,
)

/**
 * Spending a reset code on a new password.
 *
 * The code is spent here rather than on the code screen before it. The server allows a fixed
 * number of guesses against a code, so a separate "is this one right?" step would cost one of
 * them for nothing and hand an attacker a free oracle.
 */
@Serializable
data class ResetPasswordRequestDto(
    val email: String,
    val code: String,
    val newPassword: String,
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
    val avatarUrl: String? = null,
    /**
     * Absent means verified, not unverified. dz-server began sending this field in the same change
     * that began refusing unverified accounts, so a response without it comes from a server that
     * does not gate on verification at all. Defaulting to false made every account on such a
     * server look unverified, and the splash then sent every one of them to a code screen on every
     * launch — a trap with no way out on a server that cannot check the code.
     */
    val emailVerified: Boolean = true
)
