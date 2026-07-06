package com.example.dz.presentation.payment.purchase_confirmation

sealed interface PurchaseConfirmationEffect {
    data object NavigateToPaymentMethods : PurchaseConfirmationEffect
    data object NavigateBack : PurchaseConfirmationEffect
}
