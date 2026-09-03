package com.example.dz.presentation.payment.payment_methods

enum class PaymentBrand { Paypal, Visa, ApplePay }

data class PaymentMethodItem(
    val id: String,
    val brand: PaymentBrand,
    val title: String,
    val subtitle: String
)

val defaultPaymentMethods: List<PaymentMethodItem> = emptyList()

data class PaymentMethodsUiState(
    val methods: List<PaymentMethodItem> = defaultPaymentMethods,
    val selectedId: String = defaultPaymentMethods.firstOrNull()?.id.orEmpty(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
