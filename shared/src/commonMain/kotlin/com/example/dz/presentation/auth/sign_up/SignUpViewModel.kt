package com.example.dz.presentation.auth.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.auth.SignUpUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUp: SignUpUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SignUpEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.FullNameChanged ->
                _uiState.update { it.copy(fullName = event.fullName, errorMessage = null) }
            is SignUpEvent.UsernameChanged ->
                _uiState.update { it.copy(username = event.username, errorMessage = null) }
            is SignUpEvent.EmailChanged ->
                _uiState.update { it.copy(email = event.email, errorMessage = null) }
            is SignUpEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.password, errorMessage = null) }
            SignUpEvent.CreateAccountClicked -> createAccount()
            SignUpEvent.SignInClicked -> emitEffect(SignUpEffect.NavigateToLogin)
            SignUpEvent.GoogleClicked,
            SignUpEvent.AppleClicked -> Unit
        }
    }

    private fun createAccount() {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value
            when (val result = signUp(state.fullName.trim(), state.email.trim(), state.password)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(SignUpEffect.NavigateToVerification)
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
