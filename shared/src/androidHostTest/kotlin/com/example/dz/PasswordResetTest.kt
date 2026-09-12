package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.LoginUseCase
import com.example.dz.domain.usecase.auth.RequestPasswordResetUseCase
import com.example.dz.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.dz.domain.usecase.auth.ResetPasswordUseCase
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordEffect
import com.example.dz.presentation.auth.login.LoginEffect
import com.example.dz.presentation.auth.login.LoginEvent
import com.example.dz.presentation.auth.login.LoginViewModel
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordEvent
import com.example.dz.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.dz.presentation.auth.new_password.NewPasswordEffect
import com.example.dz.presentation.auth.new_password.NewPasswordEvent
import com.example.dz.presentation.auth.new_password.NewPasswordUiState
import com.example.dz.presentation.auth.new_password.NewPasswordViewModel
import com.example.dz.presentation.auth.new_password.RESET_SUCCESS_DWELL_MILLIS
import com.example.dz.presentation.auth.sign_up.SignUpUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
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
        override suspend fun deleteAccount(): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(null)
    }

    private fun forgotViewModel(repository: RecordingAuthRepository) =
        ForgotPasswordViewModel(requestPasswordReset = RequestPasswordResetUseCase(repository))

    private fun newPasswordViewModel(
        repository: RecordingAuthRepository,
        code: String = "123456",
    ) = NewPasswordViewModel(
        email = "ada@example.com",
        code = code,
        resetPassword = ResetPasswordUseCase(repository),
    )

    /** Records every effect this view model emits, so a test can assert that none arrived. */
    private fun TestScope.collectEffects(
        viewModel: ForgotPasswordViewModel,
    ): List<ForgotPasswordEffect> {
        val received = mutableListOf<ForgotPasswordEffect>()
        backgroundScope.launch { viewModel.effects.collect { received += it } }
        testScheduler.runCurrent()
        return received
    }

    private fun ForgotPasswordViewModel.request(email: String) {
        onEvent(ForgotPasswordEvent.EmailChanged(email))
        onEvent(ForgotPasswordEvent.SendLinkClicked)
    }

    private fun NewPasswordViewModel.choose(password: String, confirmation: String = password) {
        onEvent(NewPasswordEvent.PasswordChanged(password))
        onEvent(NewPasswordEvent.ConfirmationChanged(confirmation))
        onEvent(NewPasswordEvent.SaveClicked)
    }

    // ── Carrying the address ─────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `the sign-in screen hands its address to the recovery screen`() = runTest(dispatcher) {
        val viewModel = LoginViewModel(
            login = LoginUseCase(RecordingAuthRepository()),
            signInWithGoogle = SignInWithGoogleUseCase(RecordingAuthRepository()),
        )

        viewModel.onEvent(LoginEvent.EmailChanged("  ada@example.com  "))
        viewModel.onEvent(LoginEvent.ForgotPasswordClicked)

        // Someone reaches for "Forgot password" having already typed who they are; asking again
        // on the next screen is asking twice.
        assertEquals(
            LoginEffect.NavigateToForgotPassword("ada@example.com"),
            viewModel.effects.first(),
        )
    }

    @Test
    fun `the recovery screen opens on the address it was handed`() {
        val viewModel = ForgotPasswordViewModel(
            email = "ada@example.com",
            requestPasswordReset = RequestPasswordResetUseCase(RecordingAuthRepository()),
        )

        assertEquals("ada@example.com", viewModel.uiState.value.email)
    }

    @Test
    fun `a finished reset opens sign-in on the address it just proved`() {
        val viewModel = LoginViewModel(
            email = "ada@example.com",
            passwordJustReset = true,
            login = LoginUseCase(RecordingAuthRepository()),
            signInWithGoogle = SignInWithGoogleUseCase(RecordingAuthRepository()),
        )

        // Arriving at a sign-in screen straight after a reset reads as a failure unless the
        // screen says otherwise, so it says so — and fills in the address either way.
        assertEquals("ada@example.com", viewModel.uiState.value.email)
        assertTrue(viewModel.uiState.value.passwordJustReset)
    }

    @Test
    fun `editing the address puts the reset notice away`() {
        val viewModel = LoginViewModel(
            email = "ada@example.com",
            passwordJustReset = true,
            login = LoginUseCase(RecordingAuthRepository()),
            signInWithGoogle = SignInWithGoogleUseCase(RecordingAuthRepository()),
        )

        viewModel.onEvent(LoginEvent.EmailChanged("someone@example.com"))

        // The notice described the address that arrived with it.
        assertFalse(viewModel.uiState.value.passwordJustReset)
    }

    // ── Asking for a code ────────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `asking for a code goes straight to the code screen`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = forgotViewModel(repository)

        viewModel.request("ada@example.com")
        val effect = viewModel.effects.first()

        // No confirmation step in between: the code screen names the address itself, so stopping
        // here to say the same thing only cost a tap.
        assertEquals("ada@example.com", repository.requestedFor)
        assertEquals(ForgotPasswordEffect.NavigateToVerification("ada@example.com"), effect)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `the address is trimmed before it is mailed and carried`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = forgotViewModel(repository)

        viewModel.request("  ada@example.com  ")
        val effect = viewModel.effects.first()

        assertEquals("ada@example.com", repository.requestedFor)
        assertEquals(ForgotPasswordEffect.NavigateToVerification("ada@example.com"), effect)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a malformed address never reaches the server`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository()
        val viewModel = forgotViewModel(repository)
        val effects = collectEffects(viewModel)

        viewModel.request("not-an-address")
        testScheduler.advanceUntilIdle()

        // The deployed server sleeps between uses, so a typo would otherwise cost a minute
        // of waiting to be told what the shape already says.
        assertNull(repository.requestedFor)
        assertNotNull(viewModel.uiState.value.emailError)
        assertTrue(effects.isEmpty(), "nothing was sent, so there is nothing to move on to")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a failed request does not move on as though a code was sent`() = runTest(dispatcher) {
        val repository = RecordingAuthRepository(
            requestResult = AppResult.Error(AppError.Network)
        )
        val viewModel = forgotViewModel(repository)
        val effects = collectEffects(viewModel)

        viewModel.request("ada@example.com")
        testScheduler.advanceUntilIdle()

        // Now that success leaves the screen, the failure has to be visible here — it is the
        // only thing that tells the reader no code is coming.
        val state = viewModel.uiState.value
        assertNotNull(state.errorMessage)
        assertFalse(state.isLoading)
        assertTrue(effects.isEmpty(), "a code that was never sent must not open the code screen")
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
            val effect = viewModel.effects.first()

            // The server issues no session for a reset and revokes the ones that existed, so
            // opening Home would be a signed-in shell with no session behind it. Saving leaves
            // on its own: there is nothing further to do here, and a screen that just sits there
            // reads as a save that did not work.
            assertEquals(NewPasswordEffect.NavigateToLogin("ada@example.com"), effect)
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
    fun `the success moment is held before the screen leaves`() = runTest(dispatcher) {
        val viewModel = newPasswordViewModel(RecordingAuthRepository())

        viewModel.choose("a-long-enough-password")
        testScheduler.runCurrent()
        // The overlay is up while the moment is held, and the form under it is inert.
        assertTrue(viewModel.uiState.value.isSaved, "the confirmation shows straight away")

        val effect = viewModel.effects.first()

        assertEquals(NewPasswordEffect.NavigateToLogin("ada@example.com"), effect)
        assertTrue(
            testScheduler.currentTime >= RESET_SUCCESS_DWELL_MILLIS,
            "leaving at once would flash the confirmation past before it could be read",
        )
    }

    @Test
    fun `the meter reads a reset password the way sign-up reads it`() {
        // Both screens set a password against the same server rule, so one calling it strong
        // while the other calls it fair would be the app disagreeing with itself.
        for (password in listOf("", "short", "exactly8", "a-long-enough-password")) {
            assertEquals(
                SignUpUiState(password = password).passwordStrength,
                NewPasswordUiState(password = password).passwordStrength,
                "the two meters disagree about \"$password\"",
            )
        }
    }

    @Test
    fun `the meter lights nothing for a password the server would refuse`() {
        assertEquals(0, NewPasswordUiState(password = "short").passwordStrength)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `back leads to sign-in in the frame after the password has changed`() =
        runTest(dispatcher) {
            val viewModel = newPasswordViewModel(RecordingAuthRepository())

            viewModel.choose("a-long-enough-password")
            // The save's own departure, taken first so the next await sees only back's.
            assertEquals(
                NewPasswordEffect.NavigateToLogin("ada@example.com"),
                viewModel.effects.first(),
            )

            viewModel.onEvent(NewPasswordEvent.BackClicked)

            // Saving leaves on its own, but back is still live for the frame in between, and
            // popping there would land on a code screen holding a code that has been spent.
            assertEquals(
                NewPasswordEffect.NavigateToLogin("ada@example.com"),
                viewModel.effects.first(),
            )
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
