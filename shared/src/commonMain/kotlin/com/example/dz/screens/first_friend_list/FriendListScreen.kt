package com.example.dz.screens.first_friend_list

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkField
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.app_components.ink.InkLabel
import com.example.dz.app_components.ink.InkTopBar
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.friends_everyone
import dz.shared.generated.resources.friends_online_now
import dz.shared.generated.resources.friends_search
import dz.shared.generated.resources.friends_title
import dz.shared.generated.resources.img_neil_alvin
import dz.shared.generated.resources.img_raunak_purohit
import dz.shared.generated.resources.img_yza_barretto
import dz.shared.generated.resources.profile_2
import dz.shared.generated.resources.profile_3
import dz.shared.generated.resources.img_maria_renzy
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class Friend(
    val id: String,
    val name: String,
    val handle: String,
    val avatarRes: DrawableResource,
    val online: Boolean
)

private val allFriends = listOf(
    Friend("patricia-lane", "Patricia Lane", "@patricia.reads", Res.drawable.profile_2, true),
    Friend("maria-renzy", "Maria Renzy", "@maria.renzy", Res.drawable.img_maria_renzy, true),
    Friend("yza-barretto", "Yza Barretto", "@yza.b", Res.drawable.img_yza_barretto, true),
    Friend("daniel-moreau", "Daniel Moreau", "@dmoreau", Res.drawable.profile_3, false),
    Friend("neil-alvin", "Neil Alvin", "@neilalvin", Res.drawable.img_neil_alvin, false),
    Friend("raunak-purohit", "Raunak Purohit", "@raunakp", Res.drawable.img_raunak_purohit, false)
)

@Composable
fun FriendListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {},
    onFriendClick: (friendId: String) -> Unit = {}
) {
    val colors = inkColors()

    var query by remember { mutableStateOf("") }
    val filtered = allFriends.filter {
        query.isBlank() || it.name.contains(query, ignoreCase = true) || it.handle.contains(query, ignoreCase = true)
    }
    val online = filtered.filter { it.online }
    val everyone = filtered.filter { !it.online }

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
            onBackClick = onBackClick,
            right = { InkIconButton(icon = InkIcons.Plus, onClick = onAddFriendClick, colors = colors) },
            colors = colors
        )

        Box(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 4.dp)) {
            InkField(
                value = query,
                onValueChange = { query = it },
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
                            onClick = { onFriendClick(friend.id) },
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
                            onClick = { onFriendClick(friend.id) },
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
    friend: Friend,
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
