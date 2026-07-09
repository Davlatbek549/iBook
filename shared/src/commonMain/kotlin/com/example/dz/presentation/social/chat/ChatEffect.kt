package com.example.dz.presentation.social.chat

sealed interface ChatEffect {
    data object NavigateBack : ChatEffect
}
