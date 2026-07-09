package com.example.dz.presentation.payment.purchase_confirmation

sealed interface PurchaseConfirmationEvent {
    data object ConfirmClicked : PurchaseConfirmationEvent
    data object DismissClicked : PurchaseConfirmationEvent
}
