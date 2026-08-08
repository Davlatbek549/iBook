package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.GetCurrentUserUseCase
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.presentation.settings.SettingsEffect
import com.example.dz.presentation.settings.SettingsEvent
import com.example.dz.presentation.settings.SettingsViewModel
import com.example.dz.presentation.splash.SplashEffect
import com.example.dz.presentation.splash.SplashViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

/**
 * Covers what a stored session is actually worth: the splash restores it instead of asking for a
 * password again, and signing out disposes of it.
 *
 * These run on virtual time, so the splash's deliberate pause costs the suite nothing.
 */
class SessionRestoreTest {

    @BeforeTest
    fun installMainDispatcher() = Dispatchers.setMain(StandardTestDispatcher())

    @AfterTest
    fun removeMainDispatcher() = Dispatchers.resetMain()

    private class FakeAuthRepository(
        private var currentUser: User? = null,
        private val currentUserResult: AppResult<User?>? = null
    ) : AuthRepository {
        var logoutCalls = 0
            private set

        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)

        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)

        override suspend fun logout(): AppResult<Unit> {
            logoutCalls++
            currentUser = null
            return AppResult.Success(Unit)
        }

        override suspend fun getCurrentUser(): AppResult<User?> =
            currentUserResult ?: AppResult.Success(currentUser)
    }

    @Test
    fun `a stored session opens the app on home`() = runTest {
        val repository = FakeAuthRepository(currentUser = User(id = "u-1", name = "Ada", email = "ada@example.com"))
        val viewModel = SplashViewModel(GetCurrentUserUseCase(repository))

        assertEquals(SplashEffect.NavigateToHome, viewModel.effects.first())
    }

    @Test
    fun `no session falls through to onboarding`() = runTest {
        val viewModel = SplashViewModel(GetCurrentUserUseCase(FakeAuthRepository(currentUser = null)))

        assertEquals(SplashEffect.NavigateToOnboarding, viewModel.effects.first())
    }

    @Test
    fun `a broken session is not treated as a signed-in one`() = runTest {
        val repository = FakeAuthRepository(currentUserResult = AppResult.Error(AppError.Unauthorized))
        val viewModel = SplashViewModel(GetCurrentUserUseCase(repository))

        assertEquals(SplashEffect.NavigateToOnboarding, viewModel.effects.first())
    }

    @Test
    fun `signing out clears the session and returns to login`() = runTest {
        val repository = FakeAuthRepository(currentUser = User(id = "u-1", name = "Ada"))
        val viewModel = SettingsViewModel(LogoutUseCase(repository))

        viewModel.onEvent(SettingsEvent.SignOutClicked)

        assertEquals(SettingsEffect.NavigateToLogin, viewModel.effects.first())
        assertEquals(1, repository.logoutCalls, "sign out must reach the repository, not just navigate")
        assertTrue(repository.getCurrentUser().let { it is AppResult.Success && it.data == null })
    }
}
