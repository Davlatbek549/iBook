package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.remote.api.ApiConfig
import com.example.dz.data.remote.api.KtorAuthApi
import com.example.dz.data.remote.api.createAuthHttpClient
import com.example.dz.data.repository.RemoteAuthRepository
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Exercises the full auth networking path (repository → Ktor API → mock engine → DTO
 * (de)serialization → session persistence) against the in-memory mock backend.
 */
class RemoteAuthRepositoryTest {

    private fun newRepo(local: LocalDataSource): RemoteAuthRepository {
        val config = ApiConfig(useMockBackend = true)
        val client = createAuthHttpClient(local = local, config = config)
        val api = KtorAuthApi(client = client, baseUrl = config.baseUrl)
        return RemoteAuthRepository(api = api, local = local)
    }

    @Test
    fun login_withValidCredentials_returnsUserAndPersistsSession() = runBlocking {
        val local = FakeLocalDataSource()
        val repo = newRepo(local)

        val result = repo.login("amelia@hartwell.co", "secret")

        assertTrue(result is AppResult.Success, "expected success, got $result")
        assertEquals("amelia@hartwell.co", result.data.email)
        assertTrue(local.isLoggedIn())
        assertNotNull(local.getToken())
        Unit
    }

    @Test
    fun login_withBlankCredentials_returnsUnauthorized() = runBlocking {
        val local = FakeLocalDataSource()
        val repo = newRepo(local)

        val result = repo.login("", "")

        assertTrue(result is AppResult.Error, "expected error, got $result")
        assertEquals(AppError.Unauthorized, result.error)
        assertTrue(!local.isLoggedIn())
        Unit
    }

    @Test
    fun signUp_returnsUserWithProvidedName() = runBlocking {
        val local = FakeLocalDataSource()
        val repo = newRepo(local)

        val result = repo.signUp("Amelia Hartwell", "amelia@hartwell.co", "secret")

        assertTrue(result is AppResult.Success, "expected success, got $result")
        assertEquals("Amelia Hartwell", result.data.name)
        assertTrue(local.isLoggedIn())
        Unit
    }

    @Test
    fun logout_clearsSession() = runBlocking {
        val local = FakeLocalDataSource()
        val repo = newRepo(local)
        repo.login("amelia@hartwell.co", "secret")
        assertTrue(local.isLoggedIn())

        repo.logout()

        assertTrue(!local.isLoggedIn())
        Unit
    }
}

