package com.example.dz.presentation.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicAvatar
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicSectionHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.library_book_count
import dz.shared.generated.resources.profile_books_this_year
import dz.shared.generated.resources.profile_friends_title
import dz.shared.generated.resources.profile_goal_frequency
import dz.shared.generated.resources.profile_hours_read
import dz.shared.generated.resources.profile_invite
import dz.shared.generated.resources.profile_membership
import dz.shared.generated.resources.profile_notifications
import dz.shared.generated.resources.profile_reading_goal
import dz.shared.generated.resources.profile_sign_out
import dz.shared.generated.resources.profile_streak
import dz.shared.generated.resources.profile_streak_progress
import dz.shared.generated.resources.profile_time_read
import org.jetbrains.compose.resources.stringResource

/**
 * Profile — identity, the streak, two lifetime stats, a friends strip, then the four rows that
 * lead everywhere else in the account. A tab-bar destination, not a pushed screen, so there is no
 * back chevron: the avatar and the tab item are the two ways in, and both land here.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun ProfileScreen(
    uiState: ProfileUiState = ProfileUiState(),
    onEvent: (ProfileEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = ORGANIC_GUTTER,
                end = ORGANIC_GUTTER,
                top = 16.dp,
                bottom = ORGANIC_TAB_BAR_CLEARANCE,
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(key = "identity") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button) { onEvent(ProfileEvent.SettingsClicked) },
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OrganicAvatar(name = uiState.name, size = 64.dp, fontSize = 24.sp)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = uiState.name,
                            fontFamily = organicHeadingFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp,
                            lineHeight = 24.2.sp,
                            color = OrganicColors.text
                        )
                        Text(
                            text = stringResource(Res.string.library_book_count, uiState.booksOwned),
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 13.sp,
                            color = OrganicColors.neutral700
                        )
                    }
                }
            }

            item(key = "streak") { StreakCard(uiState) }

            item(key = "stats") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        value = uiState.booksOwned.toString(),
                        label = stringResource(Res.string.profile_books_this_year),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = stringResource(Res.string.profile_hours_read, uiState.hoursReadThisWeek),
                        label = stringResource(Res.string.profile_time_read),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item(key = "friends") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OrganicSectionHeader(title = stringResource(Res.string.profile_friends_title))
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        uiState.friends.forEachIndexed { index, friend ->
                            FriendTile(
                                name = friend.name,
                                tint = friendTints[index % friendTints.size],
                                onClick = { onEvent(ProfileEvent.FriendsClicked) },
                            )
                        }
                        InviteTile(onClick = { onEvent(ProfileEvent.InviteClicked) })
                    }
                }
            }

            item(key = "menu") {
                Column {
                    ProfileMenuRow(
                        label = stringResource(Res.string.profile_reading_goal),
                        trailingText = stringResource(Res.string.profile_goal_frequency, uiState.targetMinutes),
                        onClick = { onEvent(ProfileEvent.ReadingGoalClicked) },
                    )
                    ProfileMenuDivider()
                    ProfileMenuRow(
                        label = stringResource(Res.string.profile_membership),
                        trailingText = uiState.membershipLabel,
                        trailingColor = OrganicColors.accent700,
                        onClick = { onEvent(ProfileEvent.MembershipClicked) },
                    )
                    ProfileMenuDivider()
                    ProfileMenuRow(
                        label = stringResource(Res.string.profile_notifications),
                        onClick = { onEvent(ProfileEvent.NotificationsClicked) },
                    )
                }
            }
        }
    }
}

/** The dark terracotta hero: today's streak, washed with a bleeding accent-800 circle. */
@Composable
private fun StreakCard(uiState: ProfileUiState) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        background = OrganicColors.accent900,
        contentPadding = PaddingValues(20.dp),
        decoration = {
            // `right:-64; top:-70; 170×170` — see Home's Keep going card for the same technique.
            drawCircle(
                color = OrganicColors.accent800,
                radius = 85.dp.toPx(),
                center = Offset(size.width - 21.dp.toPx(), 15.dp.toPx())
            )
        },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(OrganicColors.accent),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = OrganicIcons.Flame,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = stringResource(Res.string.profile_streak, uiState.streakDays),
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = OrganicColors.accent100
                )
                Text(
                    text = stringResource(
                        Res.string.profile_streak_progress,
                        uiState.minutesToday,
                        uiState.targetMinutes
                    ),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 13.sp,
                    color = OrganicColors.accent200
                )
            }
        }
    }
}

/** One of the two lifetime numbers under the streak card. */
@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    OrganicCard(modifier = modifier, elevated = true, contentPadding = PaddingValues(16.dp)) {
        Column {
            Text(
                text = value,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 26.sp,
                color = OrganicColors.text
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

/** A flat colour standing in for a photo — the row is "who you read with", not their likenesses. */
@Composable
private fun FriendTile(
    name: String,
    tint: Color,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .clickable(role = Role.Button, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(tint))
        Text(
            text = name,
            fontFamily = organicBodyFontFamily(),
            fontSize = 11.sp,
            color = OrganicColors.neutral700
        )
    }
}

/** The dashed "+" that opens the invite flow — the one tile in the row that adds rather than opens. */
@Composable
private fun InviteTile(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .clickable(role = Role.Button, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .drawBehind {
                    drawCircle(
                        color = OrganicColors.neutral500,
                        radius = size.minDimension / 2f - 1.dp.toPx(),
                        style = Stroke(width = 1.dp.toPx(), pathEffect = InviteTileDashEffect)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = OrganicIcons.Plus,
                contentDescription = stringResource(Res.string.profile_invite),
                tint = OrganicColors.neutral600,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = stringResource(Res.string.profile_invite),
            fontFamily = organicBodyFontFamily(),
            fontSize = 11.sp,
            color = OrganicColors.neutral700
        )
    }
}

/** The invite tile's outline — 4dp on, 4dp off, matching the handoff's dashed circle. */
private val InviteTileDashEffect: PathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))

/** One row of the account menu: a label, an optional trailing value, and a chevron. */
@Composable
private fun ProfileMenuRow(
    label: String,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    trailingColor: Color = OrganicColors.neutral700,
    labelColor: Color = OrganicColors.text,
    showChevron: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontFamily = organicBodyFontFamily(),
            fontSize = 15.sp,
            color = labelColor
        )
        if (trailingText != null) {
            Text(
                text = trailingText,
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = organicBodyFontFamily(),
                fontSize = 13.sp,
                color = trailingColor
            )
        }
        if (showChevron) {
            Icon(
                imageVector = OrganicIcons.ChevronRight,
                contentDescription = null,
                tint = OrganicColors.neutral600,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
private fun ProfileMenuDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(OrganicColors.text.copy(alpha = 0.16f))
    )
}

private val friendTints: List<Color> = listOf(
    OrganicColors.accent2_300,
    OrganicColors.accent300,
)

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}
