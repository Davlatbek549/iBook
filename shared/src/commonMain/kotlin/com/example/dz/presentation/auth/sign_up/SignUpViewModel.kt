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
            is SignUpEvent.TermsToggled ->
                _uiState.update {
                    it.copy(acceptedTermsVersion = if (event.accepted) LEGAL_DOCUMENTS_VERSION else null)
                }
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
            // Reaching the end of either document and agreeing is the same act as ticking the
            // box, so it closes the sheet and records the version.
            SignUpEvent.DocumentAgreed ->
                _uiState.update {
                    it.copy(openDocument = null, acceptedTermsVersion = LEGAL_DOCUMENTS_VERSION)
                }
            SignUpEvent.AppleClicked -> Unit
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
                    emitEffect(SignUpEffect.NavigateToHome)
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
