package com.example.dz.presentation.auth.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.legal.LEGAL_DOCUMENTS_VERSION
import com.example.dz.core.legal.LegalDocumentKind
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.dz.domain.usecase.auth.SignUpUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import com.example.dz.presentation.mvi.validateSignUp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUp: SignUpUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SignUpEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.FullNameChanged ->
                _uiState.update {
                    it.copy(fullName = event.fullName, errorMessage = null, nameError = null)
                }
            is SignUpEvent.TermsToggled -> onTermsToggled(event.accepted)
            is SignUpEvent.EmailChanged ->
                _uiState.update {
                    it.copy(email = event.email, errorMessage = null, emailError = null)
                }
            is SignUpEvent.PasswordChanged ->
                _uiState.update {
                    it.copy(password = event.password, errorMessage = null, passwordError = null)
                }
            SignUpEvent.CreateAccountClicked -> createAccount()
            SignUpEvent.SignInClicked -> emitEffect(SignUpEffect.NavigateToLogin)
            SignUpEvent.GoogleClicked ->
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            is SignUpEvent.GoogleTokenReceived -> exchangeGoogleToken(event.idToken)
            is SignUpEvent.GoogleSignInFailed ->
                _uiState.update { it.copy(isLoading = false, errorMessage = event.message) }
            SignUpEvent.TermsClicked ->
                _uiState.update { it.copy(openDocument = LegalDocumentKind.Terms) }
            SignUpEvent.PrivacyClicked ->
                _uiState.update { it.copy(openDocument = LegalDocumentKind.Privacy) }
            SignUpEvent.DocumentDismissed ->
                _uiState.update { it.copy(openDocument = null) }
            SignUpEvent.DocumentAgreed -> onDocumentAgreed()
            SignUpEvent.AppleClicked -> Unit
        }
    }

    /**
     * The box records consent; it is not a way to give it.
     *
     * A tap before both documents have been read opens whichever is still outstanding rather than
     * ticking, because the sentence beside the box names two documents and only reading them both
     * can honour it. The second tap, once the last one is agreed, is what finally ticks it.
     *
     * Unticking is never gated: withdrawing consent is allowed to be as cheap as it sounds.
     */
    private fun onTermsToggled(accepted: Boolean) {
        if (!accepted) {
            _uiState.update { it.copy(acceptedTermsVersion = null) }
            return
        }
        _uiState.update { state ->
            state.nextUnreadDocument
                ?.let { state.copy(openDocument = it) }
                ?: state.copy(acceptedTermsVersion = LEGAL_DOCUMENTS_VERSION)
        }
    }

    /**
     * Agreeing at the foot of a document records that one document, and nothing more.
     *
     * The box then follows on its own once none is outstanding, so a reader who opens both
     * through the links never has to come back and tick anything by hand.
     */
    private fun onDocumentAgreed() {
        _uiState.update { state ->
            val agreed = state.openDocument
                ?.let { state.agreedDocuments + it }
                ?: state.agreedDocuments
            val complete = LegalDocumentKind.entries.all { it in agreed }
            state.copy(
                openDocument = null,
                agreedDocuments = agreed,
                acceptedTermsVersion =
                    if (complete) LEGAL_DOCUMENTS_VERSION else state.acceptedTermsVersion,
            )
        }
    }

    private fun createAccount() {
        // The design disables the button until the terms are ticked; this is the same rule for
        // any caller that is not the button, such as the keyboard's Done action.
        if (!_uiState.value.canSubmit) return

        val errors = _uiState.value.let {
            validateSignUp(it.fullName.trim(), it.email.trim(), it.password)
        }
        if (!errors.isValid) {
            _uiState.update {
                it.copy(
                    nameError = errors.name,
                    emailError = errors.email,
                    passwordError = errors.password
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    nameError = null,
                    emailError = null,
                    passwordError = null
                )
            }
            val state = _uiState.value
            when (val result = signUp(state.fullName.trim(), state.email.trim(), state.password)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(SignUpEffect.NavigateToVerification(state.email.trim()))
                }
                is AppResult.Error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                    }
            }
        }
    }

    /**
     * Google returns a verified address, so there is nothing left to verify — this lands on Home
     * rather than routing through the code screen the password flow uses.
     */
    private fun exchangeGoogleToken(idToken: String) {
        // The button is disabled until the terms are ticked, but the gate belongs here too:
        // signing up through Google creates an account just as the form does.
        if (!_uiState.value.termsAccepted) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = signInWithGoogle(idToken)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    // Google nearly always vouches for the address, but when it does not, the
                    // account is as unverified as any other and goes where they all go.
                    val user = result.data
                    emitEffect(
                        if (user.emailVerified) SignUpEffect.NavigateToHome
                        else SignUpEffect.NavigateToVerification(user.email.orEmpty())
                    )
                }
                is AppResult.Error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                    }
            }
        }
    }

    private fun emitEffect(effect: SignUpEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
