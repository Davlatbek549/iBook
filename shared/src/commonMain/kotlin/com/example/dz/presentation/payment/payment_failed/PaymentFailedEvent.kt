package com.example.dz.presentation.payment.payment_failed

sealed interface PaymentFailedEvent {
    data object CloseClicked : PaymentFailedEvent
    data object TryAgainClicked : PaymentFailedEvent
    data object ChangeMethodClicked : PaymentFailedEvent
    data object ContactSupportClicked : PaymentFailedEvent
}
