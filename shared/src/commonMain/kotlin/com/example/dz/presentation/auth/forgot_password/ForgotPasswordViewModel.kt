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
 * Where a reset starts: an address goes in and the server mails a code to it.
 *
 * The confirmation deliberately does not say whether the address is registered — and it cannot,
 * because the server answers an unknown address exactly as a known one. Reporting anything more
 * specific here would turn this screen into a way to test which emails have accounts.
 */
class ForgotPasswordViewModel(
    private val requestPasswordReset: RequestPasswordResetUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ForgotPasswordEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged ->
                _uiState.update {
                    // Editing the address invalidates the confirmation: the code that was
                    // requested went to the old one.
                    it.copy(
                        email = event.email,
                        errorMessage = null,
                        emailError = null,
                        sentTo = null
                    )
                }
            ForgotPasswordEvent.SendLinkClicked -> requestCode()
            ForgotPasswordEvent.ContinueClicked -> continueToCode()
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
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, sentTo = email) }
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

    private fun continueToCode() {
        val sentTo = _uiState.value.sentTo ?: return
        emitEffect(ForgotPasswordEffect.NavigateToVerification(sentTo))
    }

    private fun emitEffect(effect: ForgotPasswordEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
