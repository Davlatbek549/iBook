package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.api.AuthApi
import com.example.dz.data.remote.dto.auth.AuthResponseDto
import com.example.dz.data.remote.dto.auth.GoogleSignInRequestDto
import com.example.dz.data.remote.dto.auth.LoginRequestDto
import com.example.dz.data.remote.dto.auth.ResendVerificationRequestDto
import com.example.dz.data.remote.dto.auth.LogoutRequestDto
import com.example.dz.data.remote.dto.auth.SignUpRequestDto
import com.example.dz.data.remote.dto.auth.UserDto
import com.example.dz.data.remote.dto.auth.VerifyEmailRequestDto
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

    override suspend fun signInWithGoogle(idToken: String): AppResult<User> =
        runRemote { api.signInWithGoogle(GoogleSignInRequestDto(idToken = idToken)) }
            .persistSession()

    override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> =
        runRemote { api.verifyEmail(VerifyEmailRequestDto(email = email, code = code)) }
            // Recorded locally too, so the next launch opens on Home rather than sending the
            // reader back to a code screen they have already finished with.
            .also { if (it is AppResult.Success) local.setEmailVerified(true) }

    override suspend fun resendVerificationCode(email: String): AppResult<Unit> =
        runRemote { api.resendVerification(ResendVerificationRequestDto(email = email)) }

    override suspend fun logout(): AppResult<Unit> {
        // Best effort on the server; the local session is always cleared. The refresh token
        // names the session to revoke — without it the server would end every session this
        // user has, signing out their other devices too.
        runRemote { api.logout(LogoutRequestDto(refreshToken = local.getRefreshToken())) }
        local.clearSession()
        return AppResult.Success(Unit)
    }

    override suspend fun getCurrentUser(): AppResult<User?> {
        if (!local.isLoggedIn()) return AppResult.Success(null)
        val userId = local.getUserId() ?: return AppResult.Success(null)
        val name = local.getUserName() ?: return AppResult.Error(AppError.Unauthorized)
        return AppResult.Success(
            User(
                id = userId,
                name = name,
                email = local.getUserEmail(),
                emailVerified = local.isEmailVerified(),
            )
        )
    }

    private fun AppResult<AuthResponseDto>.persistSession(): AppResult<User> = when (this) {
        is AppResult.Success -> {
            local.saveUserSession(
                userId = data.user.id,
                name = data.user.name,
                email = data.user.email.orEmpty(),
                token = data.token,
                refreshToken = data.refreshToken,
                emailVerified = data.user.emailVerified
            )
            AppResult.Success(data.user.toDomain())
        }
        is AppResult.Error -> this
    }
}

private fun UserDto.toDomain(): User =
    User(id = id, name = name, email = email, avatarUrl = avatarUrl, emailVerified = emailVerified)
