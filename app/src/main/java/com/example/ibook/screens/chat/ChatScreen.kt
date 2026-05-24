package com.example.ibook.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import kotlin.math.min

private enum class MessageSide {
    Incoming,
    Outgoing,
    Centered
}

private data class ChatMessage(
    val textRes: Int,
    val side: MessageSide,
    val showAvatar: Boolean = false
)

private data class ChatMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val headerHeight: Dp,
    val topButtonSize: Dp,
    val topButtonIconSize: Dp,
    val titleFontSize: Float,
    val avatarSize: Dp,
    val messageRowSpacing: Dp,
    val bubbleCorner: Dp,
    val bubblePaddingHorizontal: Dp,
    val bubblePaddingVertical: Dp,
    val bubbleMaxWidthFraction: Float,
    val messageFontSize: Float,
    val messageLineHeight: Float,
    val timestampFontSize: Float,
    val inputSpacingTop: Dp,
    val inputBarHeight: Dp,
    val inputFieldHeight: Dp,
    val inputCorner: Dp,
    val inputHorizontalPadding: Dp,
    val bottomButtonSize: Dp,
    val bottomIconSize: Dp
)

private val chatMessages = listOf(
    ChatMessage(R.string.chat_message_1, MessageSide.Outgoing),
    ChatMessage(R.string.chat_message_2, MessageSide.Incoming, showAvatar = true),
    ChatMessage(R.string.chat_message_3, MessageSide.Outgoing),
    ChatMessage(R.string.chat_message_4, MessageSide.Incoming, showAvatar = true),
    ChatMessage(R.string.chat_message_5, MessageSide.Incoming, showAvatar = true),
    ChatMessage(R.string.chat_message_6, MessageSide.Outgoing),
    ChatMessage(R.string.chat_message_7, MessageSide.Centered),
    ChatMessage(R.string.chat_message_8, MessageSide.Incoming, showAvatar = true)
)

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAttachClick: () -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    var messageInput by remember { mutableStateOf("") }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberChatMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = metrics.horizontalPadding)
            ) {
                Spacer(modifier = Modifier.height(metrics.topSpacing))

                ChatHeader(
                    metrics = metrics,
                    onBackClick = onBackClick
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(metrics.messageRowSpacing)
                ) {
                    items(chatMessages) { message ->
                        ChatMessageRow(
                            message = message,
                            metrics = metrics
                        )
                    }
                }

                Spacer(modifier = Modifier.height(metrics.inputSpacingTop))

                ChatInputBar(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    metrics = metrics,
                    onAttachClick = onAttachClick,
                    onSendClick = onSendClick
                )
            }
        }
    }
}

@Composable
private fun ChatHeader(
    metrics: ChatMetrics,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.headerHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleActionButton(
            size = metrics.topButtonSize,
            iconSize = metrics.topButtonIconSize,
            background = MaterialTheme.colorScheme.surface,
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.topButtonIconSize)
            )
        }

        Text(
            text = stringResource(R.string.chat_title_joshua),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.titleFontSize.sp
            )
        )

        Box(
            modifier = Modifier.size(metrics.avatarSize),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    )
                    .padding(metrics.avatarSize * 0.05f)
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(R.drawable.profile_7),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
private fun ChatMessageRow(
    message: ChatMessage,
    metrics: ChatMetrics
) {
    when (message.side) {
        MessageSide.Centered -> CenterTimestamp(
            text = stringResource(message.textRes),
            metrics = metrics
        )
        MessageSide.Outgoing -> OutgoingMessageBubble(
            text = stringResource(message.textRes),
            metrics = metrics
        )
        MessageSide.Incoming -> IncomingMessageBubble(
            text = stringResource(message.textRes),
            metrics = metrics,
            showAvatar = message.showAvatar
        )
    }
}

@Composable
private fun OutgoingMessageBubble(
    text: String,
    metrics: ChatMetrics
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(metrics.bubbleMaxWidthFraction)
                .clip(RoundedCornerShape(metrics.bubbleCorner))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
                .padding(
                    horizontal = metrics.bubblePaddingHorizontal,
                    vertical = metrics.bubblePaddingVertical
                )
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = metrics.messageFontSize.sp,
                    lineHeight = metrics.messageLineHeight.sp
                )
            )
        }
    }
}

