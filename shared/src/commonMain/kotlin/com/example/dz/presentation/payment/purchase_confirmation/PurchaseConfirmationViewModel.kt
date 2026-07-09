package com.example.dz.presentation.payment.purchase_confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PurchaseConfirmationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseConfirmationUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PurchaseConfirmationEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: PurchaseConfirmationEvent) {
        when (event) {
            PurchaseConfirmationEvent.ConfirmClicked -> emitEffect(PurchaseConfirmationEffect.NavigateToPaymentMethods)
            PurchaseConfirmationEvent.DismissClicked -> emitEffect(PurchaseConfirmationEffect.NavigateBack)
        }
    }

    private fun emitEffect(effect: PurchaseConfirmationEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
