package com.example.dz.presentation.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.auth.ResendVerificationCodeUseCase
import com.example.dz.domain.usecase.auth.VerifyEmailUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Code entry for both flows that have to prove someone reads a mailbox.
 *
 * Where a correct code leads is decided by [VerificationUiState.purpose], because the screen is
 * reached from sign-up and from a password reset and those end in different places.
 *
 * Only the sign-up path is checked against the server: `/auth/verify` exists, and the reset
 * endpoints do not yet. A reset therefore still routes on a well-formed code, which is a gap and
 * is marked as one in [verify] rather than hidden.
 */
class VerificationViewModel(
    email: String = "",
    purpose: VerificationPurpose = VerificationPurpose.VerifyEmail,
    private val verifyEmail: VerifyEmailUseCase,
    private val resendCode: ResendVerificationCodeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUiState(email = email, purpose = purpose))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<VerificationEffect>()
    val effects = _effects.asSharedFlow()

    private var timerJob: Job? = null

    init {
        startResendTimer()
    }

    fun onEvent(event: VerificationEvent) {
        when (event) {
            is VerificationEvent.CodeChanged -> onCodeChanged(event.code)
            VerificationEvent.VerifyClicked -> verify()
            VerificationEvent.ResendClicked -> resend()
            VerificationEvent.BackClicked -> emitEffect(VerificationEffect.NavigateBack)
        }
    }

    private fun onCodeChanged(code: String) {
        // OrganicCodeField already filters to digits and caps the length; this guards the state
        // against any other caller.
        if (code.length <= VERIFICATION_CODE_LENGTH && code.all { it.isDigit() }) {
            _uiState.update { it.copy(code = code, errorMessage = null) }
        }
    }

    private fun verify() {
        val state = _uiState.value
        if (state.isLoading || !state.isComplete) return

        when (state.purpose) {
            VerificationPurpose.VerifyEmail -> checkWithServer(state.email, state.code)
            // No reset endpoint yet, so there is nothing to check this against. It routes on a
            // well-formed code and the password screen beyond it sets no password either — both
            // are waiting on the same server work.
            VerificationPurpose.ResetPassword ->
                emitEffect(VerificationEffect.NavigateToNewPassword(state.email))
        }
    }

    private fun checkWithServer(email: String, code: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = verifyEmail(email, code)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(VerificationEffect.NavigateToHome)
                }
                is AppResult.Error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            // Cleared so a refused code can be retyped without deleting it first.
                            code = "",
                            errorMessage = result.error.toPresentationMessage(),
                        )
                    }
            }
        }
    }

    /**
     * The timer restarts on the way out rather than on the way back, so a refusal cannot leave the
     * button live and invite a second send the server would reject anyway.
     */
    private fun resend() {
        val state = _uiState.value
        if (!state.canResend || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, code = "") }
            val result = resendCode(state.email)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = (result as? AppResult.Error)?.error?.toPresentationMessage(),
                )
            }
            startResendTimer()
        }
    }

    private fun startResendTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(secondsLeft = VERIFICATION_RESEND_SECONDS) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.secondsLeft > 0) {
                delay(1000)
                _uiState.update { it.copy(secondsLeft = it.secondsLeft - 1) }
            }
        }
    }

    private fun emitEffect(effect: VerificationEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
