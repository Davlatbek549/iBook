package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.core.legal.LEGAL_DOCUMENTS_VERSION
import com.example.dz.core.legal.LegalDocumentKind
import com.example.dz.core.legal.LegalDocuments
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository
import com.example.dz.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.dz.domain.usecase.auth.SignUpUseCase
import com.example.dz.presentation.auth.sign_up.SignUpEvent
import com.example.dz.presentation.auth.sign_up.SignUpUiState
import com.example.dz.presentation.auth.sign_up.SignUpViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

/**
 * Consent on the sign-up screen.
 *
 * The point of recording a *version* rather than a bare "agreed" is that the record survives the
 * documents changing: when the wording moves, an old acceptance has to stop counting on its own,
 * without a migration or a manual reset.
 */
class LegalConsentTest {

    @BeforeTest
    fun installMainDispatcher() = Dispatchers.setMain(StandardTestDispatcher())

    @AfterTest
    fun removeMainDispatcher() = Dispatchers.resetMain()

    private class SilentAuthRepository : AuthRepository {
        override suspend fun login(email: String, password: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
            AppResult.Success(User(id = "u-1", name = name, email = email))
        override suspend fun signInWithGoogle(idToken: String): AppResult<User> =
            AppResult.Error(AppError.Unauthorized)
        override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(null)
    }

    private fun viewModel(): SignUpViewModel {
        val repository = SilentAuthRepository()
        return SignUpViewModel(SignUpUseCase(repository), SignInWithGoogleUseCase(repository))
    }

    @Test
    fun `tapping a link opens that document, not the other one`() = runTest {
        val model = viewModel()

        model.onEvent(SignUpEvent.PrivacyClicked)
        assertEquals(LegalDocumentKind.Privacy, model.uiState.value.openDocument)

        model.onEvent(SignUpEvent.DocumentDismissed)
        assertNull(model.uiState.value.openDocument)

        model.onEvent(SignUpEvent.TermsClicked)
        assertEquals(LegalDocumentKind.Terms, model.uiState.value.openDocument)
    }

    @Test
    fun `dismissing without agreeing leaves the box unticked`() = runTest {
        val model = viewModel()

        model.onEvent(SignUpEvent.TermsClicked)
        model.onEvent(SignUpEvent.DocumentDismissed)

        assertFalse(
            model.uiState.value.termsAccepted,
            "closing a document is not agreeing to it",
        )
    }

    @Test
    fun `agreeing in the sheet closes it and ticks the box`() = runTest {
        val model = viewModel()

        model.onEvent(SignUpEvent.TermsClicked)
        model.onEvent(SignUpEvent.DocumentAgreed)

        val state = model.uiState.value
        assertNull(state.openDocument)
        assertTrue(state.termsAccepted)
        assertEquals(LEGAL_DOCUMENTS_VERSION, state.acceptedTermsVersion)
    }

    @Test
    fun `an acceptance of an older version no longer counts`() = runTest {
        // What a returning reader's stored consent looks like once the documents have moved on.
        val stale = SignUpUiState(acceptedTermsVersion = LEGAL_DOCUMENTS_VERSION - 1)

        assertFalse(
            stale.termsAccepted,
            "changed wording has to be agreed to again, not inherited from the old text",
        )
        assertFalse(stale.canSubmit)
    }

    @Test
    fun `both documents carry the current version and real content`() {
        LegalDocumentKind.entries.forEach { kind ->
            val document = LegalDocuments.of(kind)
            assertEquals(
                LEGAL_DOCUMENTS_VERSION,
                document.version,
                "${document.title} would otherwise be accepted under a number it does not carry",
            )
            assertTrue(document.sections.isNotEmpty(), "${document.title} has no sections")
            assertTrue(
                document.sections.all { it.blocks.isNotEmpty() },
                "${document.title} has an empty section, which would render as a bare heading",
            )
        }
    }
}
