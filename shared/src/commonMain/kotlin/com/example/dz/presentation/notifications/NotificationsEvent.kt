package com.example.dz.presentation.notifications

sealed interface NotificationsEvent {
    data object BackClicked : NotificationsEvent
    data object MarkAllReadClicked : NotificationsEvent
    data class FilterSelected(val filter: NotificationFilter) : NotificationsEvent
    data class NotificationClicked(val notificationId: String) : NotificationsEvent
}
