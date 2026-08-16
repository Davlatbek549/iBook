package com.example.dz.presentation.notifications

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.dz.domain.model.AppNotification
import org.jetbrains.compose.resources.DrawableResource

enum class NotificationFilter { All, Friends, Store }

enum class NotificationCategory { Friends, Store, Other }

data class NotificationsUiState(
    val filter: NotificationFilter = NotificationFilter.All,
    val items: List<NotificationUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /** Items matching the currently selected filter. */
    val visibleItems: List<NotificationUi>
        get() = when (filter) {
            NotificationFilter.All -> items
            NotificationFilter.Friends -> items.filter { it.category == NotificationCategory.Friends }
            NotificationFilter.Store -> items.filter { it.category == NotificationCategory.Store }
        }
}

/**
 * Rich display model for a single notification row. The [textParts] alternate
 * emphasis: even indices are rendered bold, odd indices normal (see the screen's
 * `richText` helper).
 */
data class NotificationUi(
    val id: String,
    val textParts: List<String>,
    val time: String,
    val unread: Boolean = false,
    val category: NotificationCategory = NotificationCategory.Other,
    val avatarRes: DrawableResource? = null,
    val icon: ImageVector? = null,
    val coverRes: DrawableResource? = null
)

fun AppNotification.toNotificationUi(): NotificationUi =
    NotificationUi(
        id = id,
        textParts = listOf(title, " — $message"),
        time = date.orEmpty(),
        unread = !isRead
    )
