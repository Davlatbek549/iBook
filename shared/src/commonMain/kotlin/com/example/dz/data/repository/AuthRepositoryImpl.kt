package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository

/**
 * Offline stand-in that mints a local token for any credentials. Superseded by
 * [RemoteAuthRepository], which is what the DI graph binds; nothing it hands out
 * is a real session, so there is no refresh token to keep.
 */
class AuthRepositoryImpl(private val local: LocalDataSource) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<User> {
        val user = User(
            id = email.hashCode().toString(),
            name = email.substringBefore("@"),
            email = email
        )
        local.saveUserSession(
            userId = user.id,
            name = user.name,
            email = email,
            token = buildToken(email),
            refreshToken = null
        )
        return AppResult.Success(user)
    }

    override suspend fun signUp(name: String, email: String, password: String): AppResult<User> {
        val user = User(
            id = email.hashCode().toString(),
            name = name,
            email = email
        )
        local.saveUserSession(
            userId = user.id,
            name = user.name,
            email = email,
            token = buildToken(email),
            refreshToken = null
        )
        return AppResult.Success(user)
    }

    override suspend fun logout(): AppResult<Unit> {
        local.clearSession()
        return AppResult.Success(Unit)
    }

    override suspend fun getCurrentUser(): AppResult<User?> {
        if (!local.isLoggedIn()) return AppResult.Success(null)
        val userId = local.getUserId() ?: return AppResult.Success(null)
        val name = local.getUserName() ?: return AppResult.Error(AppError.Unauthorized)
        val email = local.getUserEmail()
        return AppResult.Success(User(id = userId, name = name, email = email))
    }

    private fun buildToken(email: String): String = "local_${email.hashCode()}_token"
}
