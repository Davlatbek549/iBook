package com.example.dz.presentation.social.friend_detail

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicProgressBar
import com.example.dz.designsystem.components.organic.organicIdentityTint
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.friend_books_this_year
import dz.shared.generated.resources.friend_day_streak
import dz.shared.generated.resources.friend_message
import dz.shared.generated.resources.friend_reading_now
import dz.shared.generated.resources.friend_shared_shelf_subtitle
import dz.shared.generated.resources.friend_shared_shelf_title
import dz.shared.generated.resources.friend_shared_with_you
import dz.shared.generated.resources.friend_since_placeholder
import dz.shared.generated.resources.nav_back
import org.jetbrains.compose.resources.stringResource

/**
 * Friend detail — identity on a sage wash, two lifetime numbers neither side of the app tracks
 * per friend yet, their current book, and a shared-shelf row with nothing behind it to open.
 *
 * The handoff's "friend's shelf" carousel from the Ink screen has no equivalent here — the new
 * design replaces it with "Shared with you", which needs a friend-to-collection link the domain
 * does not have, so the row stays but never navigates. "Friends since", the two stat numbers and
 * the reading-progress percentage are the same kind of gap, carried at the Ink screen's own
 * fidelity rather than invented fresh for this pass.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun FriendScreen(
    uiState: FriendDetailUiState = FriendDetailUiState(),
    onEvent: (FriendDetailEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth().background(OrganicColors.bg),
        contentPadding = PaddingValues(bottom = ORGANIC_TAB_BAR_CLEARANCE),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item(key = "header") { FriendHeader(uiState = uiState, onEvent = onEvent) }

        item(key = "stats") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = ORGANIC_GUTTER),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FriendStatCard(
                    value = uiState.booksThisYear.toString(),
                    label = stringResource(Res.string.friend_books_this_year),
                    modifier = Modifier.weight(1f),
                )
                FriendStatCard(
                    value = uiState.dayStreak.toString(),
                    label = stringResource(Res.string.friend_day_streak),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        if (uiState.currentBookTitle != null) {
            item(key = "reading-now") {
                Column(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OrganicKicker(text = stringResource(Res.string.friend_reading_now), color = OrganicColors.neutral600)
                    OrganicCard(modifier = Modifier.fillMaxWidth(), elevated = true) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OrganicBookCover(
                                title = uiState.currentBookTitle,
                                coverUrl = uiState.currentBookCoverUrl,
                                width = 46.dp,
                                height = 68.dp,
                                cornerRadius = 9.dp,
                            )
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = uiState.currentBookTitle,
                                    fontFamily = organicBodyFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = OrganicColors.text,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                uiState.currentBookAuthor?.let {
                                    Text(
                                        text = it,
                                        fontFamily = organicBodyFontFamily(),
                                        fontSize = 12.sp,
                                        color = OrganicColors.neutral700,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                OrganicProgressBar(
                                    progress = uiState.currentBookProgress,
                                    modifier = Modifier.fillMaxWidth(0.6f),
                                )
                            }
                            Text(
                                text = "${(uiState.currentBookProgress * 100).toInt()}%",
                                fontFamily = organicBodyFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = OrganicColors.accent700
                            )
                        }
                    }
                }
            }
        }

        item(key = "shared") {
            Column(
                modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OrganicKicker(text = stringResource(Res.string.friend_shared_with_you), color = OrganicColors.neutral600)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button) { onEvent(FriendDetailEvent.SharedShelfClicked) },
                    horizontalArrangement = Arrangement.spacedBy(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(organicIdentityTint(uiState.friendId).copy(alpha = 0.55f))
                    )
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(Res.string.friend_shared_shelf_title, uiState.firstName),
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = OrganicColors.text
                        )
                        Text(
                            text = stringResource(Res.string.friend_shared_shelf_subtitle),
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 12.sp,
                            color = OrganicColors.neutral700
                        )
                    }
                    Icon(
                        imageVector = OrganicIcons.ChevronRight,
                        contentDescription = null,
                        tint = OrganicColors.neutral600,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/** The sage wash: back/options, identity, and the two ways to reach them. */
@Composable
private fun FriendHeader(uiState: FriendDetailUiState, onEvent: (FriendDetailEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
            .background(OrganicColors.accent2_200)
            .statusBarsPadding()
            .padding(horizontal = ORGANIC_GUTTER, vertical = 12.dp)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OrganicCircleIconButton(
                icon = OrganicIcons.ChevronLeft,
                onClick = { onEvent(FriendDetailEvent.BackClicked) },
                contentDescription = stringResource(Res.string.nav_back),
                size = 38.dp,
                iconSize = 18.dp,
                background = OrganicColors.bg,
                tint = OrganicColors.accent2_900,
            )
            Box(modifier = Modifier.weight(1f))
            OrganicCircleIconButton(
                icon = OrganicIcons.ChevronRight,
                onClick = { onEvent(FriendDetailEvent.OptionsClicked) },
                contentDescription = null,
                size = 38.dp,
                iconSize = 17.dp,
                background = OrganicColors.bg,
                tint = OrganicColors.accent2_900,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(76.dp).clip(CircleShape).background(OrganicColors.accent2_700),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.avatarInitial,
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = uiState.name,
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 26.sp,
                    lineHeight = 27.3.sp,
                    color = OrganicColors.accent2_900
                )
                Text(
                    text = stringResource(Res.string.friend_since_placeholder),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 13.sp,
                    color = OrganicColors.accent2_900.copy(alpha = 0.8f)
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(OrganicColors.accent)
                    .clickable(role = Role.Button) { onEvent(FriendDetailEvent.MessageClicked) },
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = OrganicIcons.Chat,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(Res.string.friend_message),
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
            OrganicCircleIconButton(
                icon = OrganicIcons.Friends,
                onClick = { onEvent(FriendDetailEvent.ShareClicked) },
                contentDescription = null,
                size = 50.dp,
                iconSize = 18.dp,
                background = OrganicColors.bg,
                tint = OrganicColors.accent2_900,
            )
        }
    }
}

@Composable
private fun FriendStatCard(value: String, label: String, modifier: Modifier = Modifier) {
    OrganicCard(modifier = modifier, elevated = true, contentPadding = PaddingValues(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = value,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                color = OrganicColors.accent700
            )
            Text(
                text = label,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700
            )
        }
    }
}

@Preview
@Composable
private fun FriendScreenPreview() {
    FriendScreen()
}
