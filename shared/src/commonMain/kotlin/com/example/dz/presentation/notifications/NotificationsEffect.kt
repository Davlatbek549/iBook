package com.example.dz.presentation.notifications

sealed interface NotificationsEffect {
    data object NavigateBack : NotificationsEffect
    data class NavigateToChat(val notificationId: String) : NotificationsEffect
}
