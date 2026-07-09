package com.example.dz.presentation.membership

sealed interface MembershipEvent {
    data object BackClicked : MembershipEvent
    data object StartTrialClicked : MembershipEvent
}
