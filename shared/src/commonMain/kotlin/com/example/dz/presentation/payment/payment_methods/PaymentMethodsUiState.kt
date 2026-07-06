package com.example.dz.presentation.payment.payment_methods

enum class PaymentBrand { Paypal, Visa, ApplePay }

data class PaymentMethodItem(
    val id: String,
    val brand: PaymentBrand,
    val title: String,
    val subtitle: String
)

val defaultPaymentMethods = listOf(
    PaymentMethodItem("paypal", PaymentBrand.Paypal, "PayPal", "amelia@hartwell.co"),
    PaymentMethodItem("visa", PaymentBrand.Visa, "Visa ·· 4129", "Expires 08/27"),
    PaymentMethodItem("apple", PaymentBrand.ApplePay, "Apple Pay", "Device wallet")
)

data class PaymentMethodsUiState(
    val methods: List<PaymentMethodItem> = defaultPaymentMethods,
    val selectedId: String = defaultPaymentMethods.first().id,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
