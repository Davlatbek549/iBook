package com.example.dz.screens.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.ColorCategoryBiographies
import com.example.dz.theme.ColorCategoryCooking
import com.example.dz.theme.ColorProfileAround
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class NotificationUiState(
    val id: String,
    val type: NotificationType,
    val title: String,
    val time: String,
    val unread: Boolean = true
)

enum class NotificationType {
    Premium,
    Voucher,
    Message
}

private data class NotificationMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val topButtonSize: Dp,
    val topIconSize: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val listTopSpacing: Dp,
    val itemSpacing: Dp,
    val indicatorColumnWidth: Dp,
    val indicatorSize: Dp,
    val iconSize: Dp,
    val iconCorner: Dp,
    val iconInnerSize: Dp,
    val contentGap: Dp,
    val titleTextSize: TextUnit,
    val titleLineHeight: TextUnit,
    val timeTopSpacing: Dp,
    val timeTextSize: TextUnit,
    val profileSize: Dp,
    val profileCorner: Dp,
    val profileBorderWidth: Dp,
    val chatBadgeSize: Dp,
    val chatBadgeIconSize: Dp,
    val messageBubbleHeight: Dp,
    val messageBubbleCorner: Dp,
    val messageBubbleHorizontalPadding: Dp,
    val messageTopPadding: Dp
)

private data class NotificationEmptyMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val topButtonSize: Dp,
    val topIconSize: Dp,
    val actionGap: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val illustrationTopSpacing: Dp,
    val illustrationSize: Dp,
    val messageTopSpacing: Dp,
    val messageSize: TextUnit,
    val messageLineHeight: TextUnit
)

private val previewNotifications = listOf(
    NotificationUiState(
        id = "premium-one",
        type = NotificationType.Premium,
        title = "You have 12 Premium books left to read.\nRemember this!",
        time = "just now"
    ),
    NotificationUiState(
        id = "voucher-one",
        type = NotificationType.Voucher,
        title = "Get over 20 Premium books on book\nstore with 20%off voucher!!",
        time = "Wed, January, 2021"
    ),
    NotificationUiState(
        id = "message-one",
        type = NotificationType.Message,
        title = "Horror or comic?",
        time = "Wed, January, 2021"
    ),
    NotificationUiState(
        id = "premium-two",
        type = NotificationType.Premium,
        title = "You have 12 Premium books left to read.\nRemember this!",
        time = "just now"
    ),
    NotificationUiState(
        id = "voucher-two",
        type = NotificationType.Voucher,
        title = "Get over 20 Premium books on book\nstore with 20%off voucher!!",
        time = "Wed, January, 2021"
    ),
    NotificationUiState(
        id = "message-two",
        type = NotificationType.Message,
        title = "Horror or comic?",
        time = "Wed, January, 2021"
    ),
    NotificationUiState(
        id = "message-three",
        type = NotificationType.Message,
        title = "Horror or comic?",
        time = "Wed, January, 2021"
    )
)

@Composable
fun Notifications(
    modifier: Modifier = Modifier,
    notifications: List<NotificationUiState> = previewNotifications,
    onBackClick: () -> Unit = {},
    onReadFilterClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    NotificationsScreen(
        modifier = modifier,
        notifications = notifications,
        onBackClick = onBackClick,
        onReadFilterClick = onReadFilterClick,
        onChatClick = onChatClick
    )
}

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    notifications: List<NotificationUiState> = previewNotifications,
    onBackClick: () -> Unit = {},
    onReadFilterClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    if (notifications.isEmpty()) {
        NotificationsEmptyScreen(
            modifier = modifier,
            onBackClick = onBackClick,
            onReadFilterClick = onReadFilterClick,
            onChatClick = onChatClick
        )
        return
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberNotificationMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            NotificationsTopActions(
                metrics = metrics,
                onBackClick = onBackClick,
                onReadFilterClick = onReadFilterClick,
                onChatClick = onChatClick
            )

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = "Notifications",
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize * 1.08f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.listTopSpacing))

            Column(
                verticalArrangement = Arrangement.spacedBy(metrics.itemSpacing)
            ) {
                notifications.forEach { notification ->
                    NotificationRow(
                        notification = notification,
                        metrics = metrics
                    )
                }
            }

            Spacer(modifier = Modifier.height(34.dp))
        }
    }
}

