package com.example.dz.presentation.payment.purchase_details

sealed interface PurchaseDetailsEffect {
    data object NavigateBack : PurchaseDetailsEffect
    data object NavigateToPaymentMethods : PurchaseDetailsEffect
    data object NavigateToConfirmation : PurchaseDetailsEffect
}
