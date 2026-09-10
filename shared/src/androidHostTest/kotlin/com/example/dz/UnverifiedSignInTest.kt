package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.LoginUseCase
import com.example.dz.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.dz.presentation.auth.login.LoginEffect
import com.example.dz.presentation.auth.login.LoginEvent
import com.example.dz.presentation.auth.login.LoginViewModel
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
 * Sign-in has to open a session where the splash would reopen it. It used to open Home whatever
 * the account, so signing in to an unverified one worked once and then, on the next launch, the
 * splash sent it to the code screen — which read as the splash misbehaving.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UnverifiedSignInTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    private class SignsInAs(private val user: User) : AuthRepository {
        override suspend fun login(email: String, password: String) = AppResult.Success(user)
        override suspend fun signInWithGoogle(idToken: String) = AppResult.Success(user)

        private fun <T> unused(): AppResult<T> = AppResult.Error(AppError.Unauthorized)
        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> = unused()
        override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> = unused()
        override suspend fun resendVerificationCode(email: String): AppResult<Unit> = unused()
        override suspend fun requestPasswordReset(email: String): AppResult<Unit> = unused()
        override suspend fun resetPassword(email: String, code: String, newPassword: String): AppResult<Unit> = unused()
        override suspend fun logout(): AppResult<Unit> = unused()
        override suspend fun getCurrentUser(): AppResult<User?> = unused()
    }

    private fun signingInAs(verified: Boolean): LoginViewModel {
        val repository = SignsInAs(
            User(id = "u-1", name = "Ada", email = "ada@example.com", emailVerified = verified)
        )
        return LoginViewModel(
            login = LoginUseCase(repository),
            signInWithGoogle = SignInWithGoogleUseCase(repository),
        )
    }

    private fun LoginViewModel.signIn() {
        onEvent(LoginEvent.EmailChanged("ada@example.com"))
        onEvent(LoginEvent.PasswordChanged("correct-horse"))
        onEvent(LoginEvent.SignInClicked)
    }

    @Test
    fun `an unverified account signs in to the code screen, not home`() = runTest(dispatcher) {
        val viewModel = signingInAs(verified = false)

        viewModel.signIn()

        assertEquals(LoginEffect.NavigateToVerification("ada@example.com"), viewModel.effects.first())
    }

    @Test
    fun `a verified account signs in to home`() = runTest(dispatcher) {
        val viewModel = signingInAs(verified = true)

        viewModel.signIn()

        assertEquals(LoginEffect.NavigateToHome, viewModel.effects.first())
    }

    @Test
    fun `google sign-in follows the same rule`() = runTest(dispatcher) {
        val viewModel = signingInAs(verified = false)

        viewModel.onEvent(LoginEvent.GoogleTokenReceived("id-token"))

        assertEquals(LoginEffect.NavigateToVerification("ada@example.com"), viewModel.effects.first())
    }
}
