package com.example.dz.presentation.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * There is no password-reset endpoint yet, so sending the link is a no-op that simply
 * advances to the verification step. Swap the body of [sendLink] for a use case once a
 * reset flow has a real data source.
 */
class ForgotPasswordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<ForgotPasswordEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged ->
                _uiState.update { it.copy(email = event.email, errorMessage = null) }
            ForgotPasswordEvent.SendLinkClicked -> sendLink()
            ForgotPasswordEvent.BackClicked -> emitEffect(ForgotPasswordEffect.NavigateBack)
        }
    }

    private fun sendLink() {
        emitEffect(ForgotPasswordEffect.NavigateToVerification)
    }

    private fun emitEffect(effect: ForgotPasswordEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
