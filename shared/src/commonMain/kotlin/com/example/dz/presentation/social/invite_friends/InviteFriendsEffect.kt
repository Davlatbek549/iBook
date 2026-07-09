package com.example.dz.presentation.social.invite_friends

sealed interface InviteFriendsEffect {
    data object NavigateBack : InviteFriendsEffect
    data object NavigateToDiscover : InviteFriendsEffect
}
