package com.example.dz.screens.notification

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkChip
import com.example.dz.app_components.ink.InkTopBar
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.img_maria_renzy
import dz.shared.generated.resources.notif_all
import dz.shared.generated.resources.notif_friends
import dz.shared.generated.resources.notif_mark_all
import dz.shared.generated.resources.notif_store
import dz.shared.generated.resources.notif_title
import dz.shared.generated.resources.profile_2
import dz.shared.generated.resources.profile_3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private class NotificationItem(
    val avatarRes: DrawableResource? = null,
    val icon: ImageVector? = null,
    val time: String,
    val unread: Boolean = false,
    val coverRes: DrawableResource? = null,
    val text: @Composable (InkColors) -> AnnotatedString
)

private val notifications = listOf(
    NotificationItem(
        avatarRes = Res.drawable.profile_2,
        time = "2m",
        unread = true,
        coverRes = Res.drawable.book_cover_3,
        text = { c -> richText(c, "Patricia Lane", " sent you a book — ", "Red at the Bone") }
    ),
    NotificationItem(
        avatarRes = Res.drawable.img_maria_renzy,
        time = "1h",
        unread = true,
        text = { c -> richText(c, "Maria Renzy", " accepted your friend request") }
    ),
    NotificationItem(
        avatarRes = Res.drawable.profile_3,
        time = "3h",
        text = { c -> richText(c, "Daniel Moreau", " reviewed ", "Bestiary", " · 4★") }
    ),
    NotificationItem(
        icon = InkIcons.Stats,
        time = "9h",
        text = { c -> richText(c, "Goal reached", " — 30 minutes today. 21-day streak.") }
    ),
    NotificationItem(
        icon = InkIcons.Tag,
        time = "1d",
        coverRes = Res.drawable.book_cover_2,
        text = { c -> richText(c, "Price drop", " — ", "The Archer", " is now $5.99") }
    )
)

@Composable
fun Notifications(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onReadFilterClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    NotificationsScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onReadFilterClick = onReadFilterClick,
        onChatClick = onChatClick
    )
}

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onReadFilterClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
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
            onBackClick = onBackClick,
            right = {
                Text(
                    text = stringResource(Res.string.notif_mark_all),
                    modifier = Modifier
                        .clickable(onClick = onReadFilterClick)
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
            InkChip(text = stringResource(Res.string.notif_all), solid = true, colors = colors)
            InkChip(text = stringResource(Res.string.notif_friends), colors = colors)
            InkChip(text = stringResource(Res.string.notif_store), colors = colors)
        }

        Column(modifier = Modifier.padding(horizontal = 22.dp)) {
            notifications.forEachIndexed { i, item ->
                NotificationRow(item = item, onClick = onChatClick, colors = colors)
                if (i < notifications.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = colors.line)
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(
    item: NotificationItem,
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
                text = item.text(colors),
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
private fun richText(colors: InkColors, vararg parts: String): AnnotatedString =
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
