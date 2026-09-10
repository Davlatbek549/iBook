package com.example.dz

import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSourceImpl
import com.example.dz.data.remote.api.KtorAuthApi
import com.example.dz.data.repository.RemoteAuthRepository
import com.example.dz.domain.usecase.auth.GetCurrentUserUseCase
import com.example.dz.presentation.splash.SplashEffect
import com.example.dz.presentation.splash.SplashViewModel
import com.russhwolf.settings.MapSettings
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Where the app reopens after signing in, end to end: the real repository, the real on-device
 * store, and the splash's real decision, against a server that answers as dz-server does.
 *
 * The other auth tests stand in a fake for the store. These use the one that ships, because the
 * question they answer — "does the app remember that I verified?" — lives in it.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class VerificationPersistenceTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    private val json = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

    /** A session as dz-server returns it; `verifiedField` is the raw JSON, or null to omit it. */
    private fun session(verifiedField: String?) = """
        {"token":"t","refreshToken":"r","expiresIn":900,
         "user":{"id":"u-1","name":"Ada","email":"ada@example.com","avatarUrl":null
                 ${verifiedField?.let { ",\"emailVerified\":$it" } ?: ""}}}
    """.trimIndent()

    private fun server(loginVerified: String? = "true") = MockEngine { request ->
        when (request.url.encodedPath) {
            "/api/v1/auth/login" -> respond(session(loginVerified), HttpStatusCode.OK, json)
            "/api/v1/auth/signup" -> respond(session("false"), HttpStatusCode.Created, json)
            "/api/v1/auth/verify", "/api/v1/auth/verify/resend" -> respond("", HttpStatusCode.NoContent)
            else -> respond("", HttpStatusCode.NotFound)
        }
    }

    private fun repository(engine: MockEngine, local: LocalDataSourceImpl) = RemoteAuthRepository(
        KtorAuthApi(
            client = HttpClient(engine) {
                expectSuccess = true
                install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; isLenient = true }) }
            },
            baseUrl = "http://localhost/api/v1",
        ),
        local,
    )

    /** What a relaunch would open on, given what is in the store now. */
    private suspend fun reopen(repository: RemoteAuthRepository, local: LocalDataSourceImpl) =
        SplashViewModel(GetCurrentUserUseCase(repository), local).effects.first()

    @Test
    fun `a verified account signs in and reopens on home`() = runTest(dispatcher) {
        val local = LocalDataSourceImpl(MapSettings())
        val repository = repository(server(loginVerified = "true"), local)

        assertTrue(repository.login("ada@example.com", "correct-horse") is AppResult.Success)

        assertEquals(SplashEffect.NavigateToHome, reopen(repository, local))
    }

    @Test
    fun `an unverified account reopens on the code screen until its code is spent`() =
        runTest(dispatcher) {
            val local = LocalDataSourceImpl(MapSettings())
            val repository = repository(server(), local)

            repository.signUp("Ada", "ada@example.com", "correct-horse")
            assertEquals(SplashEffect.NavigateToVerification("ada@example.com"), reopen(repository, local))

            assertTrue(repository.verifyEmail("ada@example.com", "123456") is AppResult.Success)

            // Remembered on the device: the next launch does not ask again.
            assertEquals(SplashEffect.NavigateToHome, reopen(repository, local))
        }

    @Test
    fun `a server that does not mention verification traps no one on the code screen`() =
        runTest(dispatcher) {
            // What an older dz-server sends: the field arrived with the change that began refusing
            // unverified accounts, so its absence means verification is not in force at all.
            val local = LocalDataSourceImpl(MapSettings())
            val repository = repository(server(loginVerified = null), local)

            repository.login("ada@example.com", "correct-horse")

            assertEquals(SplashEffect.NavigateToHome, reopen(repository, local))
        }
}