@Composable
private fun IncomingMessageBubble(
    text: String,
    metrics: ChatMetrics,
    showAvatar: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (showAvatar) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.profile_7),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(metrics.avatarSize)
                    .clip(CircleShape)
            )
        } else {
            Spacer(modifier = Modifier.width(metrics.avatarSize))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(metrics.bubbleMaxWidthFraction)
                .clip(RoundedCornerShape(metrics.bubbleCorner))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(metrics.bubbleCorner)
                )
                .padding(
                    horizontal = metrics.bubblePaddingHorizontal,
                    vertical = metrics.bubblePaddingVertical
                )
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = metrics.messageFontSize.sp,
                    lineHeight = metrics.messageLineHeight.sp
                )
            )
        }
    }
}

@Composable
private fun CenterTimestamp(
    text: String,
    metrics: ChatMetrics
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = metrics.timestampFontSize.sp
        )
    )
}

@Composable
private fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    metrics: ChatMetrics,
    onAttachClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.inputBarHeight),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleActionButton(
            size = metrics.bottomButtonSize,
            iconSize = metrics.bottomIconSize,
            background = MaterialTheme.colorScheme.surface,
            onClick = onAttachClick
        ) {
            Icon(
                imageVector = Icons.Outlined.AlternateEmail,
                contentDescription = stringResource(R.string.cd_attach),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.bottomIconSize)
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .height(metrics.inputFieldHeight)
                .clip(RoundedCornerShape(metrics.inputCorner))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(metrics.inputCorner)
                )
                .padding(horizontal = metrics.inputHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = metrics.messageFontSize.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.chat_input_hint),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.42f),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = metrics.messageFontSize.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        CircleActionButton(
            size = metrics.bottomButtonSize,
            iconSize = metrics.bottomIconSize,
            background = MaterialTheme.colorScheme.primary,
            onClick = onSendClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = stringResource(R.string.cd_send),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(metrics.bottomIconSize)
            )
        }
    }
}

@Composable
private fun CircleActionButton(
    size: Dp,
    iconSize: Dp,
    background: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.16f),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .padding((size - iconSize) / 2),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(CircleShape)
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun rememberChatMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): ChatMetrics {
    val widthRatio = (maxWidth / 390.dp).coerceIn(0.86f, 1.18f)
    val heightRatio = (maxHeight / 844.dp).coerceIn(0.82f, 1.18f)
    val compactness = min(widthRatio, heightRatio)
    val isCompactHeight = maxHeight < 760.dp

    return ChatMetrics(
        horizontalPadding = (28.dp * widthRatio).coerceIn(18.dp, 32.dp),
        topSpacing = ((22.dp * heightRatio) * if (isCompactHeight) 0.72f else 1f).coerceIn(14.dp, 28.dp),
        headerHeight = (56.dp * compactness).coerceIn(48.dp, 60.dp),
        topButtonSize = (46.dp * compactness).coerceIn(40.dp, 50.dp),
        topButtonIconSize = (22.dp * compactness).coerceIn(18.dp, 24.dp),
        titleFontSize = (18f * compactness).coerceIn(15f, 19f),
        avatarSize = (42.dp * compactness).coerceIn(36.dp, 46.dp),
        messageRowSpacing = (12.dp * compactness).coerceIn(10.dp, 16.dp),
        bubbleCorner = (16.dp * compactness).coerceIn(14.dp, 18.dp),
        bubblePaddingHorizontal = (14.dp * compactness).coerceIn(12.dp, 16.dp),
        bubblePaddingVertical = (10.dp * compactness).coerceIn(8.dp, 12.dp),
        bubbleMaxWidthFraction = if (isCompactHeight) 0.78f else 0.74f,
        messageFontSize = (15f * compactness).coerceIn(13f, 16f),
        messageLineHeight = (22f * compactness).coerceIn(18f, 24f),
        timestampFontSize = (12f * compactness).coerceIn(11f, 13f),
        inputSpacingTop = (12.dp * compactness).coerceIn(10.dp, 16.dp),
        inputBarHeight = (64.dp * compactness).coerceIn(56.dp, 68.dp),
        inputFieldHeight = (50.dp * compactness).coerceIn(44.dp, 54.dp),
        inputCorner = (22.dp * compactness).coerceIn(18.dp, 24.dp),
        inputHorizontalPadding = (16.dp * compactness).coerceIn(12.dp, 18.dp),
        bottomButtonSize = (46.dp * compactness).coerceIn(40.dp, 50.dp),
        bottomIconSize = (20.dp * compactness).coerceIn(18.dp, 22.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true, widthDp = 390, heightDp = 844)
@Composable
private fun ChatScreenPreview() {
    MaterialTheme {
        ChatScreen()
    }
}
