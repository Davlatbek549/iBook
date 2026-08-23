package com.example.dz.presentation.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
 * The code is not checked against anything yet — the server has no verification endpoint — so
 * [verify] only routes. Where it goes is decided by [VerificationUiState.purpose], because the
 * screen is reached from sign-up and from a password reset and those end in different places.
 */
class VerificationViewModel(
    email: String = "",
    purpose: VerificationPurpose = VerificationPurpose.VerifyEmail,
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
            VerificationPurpose.VerifyEmail -> emitEffect(VerificationEffect.NavigateToHome)
            VerificationPurpose.ResetPassword ->
                emitEffect(VerificationEffect.NavigateToNewPassword(state.email))
        }
    }

    private fun resend() {
        if (!_uiState.value.canResend) return
        _uiState.update { it.copy(code = "", errorMessage = null) }
        startResendTimer()
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
