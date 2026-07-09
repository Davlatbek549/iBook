package com.example.dz.presentation.social.friends

sealed interface FriendListEvent {
    data object BackClicked : FriendListEvent
    data object AddFriendClicked : FriendListEvent
    data class QueryChanged(val query: String) : FriendListEvent
    data class FriendClicked(val friendId: String) : FriendListEvent
}
