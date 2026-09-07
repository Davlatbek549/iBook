package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.RequestPasswordResetUseCase
import com.example.dz.domain.usecase.auth.ResetPasswordUseCase
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordEffect
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordEvent
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.dz.presentation.auth.new_password.NewPasswordEffect
import com.example.dz.presentation.auth.new_password.NewPasswordEvent
import com.example.dz.presentation.auth.new_password.NewPasswordViewModel
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * The two ends of a password reset: asking for a code, and spending it on a new password.
 *
 * Both screens used to be theatre — they validated input and routed, and no request left the
 * device. These pin the wiring that replaced that, and in particular the two things that are easy
 * to get wrong once it is real: a refusal must not be mistaken for success, and a reset must not
 * pretend to sign anybody in.
 */
class PasswordResetTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    private class RecordingAuthRepository(
        private val requestResult: AppResult<Unit> = AppResult.Success(Unit),
        private val resetResult: AppResult<Unit> = AppResult.Success(Unit),
    ) : AuthRepository {
        var requestedFor: String? = null
            private set
        var resetWith: Triple<String, String, String>? = null
            private set
        var resetCalls = 0
            private set

        override suspend fun requestPasswordReset(email: String): AppResult<Unit> {
            requestedFor = email
            return requestResult
        }

        override suspend fun resetPassword(
            email: String,
            code: String,
            newPassword: String,
        ): AppResult<Unit> {
            resetCalls++
            resetWith = Triple(email, code, newPassword)
            return resetResult
        }

        override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> =
            AppResult.Success(Unit)
        override suspend fun resendVerificationCode(email: String): AppResult<Unit> =
            AppResult.Success(Unit)
        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signInWithGoogle(idToken: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(null)
    }

    private fun forgotViewModel(repository: RecordingAuthRepository) =
        ForgotPasswordViewModel(RequestPasswordResetUseCase(repository))

    private fun newPasswordViewModel(
        repository: RecordingAuthRepository,
        code: String = "123456",
    ) = NewPasswordViewModel(
        email = "ada@example.com",
        code = code,
        resetPassword = ResetPasswordUseCase(repository),
    )

    private fun ForgotPasswordViewModel.request(email: String) {
        onEvent(ForgotPasswordEvent.EmailChanged(email))
        onEvent(ForgotPasswordEvent.SendLinkClicked)
    }

    private fun NewPasswordViewModel.choose(password: String, confirmation: String = password) {
        onEvent(NewPasswordEvent.PasswordChanged(password))
        onEvent(NewPasswordEvent.ConfirmationChanged(confirmation))
        onEvent(NewPasswordEvent.SaveClicked)
    }

    // ── Asking for a code ────────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `asking for a code reaches the server and confirms in place`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = forgotViewModel(repository)

        viewModel.request("ada@example.com")
        testScheduler.advanceUntilIdle()

        assertEquals("ada@example.com", repository.requestedFor)
        assertEquals("ada@example.com", viewModel.uiState.value.sentTo)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a malformed address never reaches the server`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = forgotViewModel(repository)

        viewModel.request("not-an-address")
        testScheduler.advanceUntilIdle()

        // The deployed server sleeps between uses, so a typo would otherwise cost a minute
        // of waiting to be told what the shape already says.
        assertNull(repository.requestedFor)
        assertNotNull(viewModel.uiState.value.emailError)
        assertNull(viewModel.uiState.value.sentTo, "nothing was sent, so nothing is confirmed")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a failed request is not dressed up as a sent code`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository(
            requestResult = AppResult.Error(AppError.Network)
        )
        val viewModel = forgotViewModel(repository)

        viewModel.request("ada@example.com")
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.sentTo, "a code that was never sent must not be confirmed")
        assertNotNull(state.errorMessage)
        assertFalse(state.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `editing the address withdraws the confirmation`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = forgotViewModel(repository)
        viewModel.request("ada@example.com")
        testScheduler.advanceUntilIdle()

        viewModel.onEvent(ForgotPasswordEvent.EmailChanged("ada@example.co"))

        // The code went to the address that was there before, so the confirmation no longer
        // describes anything true.
        assertNull(viewModel.uiState.value.sentTo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `moving on to the code screen carries the address that was mailed`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = forgotViewModel(repository)
        viewModel.request("  ada@example.com  ")
        testScheduler.advanceUntilIdle()

        viewModel.onEvent(ForgotPasswordEvent.ContinueClicked)
        val effect = viewModel.effects.first()

        assertEquals(ForgotPasswordEffect.NavigateToVerification("ada@example.com"), effect)
    }

    // ── Spending it ──────────────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `saving sends the code and the new password together`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = newPasswordViewModel(repository, code = "654321")

        viewModel.choose("a-long-enough-password")
        testScheduler.advanceUntilIdle()

        assertEquals(
            Triple("ada@example.com", "654321", "a-long-enough-password"),
            repository.resetWith,
        )
        assertTrue(viewModel.uiState.value.isSaved)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a saved reset ends at sign-in, not at a home nobody is signed in to`() =
        runTest(dispatcher) {
            val repository = RecordingAuthRepository()
            val viewModel = newPasswordViewModel(repository)
            viewModel.choose("a-long-enough-password")
            testScheduler.advanceUntilIdle()

            viewModel.onEvent(NewPasswordEvent.SignInClicked)
            val effect = viewModel.effects.first()

            // The server issues no session for a reset and revokes the ones that existed, so
            // opening Home would be a signed-in shell with no session behind it.
            assertEquals(NewPasswordEffect.NavigateToLogin, effect)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `mismatched passwords never reach the server`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = newPasswordViewModel(repository)

        viewModel.choose("a-long-enough-password", confirmation = "a-different-password")
        testScheduler.advanceUntilIdle()

        assertNull(repository.resetWith)
        assertNotNull(viewModel.uiState.value.confirmationError)
        assertFalse(viewModel.uiState.value.isSaved)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a short password never reaches the server`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = newPasswordViewModel(repository)

        viewModel.choose("short")
        testScheduler.advanceUntilIdle()

        assertNull(repository.resetWith)
        assertNotNull(viewModel.uiState.value.confirmationError)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a refused code is reported here, because this is where it is spent`() =
        runTest(dispatcher) {
            val repository = RecordingAuthRepository(
                resetResult = AppResult.Error(
                    AppError.Auth(AppError.AuthReason.InvalidCredentials)
                )
            )
            val viewModel = newPasswordViewModel(repository)

            viewModel.choose("a-long-enough-password")
            testScheduler.advanceUntilIdle()

            // The code screen carried the code without checking it, so a wrong or expired one
            // surfaces at this screen and nowhere else.
            val state = viewModel.uiState.value
            assertNotNull(state.errorMessage)
            assertFalse(state.isSaved, "a refusal must not look like a completed reset")
            assertFalse(state.isLoading)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `saving twice cannot spend the code twice`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = newPasswordViewModel(repository)
        viewModel.choose("a-long-enough-password")
        testScheduler.advanceUntilIdle()

        viewModel.onEvent(NewPasswordEvent.SaveClicked)
        testScheduler.advanceUntilIdle()

        // The code is spent; a second attempt could only be refused, and the reader has already
        // been told it worked.
        assertEquals(1, repository.resetCalls, "the second save must not reach the server")
        assertTrue(viewModel.uiState.value.isSaved)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `back leads to sign-in once the password has changed`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = newPasswordViewModel(repository)
        viewModel.choose("a-long-enough-password")
        testScheduler.advanceUntilIdle()

        viewModel.onEvent(NewPasswordEvent.BackClicked)

        // Popping would land on the code screen holding a code this reset has already spent —
        // the dead end the success path clears the stack to avoid.
        assertEquals(NewPasswordEffect.NavigateToLogin, viewModel.effects.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `back still returns to the code screen before the password has changed`() =
        runTest(dispatcher) {
            val viewModel = newPasswordViewModel(RecordingAuthRepository())

            viewModel.onEvent(NewPasswordEvent.BackClicked)

            // Nothing has been spent yet, so a mistyped code is still worth going back to fix.
            assertEquals(NewPasswordEffect.NavigateBack, viewModel.effects.first())
        }
}
