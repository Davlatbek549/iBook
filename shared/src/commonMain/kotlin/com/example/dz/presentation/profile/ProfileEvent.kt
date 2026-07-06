package com.example.dz.presentation.profile

sealed interface ProfileEvent {
    data object BackClicked : ProfileEvent
    data object EditClicked : ProfileEvent
    data object NotificationsClicked : ProfileEvent
    data object FriendsClicked : ProfileEvent
    data object GoalsClicked : ProfileEvent
    data object CollectionsClicked : ProfileEvent
    data object PurchasesClicked : ProfileEvent
    data object MembershipClicked : ProfileEvent
    data object SettingsClicked : ProfileEvent
}
