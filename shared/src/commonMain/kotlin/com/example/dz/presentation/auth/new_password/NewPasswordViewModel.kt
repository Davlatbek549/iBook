package com.example.dz.presentation.auth.new_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.auth.ResetPasswordUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
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
 * [code] is held here rather than in [NewPasswordUiState] because the screen never draws it — it
 * was typed on the screen before this one and is only in hand to be spent.
 *
 * This is also where a wrong code surfaces. The step before carries it without checking, so the
 * refusal for a bad code and the refusal for an expired one both arrive at this screen.
 */
class NewPasswordViewModel(
    email: String = "",
    private val code: String = "",
    private val resetPassword: ResetPasswordUseCase,
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
            NewPasswordEvent.SignInClicked -> emitEffect(NewPasswordEffect.NavigateToLogin)
            // Once the password has changed there is nothing behind this screen worth returning
            // to: the code that got here has been spent, and the screen that collected it can
            // only refuse. Back therefore means the same as the button — on to sign-in.
            NewPasswordEvent.BackClicked -> emitEffect(
                if (_uiState.value.isSaved) NewPasswordEffect.NavigateToLogin
                else NewPasswordEffect.NavigateBack
            )
        }
    }

    private fun save() {
        val state = _uiState.value
        // Saved is terminal: the code has been spent, so a second save could only fail.
        if (state.isLoading || state.isSaved) return

        val invalid = validateNewPassword(state.password, state.confirmation)
        if (invalid != null) {
            _uiState.update { it.copy(confirmationError = invalid) }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, confirmationError = null)
            }
            when (val result = resetPassword(state.email, code, state.password)) {
                is AppResult.Success ->
                    _uiState.update { it.copy(isLoading = false, isSaved = true) }
                is AppResult.Error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.toPresentationMessage(),
                        )
                    }
            }
        }
    }

    private fun emitEffect(effect: NewPasswordEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
