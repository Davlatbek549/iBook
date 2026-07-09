package com.example.dz.presentation.premium_membership

data class PremiumMembershipUiState(
    val memberName: String = "Amelia Hartwell",
    val hasActiveMembership: Boolean = true,
    val renewalPrice: String = "$59.99 / yr",
    val booksReadThisYear: String = "18 books",
    val amountSaved: String = "$112",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
