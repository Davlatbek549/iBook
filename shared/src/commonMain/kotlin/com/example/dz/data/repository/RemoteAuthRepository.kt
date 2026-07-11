package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.api.AuthApi
import com.example.dz.data.remote.dto.auth.AuthResponseDto
import com.example.dz.data.remote.dto.auth.LoginRequestDto
import com.example.dz.data.remote.dto.auth.SignUpRequestDto
import com.example.dz.data.remote.dto.auth.UserDto
import com.example.dz.data.remote.runRemote
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository

/**
 * Talks to the auth backend via [AuthApi] and persists the returned session locally so the app
 * can restore it offline. The session (current user) is read from [LocalDataSource] rather than
 * the network, matching how tokens are stored.
 */
class RemoteAuthRepository(
    private val api: AuthApi,
    private val local: LocalDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<User> =
        runRemote { api.login(LoginRequestDto(email = email, password = password)) }
            .persistSession()

    override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
        runRemote { api.signUp(SignUpRequestDto(name = name, email = email, password = password)) }
            .persistSession()

    override suspend fun logout(): AppResult<Unit> {
        // Best effort on the server; the local session is always cleared.
        runRemote { api.logout() }
        local.clearSession()
        return AppResult.Success(Unit)
    }

    override suspend fun getCurrentUser(): AppResult<User?> {
        if (!local.isLoggedIn()) return AppResult.Success(null)
        val userId = local.getUserId() ?: return AppResult.Success(null)
        val name = local.getUserName() ?: return AppResult.Error(AppError.Unauthorized)
        return AppResult.Success(User(id = userId, name = name, email = local.getUserEmail()))
    }

    private fun AppResult<AuthResponseDto>.persistSession(): AppResult<User> = when (this) {
        is AppResult.Success -> {
            local.saveUserSession(
                userId = data.user.id,
                name = data.user.name,
                email = data.user.email.orEmpty(),
                token = data.token
            )
            AppResult.Success(data.user.toDomain())
        }
        is AppResult.Error -> this
    }
}

private fun UserDto.toDomain(): User =
    User(id = id, name = name, email = email, avatarUrl = avatarUrl)
