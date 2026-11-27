package com.example.dz.presentation.notifications

import com.example.dz.domain.model.AppNotification

enum class NotificationFilter { All, Friends, Goals, Store }

/**
 * Nothing in [AppNotification] says what a notification is about, so every real one lands in
 * [Other] — the Friends/Goals/Store chips are visual-complete for the design but will not
 * actually narrow anything until the server tags notifications with a type.
 */
enum class NotificationCategory { Friends, Goals, Store, Other }

data class NotificationsUiState(
    val filter: NotificationFilter = NotificationFilter.All,
    val items: List<NotificationUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val unreadCount: Int
        get() = items.count { it.unread }

    val visibleItems: List<NotificationUi>
        get() = when (filter) {
            NotificationFilter.All -> items
            NotificationFilter.Friends -> items.filter { it.category == NotificationCategory.Friends }
            NotificationFilter.Goals -> items.filter { it.category == NotificationCategory.Goals }
            NotificationFilter.Store -> items.filter { it.category == NotificationCategory.Store }
        }

    val todayItems: List<NotificationUi> get() = visibleItems.filter { it.isToday }
    val earlierItems: List<NotificationUi> get() = visibleItems.filter { !it.isToday }
}

data class NotificationUi(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val unread: Boolean = false,
    val isToday: Boolean = true,
    val category: NotificationCategory = NotificationCategory.Other
)

/**
 * [AppNotification.date] is already a relative string from the server ("2h", "1d"), not a
 * timestamp — there is nothing to group by calendar day with. Minutes/hours read as today;
 * anything counted in days reads as earlier, which is the same split the handoff's own mock
 * draws even though it labels the second half "Yesterday" for entries up to four days old.
 */
fun AppNotification.toNotificationUi(): NotificationUi {
    val relative = date.orEmpty()
    return NotificationUi(
        id = id,
        title = title,
        message = message,
        time = relative,
        unread = !isRead,
        isToday = relative.isBlank() || relative.endsWith("m") || relative.endsWith("h"),
    )
}
