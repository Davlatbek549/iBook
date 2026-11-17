package com.example.dz.presentation.payment.purchase_confirmation

data class PurchaseConfirmationUiState(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val coverUrl: String? = null,
    val bookPrice: String = "$0.00",
    val discount: String = "$0.00",
    val tax: String = "$0.00",
    val total: String = "$0.00",
)
