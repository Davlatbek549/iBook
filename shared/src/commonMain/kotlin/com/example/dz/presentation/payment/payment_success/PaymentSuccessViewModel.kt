package com.example.dz.presentation.payment.payment_success

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentSuccessViewModel(checkout: com.example.dz.presentation.payment.common.CheckoutSession) : ViewModel() {
    private val _uiState = MutableStateFlow(checkout.purchase?.let { order ->
        PaymentSuccessUiState(bookId = order.book.id, title = order.book.title,
            author = order.book.authors.firstOrNull()?.name.orEmpty(), coverUrl = order.book.coverUrl, total = order.total)
    } ?: PaymentSuccessUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PaymentSuccessEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: PaymentSuccessEvent) {
        when (event) {
            PaymentSuccessEvent.BackToStoreClicked -> emitEffect(PaymentSuccessEffect.NavigateToHome)
            PaymentSuccessEvent.StartReadingClicked -> if (_uiState.value.bookId.isNotBlank()) {
                emitEffect(PaymentSuccessEffect.NavigateToReading(_uiState.value.bookId))
            }
            PaymentSuccessEvent.ViewReceiptClicked -> if (_uiState.value.bookId.isNotBlank()) {
                emitEffect(PaymentSuccessEffect.NavigateToReceipt(_uiState.value.bookId))
            }
        }
    }

    private fun emitEffect(effect: PaymentSuccessEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
