package com.example.dz.data.repository

import com.example.dz.core.auth.MailedCodeKind
import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.api.AuthApi
import com.example.dz.data.remote.dto.auth.AuthResponseDto
import com.example.dz.data.remote.dto.auth.ForgotPasswordRequestDto
import com.example.dz.data.remote.dto.auth.GoogleSignInRequestDto
import com.example.dz.data.remote.dto.auth.LoginRequestDto
import com.example.dz.data.remote.dto.auth.ResendVerificationRequestDto
import com.example.dz.data.remote.dto.auth.ResetPasswordRequestDto
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
            // Signing up mails a code, so the cooldown starts here rather than on the code screen
            // — which may not be built until a later launch.
            .also { if (it is AppResult.Success) recordCodeSent(MailedCodeKind.EmailVerification) }

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
            .also { if (it is AppResult.Success) recordCodeSent(MailedCodeKind.EmailVerification) }

    override suspend fun requestPasswordReset(email: String): AppResult<Unit> =
        runRemote { api.forgotPassword(ForgotPasswordRequestDto(email = email)) }
            .also { if (it is AppResult.Success) recordCodeSent(MailedCodeKind.PasswordReset) }

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String,
    ): AppResult<Unit> =
        runRemote {
            api.resetPassword(
                ResetPasswordRequestDto(email = email, code = code, newPassword = newPassword)
            )
        }
            // The server has just revoked every session, including whatever this device was
            // holding. Keeping it would leave a token that can never refresh and a Home whose
            // every request is refused.
            .also { if (it is AppResult.Success) local.clearSession() }

    override suspend fun logout(): AppResult<Unit> {
        // Best effort on the server; the local session is always cleared. The refresh token
        // names the session to revoke — without it the server would end every session this
        // user has, signing out their other devices too.
        runRemote { api.logout(LogoutRequestDto(refreshToken = local.getRefreshToken())) }
        local.clearSession()
        return AppResult.Success(Unit)
    }

    override suspend fun deleteAccount(): AppResult<Unit> =
        runRemote { api.deleteAccount() }
            // Only once the server has let go of it. A failed attempt leaves the reader signed in
            // to an account that still exists, which is the truth, and lets them try again.
            .also { if (it is AppResult.Success) local.clearSession() }

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

    /**
     * Stamps the moment a code was put in the post, so the resend countdown can be worked out
     * from it on a later launch. Only successes are recorded: a request that failed sent nothing,
     * and owes the reader no wait.
     */
    private fun recordCodeSent(kind: MailedCodeKind) {
        local.saveSetting(kind.lastSentSettingKey, currentEpochMillis().toString())
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
