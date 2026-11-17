package com.example.dz.presentation.premium_membership

data class PremiumMembershipUiState(
    val hasActiveMembership: Boolean = false,
    val planTitle: String = "",
    val planPrice: String = "",
    val benefits: List<String> = emptyList(),
    val alternatePlanTitle: String? = null,
    val alternatePlanPrice: String? = null,
    val paymentMethodLabel: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
