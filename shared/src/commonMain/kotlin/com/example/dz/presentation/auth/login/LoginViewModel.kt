package com.example.dz.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.usecase.auth.LoginUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val login: LoginUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged ->
                _uiState.update { it.copy(email = event.email, errorMessage = null) }
            is LoginEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.password, errorMessage = null) }
            LoginEvent.SignInClicked -> signIn()
            LoginEvent.ForgotPasswordClicked -> emitEffect(LoginEffect.NavigateToForgotPassword)
            LoginEvent.SignUpClicked -> emitEffect(LoginEffect.NavigateToSignUp)
            LoginEvent.GoogleClicked,
            LoginEvent.AppleClicked -> Unit
        }
    }

    private fun signIn() {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val state = _uiState.value
            when (val result = login(state.email.trim(), state.password)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(LoginEffect.NavigateToHome)
                }
                is AppResult.Error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                    }
            }
        }
    }

    private fun emitEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
