package com.example.dz.presentation.payment.payment_success

sealed interface PaymentSuccessEffect {
    data object NavigateToHome : PaymentSuccessEffect
    data object NavigateToReading : PaymentSuccessEffect
}
