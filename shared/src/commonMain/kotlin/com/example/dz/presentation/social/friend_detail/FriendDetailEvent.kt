package com.example.dz.presentation.social.friend_detail

sealed interface FriendDetailEvent {
    data object BackClicked : FriendDetailEvent
    data object OptionsClicked : FriendDetailEvent
    data object MessageClicked : FriendDetailEvent
    data object SeeAllClicked : FriendDetailEvent
}
