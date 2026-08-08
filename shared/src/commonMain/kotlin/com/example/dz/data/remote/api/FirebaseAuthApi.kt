package com.example.dz.data.remote.api

import com.example.dz.core.error.AppError
import com.example.dz.data.remote.AuthBackendException
import com.example.dz.data.remote.dto.auth.AuthResponseDto
import com.example.dz.data.remote.dto.auth.FirebaseCredentialsRequestDto
import com.example.dz.data.remote.dto.auth.FirebaseErrorResponseDto
import com.example.dz.data.remote.dto.auth.FirebaseSessionDto
import com.example.dz.data.remote.dto.auth.FirebaseUpdateProfileRequestDto
import com.example.dz.data.remote.dto.auth.LoginRequestDto
import com.example.dz.data.remote.dto.auth.LogoutRequestDto
import com.example.dz.data.remote.dto.auth.SignUpRequestDto
import com.example.dz.data.remote.dto.auth.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

/**
 * [AuthApi] backed by Firebase Authentication's Identity Toolkit REST API. Firebase owns the
 * server side entirely (password hashing, token issuing, the users store), so there is nothing to
 * deploy — the app talks to `identitytoolkit.googleapis.com` directly with the project's Web API
 * key.
 *
 * Contract mapping:
 * - sign-up → `accounts:signUp`, then `accounts:update` to attach the display name;
 * - login   → `accounts:signInWithPassword`;
 * - logout  → local-only: Firebase ID tokens are stateless, the repository clears the session.
 *
 * The returned `token` is the Firebase ID token (~1h lifetime). When it expires the backend
 * answers 401 and the app routes back to login; silent refresh via the refresh token is a later
 * improvement.
 */
class FirebaseAuthApi(
    private val client: HttpClient,
    private val config: FirebaseConfig
) : AuthApi {

    override suspend fun login(request: LoginRequestDto): AuthResponseDto {
        val session = call(
            endpoint = "accounts:signInWithPassword",
            body = FirebaseCredentialsRequestDto(email = request.email, password = request.password)
        )
        return session.toAuthResponse()
    }

    override suspend fun signUp(request: SignUpRequestDto): AuthResponseDto {
        val session = call(
            endpoint = "accounts:signUp",
            body = FirebaseCredentialsRequestDto(email = request.email, password = request.password)
        )
        // Firebase cannot set a display name during sign-up, so attach it right after. The
        // original ID token stays valid because the update does not request a new one.
        call(
            endpoint = "accounts:update",
            body = FirebaseUpdateProfileRequestDto(idToken = session.idToken, displayName = request.name)
        )
        return session.copy(displayName = request.name).toAuthResponse()
    }

    override suspend fun logout(request: LogoutRequestDto) {
        // Stateless ID tokens: nothing to revoke server-side for this flow, so the refresh token
        // in [request] has no use here. RemoteAuthRepository clears the locally stored session.
    }

    private suspend inline fun <reified B> call(endpoint: String, body: B): FirebaseSessionDto {
        val response = client.post("${config.baseUrl}/$endpoint?key=${config.apiKey}") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        if (!response.status.isSuccess()) {
            throw AuthBackendException(parseAuthReason(response.bodyAsText()))
        }
        return response.body()
    }

    private fun FirebaseSessionDto.toAuthResponse(): AuthResponseDto =
        AuthResponseDto(
            token = idToken,
            user = UserDto(
                id = localId,
                name = displayName?.takeIf { it.isNotBlank() }
                    ?: email.orEmpty().substringBefore("@"),
                email = email
            )
        )
}

private val errorJson = Json { ignoreUnknownKeys = true; isLenient = true }

/**
 * Maps Firebase error codes (e.g. `EMAIL_EXISTS`, `WEAK_PASSWORD : Password should be at least 6
 * characters`) onto [AppError.AuthReason] so the UI can show precise messages.
 */
internal fun parseAuthReason(rawBody: String): AppError.AuthReason {
    val message = runCatching { errorJson.decodeFromString<FirebaseErrorResponseDto>(rawBody) }
        .getOrNull()?.error?.message.orEmpty()
    return when {
        message.startsWith("EMAIL_EXISTS") -> AppError.AuthReason.EmailAlreadyInUse
        message.startsWith("EMAIL_NOT_FOUND") ||
            message.startsWith("INVALID_PASSWORD") ||
            message.startsWith("INVALID_LOGIN_CREDENTIALS") -> AppError.AuthReason.InvalidCredentials
        message.startsWith("INVALID_EMAIL") ||
            message.startsWith("MISSING_EMAIL") -> AppError.AuthReason.InvalidEmail
        message.startsWith("WEAK_PASSWORD") ||
            message.startsWith("MISSING_PASSWORD") -> AppError.AuthReason.WeakPassword
        message.startsWith("USER_DISABLED") -> AppError.AuthReason.UserDisabled
        message.startsWith("TOO_MANY_ATTEMPTS_TRY_LATER") -> AppError.AuthReason.TooManyAttempts
        else -> AppError.AuthReason.Unknown
    }
}
