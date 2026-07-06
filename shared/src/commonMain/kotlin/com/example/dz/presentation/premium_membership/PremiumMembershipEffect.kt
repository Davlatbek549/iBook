package com.example.dz.presentation.premium_membership

sealed interface PremiumMembershipEffect {
    data object NavigateBack : PremiumMembershipEffect
    data object NavigateToSettings : PremiumMembershipEffect
}
