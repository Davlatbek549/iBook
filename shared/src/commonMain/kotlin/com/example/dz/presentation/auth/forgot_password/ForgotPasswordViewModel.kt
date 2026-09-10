package com.example.dz.presentation.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.auth.RequestPasswordResetUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import com.example.dz.presentation.mvi.validateEmail
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Where a reset starts: an address goes in, the server mails a code to it, and the reader is
 * carried straight on to type it.
 *
 * There is no confirmation step in between. The screen used to report the send in place and wait
 * for a second tap, so that a typo could be caught before leaving — but the code screen names the
 * address itself and can be backed out of, so that step asked for a tap and bought nothing.
 *
 * Nothing here says whether the address is registered — and nothing can, because the server
 * answers an unknown address exactly as a known one. Saying more would turn this screen into a
 * way to test which emails have accounts; the wording that keeps that promise travels on to the
 * code screen, which is where the reader now waits.
 */
class ForgotPasswordViewModel(
    email: String = "",
    private val requestPasswordReset: RequestPasswordResetUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState(email = email))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ForgotPasswordEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged ->
                _uiState.update {
                    it.copy(email = event.email, errorMessage = null, emailError = null)
                }
            ForgotPasswordEvent.SendLinkClicked -> requestCode()
            ForgotPasswordEvent.BackClicked -> emitEffect(ForgotPasswordEffect.NavigateBack)
        }
    }

    private fun requestCode() {
        if (_uiState.value.isLoading) return

        // Checked here so an obvious typo does not cost a round trip to a server that may be
        // waking from sleep — see [validateEmail].
        val email = _uiState.value.email.trim()
        val invalid = validateEmail(email)
        if (invalid != null) {
            _uiState.update { it.copy(emailError = invalid) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, emailError = null) }
            when (val result = requestPasswordReset(email)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(ForgotPasswordEffect.NavigateToVerification(email))
                }
                is AppResult.Error ->
                    // Only a real failure — a dead connection, a server that is down — lands
                    // here. "No such account" is not one of them, by design.
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.toPresentationMessage(),
                        )
                    }
            }
        }
    }

    private fun emitEffect(effect: ForgotPasswordEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
