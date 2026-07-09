package com.example.dz.presentation.payment.payment_failed

sealed interface PaymentFailedEffect {
    data object NavigateBack : PaymentFailedEffect
    data object NavigateToPaymentMethods : PaymentFailedEffect
    data object NavigateToPurchaseDetails : PaymentFailedEffect
}
