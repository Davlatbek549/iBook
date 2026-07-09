package com.example.dz.presentation.social.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkField
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.friends_everyone
import dz.shared.generated.resources.friends_online_now
import dz.shared.generated.resources.friends_search
import dz.shared.generated.resources.friends_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FriendListScreen(
    uiState: FriendListUiState = FriendListUiState(),
    onEvent: (FriendListEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = inkColors()

    val online = uiState.onlineFriends
    val everyone = uiState.everyoneFriends

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        InkTopBar(
            title = stringResource(Res.string.friends_title),
            subtitle = "12 friends · 3 online",
            onBackClick = { onEvent(FriendListEvent.BackClicked) },
            right = { InkIconButton(icon = InkIcons.Plus, onClick = { onEvent(FriendListEvent.AddFriendClicked) }, colors = colors) },
            colors = colors
        )

        Box(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 4.dp)) {
            InkField(
                value = uiState.query,
                onValueChange = { onEvent(FriendListEvent.QueryChanged(it)) },
                placeholder = stringResource(Res.string.friends_search),
                leadingIcon = InkIcons.Search,
                colors = colors
            )
        }

        if (online.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 20.dp)) {
                InkLabel(text = stringResource(Res.string.friends_online_now), colors = colors)
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    online.forEach { friend ->
                        FriendRow(
                            friend = friend,
                            onClick = { onEvent(FriendListEvent.FriendClicked(friend.id)) },
                            colors = colors
                        )
                        HorizontalDivider(thickness = 1.dp, color = colors.line)
                    }
                }
            }
        }

        if (everyone.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 18.dp)) {
                InkLabel(text = stringResource(Res.string.friends_everyone), colors = colors)
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    everyone.forEachIndexed { i, friend ->
                        FriendRow(
                            friend = friend,
                            onClick = { onEvent(FriendListEvent.FriendClicked(friend.id)) },
                            colors = colors
                        )
                        if (i < everyone.size - 1) {
                            HorizontalDivider(thickness = 1.dp, color = colors.line)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FriendRow(
    friend: FriendUi,
    onClick: () -> Unit,
    colors: InkColors,
) {
    val bodyFont = inkBodyFontFamily()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Box {
            Image(
                painter = painterResource(friend.avatarRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm))
            )
            if (friend.online) {
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
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = friend.name,
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.5.sp,
                color = colors.ink
            )
            Text(
                text = friend.handle,
                modifier = Modifier.padding(top = 5.dp),
                fontFamily = bodyFont,
                fontSize = 11.5.sp,
                color = colors.muted
            )
        }
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(InkShape.radiusSm))
                .let {
                    if (friend.online) it.background(colors.accentSoft)
                    else it.border(1.dp, colors.line, RoundedCornerShape(InkShape.radiusSm))
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = InkIcons.Chat,
                contentDescription = null,
                tint = if (friend.online) colors.accent else colors.muted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun FriendListScreenPreview() {
    FriendListScreen()
}
