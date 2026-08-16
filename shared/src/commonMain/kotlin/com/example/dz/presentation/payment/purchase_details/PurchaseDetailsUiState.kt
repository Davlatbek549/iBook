package com.example.dz.presentation.payment.purchase_details

import com.example.dz.domain.model.Purchase

data class PurchaseDetailsUiState(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val bookPrice: String = "$0.00",
    val coinsDiscount: String = "$0.00",
    val tax: String = "$0.00",
    val total: String = "$0.00",
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
