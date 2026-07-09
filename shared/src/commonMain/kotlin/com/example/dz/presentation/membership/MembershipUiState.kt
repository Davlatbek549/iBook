package com.example.dz.presentation.membership

data class MembershipUiState(
    val monthlyPrice: String = "$7.99",
    val yearlyPrice: String = "$59.99",
    val yearlyPerMonth: String = "$5.00 / month",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
