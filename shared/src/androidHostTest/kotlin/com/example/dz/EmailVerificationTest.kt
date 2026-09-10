package com.example.dz

import com.example.dz.core.auth.MailedCodeKind
import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.domain.usecase.auth.RequestPasswordResetUseCase
import com.example.dz.domain.usecase.auth.ResendVerificationCodeUseCase
import com.example.dz.domain.usecase.auth.VerifyEmailUseCase
import com.example.dz.presentation.auth.verification.VerificationEffect
import com.example.dz.presentation.auth.verification.VerificationEvent
import com.example.dz.presentation.auth.verification.VERIFICATION_CODE_LIFETIME_SECONDS
import com.example.dz.presentation.auth.verification.VERIFICATION_RESEND_SECONDS
import com.example.dz.presentation.auth.verification.VerificationPurpose
import com.example.dz.presentation.auth.verification.mailedCodeKind
import com.example.dz.presentation.auth.verification.VerificationViewModel
import kotlinx.coroutines.CompletableDeferred
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
        /** When set, a send waits on it — so a test can look at the screen mid-send. */
        private val sendGate: CompletableDeferred<Unit>? = null,
    ) : AuthRepository {
        var verifiedWith: Pair<String, String>? = null
            private set
        var resendCalls = 0
            private set
        var resetRequests = 0
            private set

        override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> {
            verifiedWith = email to code
            return verifyResult
        }

        override suspend fun resendVerificationCode(email: String): AppResult<Unit> {
            resendCalls++
            sendGate?.await()
            return resendResult
        }

        override suspend fun requestPasswordReset(email: String): AppResult<Unit> {
            resetRequests++
            return resendResult
        }

        override suspend fun resetPassword(
            email: String,
            code: String,
            newPassword: String,
        ): AppResult<Unit> = AppResult.Success(Unit)

        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signInWithGoogle(idToken: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        var logoutCalls = 0
            private set

        override suspend fun logout(): AppResult<Unit> {
            logoutCalls++
            return AppResult.Success(Unit)
        }
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(null)
    }

    /**
     * A store that has just had a code of [kind] posted to it, which is the state the code screen
     * is normally reached in. Without one the screen owes no wait at all — see the stale-code test.
     */
    private fun localWithFreshCode(kind: MailedCodeKind) = FakeLocalDataSource().apply {
        saveSetting(kind.lastSentSettingKey, currentEpochMillis().toString())
    }

    private fun viewModel(
        repository: RecordingAuthRepository,
        purpose: VerificationPurpose = VerificationPurpose.VerifyEmail,
        local: LocalDataSource = localWithFreshCode(purpose.mailedCodeKind),
    ) = VerificationViewModel(
        email = "ada@example.com",
        purpose = purpose,
        verifyEmail = VerifyEmailUseCase(repository),
        resendCode = ResendVerificationCodeUseCase(repository),
        requestPasswordReset = RequestPasswordResetUseCase(repository),
        logout = LogoutUseCase(repository),
        local = local,
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
    fun `a reset code is carried to the password screen rather than spent here`() =
        runTest(dispatcher) {
            val repository = RecordingAuthRepository()
            val viewModel = viewModel(repository, purpose = VerificationPurpose.ResetPassword)

            viewModel.enter("123456")
            val effect = viewModel.effects.first()

            // Checking it here would cost one of the guesses the server allows, and the reset
            // call needs one of them for the change itself.
            assertNull(repository.verifiedWith, "a reset code is not spent on the code screen")
            assertEquals(
                VerificationEffect.NavigateToNewPassword("ada@example.com", "123456"),
                effect,
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `resending in a reset asks for a reset code, not a confirmation one`() =
        runTest(dispatcher) {
            val repository = RecordingAuthRepository()
            val viewModel = viewModel(repository, purpose = VerificationPurpose.ResetPassword)

            testScheduler.advanceTimeBy(VERIFICATION_RESEND_SECONDS * 1000L + 100)
            testScheduler.runCurrent()
            viewModel.onEvent(VerificationEvent.ResendClicked)
            testScheduler.runCurrent()

            // The two are separate on the server: a confirmation code cannot finish a reset, so
            // asking the wrong endpoint would mail something useless to the reader.
            assertEquals(1, repository.resetRequests)
            assertEquals(0, repository.resendCalls, "that endpoint issues the wrong kind of code")
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a code sent long ago owes no wait at all`() = runTest(dispatcher) {
        // What a relaunch on an unverified session looks like: the screen is built fresh, but the
        // code it is waiting on went out well before the app was last closed.
        val local = FakeLocalDataSource().apply {
            saveSetting(
                MailedCodeKind.EmailVerification.lastSentSettingKey,
                (currentEpochMillis() - 10 * 60 * 1000).toString(),
            )
        }
        val viewModel = viewModel(RecordingAuthRepository(), local = local)

        assertTrue(
            viewModel.uiState.value.canResend,
            "a countdown started from now would make the reader sit out a wait already served",
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a reset code does not count as a confirmation code`() = runTest(dispatcher) {
        // The two codes are separate on the server, so a fresh reset code leaves this screen with
        // nothing to type — and it sends a confirmation code of its own.
        val repository = RecordingAuthRepository()
        val local = FakeLocalDataSource().apply {
            saveSetting(
                MailedCodeKind.PasswordReset.lastSentSettingKey,
                currentEpochMillis().toString(),
            )
        }
        viewModel(repository, local = local)
        testScheduler.runCurrent()

        assertEquals(1, repository.resendCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `arriving with no code sends one`() = runTest(dispatcher) {
        // Where signing in to an account that predates verification lands: it was never sent a
        // code, and the screen saying one is on its way would otherwise be untrue.
        val repository = RecordingAuthRepository()
        val viewModel = viewModel(repository, local = FakeLocalDataSource())
        testScheduler.runCurrent()

        assertEquals(1, repository.resendCalls, "the screen promises a code; one has to go out")
        assertEquals(
            VERIFICATION_RESEND_SECONDS,
            viewModel.uiState.value.secondsLeft,
            "and the cooldown starts from that send",
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `an expired code is replaced on arrival`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val local = FakeLocalDataSource().apply {
            saveSetting(
                MailedCodeKind.EmailVerification.lastSentSettingKey,
                (currentEpochMillis() - (VERIFICATION_CODE_LIFETIME_SECONDS + 60) * 1000L).toString(),
            )
        }
        viewModel(repository, local = local)
        testScheduler.runCurrent()

        assertEquals(1, repository.resendCalls, "the last code has expired; there is nothing to type")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a live code is not sent again on arrival`() = runTest(dispatcher) {
        // Straight after sign-up, which has just mailed one: a second would only confuse.
        val repository = RecordingAuthRepository()
        viewModel(repository, local = localWithFreshCode(MailedCodeKind.EmailVerification))
        testScheduler.runCurrent()

        assertEquals(0, repository.resendCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a reset code screen never sends on its own`() = runTest(dispatcher) {
        // Only reached from the forgot-password screen, which has just sent the code.
        val repository = RecordingAuthRepository()
        viewModel(repository, purpose = VerificationPurpose.ResetPassword, local = FakeLocalDataSource())
        testScheduler.runCurrent()

        assertEquals(0, repository.resetRequests)
        assertEquals(0, repository.resendCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a code being sent does not read as one being checked`() = runTest(dispatcher) {
        // The send can run as the screen opens, before anything is typed — against a server that
        // may be waking for up to a minute. "Verifying…" on the button then would be untrue.
        val gate = CompletableDeferred<Unit>()
        val viewModel = viewModel(RecordingAuthRepository(sendGate = gate), local = FakeLocalDataSource())
        testScheduler.runCurrent()

        assertTrue(viewModel.uiState.value.isSendingCode)
        assertFalse(viewModel.uiState.value.isLoading, "nothing is being verified")

        gate.complete(Unit)
        testScheduler.runCurrent()
        assertFalse(viewModel.uiState.value.isSendingCode)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `abandoning gives up the session and leaves for sign-in`() = runTest(dispatcher) {
        // The way out when the splash opened this screen directly and there is nothing to pop
        // back to. Without the sign-out the next launch would land right back here.
        val repository = RecordingAuthRepository()
        val viewModel = viewModel(repository)

        viewModel.onEvent(VerificationEvent.AbandonSession)
        val effect = viewModel.effects.first()

        assertEquals(1, repository.logoutCalls, "the unverified session has to actually go")
        assertEquals(VerificationEffect.NavigateToLogin, effect)
    }
}
