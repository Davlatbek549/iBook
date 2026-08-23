package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<User>
    suspend fun signUp(name: String, email: String, password: String): AppResult<User>
    /** Trades a provider assertion for a session; the server decides whether to trust it. */
    suspend fun signInWithGoogle(idToken: String): AppResult<User>
    suspend fun logout(): AppResult<Unit>
    suspend fun getCurrentUser(): AppResult<User?>
}
