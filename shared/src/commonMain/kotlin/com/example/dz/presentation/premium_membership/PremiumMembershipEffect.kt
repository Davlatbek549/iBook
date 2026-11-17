package com.example.dz.presentation.premium_membership

sealed interface PremiumMembershipEffect {
    data object NavigateBack : PremiumMembershipEffect
    data object NavigateToPaymentMethods : PremiumMembershipEffect
    data object NavigateToPaymentConfirmation : PremiumMembershipEffect
    data object NavigateToBillingHistory : PremiumMembershipEffect
    // Cancelling has nowhere real to land yet — this mirrors the Ink screen's own choice.
    data object NavigateToSettings : PremiumMembershipEffect
}
