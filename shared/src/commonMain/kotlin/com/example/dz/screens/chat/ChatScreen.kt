package com.example.dz.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.chat_online
import dz.shared.generated.resources.chat_today
import dz.shared.generated.resources.chat_view_book
import dz.shared.generated.resources.chat_write
import dz.shared.generated.resources.profile_2
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAttachClick: () -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
    ) {
        // header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            InkIconButton(icon = InkIcons.Back, onClick = onBackClick, colors = colors)
            Box {
                Image(
                    painter = painterResource(Res.drawable.profile_2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(InkShape.radiusSm))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(11.dp)
                        .clip(CircleShape)
                        .background(colors.paper)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(colors.accent)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Patricia Lane",
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = colors.ink
                )
                Text(
                    text = stringResource(Res.string.chat_online),
                    modifier = Modifier.padding(top = 4.dp),
                    fontFamily = bodyFont,
                    fontSize = 11.sp,
                    color = colors.accent
                )
            }
            InkIconButton(icon = InkIcons.MoreVertical, onClick = onAttachClick, colors = colors)
        }
        HorizontalDivider(thickness = 1.dp, color = colors.line)

        // messages
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, end = 22.dp, top = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(Res.string.chat_today).uppercase(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = colors.muted
            )
            Bubble(
                text = "Finished Olive, Again last night — the last chapter undid me completely.",
                time = "14:02",
                fromMe = false,
                colors = colors
            )
            Bubble(
                text = "I told you! Strout never raises her voice and it still lands harder than anything.",
                time = "14:05",
                fromMe = true,
                colors = colors
            )
            Bubble(
                text = "Okay, what's next then. You always know.",
                time = "14:06",
                fromMe = false,
                colors = colors
            )
            // shared book card
            Column(
                modifier = Modifier
                    .align(Alignment.End)
                    .widthIn(max = 250.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(InkShape.radius))
                        .background(colors.surface)
                        .border(1.dp, colors.line, RoundedCornerShape(InkShape.radius))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.book_cover_3),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 40.dp, height = 58.dp)
                            .clip(RoundedCornerShape(InkShape.cover - 1.dp))
                    )
                    Column {
                        Text(
                            text = "Red at the Bone",
                            fontFamily = displayFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.5.sp,
                            color = colors.ink
                        )
                        Text(
                            text = "Jacqueline Woodson",
                            modifier = Modifier.padding(top = 4.dp),
                            fontFamily = bodyFont,
                            fontSize = 11.sp,
                            color = colors.muted
                        )
                        Text(
                            text = stringResource(Res.string.chat_view_book),
                            modifier = Modifier.padding(top = 7.dp),
                            fontFamily = bodyFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = colors.accent
                        )
                    }
                }
                Text(
                    text = "14:07 · Read",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 5.dp, end = 4.dp),
                    fontFamily = bodyFont,
                    fontSize = 10.sp,
                    color = colors.muted
                )
            }
        }

        // composer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp)
                .background(colors.paper)
                .navigationBarsPadding()
                .padding(start = 22.dp, end = 22.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(CircleShape)
                    .background(colors.surface)
                    .border(1.dp, colors.line, CircleShape)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(Res.string.chat_write),
                    fontFamily = bodyFont,
                    fontSize = 13.sp,
                    color = colors.muted
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(colors.accent)
                    .clickable(onClick = onSendClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = InkIcons.Send,
                    contentDescription = null,
                    tint = colors.onAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun Bubble(
    text: String,
    time: String,
    fromMe: Boolean,
    colors: InkColors,
) {
    val bodyFont = inkBodyFontFamily()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (fromMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 270.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = InkShape.radius,
                        topEnd = InkShape.radius,
                        bottomStart = if (fromMe) InkShape.radius else 4.dp,
                        bottomEnd = if (fromMe) 4.dp else InkShape.radius
                    )
                )
                .background(if (fromMe) colors.accent else colors.surface)
                .let {
                    if (fromMe) it else it.border(
                        1.dp, colors.line,
                        RoundedCornerShape(
                            topStart = InkShape.radius, topEnd = InkShape.radius,
                            bottomStart = InkShape.radius, bottomEnd = 4.dp
                        )
                    )
                }
                .padding(horizontal = 14.dp, vertical = 11.dp)
        ) {
            Text(
                text = text,
                fontFamily = bodyFont,
                fontSize = 13.5.sp,
                lineHeight = 21.sp,
                color = if (fromMe) colors.onAccent else colors.ink
            )
        }
        Text(
            text = time,
            modifier = Modifier.padding(top = 5.dp, start = 4.dp, end = 4.dp),
            fontFamily = bodyFont,
            fontSize = 10.sp,
            color = colors.muted
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun ChatScreenPreview() {
    ChatScreen()
}
