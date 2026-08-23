package com.example.dz.presentation.auth.new_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.presentation.mvi.validateNewPassword
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Last step of a password reset: the code proved the reader owns the mailbox, this changes what
 * the account's password actually is.
 *
 * Nothing is saved yet — the server has no reset endpoint — so [save] validates and routes.
 * The single call to add lands in the same place, with [NewPasswordUiState.email] and the new
 * password already to hand.
 */
class NewPasswordViewModel(
    email: String = "",
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewPasswordUiState(email = email))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<NewPasswordEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: NewPasswordEvent) {
        when (event) {
            is NewPasswordEvent.PasswordChanged ->
                _uiState.update {
                    it.copy(
                        password = event.password,
                        errorMessage = null,
                        confirmationError = null
                    )
                }
            is NewPasswordEvent.ConfirmationChanged ->
                _uiState.update {
                    it.copy(
                        confirmation = event.confirmation,
                        errorMessage = null,
                        confirmationError = null
                    )
                }
            NewPasswordEvent.SaveClicked -> save()
            NewPasswordEvent.BackClicked -> emitEffect(NewPasswordEffect.NavigateBack)
        }
    }

    private fun save() {
        val state = _uiState.value
        if (state.isLoading) return

        val invalid = validateNewPassword(state.password, state.confirmation)
        if (invalid != null) {
            _uiState.update { it.copy(confirmationError = invalid) }
            return
        }

        emitEffect(NewPasswordEffect.NavigateToHome)
    }

    private fun emitEffect(effect: NewPasswordEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
