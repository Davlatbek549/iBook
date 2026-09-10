package com.example.dz.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.User
import com.example.dz.domain.usecase.auth.LoginUseCase
import com.example.dz.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.dz.presentation.mvi.toPresentationMessage
import com.example.dz.presentation.mvi.validateSignIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    email: String = "",
    passwordJustReset: Boolean = false,
    private val login: LoginUseCase,
    private val signInWithGoogle: SignInWithGoogleUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        LoginUiState(email = email, passwordJustReset = passwordJustReset)
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged ->
                _uiState.update {
                    // Editing the address ends the reset notice: it described the address that
                    // was there when the reader arrived.
                    it.copy(
                        email = event.email,
                        errorMessage = null,
                        emailError = null,
                        passwordJustReset = false,
                    )
                }
            is LoginEvent.PasswordChanged ->
                _uiState.update {
                    it.copy(password = event.password, errorMessage = null, passwordError = null)
                }
            LoginEvent.SignInClicked -> signIn()
            LoginEvent.ForgotPasswordClicked ->
                emitEffect(LoginEffect.NavigateToForgotPassword(_uiState.value.email.trim()))
            LoginEvent.SignUpClicked -> emitEffect(LoginEffect.NavigateToSignUp)
            // The picker itself is launched by the screen, which is where the platform
            // handle lives; the view model only owns the busy flag around it.
            LoginEvent.GoogleClicked ->
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            is LoginEvent.GoogleTokenReceived -> exchangeGoogleToken(event.idToken)
            is LoginEvent.GoogleSignInFailed ->
                _uiState.update { it.copy(isLoading = false, errorMessage = event.message) }
            LoginEvent.AppleClicked -> Unit
        }
    }

    private fun signIn() {
        if (_uiState.value.isLoading) return

        val errors = _uiState.value.let { validateSignIn(it.email.trim(), it.password) }
        if (!errors.isValid) {
            _uiState.update {
                it.copy(emailError = errors.email, passwordError = errors.password)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, emailError = null, passwordError = null)
            }
            val state = _uiState.value
            when (val result = login(state.email.trim(), state.password)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(destinationFor(result.data, fallbackEmail = state.email.trim()))
                }
                is AppResult.Error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                    }
            }
        }
    }

    private fun exchangeGoogleToken(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = signInWithGoogle(idToken)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(destinationFor(result.data, fallbackEmail = ""))
                }
                is AppResult.Error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.error.toPresentationMessage())
                    }
            }
        }
    }

    /**
     * Where a new session opens — the same rule the splash applies to a restored one. Sign-in used
     * to open Home unconditionally, so an unverified account was let in once and only met the code
     * screen on the next launch, which looked like the splash had gone wrong.
     */
    private fun destinationFor(user: User, fallbackEmail: String): LoginEffect =
        if (user.emailVerified) LoginEffect.NavigateToHome
        else LoginEffect.NavigateToVerification(user.email?.takeIf { it.isNotBlank() } ?: fallbackEmail)

    private fun emitEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
