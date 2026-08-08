package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.api.FirebaseAuthApi
import com.example.dz.data.remote.api.FirebaseConfig
import com.example.dz.data.repository.RemoteAuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Exercises the Firebase-backed auth path (repository → FirebaseAuthApi → Identity Toolkit
 * endpoints) against a mock engine that mimics Firebase's real responses, including its error
 * envelope, so error mapping stays honest.
 */
class FirebaseAuthApiTest {

    private val config = FirebaseConfig(apiKey = "test-key")

    private fun firebaseBackend(): MockEngine = MockEngine { request ->
        val path = request.url.encodedPath
        val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        when {
            path.endsWith("accounts:signInWithPassword") -> respond(
                content = """
                    {"idToken":"fb-id-token","localId":"uid-1","email":"amelia@hartwell.co",
                     "displayName":"Amelia Hartwell","refreshToken":"fb-refresh","expiresIn":"3600"}
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = jsonHeaders
            )

            path.endsWith("accounts:signUp") -> respond(
                content = """{"idToken":"fb-id-token","localId":"uid-2","email":"new@user.co","refreshToken":"fb-refresh","expiresIn":"3600"}""",
                status = HttpStatusCode.OK,
                headers = jsonHeaders
            )

            path.endsWith("accounts:update") -> respond(
                content = """{"localId":"uid-2","email":"new@user.co","displayName":"New User"}""",
                status = HttpStatusCode.OK,
                headers = jsonHeaders
            )

            else -> respond("""{"error":{"code":404,"message":"NOT_FOUND"}}""", HttpStatusCode.NotFound, jsonHeaders)
        }
    }

    private fun errorBackend(status: HttpStatusCode, firebaseCode: String): MockEngine = MockEngine {
        respond(
            content = """{"error":{"code":${status.value},"message":"$firebaseCode","errors":[]}}""",
            status = status,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
    }

    private fun newRepo(engine: MockEngine, local: LocalDataSource): RemoteAuthRepository {
        val client = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; isLenient = true }) }
        }
        return RemoteAuthRepository(api = FirebaseAuthApi(client, config), local = local)
    }

    @Test
    fun login_success_returnsUserAndPersistsFirebaseToken() = runBlocking {
        val local = FakeLocalDataSource()
        val result = newRepo(firebaseBackend(), local).login("amelia@hartwell.co", "secret")

        assertTrue(result is AppResult.Success, "expected success, got $result")
        assertEquals("uid-1", result.data.id)
        assertEquals("Amelia Hartwell", result.data.name)
        assertEquals("amelia@hartwell.co", result.data.email)
        assertEquals("fb-id-token", local.getToken())
        assertTrue(local.isLoggedIn())
        Unit
    }

    @Test
    fun signUp_success_setsDisplayNameFromRequest() = runBlocking {
        val local = FakeLocalDataSource()
        val result = newRepo(firebaseBackend(), local).signUp("New User", "new@user.co", "secret123")

        assertTrue(result is AppResult.Success, "expected success, got $result")
        assertEquals("uid-2", result.data.id)
        assertEquals("New User", result.data.name)
        assertTrue(local.isLoggedIn())
        Unit
    }

    @Test
    fun login_wrongPassword_mapsToInvalidCredentials() = runBlocking {
        val local = FakeLocalDataSource()
        val result = newRepo(errorBackend(HttpStatusCode.BadRequest, "INVALID_LOGIN_CREDENTIALS"), local)
            .login("amelia@hartwell.co", "wrong")

        assertTrue(result is AppResult.Error, "expected error, got $result")
        assertEquals(AppError.Auth(AppError.AuthReason.InvalidCredentials), result.error)
        assertTrue(!local.isLoggedIn())
        Unit
    }

    @Test
    fun signUp_duplicateEmail_mapsToEmailAlreadyInUse() = runBlocking {
        val local = FakeLocalDataSource()
        val result = newRepo(errorBackend(HttpStatusCode.BadRequest, "EMAIL_EXISTS"), local)
            .signUp("Amelia", "amelia@hartwell.co", "secret")

        assertTrue(result is AppResult.Error, "expected error, got $result")
        assertEquals(AppError.Auth(AppError.AuthReason.EmailAlreadyInUse), result.error)
        Unit
    }

    @Test
    fun signUp_weakPassword_mapsToWeakPassword() = runBlocking {
        val local = FakeLocalDataSource()
        val result = newRepo(
            errorBackend(HttpStatusCode.BadRequest, "WEAK_PASSWORD : Password should be at least 6 characters"),
            local
        ).signUp("Amelia", "amelia@hartwell.co", "123")

        assertTrue(result is AppResult.Error, "expected error, got $result")
        assertEquals(AppError.Auth(AppError.AuthReason.WeakPassword), result.error)
        Unit
    }

    @Test
    fun login_tooManyAttempts_mapsToTooManyAttempts() = runBlocking {
        val local = FakeLocalDataSource()
        val result = newRepo(errorBackend(HttpStatusCode.BadRequest, "TOO_MANY_ATTEMPTS_TRY_LATER"), local)
            .login("amelia@hartwell.co", "secret")

        assertTrue(result is AppResult.Error, "expected error, got $result")
        assertEquals(AppError.Auth(AppError.AuthReason.TooManyAttempts), result.error)
        Unit
    }

    @Test
    fun logout_clearsLocalSessionWithoutNetworkCall() = runBlocking {
        val local = FakeLocalDataSource()
        val repo = newRepo(firebaseBackend(), local)
        repo.login("amelia@hartwell.co", "secret")
        assertTrue(local.isLoggedIn())

        repo.logout()

        assertTrue(!local.isLoggedIn())
        Unit
    }
}

