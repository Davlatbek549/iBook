package com.example.dz.presentation.payment.payment_methods

sealed interface PaymentMethodsEffect {
    data object NavigateBack : PaymentMethodsEffect
    data object NavigateToSuccess : PaymentMethodsEffect
    data object NavigateToFailure : PaymentMethodsEffect
}
