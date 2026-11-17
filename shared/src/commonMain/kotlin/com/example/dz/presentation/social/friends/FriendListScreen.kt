package com.example.dz.presentation.social.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.components.organic.organicIdentityTint
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.friends_no_book_open
import dz.shared.generated.resources.friends_quiet_lately
import dz.shared.generated.resources.friends_reading_book
import dz.shared.generated.resources.friends_search
import dz.shared.generated.resources.friends_title
import dz.shared.generated.resources.friend_reading_now
import dz.shared.generated.resources.nav_back
import org.jetbrains.compose.resources.stringResource

/**
 * Friends — search, then who is reading right now against who has gone quiet. An empty roster
 * never renders here at all: [FriendListViewModel] hands off to the dedicated empty screen instead.
 *
 * The handoff's "2 friend requests" banner and per-row "2h ago" timestamps have no source — there
 * is no pending-request concept in the domain and no last-active timestamp on a friend — so both
 * are left out rather than shown as numbers that can never change or a request nothing can act on.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun FriendListScreen(
    uiState: FriendListUiState = FriendListUiState(),
    onEvent: (FriendListEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = stringResource(Res.string.friends_title),
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = { onEvent(FriendListEvent.BackClicked) },
                            contentDescription = stringResource(Res.string.nav_back),
                            size = 38.dp,
                            iconSize = 18.dp,
                        )
                    },
                    trailing = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.Friends,
                            onClick = { onEvent(FriendListEvent.AddFriendClicked) },
                            contentDescription = stringResource(Res.string.friends_title),
                            size = 38.dp,
                            iconSize = 17.dp,
                            background = OrganicColors.accent,
                            tint = Color.White,
                        )
                    },
                )
            }

            item(key = "search") {
                FriendSearchField(
                    value = uiState.query,
                    onValueChange = { onEvent(FriendListEvent.QueryChanged(it)) },
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                )
            }

            if (uiState.readingNow.isNotEmpty()) {
                item(key = "reading-now-label") {
                    OrganicKicker(
                        text = stringResource(Res.string.friend_reading_now),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    )
                }
                items(uiState.readingNow.size, key = { "reading-${uiState.readingNow[it].id}" }) { index ->
                    val friend = uiState.readingNow[index]
                    FriendRow(
                        friend = friend,
                        subtitle = stringResource(Res.string.friends_reading_book, friend.currentBookTitle.orEmpty()),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        onClick = { onEvent(FriendListEvent.FriendClicked(friend.id)) },
                    )
                }
            }

            if (uiState.quietLately.isNotEmpty()) {
                item(key = "quiet-label") {
                    OrganicKicker(
                        text = stringResource(Res.string.friends_quiet_lately),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    )
                }
                items(uiState.quietLately.size, key = { "quiet-${uiState.quietLately[it].id}" }) { index ->
                    val friend = uiState.quietLately[index]
                    FriendRow(
                        friend = friend,
                        subtitle = stringResource(Res.string.friends_no_book_open),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        onClick = { onEvent(FriendListEvent.FriendClicked(friend.id)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun FriendSearchField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral200),
        textStyle = TextStyle(
            fontFamily = organicBodyFontFamily(),
            fontSize = 14.sp,
            color = OrganicColors.text
        ),
        singleLine = true,
        cursorBrush = SolidColor(OrganicColors.accent),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = OrganicIcons.Search,
                    contentDescription = null,
                    tint = OrganicColors.neutral600,
                    modifier = Modifier.size(16.dp)
                )
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.friends_search),
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 14.sp,
                            color = OrganicColors.neutral600
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun FriendRow(
    friend: FriendRowUi,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganicAvatar(
            name = friend.name,
            size = 46.dp,
            background = organicIdentityTint(friend.id),
            textColor = Color.White,
            fontSize = 18.sp,
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = friend.name,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = OrganicColors.text
            )
            Text(
                text = subtitle,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                lineHeight = 16.8.sp,
                color = OrganicColors.neutral700
            )
        }
    }
}

@Preview
@Composable
private fun FriendListScreenPreview() {
    FriendListScreen()
}
