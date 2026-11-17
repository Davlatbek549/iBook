package com.example.dz.presentation.profile

sealed interface ProfileEffect {
    data object NavigateToSettings : ProfileEffect
    data object NavigateToFriends : ProfileEffect
    data object NavigateToInviteFriends : ProfileEffect
    data object NavigateToReadingGoal : ProfileEffect
    data object NavigateToMembership : ProfileEffect
    data object NavigateToNotifications : ProfileEffect
    data object NavigateToLogin : ProfileEffect
}
