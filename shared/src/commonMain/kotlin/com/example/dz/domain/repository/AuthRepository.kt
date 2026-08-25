package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<User>
    suspend fun signUp(name: String, email: String, password: String): AppResult<User>
    /** Trades a provider assertion for a session; the server decides whether to trust it. */
    suspend fun signInWithGoogle(idToken: String): AppResult<User>
    /** Spends a code and marks the address verified. */
    suspend fun verifyEmail(email: String, code: String): AppResult<Unit>

    /**
     * Asks for another code. Succeeds even for an address with no account — the server answers
     * alike either way, so that this cannot be used to discover who is registered.
     */
    suspend fun resendVerificationCode(email: String): AppResult<Unit>

    suspend fun logout(): AppResult<Unit>
    suspend fun getCurrentUser(): AppResult<User?>
}
