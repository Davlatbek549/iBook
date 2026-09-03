package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.dz.domain.usecase.auth.SignUpUseCase
import com.example.dz.presentation.auth.sign_up.SignUpEvent
import com.example.dz.presentation.auth.sign_up.SignUpViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

/**
 * The deployed server sleeps between uses, so a rejected sign-up can cost a minute of waiting.
 * These pin that the cases the server would refuse anyway never leave the device.
 */
class CredentialValidationTest {

    @BeforeTest
    fun installMainDispatcher() = Dispatchers.setMain(StandardTestDispatcher())

    @AfterTest
    fun removeMainDispatcher() = Dispatchers.resetMain()

    /**
     * When [holdSignUp] is set, sign-up parks until the test releases it — that pause stands in for
     * the server taking its time, which is the only window in which "busy" is observable.
     */
    private class RecordingAuthRepository(
        private val holdSignUp: CompletableDeferred<Unit>? = null
    ) : AuthRepository {
        var signUpCalls = 0
            private set

        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)

        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> {
            signUpCalls++
            holdSignUp?.await()
            return AppResult.Success(User(id = "u-1", name = name, email = email))
        }

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
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(null)
    }

    private fun viewModelWith(repository: RecordingAuthRepository) =
        SignUpViewModel(SignUpUseCase(repository), SignInWithGoogleUseCase(repository))

    /** Consents too — the design gates Create account on it, so nothing sends without it. */
    private fun SignUpViewModel.fill(name: String, email: String, password: String) {
        onEvent(SignUpEvent.FullNameChanged(name))
        onEvent(SignUpEvent.EmailChanged(email))
        onEvent(SignUpEvent.PasswordChanged(password))
        acceptLegalDocuments()
    }

    /**
     * Consent as a reader now gives it. The box cannot be ticked outright any more, so both
     * documents are opened and agreed to in turn — the same journey the screen requires.
     */
    private fun SignUpViewModel.acceptLegalDocuments() {
        onEvent(SignUpEvent.TermsClicked)
        onEvent(SignUpEvent.DocumentAgreed)
        onEvent(SignUpEvent.PrivacyClicked)
        onEvent(SignUpEvent.DocumentAgreed)
    }

    @Test
    fun `a short password is refused without reaching the server`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = viewModelWith(repository)

        viewModel.fill("Ada Lovelace", "ada@example.com", "short")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)

        assertEquals(0, repository.signUpCalls, "the server would only reject this after a round trip")
        assertEquals(
            "Password is too short. Use at least 8 characters.",
            viewModel.uiState.value.passwordError,
            "the message belongs to the box that is wrong, not the foot of the form"
        )
    }

    @Test
    fun `the refusal quotes the length the server actually enforces`() = runTest {
        val viewModel = viewModelWith(RecordingAuthRepository())

        // Seven characters: the old message said six would do, and the server refused it.
        viewModel.fill("Ada Lovelace", "ada@example.com", "sevench")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)

        assertTrue(
            viewModel.uiState.value.passwordError.orEmpty().contains("8 characters"),
            "advice the server contradicts sends users in circles"
        )
    }

    @Test
    fun `a malformed email is refused without reaching the server`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = viewModelWith(repository)

        viewModel.fill("Ada Lovelace", "not-an-email", "correct-horse")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)

        assertEquals(0, repository.signUpCalls)
        assertEquals("Please enter a valid email address.", viewModel.uiState.value.emailError)
    }

    @Test
    fun `a missing name is refused without reaching the server`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = viewModelWith(repository)

        viewModel.fill("   ", "ada@example.com", "correct-horse")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)

        assertEquals(0, repository.signUpCalls)
        assertEquals("Please enter your name.", viewModel.uiState.value.nameError)
    }

    @Test
    fun `valid credentials do reach the server`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = viewModelWith(repository)

        viewModel.fill("Ada Lovelace", "ada@example.com", "correct-horse")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)
        testScheduler.advanceUntilIdle()

        assertEquals(1, repository.signUpCalls, "validation must not block a legitimate sign-up")
        val state = viewModel.uiState.value
        assertEquals(null, state.errorMessage)
        assertEquals(null, state.nameError)
        assertEquals(null, state.emailError)
        assertEquals(null, state.passwordError)
    }

    @Test
    fun `sign-up does not send until the terms are accepted`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = viewModelWith(repository)

        // Everything valid except the box, which the design makes a precondition.
        viewModel.onEvent(SignUpEvent.FullNameChanged("Ada Lovelace"))
        viewModel.onEvent(SignUpEvent.EmailChanged("ada@example.com"))
        viewModel.onEvent(SignUpEvent.PasswordChanged("correct-horse"))
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)
        testScheduler.advanceUntilIdle()

        assertEquals(0, repository.signUpCalls, "an unticked agreement must not create an account")

        viewModel.acceptLegalDocuments()
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)
        testScheduler.advanceUntilIdle()

        assertEquals(
            1,
            repository.signUpCalls,
            "agreeing to both documents unblocks the same tap",
        )
    }

    @Test
    fun `the button reports being busy while the request is in flight`() = runTest {
        val serverResponds = CompletableDeferred<Unit>()
        val viewModel = viewModelWith(RecordingAuthRepository(holdSignUp = serverResponds))

        viewModel.fill("Ada Lovelace", "ada@example.com", "correct-horse")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)
        testScheduler.runCurrent()

        // Mid-request: this is the stretch the user sits through, and the screen has to show it.
        assertTrue(viewModel.uiState.value.isLoading, "a silent screen reads as a frozen app")

        serverResponds.complete(Unit)
        testScheduler.advanceUntilIdle()
        assertTrue(!viewModel.uiState.value.isLoading, "the button must free up once the call lands")
    }

    @Test
    fun `tapping again mid-request does not send a second sign-up`() = runTest {
        val serverResponds = CompletableDeferred<Unit>()
        val repository = RecordingAuthRepository(holdSignUp = serverResponds)
        val viewModel = viewModelWith(repository)

        viewModel.fill("Ada Lovelace", "ada@example.com", "correct-horse")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)
        testScheduler.runCurrent()
        // An impatient user on a slow connection taps a few more times.
        repeat(3) { viewModel.onEvent(SignUpEvent.CreateAccountClicked) }
        testScheduler.runCurrent()

        assertEquals(1, repository.signUpCalls, "duplicate taps must not create duplicate accounts")

        serverResponds.complete(Unit)
        testScheduler.advanceUntilIdle()
    }
}
