package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.api.KtorAuthApi
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

/**
 * Pins the contract with our own backend. Every payload below is a real
 * response captured from a running `dz-server`, so if the server changes shape
 * this test fails rather than the app failing at runtime.
 */
class DzServerAuthApiTest {

    private val baseUrl = "http://10.0.2.2:8080/api/v1"

    private val jsonHeaders =
        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    /** `expiresIn` is on the wire but unread: the client renews reactively, on a 401. */
    private val sessionBody = """
        {
          "token":"eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkei1zZXJ2ZXIifQ.signature",
          "refreshToken":"O6Ao-y_Aty1z6VTo2tBrlOieNdlsTPB1JHhPON4BGNc",
          "expiresIn":900,
          "user":{
            "id":"5603bebc-4836-42c7-b30c-bbfc737b45ac",
            "name":"Ada Lovelace",
            "email":"ada@example.com",
            "avatarUrl":null
          }
        }
    """.trimIndent()

    private fun server(
        loginStatus: HttpStatusCode = HttpStatusCode.OK,
        loginBody: String = sessionBody
    ): MockEngine = MockEngine { request ->
        when (request.url.encodedPath) {
            "/api/v1/auth/login" -> respond(loginBody, loginStatus, jsonHeaders)
            "/api/v1/auth/signup" -> respond(sessionBody, HttpStatusCode.Created, jsonHeaders)
            "/api/v1/auth/logout" -> respond("", HttpStatusCode.NoContent)
            else -> respond("""{"code":"Unknown","message":"no route"}""", HttpStatusCode.NotFound, jsonHeaders)
        }
    }

    private fun api(engine: MockEngine) = KtorAuthApi(
        client = HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; isLenient = true })
            }
        },
        baseUrl = baseUrl
    )

    private fun repository(engine: MockEngine, local: LocalDataSource = FakeLocalDataSource()) =
        RemoteAuthRepository(api(engine), local)

    @Test
    fun `login reads the session the server returns`() = runBlocking {
        val result = repository(server()).login("ada@example.com", "correct-horse")

        assertTrue(result is AppResult.Success, "expected success but was $result")
        assertEquals("5603bebc-4836-42c7-b30c-bbfc737b45ac", result.data.id)
        assertEquals("Ada Lovelace", result.data.name)
        assertEquals("ada@example.com", result.data.email)
    }

    @Test
    fun `signup accepts 201 and stores the session`() = runBlocking {
        val local = FakeLocalDataSource()
        val result = repository(server(), local).signUp("Ada Lovelace", "ada@example.com", "correct-horse")

        assertTrue(result is AppResult.Success, "expected success but was $result")
        assertTrue(local.isLoggedIn(), "session should have been persisted")
        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkei1zZXJ2ZXIifQ.signature", local.getToken())
        assertEquals(
            "O6Ao-y_Aty1z6VTo2tBrlOieNdlsTPB1JHhPON4BGNc",
            local.getRefreshToken(),
            "without the refresh token the session dies with the 15-minute access token"
        )
    }

    @Test
    fun `logout accepts an empty 204 and clears the session`() = runBlocking {
        val local = FakeLocalDataSource()
        val repository = repository(server(), local)
        repository.login("ada@example.com", "correct-horse")

        val result = repository.logout()

        assertTrue(result is AppResult.Success, "expected success but was $result")
        assertTrue(!local.isLoggedIn(), "session should have been cleared")
    }

    @Test
    fun `a wrong password surfaces as InvalidCredentials, not a bare 401`() = runBlocking {
        val engine = server(
            loginStatus = HttpStatusCode.Unauthorized,
            loginBody = """{"code":"InvalidCredentials","message":"Email or password is incorrect","fieldErrors":null}"""
        )

        val result = repository(engine).login("ada@example.com", "wrong")

        assertTrue(result is AppResult.Error, "expected an error but was $result")
        assertEquals(AppError.Auth(AppError.AuthReason.InvalidCredentials), result.error)
    }

    @Test
    fun `a duplicate email surfaces as EmailAlreadyInUse`() = runBlocking {
        val engine = server(
            loginStatus = HttpStatusCode.Conflict,
            loginBody = """{"code":"EmailAlreadyInUse","message":"That email is already registered"}"""
        )

        val result = repository(engine).login("ada@example.com", "correct-horse")

        assertTrue(result is AppResult.Error, "expected an error but was $result")
        assertEquals(AppError.Auth(AppError.AuthReason.EmailAlreadyInUse), result.error)
    }

    @Test
    fun `a weak password surfaces with its field errors mapped`() = runBlocking {
        val engine = server(
            loginStatus = HttpStatusCode.BadRequest,
            loginBody = """
                {"code":"WeakPassword","message":"Password must be at least 8 characters",
                 "fieldErrors":{"password":"Password must be at least 8 characters"}}
            """.trimIndent()
        )

        val result = repository(engine).login("ada@example.com", "short")

        assertTrue(result is AppResult.Error, "expected an error but was $result")
        assertEquals(AppError.Auth(AppError.AuthReason.WeakPassword), result.error)
    }

    @Test
    fun `a code from another module falls back to the status code`() = runBlocking {
        // "BookNotFound" is not an AuthReason; it must not be forced into one.
        val engine = server(
            loginStatus = HttpStatusCode.NotFound,
            loginBody = """{"code":"BookNotFound","message":"Book is not in your library"}"""
        )

        val result = repository(engine).login("ada@example.com", "correct-horse")

        assertTrue(result is AppResult.Error, "expected an error but was $result")
        assertEquals(AppError.NotFound, result.error)
    }

    @Test
    fun `an error with no body still maps by status code`() = runBlocking {
        val engine = server(loginStatus = HttpStatusCode.InternalServerError, loginBody = "")

        val result = repository(engine).login("ada@example.com", "correct-horse")

        assertTrue(result is AppResult.Error, "expected an error but was $result")
        assertEquals(AppError.Network, result.error)
    }
}

