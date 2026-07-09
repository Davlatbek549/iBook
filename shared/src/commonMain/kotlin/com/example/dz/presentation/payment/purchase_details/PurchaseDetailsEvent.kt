package com.example.dz.presentation.payment.purchase_details

sealed interface PurchaseDetailsEvent {
    data object BackClicked : PurchaseDetailsEvent
    data object ApplyCoinsClicked : PurchaseDetailsEvent
    data object ChangePaymentClicked : PurchaseDetailsEvent
    data object PayNowClicked : PurchaseDetailsEvent
}
