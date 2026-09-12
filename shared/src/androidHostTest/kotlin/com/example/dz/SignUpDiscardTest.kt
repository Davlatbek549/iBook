package com.example.dz

import com.example.dz.core.auth.MailedCodeKind
import com.example.dz.core.result.AppResult
import com.example.dz.core.time.currentEpochMillis
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.local.LocalDataSourceImpl
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.account.DiscardSignUpUseCase
import com.example.dz.domain.usecase.auth.LogoutUseCase
import com.example.dz.domain.usecase.auth.RequestPasswordResetUseCase
import com.example.dz.domain.usecase.auth.ResendVerificationCodeUseCase
import com.example.dz.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.dz.domain.usecase.auth.SignUpUseCase
import com.example.dz.domain.usecase.auth.VerifyEmailUseCase
import com.example.dz.presentation.auth.sign_up.SignUpEffect
import com.example.dz.presentation.auth.sign_up.SignUpEvent
import com.example.dz.presentation.auth.sign_up.SignUpViewModel
import com.example.dz.presentation.auth.verification.VerificationEffect
import com.example.dz.presentation.auth.verification.VerificationEvent
import com.example.dz.presentation.auth.verification.VerificationPurpose
import com.example.dz.presentation.auth.verification.VerificationViewModel
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
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
import kotlin.test.assertTrue

/**
 * Backing out of the code screen straight after sign-up, usually to fix a mistyped address. The
 * account made for that address used to stay behind, holding it, and the corrected sign-up became
 * a second account. Now the way back takes the first one with it.
 */
class SignUpDiscardTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    /** The address as the reader typed it, slip and all — the slip they go back to fix. */
    private val mistyped = "ada@exmaple.com"

    /** Keeps to the real repository's contract: the session goes once the server lets go. */
    private class FreshAccountRepository(
        private val local: LocalDataSource,
        /** When set, deletion waits on it — a server taking its time, or never answering. */
        private val deleteGate: CompletableDeferred<Unit>? = null,
        private val user: User =
            User(id = "u-1", name = "Ada", email = "ada@exmaple.com", emailVerified = false),
    ) : AuthRepository {
        var deleteCalls = 0
            private set
        var verifyCalls = 0
            private set

        override suspend fun deleteAccount(): AppResult<Unit> {
            deleteCalls++
            deleteGate?.await()
            local.clearSession()
            return AppResult.Success(Unit)
        }

        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            AppResult.Success(user.copy(email = email))

        override suspend fun signInWithGoogle(idToken: String): AppResult<User> =
            AppResult.Success(user)

        override suspend fun verifyEmail(email: String, code: String): AppResult<Unit> {
            verifyCalls++
            return AppResult.Success(Unit)
        }

        override suspend fun resendVerificationCode(email: String): AppResult<Unit> =
            AppResult.Success(Unit)
        override suspend fun requestPasswordReset(email: String): AppResult<Unit> =
            AppResult.Success(Unit)
        override suspend fun resetPassword(
            email: String,
            code: String,
            newPassword: String,
        ): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Success(user)
        override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(user)
    }

    /** The device as sign-up leaves it: a session for the new account, and its code on the way. */
    private fun justSignedUp() = LocalDataSourceImpl(MapSettings()).apply {
        saveUserSession(
            userId = "u-1",
            name = "Ada",
            email = mistyped,
            token = "access",
            refreshToken = "refresh",
            emailVerified = false,
        )
        saveSetting(MailedCodeKind.EmailVerification.lastSentSettingKey, currentEpochMillis().toString())
    }

    private fun codeScreen(
        repository: AuthRepository,
        local: LocalDataSource,
        accountJustCreated: Boolean = true,
    ) = VerificationViewModel(
        email = mistyped,
        purpose = VerificationPurpose.VerifyEmail,
        accountJustCreated = accountJustCreated,
        verifyEmail = VerifyEmailUseCase(repository),
        resendCode = ResendVerificationCodeUseCase(repository),
        requestPasswordReset = RequestPasswordResetUseCase(repository),
        logout = LogoutUseCase(repository),
        discardSignUp = DiscardSignUpUseCase(repository),
        local = local,
    )

    // ── Which accounts may be taken back ─────────────────────────────────────

    private fun SignUpViewModel.acceptLegalDocuments() {
        onEvent(SignUpEvent.TermsClicked)
        onEvent(SignUpEvent.DocumentAgreed)
        onEvent(SignUpEvent.PrivacyClicked)
        onEvent(SignUpEvent.DocumentAgreed)
    }

    @Test
    fun `an account the form made is marked as just created`() = runTest(dispatcher) {
        val repository = FreshAccountRepository(FakeLocalDataSource())
        val viewModel = SignUpViewModel(SignUpUseCase(repository), SignInWithGoogleUseCase(repository))

        viewModel.onEvent(SignUpEvent.FullNameChanged("Ada Lovelace"))
        viewModel.onEvent(SignUpEvent.EmailChanged(mistyped))
        viewModel.onEvent(SignUpEvent.PasswordChanged("correct-horse"))
        viewModel.acceptLegalDocuments()
        viewModel.onEvent(SignUpEvent.CreateAccountClicked)

        assertEquals(
            SignUpEffect.NavigateToVerification(mistyped, accountJustCreated = true),
            viewModel.effects.first(),
        )
    }

    @Test
    fun `an account Google signed in to is never marked as just created`() = runTest(dispatcher) {
        // Google on the sign-up screen also signs in to an account that already exists, and an
        // unverified one still lands on the code screen. Backing out of that must not delete it.
        val repository = FreshAccountRepository(FakeLocalDataSource())
        val viewModel = SignUpViewModel(SignUpUseCase(repository), SignInWithGoogleUseCase(repository))

        viewModel.acceptLegalDocuments()
        viewModel.onEvent(SignUpEvent.GoogleTokenReceived("id-token"))

        assertEquals(
            SignUpEffect.NavigateToVerification(mistyped, accountJustCreated = false),
            viewModel.effects.first(),
        )
    }

    // ── The way back ─────────────────────────────────────────────────────────

    @Test
    fun `backing out deletes the account just made and returns to the form`() = runTest(dispatcher) {
        val local = justSignedUp()
        val repository = FreshAccountRepository(local)
        val viewModel = codeScreen(repository, local)

        viewModel.onEvent(VerificationEvent.BackClicked)

        assertEquals(VerificationEffect.NavigateBack, viewModel.effects.first())
        assertEquals(1, repository.deleteCalls, "the mistyped address must not keep an account")
        assertFalse(local.isLoggedIn())
        assertEquals(
            "",
            local.getSetting(MailedCodeKind.EmailVerification.lastSentSettingKey),
            "the code on its way belonged to the account that is gone",
        )
        assertFalse(viewModel.uiState.value.isLeaving)
    }

    @Test
    fun `a server that never answers does not hold the reader on the screen`() =
        runTest(dispatcher) {
            val local = justSignedUp()
            val repository = FreshAccountRepository(local, deleteGate = CompletableDeferred())
            val viewModel = codeScreen(repository, local)

            viewModel.onEvent(VerificationEvent.BackClicked)
            val startedAt = testScheduler.currentTime
            val effect = viewModel.effects.first()

            assertEquals(VerificationEffect.NavigateBack, effect)
            assertTrue(
                testScheduler.currentTime - startedAt <= 5_000,
                "a back press may wait on the server, but not for as long as it likes",
            )
            // The account may outlive this on the server, but a session kept for it here would
            // send the next launch straight back to its code screen.
            assertFalse(local.isLoggedIn())
        }

    @Test
    fun `nothing is verified or sent twice while the reader is leaving`() = runTest(dispatcher) {
        val gate = CompletableDeferred<Unit>()
        val local = justSignedUp()
        val repository = FreshAccountRepository(local, deleteGate = gate)
        val viewModel = codeScreen(repository, local)

        viewModel.onEvent(VerificationEvent.BackClicked)
        testScheduler.runCurrent()
        assertTrue(viewModel.uiState.value.isLeaving, "the back arrow shows that it is working")

        // A code finished mid-discard would open Home on an account about to stop existing.
        viewModel.onEvent(VerificationEvent.CodeChanged("123456"))
        viewModel.onEvent(VerificationEvent.VerifyClicked)
        viewModel.onEvent(VerificationEvent.BackClicked)
        testScheduler.runCurrent()

        assertEquals(0, repository.verifyCalls)
        assertEquals(1, repository.deleteCalls, "a second press must not send a second request")

        gate.complete(Unit)
        assertEquals(VerificationEffect.NavigateBack, viewModel.effects.first())
    }

    @Test
    fun `a code screen reached by signing in never deletes on the way out`() = runTest(dispatcher) {
        val local = justSignedUp()
        val repository = FreshAccountRepository(local)
        val viewModel = codeScreen(repository, local, accountJustCreated = false)

        viewModel.onEvent(VerificationEvent.BackClicked)

        assertEquals(VerificationEffect.NavigateBack, viewModel.effects.first())
        assertEquals(0, repository.deleteCalls)
        assertTrue(local.isLoggedIn(), "only the nav graph's own way out may give the session up")
    }
}
