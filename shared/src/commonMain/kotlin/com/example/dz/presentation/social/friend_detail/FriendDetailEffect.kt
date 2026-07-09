package com.example.dz.presentation.social.friend_detail

sealed interface FriendDetailEffect {
    data object NavigateBack : FriendDetailEffect
    data class NavigateToChat(val friendId: String) : FriendDetailEffect
}
