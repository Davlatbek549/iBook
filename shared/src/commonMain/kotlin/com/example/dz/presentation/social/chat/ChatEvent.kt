package com.example.dz.presentation.social.chat

sealed interface ChatEvent {
    data object BackClicked : ChatEvent
    data object OptionsClicked : ChatEvent
    data object SendClicked : ChatEvent
    data class ComposerChanged(val text: String) : ChatEvent
}
