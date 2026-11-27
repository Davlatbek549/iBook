package com.example.dz.presentation.payment.payment_success

data class PaymentSuccessUiState(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val coverUrl: String? = null,
    val total: String = "$0.00",
    val email: String = "",
)
