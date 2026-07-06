package com.example.dz.presentation.payment.purchase_details

import com.example.dz.domain.model.Purchase

data class PurchaseDetailsUiState(
    val bookId: String = "",
    val title: String = "Mexican Gothic",
    val author: String = "Silvia Moreno-Garcia",
    val bookPrice: String = "$12.99",
    val coinsDiscount: String = "−$2.40",
    val tax: String = "$0.84",
    val total: String = "$11.43",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

fun Purchase.toPurchaseDetailsUiState(): PurchaseDetailsUiState =
    PurchaseDetailsUiState(
        bookId = book.id,
        title = book.title,
        author = book.authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        bookPrice = bookPrice,
        coinsDiscount = discount ?: "$0.00",
        tax = tax ?: "$0.00",
        total = total
    )
