package com.example.dz.presentation.membership

sealed interface MembershipEffect {
    data object NavigateBack : MembershipEffect
    data object NavigateToPremium : MembershipEffect
}
