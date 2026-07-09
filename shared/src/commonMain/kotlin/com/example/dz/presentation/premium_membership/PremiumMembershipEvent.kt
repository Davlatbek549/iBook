package com.example.dz.presentation.premium_membership

sealed interface PremiumMembershipEvent {
    data object BackClicked : PremiumMembershipEvent
    data object BillingDateClicked : PremiumMembershipEvent
    data object BillingHistoryClicked : PremiumMembershipEvent
    data object CancelClicked : PremiumMembershipEvent
}
