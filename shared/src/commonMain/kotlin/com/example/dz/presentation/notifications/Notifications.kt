package com.example.dz.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicAvatar
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.presentation.common.uniqueLazyKeys
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.nav_back
import dz.shared.generated.resources.notif_all
import dz.shared.generated.resources.notif_earlier
import dz.shared.generated.resources.notif_empty
import dz.shared.generated.resources.notif_friends
import dz.shared.generated.resources.notif_goals
import dz.shared.generated.resources.notif_mark_all
import dz.shared.generated.resources.notif_store
import dz.shared.generated.resources.notif_time_ago
import dz.shared.generated.resources.notif_title
import dz.shared.generated.resources.notif_today
import org.jetbrains.compose.resources.stringResource

/**
 * Notifications — unread count, four filter chips, then everything grouped into Today and
 * Earlier. Every row leads with the same initial-circle treatment the rest of the app uses for
 * identity: nothing in the data says what kind of event a notification is, so there is no real
 * per-type icon to draw instead.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState = NotificationsUiState(),
    onEvent: (NotificationsEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "header") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OrganicCircleIconButton(
                        icon = OrganicIcons.ChevronLeft,
                        onClick = { onEvent(NotificationsEvent.BackClicked) },
                        contentDescription = stringResource(Res.string.nav_back),
                        size = 38.dp,
                        iconSize = 18.dp,
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.notif_title),
                            modifier = Modifier.weight(1f, fill = false),
                            fontFamily = organicHeadingFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 30.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = OrganicColors.text
                        )
                        if (uiState.unreadCount > 0) {
                            Text(
                                text = uiState.unreadCount.toString(),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(OrganicShape.pill))
                                    .background(OrganicColors.accent)
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                fontFamily = organicBodyFontFamily(),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = stringResource(Res.string.notif_mark_all),
                        modifier = Modifier.clickable(role = Role.Button) { onEvent(NotificationsEvent.MarkAllReadClicked) },
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OrganicColors.accent700
                    )
                }
            }

            item(key = "filters") {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    item {
                        FilterChip(stringResource(Res.string.notif_all), NotificationFilter.All, uiState.filter) {
                            onEvent(NotificationsEvent.FilterSelected(NotificationFilter.All))
                        }
                    }
                    item {
                        FilterChip(stringResource(Res.string.notif_friends), NotificationFilter.Friends, uiState.filter) {
                            onEvent(NotificationsEvent.FilterSelected(NotificationFilter.Friends))
                        }
                    }
                    item {
                        FilterChip(stringResource(Res.string.notif_goals), NotificationFilter.Goals, uiState.filter) {
                            onEvent(NotificationsEvent.FilterSelected(NotificationFilter.Goals))
                        }
                    }
                    item {
                        FilterChip(stringResource(Res.string.notif_store), NotificationFilter.Store, uiState.filter) {
                            onEvent(NotificationsEvent.FilterSelected(NotificationFilter.Store))
                        }
                    }
                }
            }

            if (uiState.visibleItems.isEmpty()) {
                item(key = "empty") {
                    Text(
                        text = stringResource(Res.string.notif_empty),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 13.sp,
                        color = OrganicColors.neutral700
                    )
                }
            }

            if (uiState.todayItems.isNotEmpty()) {
                item(key = "today-label") { GroupLabel(stringResource(Res.string.notif_today)) }
                val todayKeys = uiState.todayItems.uniqueLazyKeys { it.id }
                items(uiState.todayItems.size, key = { todayKeys[it] }) { index ->
                    val item = uiState.todayItems[index]
                    NotificationRow(
                        item = item,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        onClick = { onEvent(NotificationsEvent.NotificationClicked(item.id)) },
                    )
                }
            }

            if (uiState.earlierItems.isNotEmpty()) {
                item(key = "earlier-label") { GroupLabel(stringResource(Res.string.notif_earlier)) }
                val earlierKeys = uiState.earlierItems.uniqueLazyKeys { it.id }
                items(uiState.earlierItems.size, key = { earlierKeys[it] }) { index ->
                    val item = uiState.earlierItems[index]
                    NotificationRow(
                        item = item,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        onClick = { onEvent(NotificationsEvent.NotificationClicked(item.id)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupLabel(text: String) {
    OrganicKicker(
        text = text,
        color = OrganicColors.neutral600,
        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
    )
}

@Composable
private fun FilterChip(
    text: String,
    filter: NotificationFilter,
    selected: NotificationFilter,
    onClick: () -> Unit,
) {
    val isSelected = filter == selected
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(if (isSelected) OrganicColors.accent else OrganicColors.neutral200)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 9.dp),
        fontFamily = organicBodyFontFamily(),
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
        fontSize = 13.sp,
        color = if (isSelected) Color.White else OrganicColors.text
    )
}

/** Unread rows sit on a filled card; read ones fade to bare rows — the design's own distinction. */
@Composable
private fun NotificationRow(
    item: NotificationUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(OrganicShape.radiusLg))
            .background(if (item.unread) OrganicColors.neutral100 else Color.Transparent)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalAlignment = Alignment.Top
    ) {
        OrganicAvatar(name = item.title, size = 40.dp, fontSize = 15.sp)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = item.title,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                color = OrganicColors.text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.message,
                fontFamily = organicBodyFontFamily(),
                fontSize = 13.sp,
                lineHeight = 18.85.sp,
                color = OrganicColors.neutral700,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(Res.string.notif_time_ago, item.time),
                fontFamily = organicBodyFontFamily(),
                fontSize = 11.sp,
                color = OrganicColors.neutral600
            )
            if (item.unread) {
                Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(OrganicColors.accent))
            }
        }
    }
}

@Preview
@Composable
private fun NotificationsScreenPreview() {
    NotificationsScreen()
}