@Composable
fun NotificationsEmptyScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onReadFilterClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberNotificationEmptyMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = metrics.horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NotificationCircleButton(
                    buttonSize = metrics.topButtonSize,
                    backgroundColor = colorScheme.outline.copy(alpha = 0.16f),
                    onClick = onBackClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = "Back",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(metrics.topIconSize * 0.55f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(metrics.actionGap),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NotificationCircleButton(
                        buttonSize = metrics.topButtonSize,
                        backgroundColor = colorScheme.outline.copy(alpha = 0.16f),
                        onClick = onReadFilterClick
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_visibility_on),
                            contentDescription = "View read notifications",
                            tint = colorScheme.onSurface.copy(alpha = 0.22f),
                            modifier = Modifier.size(metrics.topIconSize * 0.74f)
                        )
                    }

                    NotificationCircleButton(
                        buttonSize = metrics.topButtonSize,
                        backgroundColor = colorScheme.primary,
                        onClick = onChatClick
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_chat),
                            contentDescription = "Messages",
                            tint = colorScheme.onPrimary,
                            modifier = Modifier.size(metrics.topIconSize * 0.76f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = "Notifications",
                modifier = Modifier.fillMaxWidth(),
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize * 1.08f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.illustrationTopSpacing))

            Image(
                painter = painterResource(Res.drawable.notification_empty_illustration),
                contentDescription = "No notifications",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(metrics.illustrationSize)
            )

            Spacer(modifier = Modifier.height(metrics.messageTopSpacing))

            Text(
                text = "No Notifications Right Now!",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = metrics.messageSize,
                    lineHeight = metrics.messageLineHeight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun NotificationsTopActions(
    metrics: NotificationMetrics,
    onBackClick: () -> Unit,
    onReadFilterClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NotificationCircleButton(
            buttonSize = metrics.topButtonSize,
            backgroundColor = colorScheme.outline.copy(alpha = 0.16f),
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Unspecified,
                modifier = Modifier.size(metrics.topIconSize * 0.55f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NotificationCircleButton(
                buttonSize = metrics.topButtonSize,
                backgroundColor = colorScheme.outline.copy(alpha = 0.16f),
                onClick = onReadFilterClick
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_visibility_on),
                    contentDescription = "View read notifications",
                    tint = colorScheme.primary,
                    modifier = Modifier.size(metrics.topIconSize * 0.74f)
                )
            }

            NotificationCircleButton(
                buttonSize = metrics.topButtonSize,
                backgroundColor = colorScheme.primary,
                onClick = onChatClick
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_chat),
                    contentDescription = "Messages",
                    tint = colorScheme.onPrimary,
                    modifier = Modifier.size(metrics.topIconSize * 0.76f)
                )
            }
        }
    }
}

@Composable
private fun NotificationCircleButton(
    buttonSize: Dp,
    backgroundColor: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun rememberNotificationEmptyMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): NotificationEmptyMetrics {
    return remember(maxWidth, maxHeight) {
        val horizontalPadding = (maxWidth * 0.075f).coerceIn(24.dp, 32.dp)
        val topButtonSize = (maxWidth * 0.12f).coerceIn(42.dp, 50.dp)
        val titleSize = (maxWidth.value * 0.095f).coerceIn(34f, 38f).sp
        val messageSize = (maxWidth.value * 0.05f).coerceIn(18f, 21f).sp

        NotificationEmptyMetrics(
            horizontalPadding = horizontalPadding,
            topSpacing = (maxHeight * 0.05f).coerceIn(28.dp, 44.dp),
            topButtonSize = topButtonSize,
            topIconSize = topButtonSize * 0.68f,
            actionGap = (maxWidth * 0.026f).coerceIn(10.dp, 14.dp),
            titleTopSpacing = (maxHeight * 0.038f).coerceIn(24.dp, 34.dp),
            titleSize = titleSize,
            illustrationTopSpacing = (maxHeight * 0.105f).coerceIn(58.dp, 96.dp),
            illustrationSize = (maxWidth * 0.82f).coerceIn(260.dp, 340.dp),
            messageTopSpacing = (maxHeight * 0.068f).coerceIn(36.dp, 58.dp),
            messageSize = messageSize,
            messageLineHeight = messageSize * 1.25f
        )
    }
}

@Composable
private fun NotificationRow(
    notification: NotificationUiState,
    metrics: NotificationMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(metrics.indicatorColumnWidth)
                .height(metrics.iconSize),
            contentAlignment = Alignment.CenterStart
        ) {
            if (notification.unread) {
                Box(
                    modifier = Modifier
                        .size(metrics.indicatorSize)
                        .clip(CircleShape)
                        .background(colorScheme.primary)
                )
            }
        }

        when (notification.type) {
            NotificationType.Premium -> NotificationIconTile(
                backgroundColor = colorScheme.tertiary,
                iconTint = colorScheme.onTertiary,
                iconRes = Res.drawable.ic_star,
                contentDescription = "Premium notification",
                metrics = metrics
            )
            NotificationType.Voucher -> NotificationIconTile(
                backgroundColor = colorScheme.secondary,
                iconTint = colorScheme.onSecondary,
                iconRes = Res.drawable.ic_tag,
                contentDescription = "Voucher notification",
                metrics = metrics
            )
            NotificationType.Message -> NotificationProfileIcon(metrics = metrics)
        }

        Spacer(modifier = Modifier.width(metrics.contentGap))

        when (notification.type) {
            NotificationType.Message -> NotificationMessageContent(
                title = notification.title,
                time = notification.time,
                metrics = metrics
            )
            else -> NotificationTextContent(
                title = notification.title,
                time = notification.time,
                metrics = metrics
            )
        }
    }
}

