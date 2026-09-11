package com.example.dz

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.dz.core.auth.MailedCodeKind
import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.CollectionLocalDataSource
import com.example.dz.data.local.LibraryLocalDataSource
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.local.LocalDataSourceImpl
import com.example.dz.data.remote.api.KtorAuthApi
import com.example.dz.data.repository.LocalDeviceDataRepository
import com.example.dz.data.repository.RemoteAuthRepository
import com.example.dz.database.DzDatabase
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.repository.DeviceDataRepository
import com.example.dz.domain.usecase.account.DeleteAccountUseCase
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.presentation.settings.SettingsEffect
import com.example.dz.presentation.settings.SettingsEvent
import com.example.dz.presentation.settings.SettingsViewModel
import com.russhwolf.settings.MapSettings
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Deleting an account from Settings: the server is asked first, and only its answer decides what
 * this device then forgets — everything, or nothing at all.
 */
class AccountDeletionTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    // ── The request ──────────────────────────────────────────────────────────

    private var lastMethod: HttpMethod? = null
    private var lastPath: String? = null

    private fun server(status: HttpStatusCode) = MockEngine { request ->
        lastMethod = request.method
        lastPath = request.url.encodedPath
        respond("", status)
    }

    private fun repository(engine: MockEngine, local: LocalDataSource) = RemoteAuthRepository(
        KtorAuthApi(HttpClient(engine) { expectSuccess = true }, "http://10.0.2.2:8080/api/v1"),
        local,
    )

    private fun signedIn() = LocalDataSourceImpl(MapSettings()).apply {
        saveUserSession(
            userId = "u-1",
            name = "Ada",
            email = "ada@example.com",
            token = "access",
            refreshToken = "refresh",
            emailVerified = true,
        )
    }

    @Test
    fun `a deletion the server confirms ends the session on this device`() = runTest(dispatcher) {
        val local = signedIn()

        val result = repository(server(HttpStatusCode.NoContent), local).deleteAccount()

        assertTrue(result is AppResult.Success)
        assertEquals(HttpMethod.Delete, lastMethod)
        assertEquals("/api/v1/users/me", lastPath)
        assertFalse(local.isLoggedIn(), "the account is gone, and a session for it is worth nothing")
        assertNull(local.getToken())
    }

    @Test
    fun `a deletion the server refuses leaves the reader signed in`() = runTest(dispatcher) {
        val local = signedIn()

        val result = repository(server(HttpStatusCode.ServiceUnavailable), local).deleteAccount()

        // The account still exists, so saying otherwise — by signing the reader out — would be
        // untrue, and would take away the session they need to try again.
        assertTrue(result is AppResult.Error)
        assertTrue(local.isLoggedIn())
        assertEquals("access", local.getToken())
    }

    // ── What the device forgets ──────────────────────────────────────────────

    private fun newDatabase(): DzDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DzDatabase.Schema.create(driver)
        return DzDatabase(driver)
    }

    @Test
    fun `erasing takes the library, collections, downloads and account settings`() =
        runTest(dispatcher) {
            val database = newDatabase()
            val library = LibraryLocalDataSource(database)
            val collections = CollectionLocalDataSource(database)
            val files = FakeFileStorage()
            val local = signedIn().apply {
                saveSetting("profile_books_read", "12")
                saveSetting("purchase_status_b1", "Success")
                saveSetting(MailedCodeKind.EmailVerification.lastSentSettingKey, "1")
                setOnboardingCompleted(true)
                saveSetting("unrelated_to_any_account", "kept")
            }

            val book = Book(id = "b1", title = "Middlemarch")
            library.upsert(LibraryBook(book = book), addedAt = 1L)
            library.setDownload("b1", downloaded = true, path = files.save("b1", "text"))
            collections.create(Collection(id = "c1", title = "Favourites", books = listOf(book)), 1L)

            LocalDeviceDataRepository(library, collections, files, local).eraseAccountData()

            assertTrue(library.getLibraryBooks().isEmpty())
            assertTrue(collections.getCollections().isEmpty())
            assertTrue(files.files.isEmpty(), "a downloaded book is the account's too")
            assertFalse(local.isLoggedIn())
            assertEquals("", local.getSetting("profile_books_read"))
            assertEquals("", local.getSetting("purchase_status_b1"))
            assertEquals("", local.getSetting(MailedCodeKind.EmailVerification.lastSentSettingKey))
            // The device's own history is not the account's to take.
            assertTrue(local.isOnboardingCompleted(), "the next reader here has seen onboarding too")
            assertEquals("kept", local.getSetting("unrelated_to_any_account"))
        }

    // ── The use case and the screen ──────────────────────────────────────────

    private class DeletingAuthRepository(
        private val result: AppResult<Unit> = AppResult.Success(Unit),
        /** When set, deletion waits on it — so a test can act while the request is out. */
        private val gate: CompletableDeferred<Unit>? = null,
    ) : AuthRepository {
        var deleteCalls = 0
            private set

        override suspend fun deleteAccount(): AppResult<Unit> {
            deleteCalls++
            gate?.await()
            return result
        }

        override suspend fun login(email: String, password: String): AppResult<User> = unused()
        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            unused()
        override suspend fun signInWithGoogle(idToken: String): AppResult<User> = unused()
        override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> = unused()
        override suspend fun resendVerificationCode(email: String): AppResult<Unit> = unused()
        override suspend fun requestPasswordReset(email: String): AppResult<Unit> = unused()
        override suspend fun resetPassword(
            email: String,
            code: String,
            newPassword: String,
        ): AppResult<Unit> = unused()
        override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getCurrentUser(): AppResult<User?> = unused()

        private fun unused(): Nothing = error("not part of deleting an account")
    }

    private class RecordingDeviceData : DeviceDataRepository {
        var erased = false
            private set

        override suspend fun eraseAccountData() {
            erased = true
        }
    }

    private fun settings(repository: AuthRepository, deviceData: DeviceDataRepository) =
        SettingsViewModel(LogoutUseCase(repository), DeleteAccountUseCase(repository, deviceData))

    @Test
    fun `the device is only erased once the server has deleted the account`() = runTest(dispatcher) {
        val deviceData = RecordingDeviceData()

        DeleteAccountUseCase(
            DeletingAuthRepository(result = AppResult.Error(AppError.Network)),
            deviceData,
        ).invoke()

        assertFalse(deviceData.erased, "a failed deletion must leave the library for the next try")
    }

    @Test
    fun `nothing is deleted until the reader confirms`() = runTest(dispatcher) {
        val repository = DeletingAuthRepository()
        val viewModel = settings(repository, RecordingDeviceData())

        viewModel.onEvent(SettingsEvent.DeleteAccountClicked)
        testScheduler.runCurrent()
        assertTrue(viewModel.uiState.value.isDeleteConfirmationVisible)
        assertEquals(0, repository.deleteCalls, "the row only asks; it does not delete")

        viewModel.onEvent(SettingsEvent.DeleteAccountDismissed)
        testScheduler.runCurrent()
        assertFalse(viewModel.uiState.value.isDeleteConfirmationVisible)
        assertEquals(0, repository.deleteCalls)
    }

    @Test
    fun `a confirmed deletion erases the device and leaves for sign-in`() = runTest(dispatcher) {
        val repository = DeletingAuthRepository()
        val deviceData = RecordingDeviceData()
        val viewModel = settings(repository, deviceData)

        viewModel.onEvent(SettingsEvent.DeleteAccountClicked)
        viewModel.onEvent(SettingsEvent.DeleteAccountConfirmed)

        assertEquals(SettingsEffect.NavigateToLogin, viewModel.effects.first())
        assertEquals(1, repository.deleteCalls)
        assertTrue(deviceData.erased)
    }

    @Test
    fun `a failed deletion says why and stays for another try`() = runTest(dispatcher) {
        val repository = DeletingAuthRepository(result = AppResult.Error(AppError.Network))
        val deviceData = RecordingDeviceData()
        val viewModel = settings(repository, deviceData)
        val effects = mutableListOf<SettingsEffect>()
        val collector = launch { viewModel.effects.collect { effects += it } }

        viewModel.onEvent(SettingsEvent.DeleteAccountClicked)
        viewModel.onEvent(SettingsEvent.DeleteAccountConfirmed)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.deleteAccountError, "a failure has to be shown")
        assertTrue(state.isDeleteConfirmationVisible, "the dialog stays, so trying again is one tap")
        assertFalse(state.isDeletingAccount)
        assertTrue(effects.isEmpty(), "the account still exists; there is nowhere to leave for")
        assertFalse(deviceData.erased)
        collector.cancel()
    }

    @Test
    fun `a refused session is told to sign in again, not to check the connection`() =
        runTest(dispatcher) {
            val viewModel = settings(
                DeletingAuthRepository(result = AppResult.Error(AppError.Unauthorized)),
                RecordingDeviceData(),
            )

            viewModel.onEvent(SettingsEvent.DeleteAccountClicked)
            viewModel.onEvent(SettingsEvent.DeleteAccountConfirmed)
            testScheduler.advanceUntilIdle()

            assertEquals(
                "Your session has ended. Sign in again to delete your account.",
                viewModel.uiState.value.deleteAccountError,
            )
        }

    @Test
    fun `the dialog cannot be walked away from while the request is out`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val repository = DeletingAuthRepository(gate = gate)
        val viewModel = settings(repository, RecordingDeviceData())

        viewModel.onEvent(SettingsEvent.DeleteAccountClicked)
        viewModel.onEvent(SettingsEvent.DeleteAccountConfirmed)
        testScheduler.runCurrent()
        assertTrue(viewModel.uiState.value.isDeletingAccount)

        // Closing now would leave the reader not knowing whether the account went.
        viewModel.onEvent(SettingsEvent.DeleteAccountDismissed)
        viewModel.onEvent(SettingsEvent.DeleteAccountConfirmed)
        viewModel.onEvent(SettingsEvent.SignOutClicked)
        testScheduler.runCurrent()

        assertTrue(viewModel.uiState.value.isDeleteConfirmationVisible)
        assertEquals(1, repository.deleteCalls, "a second tap must not send a second request")
        assertFalse(viewModel.uiState.value.isSigningOut, "signing out mid-deletion would race it")

        gate.complete(Unit)
        assertEquals(SettingsEffect.NavigateToLogin, viewModel.effects.first())
    }
}
