package com.example.dz.presentation.payment.payment_methods

sealed interface PaymentMethodsEvent {
    data object BackClicked : PaymentMethodsEvent
    data object AddMethodClicked : PaymentMethodsEvent
    data class MethodSelected(val methodId: String) : PaymentMethodsEvent
}
