package com.example.dz.presentation.notifications

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.dz.designsystem.components.icons.InkIcons
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.img_maria_renzy
import dz.shared.generated.resources.profile_2
import dz.shared.generated.resources.profile_3
import org.jetbrains.compose.resources.DrawableResource

enum class NotificationFilter { All, Friends, Store }

enum class NotificationCategory { Friends, Store, Other }

data class NotificationsUiState(
    val filter: NotificationFilter = NotificationFilter.All,
    val items: List<NotificationUi> = defaultNotifications,
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

val defaultNotifications: List<NotificationUi> = listOf(
    NotificationUi(
        id = "patricia-sent-book",
        avatarRes = Res.drawable.profile_2,
        time = "2m",
        unread = true,
        category = NotificationCategory.Friends,
        coverRes = Res.drawable.book_cover_3,
        textParts = listOf("Patricia Lane", " sent you a book — ", "Red at the Bone")
    ),
    NotificationUi(
        id = "maria-accepted",
        avatarRes = Res.drawable.img_maria_renzy,
        time = "1h",
        unread = true,
        category = NotificationCategory.Friends,
        textParts = listOf("Maria Renzy", " accepted your friend request")
    ),
    NotificationUi(
        id = "daniel-reviewed",
        avatarRes = Res.drawable.profile_3,
        time = "3h",
        category = NotificationCategory.Friends,
        textParts = listOf("Daniel Moreau", " reviewed ", "Bestiary", " · 4★")
    ),
    NotificationUi(
        id = "goal-reached",
        icon = InkIcons.Stats,
        time = "9h",
        category = NotificationCategory.Other,
        textParts = listOf("Goal reached", " — 30 minutes today. 21-day streak.")
    ),
    NotificationUi(
        id = "price-drop",
        icon = InkIcons.Tag,
        time = "1d",
        category = NotificationCategory.Store,
        coverRes = Res.drawable.book_cover_2,
        textParts = listOf("Price drop", " — ", "The Archer", " is now $5.99")
    )
)
