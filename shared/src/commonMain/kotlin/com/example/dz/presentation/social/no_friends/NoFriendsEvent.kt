package com.example.dz.presentation.social.no_friends

sealed interface NoFriendsEvent {
    data object BackClicked : NoFriendsEvent
    data object FindFriendsClicked : NoFriendsEvent
    data object InviteContactsClicked : NoFriendsEvent
}
