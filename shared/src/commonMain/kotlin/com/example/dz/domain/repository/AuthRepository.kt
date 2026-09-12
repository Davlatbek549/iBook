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

    /**
     * Asks for a code to reset a forgotten password. Succeeds even for an address with no
     * account, for the same reason [resendVerificationCode] does.
     */
    suspend fun requestPasswordReset(email: String): AppResult<Unit>

    /**
     * Spends a reset code and sets [newPassword].
     *
     * The server revokes every session on success, this device's included — a reset is what
     * someone does when they think the old password is known to somebody else.
     */
    suspend fun resetPassword(email: String, code: String, newPassword: String): AppResult<Unit>

    suspend fun logout(): AppResult<Unit>

    /**
     * Deletes the signed-in account on the server, and the session on this device with it. What
     * else the device holds for the account is [DeviceDataRepository]'s to erase.
     */
    suspend fun deleteAccount(): AppResult<Unit>
    suspend fun getCurrentUser(): AppResult<User?>
}
