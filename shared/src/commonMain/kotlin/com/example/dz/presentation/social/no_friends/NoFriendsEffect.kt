package com.example.dz.presentation.social.no_friends

sealed interface NoFriendsEffect {
    data object NavigateBack : NoFriendsEffect
    data object NavigateToInvite : NoFriendsEffect
}
