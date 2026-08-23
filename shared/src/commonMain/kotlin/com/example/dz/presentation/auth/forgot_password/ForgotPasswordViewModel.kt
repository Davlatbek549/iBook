package com.example.dz.presentation.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.presentation.mvi.validateEmail
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The address is checked here and the screen confirms in place, but nothing is actually mailed:
 * the server has no password-reset endpoint yet. [requestCode] is the single place that changes
 * when it does — everything around it is already shaped for the real call.
 *
 * The confirmation deliberately does not say whether the address is registered. Answering that
 * would turn this screen into a way to test which emails have accounts.
 */
class ForgotPasswordViewModel : ViewModel() {
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

        val email = _uiState.value.email.trim()
        val invalid = validateEmail(email)
        if (invalid != null) {
            _uiState.update { it.copy(emailError = invalid) }
            return
        }

        _uiState.update { it.copy(sentTo = email, errorMessage = null, emailError = null) }
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
