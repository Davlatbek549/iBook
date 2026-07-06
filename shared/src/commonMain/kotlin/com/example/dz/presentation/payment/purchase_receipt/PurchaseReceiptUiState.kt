package com.example.dz.presentation.payment.purchase_receipt

import com.example.dz.domain.model.Purchase

data class PurchaseReceiptUiState(
    val bookId: String = "",
    val title: String = "Mexican Gothic",
    val author: String = "Silvia Moreno-Garcia",
    val orderId: String = "Order DZ-20614",
    val dateLabel: String = "June 8, 2026 · 14:12",
    val paymentLabel: String = "PayPal",
    val paymentEmail: String = "amelia@hartwell.co",
    val paymentTotal: String = "$11.43",
    val bookPrice: String = "$12.99",
    val coinsDiscount: String = "−$2.40",
    val tax: String = "$0.84",
    val totalCharged: String = "$11.43",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

fun Purchase.toPurchaseReceiptUiState(): PurchaseReceiptUiState =
    PurchaseReceiptUiState(
        bookId = book.id,
        title = book.title,
        author = book.authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        paymentLabel = paymentMethod?.title ?: "PayPal",
        paymentEmail = paymentMethod?.subtitle ?: "amelia@hartwell.co",
        paymentTotal = total,
        bookPrice = bookPrice,
        coinsDiscount = discount ?: "$0.00",
        tax = tax ?: "$0.00",
        totalCharged = total
    )
