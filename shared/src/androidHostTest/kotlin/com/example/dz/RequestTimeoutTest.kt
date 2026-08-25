package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.remote.api.KtorAuthApi
import com.example.dz.data.repository.RemoteAuthRepository
import com.example.dz.presentation.mvi.toPresentationMessage
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * A request that runs out of time is not a connection failure.
 *
 * The distinction is not cosmetic. The server is hosted on a tier that sleeps, so a cold start
 * can outlast any timeout worth setting — and when it does, the server still finishes the work.
 * Telling the reader to check their connection sends them to look at something that is working,
 * and hides that their sign-up may well have succeeded.
 */
class RequestTimeoutTest {

    private fun repositoryThatTimesOut(): RemoteAuthRepository {
        val engine = MockEngine { request ->
            throw HttpRequestTimeoutException(request.url.toString(), 150_000L)
        }
        return RemoteAuthRepository(
            api = KtorAuthApi(
                client = HttpClient(engine) {
                    expectSuccess = true
                    install(ContentNegotiation) {
                        json(Json { ignoreUnknownKeys = true; isLenient = true })
                    }
                },
                baseUrl = "http://localhost:8080/api/v1"
            ),
            local = FakeLocalDataSource()
        )
    }

    @Test
    fun `a sign-up that runs out of time reports a timeout, not a network failure`() = runBlocking {
        val result = repositoryThatTimesOut().signUp("Ada Lovelace", "ada@example.com", "correct-horse")

        assertTrue(result is AppResult.Error, "expected an error but was $result")
        assertEquals(AppError.Timeout, result.error)
    }

    @Test
    fun `a login that runs out of time reports a timeout too`() = runBlocking {
        val result = repositoryThatTimesOut().login("ada@example.com", "correct-horse")

        assertTrue(result is AppResult.Error, "expected an error but was $result")
        assertEquals(AppError.Timeout, result.error)
    }

    @Test
    fun `the message does not blame the connection or claim the request failed`() {
        val message = AppError.Timeout.toPresentationMessage()

        assertTrue(message.isNotBlank())
        assertTrue(
            !message.contains("connection", ignoreCase = true),
            "a timeout must not send the reader to check a connection that is fine: $message"
        )
    }
}
