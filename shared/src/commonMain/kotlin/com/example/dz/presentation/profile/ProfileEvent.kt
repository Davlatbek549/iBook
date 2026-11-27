package com.example.dz.presentation.profile

sealed interface ProfileEvent {
    data object SettingsClicked : ProfileEvent
    data object FriendsClicked : ProfileEvent
    data object InviteClicked : ProfileEvent
    data object ReadingGoalClicked : ProfileEvent
    data object MembershipClicked : ProfileEvent
    data object NotificationsClicked : ProfileEvent
    data object SignOutClicked : ProfileEvent
}
