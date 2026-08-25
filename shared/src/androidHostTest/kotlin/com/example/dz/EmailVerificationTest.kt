package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.ResendVerificationCodeUseCase
import com.example.dz.domain.usecase.auth.VerifyEmailUseCase
import com.example.dz.presentation.auth.verification.VerificationEffect
import com.example.dz.presentation.auth.verification.VerificationEvent
import com.example.dz.presentation.auth.verification.VERIFICATION_RESEND_SECONDS
import com.example.dz.presentation.auth.verification.VerificationPurpose
import com.example.dz.presentation.auth.verification.VerificationViewModel
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * The code screen used to route on any six digits, because there was no endpoint behind it. These
 * pin the wiring that replaced that: a code is now spent against the server, and a refusal stops
 * the reader rather than letting them through.
 */
class EmailVerificationTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    private class RecordingAuthRepository(
        private val verifyResult: AppResult<Unit> = AppResult.Success(Unit),
        private val resendResult: AppResult<Unit> = AppResult.Success(Unit),
    ) : AuthRepository {
        var verifiedWith: Pair<String, String>? = null
            private set
        var resendCalls = 0
            private set

        override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> {
            verifiedWith = email to code
            return verifyResult
        }

        override suspend fun resendVerificationCode(email: String): AppResult<Unit> {
            resendCalls++
            return resendResult
        }

        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signInWithGoogle(idToken: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(null)
    }

    private fun viewModel(
        repository: RecordingAuthRepository,
        purpose: VerificationPurpose = VerificationPurpose.VerifyEmail,
    ) = VerificationViewModel(
        email = "ada@example.com",
        purpose = purpose,
        verifyEmail = VerifyEmailUseCase(repository),
        resendCode = ResendVerificationCodeUseCase(repository),
    )

    private fun VerificationViewModel.enter(code: String) {
        onEvent(VerificationEvent.CodeChanged(code))
        onEvent(VerificationEvent.VerifyClicked)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a correct code is spent against the server and opens home`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = viewModel(repository)

        viewModel.enter("123456")
        val effect = viewModel.effects.first()

        assertEquals("ada@example.com" to "123456", repository.verifiedWith)
        assertEquals(VerificationEffect.NavigateToHome, effect)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a refused code stops the reader instead of letting them through`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository(
            verifyResult = AppResult.Error(AppError.Auth(AppError.AuthReason.InvalidCredentials))
        )
        val viewModel = viewModel(repository)

        viewModel.enter("000000")
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.errorMessage, "a refusal has to be shown")
        assertEquals("", state.code, "the box is cleared so the code can be retyped")
        assertTrue(!state.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `an incomplete code never reaches the server`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = viewModel(repository)

        viewModel.enter("123")
        testScheduler.advanceUntilIdle()

        assertNull(repository.verifiedWith, "five digits is not a code worth spending a request on")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `resend asks the server and restarts the countdown`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = viewModel(repository)

        // The button is dead until the countdown runs out.
        viewModel.onEvent(VerificationEvent.ResendClicked)
        testScheduler.runCurrent()
        assertEquals(0, repository.resendCalls, "resending before the timer expires must not send")

        // Time is advanced deliberately rather than with advanceUntilIdle, which would drain the
        // countdown that restarts at the end and leave nothing to assert about.
        testScheduler.advanceTimeBy(VERIFICATION_RESEND_SECONDS * 1000L + 100)
        testScheduler.runCurrent()
        assertTrue(viewModel.uiState.value.canResend, "the button should be live once it expires")

        viewModel.onEvent(VerificationEvent.ResendClicked)
        testScheduler.runCurrent()

        assertEquals(1, repository.resendCalls)
        assertEquals(
            VERIFICATION_RESEND_SECONDS,
            viewModel.uiState.value.secondsLeft,
            "the countdown restarts, so the button cannot be tapped again immediately"
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a reset still routes, because the server has no endpoint for it yet`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = viewModel(repository, purpose = VerificationPurpose.ResetPassword)

        viewModel.enter("123456")
        val effect = viewModel.effects.first()

        assertNull(repository.verifiedWith, "there is nothing to check a reset code against")
        assertEquals(VerificationEffect.NavigateToNewPassword("ada@example.com"), effect)
    }
}
