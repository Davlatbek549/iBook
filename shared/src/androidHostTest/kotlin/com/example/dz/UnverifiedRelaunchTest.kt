package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.GetCurrentUserUseCase
import com.example.dz.presentation.splash.SplashEffect
import com.example.dz.presentation.splash.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Sign-up issues a session before the code is spent, so the reader can hold one while they read
 * their mail. That made verifying skippable: killing the app during verification and reopening it
 * found a session and went straight to Home.
 *
 * The server refuses everything else until the address is proven, so opening Home in that state
 * would show a shell whose every request comes back refused.
 */
class UnverifiedRelaunchTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    private class StoredSession(private val user: User?) : AuthRepository {
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(user)
        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signInWithGoogle(idToken: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> =
            AppResult.Success(Unit)
        override suspend fun resendVerificationCode(email: String): AppResult<Unit> =
            AppResult.Success(Unit)

        override suspend fun requestPasswordReset(email: String): AppResult<Unit> =
            AppResult.Success(Unit)

        override suspend fun resetPassword(
            email: String,
            code: String,
            newPassword: String,
        ): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)
    }

    private fun splashFor(user: User?) =
        SplashViewModel(GetCurrentUserUseCase(StoredSession(user)))

    private fun user(verified: Boolean) = User(
        id = "u-1",
        name = "Ada Lovelace",
        email = "ada@example.com",
        emailVerified = verified,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `an unverified session reopens on the code screen, not on home`() = runTest(dispatcher) {
        val viewModel = splashFor(user(verified = false))

        val effect = viewModel.effects.first()

        assertEquals(SplashEffect.NavigateToVerification("ada@example.com"), effect)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a verified session still opens on home`() = runTest(dispatcher) {
        val viewModel = splashFor(user(verified = true))

        val effect = viewModel.effects.first()

        assertEquals(SplashEffect.NavigateToHome, effect)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `no session still starts from the top`() = runTest(dispatcher) {
        val viewModel = splashFor(null)

        val effect = viewModel.effects.first()

        assertEquals(SplashEffect.NavigateToOnboarding, effect)
    }
}
