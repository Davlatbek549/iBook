package com.example.dz.presentation.payment.purchase_receipt

sealed interface PurchaseReceiptEffect {
    data object NavigateBack : PurchaseReceiptEffect
    data class NavigateToReading(val bookId: String) : PurchaseReceiptEffect
}
