package com.example.dz.presentation.payment.payment_success

sealed interface PaymentSuccessEvent {
    data object BackToStoreClicked : PaymentSuccessEvent
    data object StartReadingClicked : PaymentSuccessEvent
}