@Composable
private fun NotificationIconTile(
    backgroundColor: Color,
    iconTint: Color,
    iconRes: DrawableResource,
    contentDescription: String,
    metrics: NotificationMetrics
) {
    Box(
        modifier = Modifier
            .size(metrics.iconSize)
            .clip(RoundedCornerShape(metrics.iconCorner))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(metrics.iconInnerSize)
        )
    }
}

@Composable
private fun NotificationProfileIcon(
    metrics: NotificationMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier.size(metrics.iconSize)
    ) {
        Image(
            painter = painterResource(Res.drawable.profile_11),
            contentDescription = "Message sender",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(metrics.profileSize)
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(metrics.profileCorner))
                .border(
                    width = metrics.profileBorderWidth,
                    color = ColorProfileAround,
                    shape = RoundedCornerShape(metrics.profileCorner)
                )
        )

        Box(
            modifier = Modifier
                .size(metrics.chatBadgeSize)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chat),
                contentDescription = "Message",
                tint = colorScheme.onPrimary,
                modifier = Modifier.size(metrics.chatBadgeIconSize)
            )
        }
    }
}

@Composable
private fun NotificationTextContent(
    title: String,
    time: String,
    metrics: NotificationMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        Text(
            text = title,
            color = colorScheme.onSurface.copy(alpha = 0.78f),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.titleTextSize,
                lineHeight = metrics.titleLineHeight,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(metrics.timeTopSpacing))

        Text(
            text = time,
            color = colorScheme.onSurface.copy(alpha = 0.42f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.timeTextSize,
                lineHeight = metrics.timeTextSize * 1.2f,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun NotificationMessageContent(
    title: String,
    time: String,
    metrics: NotificationMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(metrics.messageBubbleHeight)
                .clip(RoundedCornerShape(metrics.messageBubbleCorner))
                .background(colorScheme.outline.copy(alpha = 0.10f))
                .padding(
                    start = metrics.messageBubbleHorizontalPadding,
                    end = metrics.messageBubbleHorizontalPadding,
                    top = metrics.messageTopPadding
                ),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = title,
                color = colorScheme.onSurface.copy(alpha = 0.78f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.titleTextSize,
                    lineHeight = metrics.titleLineHeight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = time,
            color = colorScheme.onSurface.copy(alpha = 0.42f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.timeTextSize,
                lineHeight = metrics.timeTextSize * 1.2f,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberNotificationMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): NotificationMetrics {
    val compact = maxWidth < 370.dp
    val short = maxHeight < 760.dp

    return remember(maxWidth, maxHeight) {
        NotificationMetrics(
            horizontalPadding = if (compact) 24.dp else 29.dp,
            topSpacing = if (short) 25.dp else 40.dp,
            topButtonSize = if (compact) 46.dp else 49.dp,
            topIconSize = if (compact) 30.dp else 32.dp,
            titleTopSpacing = if (short) 20.dp else 30.dp,
            titleSize = if (compact) 38.sp else 41.sp,
            listTopSpacing = if (short) 40.dp else 40.dp,
            itemSpacing = if (compact) 20.dp else 10.dp,
            indicatorColumnWidth = if (compact) 16.dp else 18.dp,
            indicatorSize = 10.dp,
            iconSize = if (compact) 50.dp else 50.dp,
            iconCorner = 18.dp,
            iconInnerSize = if (compact) 26.dp else 29.dp,
            contentGap = if (compact) 20.dp else 20.dp,
            titleTextSize = if (compact) 15.sp else 16.sp,
            titleLineHeight = if (compact) 22.sp else 24.sp,
            timeTopSpacing = if (compact) 15.dp else 10.dp,
            timeTextSize = if (compact) 15.sp else 16.sp,
            profileSize = if (compact) 55.dp else 58.dp,
            profileCorner = 15.dp,
            profileBorderWidth = 2.5.dp,
            chatBadgeSize = if (compact) 25.dp else 27.dp,
            chatBadgeIconSize = if (compact) 13.dp else 14.dp,
            messageBubbleHeight = if (compact) 66.dp else 70.dp,
            messageBubbleCorner = 16.dp,
            messageBubbleHorizontalPadding = if (compact) 18.dp else 20.dp,
            messageTopPadding = if (compact) 20.dp else 21.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsEmptyScreenPreview() {
    DZTheme {
        NotificationsEmptyScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    DZTheme {
        NotificationsScreen()
    }
}
