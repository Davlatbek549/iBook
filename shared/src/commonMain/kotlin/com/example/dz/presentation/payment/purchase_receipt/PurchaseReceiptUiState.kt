package com.example.dz.presentation.payment.purchase_receipt

import com.example.dz.domain.model.Purchase

data class PurchaseReceiptUiState(
    val bookId: String = "",
    val coverUrl: String? = null,
    val isPaid: Boolean = false,
    val title: String = "",
    val author: String = "",
    val orderId: String = "",
    val dateLabel: String = "",
    val paymentLabel: String = "",
    val paymentEmail: String = "",
    val paymentTotal: String = "$0.00",
    val bookPrice: String = "$0.00",
    val coinsDiscount: String = "$0.00",
    val tax: String = "$0.00",
    val totalCharged: String = "$0.00",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

fun Purchase.toPurchaseReceiptUiState(): PurchaseReceiptUiState =
    PurchaseReceiptUiState(
        bookId = book.id,
        coverUrl = book.coverUrl,
        orderId = id,
        isPaid = status == com.example.dz.domain.model.PurchaseStatus.Success,
        title = book.title,
        author = book.authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        paymentLabel = paymentMethod?.title ?: "",
        paymentEmail = paymentMethod?.subtitle ?: "",
        paymentTotal = total,
        bookPrice = bookPrice,
        coinsDiscount = discount ?: "$0.00",
        tax = tax ?: "$0.00",
        totalCharged = total
    )
