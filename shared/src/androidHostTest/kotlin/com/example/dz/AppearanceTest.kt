package com.example.dz

import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSourceImpl
import com.example.dz.data.repository.LocalAppearanceRepository
import com.example.dz.domain.model.Appearance
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.repository.DeviceDataRepository
import com.example.dz.domain.usecase.account.DeleteAccountUseCase
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.presentation.settings.SettingsEvent
import com.example.dz.presentation.settings.SettingsViewModel
import com.russhwolf.settings.MapSettings
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * The Appearance row in Settings: the device's appearance until someone chooses otherwise, a
 * choice that holds across launches and through signing out, and a picker that applies what it is
 * given at once.
 */
class AppearanceTest {

    @BeforeTest
    fun installMainDispatcher() = Dispatchers.setMain(StandardTestDispatcher())

    @AfterTest
    fun removeMainDispatcher() = Dispatchers.resetMain()

    @Test
    fun `everyone starts by following the device`() {
        val appearances = LocalAppearanceRepository(FakeLocalDataSource())

        assertEquals(Appearance.SYSTEM, appearances.appearance.value)
    }

    @Test
    fun `a choice is still there the next time the app opens`() {
        val local = FakeLocalDataSource()
        LocalAppearanceRepository(local).setAppearance(Appearance.DARK)

        assertEquals(Appearance.DARK, LocalAppearanceRepository(local).appearance.value)
    }

    @Test
    fun `an appearance this version does not know follows the device instead of failing`() {
        val local = FakeLocalDataSource().apply {
            saveSetting(LocalAppearanceRepository.KEY, "SEPIA")
        }

        assertEquals(Appearance.SYSTEM, LocalAppearanceRepository(local).appearance.value)
    }

    @Test
    fun `signing out or deleting the account leaves the appearance alone`() {
        val local = LocalDataSourceImpl(MapSettings())
        LocalAppearanceRepository(local).setAppearance(Appearance.LIGHT)

        local.clearUserData()

        assertEquals(Appearance.LIGHT, LocalAppearanceRepository(local).appearance.value)
    }

    @Test
    fun `settings opens on the stored appearance`() {
        val appearances = LocalAppearanceRepository(FakeLocalDataSource()).apply {
            setAppearance(Appearance.LIGHT)
        }

        assertEquals(Appearance.LIGHT, settings(appearances).uiState.value.appearance)
    }

    @Test
    fun `choosing an appearance applies it and closes the picker`() {
        val appearances = LocalAppearanceRepository(FakeLocalDataSource())
        val viewModel = settings(appearances)

        viewModel.onEvent(SettingsEvent.AppearanceClicked)
        assertTrue(viewModel.uiState.value.isAppearancePickerVisible)

        viewModel.onEvent(SettingsEvent.AppearanceChosen(Appearance.DARK))

        assertEquals(Appearance.DARK, viewModel.uiState.value.appearance)
        assertFalse(viewModel.uiState.value.isAppearancePickerVisible)
        assertEquals(
            Appearance.DARK,
            appearances.appearance.value,
            "the app root draws from the repository, so the choice has to reach it",
        )
    }

    @Test
    fun `dismissing the picker changes nothing`() {
        val appearances = LocalAppearanceRepository(FakeLocalDataSource())
        val viewModel = settings(appearances)

        viewModel.onEvent(SettingsEvent.AppearanceClicked)
        viewModel.onEvent(SettingsEvent.AppearancePickerDismissed)

        assertFalse(viewModel.uiState.value.isAppearancePickerVisible)
        assertEquals(Appearance.SYSTEM, viewModel.uiState.value.appearance)
        assertEquals(Appearance.SYSTEM, appearances.appearance.value)
    }

    private fun settings(appearances: LocalAppearanceRepository) = SettingsViewModel(
        LogoutUseCase(NoAccount),
        DeleteAccountUseCase(NoAccount, NoDeviceData),
        appearances,
    )
}

/** Choosing an appearance never reaches the account, so every call here is a test gone wrong. */
private object NoAccount : AuthRepository {
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
    override suspend fun logout(): AppResult<Unit> = unused()
    override suspend fun deleteAccount(): AppResult<Unit> = unused()
    override suspend fun getCurrentUser(): AppResult<User?> = unused()

    private fun unused(): Nothing = error("not part of choosing an appearance")
}

private object NoDeviceData : DeviceDataRepository {
    override suspend fun eraseAccountData() = Unit
}
