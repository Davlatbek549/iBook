package com.example.dz.presentation.social.invite_friends

sealed interface InviteFriendsEvent {
    data object BackClicked : InviteFriendsEvent
    data object ShareClicked : InviteFriendsEvent
    data class ContactActionClicked(val contactId: String) : InviteFriendsEvent
}
