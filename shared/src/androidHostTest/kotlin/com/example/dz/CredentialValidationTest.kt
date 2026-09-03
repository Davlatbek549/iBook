package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
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

        override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(null)
    }

    private fun viewModelWith(repository: RecordingAuthRepository) =
        SignUpViewModel(SignUpUseCase(repository))

    private fun SignUpViewModel.fill(name: String, email: String, password: String) {
        onEvent(SignUpEvent.FullNameChanged(name))
        onEvent(SignUpEvent.EmailChanged(email))
        onEvent(SignUpEvent.PasswordChanged(password))
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
            viewModel.uiState.value.errorMessage
        )
    }

    @Test
    fun `the refusal quotes the length the server actually enforces`() = runTest {
        val viewModel = viewModelWith(RecordingAuthRepository())

        // Seven characters: the old message said six would do, and the server refused it.
        viewModel.fill("Ada Lovelace", "ada@example.com", "sevench")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)

        assertTrue(
            viewModel.uiState.value.errorMessage.orEmpty().contains("8 characters"),
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
        assertEquals("Please enter a valid email address.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `a missing name is refused without reaching the server`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = viewModelWith(repository)

        viewModel.fill("   ", "ada@example.com", "correct-horse")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)

        assertEquals(0, repository.signUpCalls)
        assertEquals("Please enter your name.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `valid credentials do reach the server`() = runTest {
        val repository = RecordingAuthRepository()
        val viewModel = viewModelWith(repository)

        viewModel.fill("Ada Lovelace", "ada@example.com", "correct-horse")
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)
        testScheduler.advanceUntilIdle()

        assertEquals(1, repository.signUpCalls, "validation must not block a legitimate sign-up")
        assertEquals(null, viewModel.uiState.value.errorMessage)
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
