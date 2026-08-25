package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.LoginUseCase
import com.example.dz.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.dz.domain.usecase.auth.SignUpUseCase
import com.example.dz.presentation.auth.login.LoginEffect
import com.example.dz.presentation.auth.login.LoginEvent
import com.example.dz.presentation.auth.login.LoginViewModel
import com.example.dz.presentation.auth.sign_up.SignUpEffect
import com.example.dz.presentation.auth.sign_up.SignUpEvent
import com.example.dz.presentation.auth.sign_up.SignUpViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

/**
 * The Google path across both auth screens.
 *
 * The account picker itself is platform code and is not exercised here; what these pin is
 * everything after it — that a token is spent exactly once, that a refusal is reported, and that
 * backing out of the picker is not treated as a failure.
 */
class GoogleSignInTest {

    @BeforeTest
    fun installMainDispatcher() = Dispatchers.setMain(StandardTestDispatcher())

    @AfterTest
    fun removeMainDispatcher() = Dispatchers.resetMain()

    private class RecordingAuthRepository(
        private val result: AppResult<User> =
            AppResult.Success(User(id = "u-1", name = "Ada", email = "ada@example.com"))
    ) : AuthRepository {
        var googleCalls = 0
            private set
        var lastToken: String? = null
            private set

        override suspend fun signInWithGoogle(idToken: String): AppResult<User> {
            googleCalls++
            lastToken = idToken
            return result
        }

        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)

        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)

        override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> =
            AppResult.Success(Unit)

        override suspend fun resendVerificationCode(email: String): AppResult<Unit> =
            AppResult.Success(Unit)

        override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(null)
    }

    private fun loginViewModel(repository: RecordingAuthRepository) =
        LoginViewModel(LoginUseCase(repository), SignInWithGoogleUseCase(repository))

    private fun signUpViewModel(repository: RecordingAuthRepository) =
        SignUpViewModel(SignUpUseCase(repository), SignInWithGoogleUseCase(repository))

    // ── Sign in ──────────────────────────────────────────────────────────────

    @Test
    fun `a Google token is exchanged and lands on home`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = loginViewModel(repository)

        val effects = mutableListOf<LoginEffect>()
        val collector = launch { viewModel.effects.collect { effects += it } }

        viewModel.onEvent(LoginEvent.GoogleTokenReceived("id-token-abc"))
        testScheduler.advanceUntilIdle()

        assertEquals(1, repository.googleCalls)
        assertEquals("id-token-abc", repository.lastToken, "the token must reach the server unaltered")
        assertTrue(effects.contains(LoginEffect.NavigateToHome))
        assertTrue(!viewModel.uiState.value.isLoading)
        collector.cancel()
    }

    @Test
    fun `a server refusal is shown rather than swallowed`() = runTest {
        val repository = RecordingAuthRepository(
            result = AppResult.Error(AppError.Auth(AppError.AuthReason.InvalidCredentials))
        )
        val viewModel = loginViewModel(repository)

        viewModel.onEvent(LoginEvent.GoogleTokenReceived("forged"))
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Email or password is incorrect.", state.errorMessage)
        assertTrue(!state.isLoading, "a refusal has to release the button")
    }

    @Test
    fun `backing out of the picker is not an error`() = runTest {
        val viewModel = loginViewModel(RecordingAuthRepository())

        viewModel.onEvent(LoginEvent.GoogleClicked)
        assertTrue(viewModel.uiState.value.isLoading, "the picker is opening; the button is busy")

        // Cancellation arrives as a null message — a choice, not a failure.
        viewModel.onEvent(LoginEvent.GoogleSignInFailed(null))
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(!state.isLoading, "dismissing the sheet must free the button")
        assertNull(state.errorMessage, "changing your mind is not something to apologise for")
    }

    @Test
    fun `a picker failure is reported`() = runTest {
        val viewModel = loginViewModel(RecordingAuthRepository())

        viewModel.onEvent(LoginEvent.GoogleClicked)
        viewModel.onEvent(LoginEvent.GoogleSignInFailed("No Google account on this device"))
        testScheduler.advanceUntilIdle()

        assertEquals(
            "No Google account on this device",
            viewModel.uiState.value.errorMessage
        )
    }

    // ── Sign up ──────────────────────────────────────────────────────────────

    @Test
    fun `Google sign-up skips code entry`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = signUpViewModel(repository)

        val effects = mutableListOf<SignUpEffect>()
        val collector = launch { viewModel.effects.collect { effects += it } }

        viewModel.onEvent(SignUpEvent.TermsToggled(accepted = true))
        viewModel.onEvent(SignUpEvent.GoogleTokenReceived("id-token-abc"))
        testScheduler.advanceUntilIdle()

        assertEquals(1, repository.googleCalls)
        // Google hands over an address it has already verified, so there is nothing left to prove.
        assertTrue(
            effects.contains(SignUpEffect.NavigateToHome),
            "a verified Google address should not be sent through the code screen"
        )
        collector.cancel()
    }

    @Test
    fun `Google sign-up refuses until the terms are accepted`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = signUpViewModel(repository)

        // The button is disabled in the UI, but the rule has to hold here too: signing up
        // through Google creates an account exactly as the form does.
        viewModel.onEvent(SignUpEvent.GoogleTokenReceived("id-token-abc"))
        testScheduler.advanceUntilIdle()
        assertEquals(0, repository.googleCalls, "an unticked agreement must not create an account")

        viewModel.onEvent(SignUpEvent.TermsToggled(accepted = true))
        viewModel.onEvent(SignUpEvent.GoogleTokenReceived("id-token-abc"))
        testScheduler.advanceUntilIdle()
        assertEquals(1, repository.googleCalls, "ticking the box unblocks the same token")
    }
}
