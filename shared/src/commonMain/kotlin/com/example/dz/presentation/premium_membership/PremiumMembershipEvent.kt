package com.example.dz.presentation.premium_membership

sealed interface PremiumMembershipEvent {
    data object BackClicked : PremiumMembershipEvent
    data object ChangeCardClicked : PremiumMembershipEvent
    data object SwitchPlanClicked : PremiumMembershipEvent
    data object BillingHistoryClicked : PremiumMembershipEvent
    data object CancelClicked : PremiumMembershipEvent
}
