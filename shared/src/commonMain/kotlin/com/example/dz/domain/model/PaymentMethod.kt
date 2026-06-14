package com.example.dz.domain.model

enum class PaymentBrand {
    Paypal,
    Visa,
    ApplePay,
    Mastercard,
    Unknown
}

data class PaymentMethod(
    val id: String,
    val brand: PaymentBrand,
    val title: String,
    val subtitle: String? = null,
    val isSelected: Boolean = false
)
