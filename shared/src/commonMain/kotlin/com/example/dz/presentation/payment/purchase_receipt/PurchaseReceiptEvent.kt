package com.example.dz.presentation.payment.purchase_receipt

sealed interface PurchaseReceiptEvent {
    data object BackClicked : PurchaseReceiptEvent
    data object ShareClicked : PurchaseReceiptEvent
    data object ReceiptClicked : PurchaseReceiptEvent
    data object ReadNowClicked : PurchaseReceiptEvent
}
