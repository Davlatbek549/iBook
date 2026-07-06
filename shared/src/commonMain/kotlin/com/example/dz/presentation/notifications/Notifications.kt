package com.example.dz.presentation.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.ink.InkChip
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.notif_all
import dz.shared.generated.resources.notif_friends
import dz.shared.generated.resources.notif_mark_all
import dz.shared.generated.resources.notif_store
import dz.shared.generated.resources.notif_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState = NotificationsUiState(),
    onEvent: (NotificationsEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = inkColors()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        InkTopBar(
            title = stringResource(Res.string.notif_title),
            onBackClick = { onEvent(NotificationsEvent.BackClicked) },
            right = {
                Text(
                    text = stringResource(Res.string.notif_mark_all),
                    modifier = Modifier
                        .clickable { onEvent(NotificationsEvent.MarkAllReadClicked) }
                        .padding(6.dp),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colors.accent
                )
            },
            colors = colors
        )

        Row(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(stringResource(Res.string.notif_all), NotificationFilter.All, uiState.filter, onEvent, colors)
            FilterChip(stringResource(Res.string.notif_friends), NotificationFilter.Friends, uiState.filter, onEvent, colors)
            FilterChip(stringResource(Res.string.notif_store), NotificationFilter.Store, uiState.filter, onEvent, colors)
        }

        Column(modifier = Modifier.padding(horizontal = 22.dp)) {
            val items = uiState.visibleItems
            items.forEachIndexed { i, item ->
                NotificationRow(
                    item = item,
                    onClick = { onEvent(NotificationsEvent.NotificationClicked(item.id)) },
                    colors = colors
                )
                if (i < items.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = colors.line)
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    filter: NotificationFilter,
    selected: NotificationFilter,
    onEvent: (NotificationsEvent) -> Unit,
    colors: InkColors,
) {
    Box(modifier = Modifier.clickable { onEvent(NotificationsEvent.FilterSelected(filter)) }) {
        InkChip(text = text, solid = filter == selected, colors = colors)
    }
}

@Composable
private fun NotificationRow(
    item: NotificationUi,
    onClick: () -> Unit,
    colors: InkColors,
) {
    val bodyFont = inkBodyFontFamily()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        if (item.avatarRes != null) {
            Image(
                painter = painterResource(item.avatarRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm))
            )
        } else if (item.icon != null) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm))
                    .background(colors.alt),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = richText(colors, item.textParts),
                fontFamily = bodyFont,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = colors.inkSoft
            )
            Text(
                text = "${item.time} ago",
                modifier = Modifier.padding(top = 5.dp),
                fontFamily = bodyFont,
                fontSize = 10.5.sp,
                color = colors.muted
            )
        }
        if (item.coverRes != null) {
            Image(
                painter = painterResource(item.coverRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 30.dp, height = 44.dp)
                    .clip(RoundedCornerShape(InkShape.cover - 2.dp))
            )
        }
        if (item.unread) {
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(7.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(colors.accent)
            )
        }
    }
}

@Composable
private fun richText(colors: InkColors, parts: List<String>): AnnotatedString =
    buildAnnotatedString {
        parts.forEachIndexed { i, part ->
            if (i % 2 == 0) {
                withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) {
                    append(part)
                }
            } else {
                append(part)
            }
        }
    }

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun NotificationsScreenPreview() {
    NotificationsScreen()
}
