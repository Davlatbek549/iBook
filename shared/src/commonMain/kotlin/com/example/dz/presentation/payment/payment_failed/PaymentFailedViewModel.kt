package com.example.dz.presentation.payment.payment_failed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentFailedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentFailedUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PaymentFailedEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: PaymentFailedEvent) {
        when (event) {
            PaymentFailedEvent.CloseClicked -> emitEffect(PaymentFailedEffect.NavigateBack)
            PaymentFailedEvent.TryAgainClicked -> emitEffect(PaymentFailedEffect.NavigateToPurchaseDetails)
            PaymentFailedEvent.ChangeMethodClicked -> emitEffect(PaymentFailedEffect.NavigateToPaymentMethods)
            PaymentFailedEvent.ContactSupportClicked -> Unit
        }
    }

    private fun emitEffect(effect: PaymentFailedEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
