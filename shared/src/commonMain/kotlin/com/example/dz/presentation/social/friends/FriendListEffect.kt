package com.example.dz.presentation.social.friends

sealed interface FriendListEffect {
    data object NavigateBack : FriendListEffect
    data object NavigateToInvite : FriendListEffect
    data class NavigateToFriendDetail(val friendId: String) : FriendListEffect
}
