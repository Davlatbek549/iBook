package com.example.dz.presentation.profile

sealed interface ProfileEffect {
    data object NavigateBack : ProfileEffect
    data object NavigateToNotifications : ProfileEffect
    data object NavigateToFriends : ProfileEffect
    data object NavigateToGoals : ProfileEffect
    data object NavigateToCollections : ProfileEffect
    data object NavigateToPurchases : ProfileEffect
    data object NavigateToMembership : ProfileEffect
    data object NavigateToSettings : ProfileEffect
}
