package com.example.dz.presentation.social.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.OrganicAvatar
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.organicIdentityTint
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.chat_message_placeholder
import dz.shared.generated.resources.chat_today
import dz.shared.generated.resources.friends_reading_book
import org.jetbrains.compose.resources.stringResource

/**
 * Chat — message thread with a friend in the Organic design system.
 *
 * Geometry from `#scr-chat`: 38dp back button, 40dp avatar with bold 15sp name + "Reading {book}"
 * subtitle, plain sent/received bubbles (no per-message timestamp — the mock shows only one
 * "Today" divider), pill composer bar with a leading attachment glyph and trailing send button.
 * The mock's book-share card and "..." typing indicator have no backing data (no message type
 * field, no typing signal) so they're intentionally left out rather than hardcoded.
 */
@Composable
fun ChatScreen(
    uiState: ChatUiState = ChatUiState(),
    onEvent: (ChatEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    OrganicScreen(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ORGANIC_GUTTER, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OrganicCircleIconButton(
                    icon = OrganicIcons.ChevronLeft,
                    onClick = { onEvent(ChatEvent.BackClicked) },
                    contentDescription = "Back",
                    size = 38.dp
                )

                OrganicAvatar(
                    name = uiState.friendName,
                    size = 40.dp,
                    fontSize = 16.sp,
                    background = organicIdentityTint(uiState.friendId)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = uiState.friendName,
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = OrganicColors.text
                    )
                    val bookTitle = uiState.friendCurrentBookTitle
                    if (bookTitle != null) {
                        Text(
                            text = stringResource(Res.string.friends_reading_book, bookTitle),
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = OrganicColors.accent2_900
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = OrganicColors.neutral200)

            // Messages List
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ORGANIC_GUTTER, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.messages.isNotEmpty()) {
                    OrganicKicker(
                        text = stringResource(Res.string.chat_today),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                uiState.messages.forEach { message ->
                    Bubble(text = message.text, fromMe = message.fromMe)
                }
            }

            // Composer Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OrganicColors.bg)
                    .navigationBarsPadding()
                    .padding(horizontal = ORGANIC_GUTTER, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(OrganicColors.neutral200),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = OrganicIcons.Plus,
                        contentDescription = null,
                        tint = OrganicColors.neutral800,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .background(OrganicColors.neutral100)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = uiState.composerText,
                        onValueChange = { onEvent(ChatEvent.ComposerChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 14.sp,
                            color = OrganicColors.text
                        ),
                        cursorBrush = SolidColor(OrganicColors.accent),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            if (uiState.canSend) onEvent(ChatEvent.SendClicked)
                        }),
                        decorationBox = { innerTextField ->
                            if (uiState.composerText.isEmpty()) {
                                Text(
                                    text = stringResource(Res.string.chat_message_placeholder, uiState.firstName),
                                    fontFamily = organicBodyFontFamily(),
                                    fontSize = 14.sp,
                                    color = OrganicColors.neutral600
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (uiState.canSend) OrganicColors.accent else OrganicColors.neutral300)
                        .clickable(enabled = uiState.canSend, role = Role.Button) {
                            onEvent(ChatEvent.SendClicked)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = OrganicIcons.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Bubble(
    text: String,
    fromMe: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = if (fromMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 265.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = OrganicShape.radiusLg,
                        topEnd = OrganicShape.radiusLg,
                        bottomStart = if (fromMe) OrganicShape.radiusLg else 6.dp,
                        bottomEnd = if (fromMe) 6.dp else OrganicShape.radiusLg
                    )
                )
                .background(if (fromMe) OrganicColors.accent else OrganicColors.neutral200)
                .padding(horizontal = 15.dp, vertical = 12.dp)
        ) {
            Text(
                text = text,
                fontFamily = organicBodyFontFamily(),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = if (fromMe) Color.White else OrganicColors.text
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun ChatScreenPreview() {
    ChatScreen()
}
