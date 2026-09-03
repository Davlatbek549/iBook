package com.example.dz.presentation.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.auth.RequestPasswordResetUseCase
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
 * A sign-up code is spent here, against `/auth/verify`. A reset code is not: the server allows a
 * fixed number of guesses against a code, and the reset itself needs one of them, so checking
 * first would burn a guess to learn nothing the next call would not say — see [verify].
 */
class VerificationViewModel(
    email: String = "",
    purpose: VerificationPurpose = VerificationPurpose.VerifyEmail,
    private val verifyEmail: VerifyEmailUseCase,
    private val resendCode: ResendVerificationCodeUseCase,
    private val requestPasswordReset: RequestPasswordResetUseCase,
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
            // Carried rather than checked. `/auth/password/reset` spends the code as part of
            // setting the password, and a wrong one is reported there — one guess, one refusal.
            VerificationPurpose.ResetPassword ->
                emitEffect(VerificationEffect.NavigateToNewPassword(state.email, state.code))
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
     *
     * Which code gets sent follows the purpose. The two are separate on the server — a
     * confirmation code is no use against a password, and the reverse — so asking the wrong
     * endpoint would mail something that cannot finish the flow the reader is in.
     */
    private fun resend() {
        val state = _uiState.value
        if (!state.canResend || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, code = "") }
            val result = when (state.purpose) {
                VerificationPurpose.VerifyEmail -> resendCode(state.email)
                VerificationPurpose.ResetPassword -> requestPasswordReset(state.email)
            }
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
