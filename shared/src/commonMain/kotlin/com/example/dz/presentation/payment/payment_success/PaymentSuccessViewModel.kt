package com.example.dz.presentation.payment.payment_success

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentSuccessViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentSuccessUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PaymentSuccessEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: PaymentSuccessEvent) {
        when (event) {
            PaymentSuccessEvent.BackToStoreClicked -> emitEffect(PaymentSuccessEffect.NavigateToHome)
            PaymentSuccessEvent.StartReadingClicked -> emitEffect(PaymentSuccessEffect.NavigateToReading)
        }
    }

    private fun emitEffect(effect: PaymentSuccessEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
