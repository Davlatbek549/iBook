package com.example.ibook.screens.first_friend_list

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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import kotlin.math.min

private data class HighlightFriend(
    val imageRes: Int,
    val bubbleTextRes: Int,
    val ringAccent: FriendRingAccent
)

private data class FriendMessage(
    val imageRes: Int,
    val nameRes: Int,
    val messageRes: Int,
    val isHighlighted: Boolean = false
)

private data class SearchFriend(
    val imageRes: Int,
    val nameRes: Int
)

private data class SearchFriendItem(
    val imageRes: Int,
    val name: String
)

private enum class FriendRingAccent {
    Primary,
    Secondary,
    Tertiary
}

private data class FriendListMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val topButtonsSize: Dp,
    val topButtonsIconSize: Dp,
    val topButtonsGap: Dp,
    val searchHeight: Dp,
    val searchCorner: Dp,
    val searchHorizontalPadding: Dp,
    val searchIconSize: Dp,
    val searchFontSize: Float,
    val highlightSpacingTop: Dp,
    val highlightSectionHeight: Dp,
    val highlightAvatarSize: Dp,
    val centerAvatarSize: Dp,
    val highlightBubbleMinHeight: Dp,
    val bubbleHorizontalPadding: Dp,
    val bubbleVerticalPadding: Dp,
    val listTopSpacing: Dp,
    val rowSpacing: Dp,
    val sectionSpacing: Dp,
    val rowAvatarSize: Dp,
    val rowBubbleMinHeight: Dp,
    val rowBubbleCorner: Dp,
    val rowBubblePaddingHorizontal: Dp,
    val rowBubblePaddingVertical: Dp,
    val nameFontSize: Float,
    val messageFontSize: Float,
    val messageLineHeight: Float,
    val searchResultSpacing: Dp
)

private val highlightFriends = listOf(
    HighlightFriend(
        imageRes = R.drawable.profile_5,
        bubbleTextRes = R.string.friend_highlight_call_me,
        ringAccent = FriendRingAccent.Tertiary
    ),
    HighlightFriend(
        imageRes = R.drawable.profile_10,
        bubbleTextRes = R.string.friend_highlight_horror_or_comic,
        ringAccent = FriendRingAccent.Primary
    ),
    HighlightFriend(
        imageRes = R.drawable.profile_4,
        bubbleTextRes = R.string.friend_highlight_bad_book,
        ringAccent = FriendRingAccent.Secondary
    )
)

private val friendMessages = listOf(
    FriendMessage(
        imageRes = R.drawable.profile_6,
        nameRes = R.string.friend_name_gisele,
        messageRes = R.string.friend_message_gisele,
        isHighlighted = true
    ),
    FriendMessage(
        imageRes = R.drawable.profile_11,
        nameRes = R.string.friend_name_vadimli,
        messageRes = R.string.friend_message_vadimli
    ),
    FriendMessage(
        imageRes = R.drawable.profile_8,
        nameRes = R.string.friend_name_oulupulu,
        messageRes = R.string.friend_message_oulupulu
    ),
    FriendMessage(
        imageRes = R.drawable.profile_9,
        nameRes = R.string.friend_name_oleksandr,
        messageRes = R.string.friend_message_oleksandr
    ),
    FriendMessage(
        imageRes = R.drawable.profile_3,
        nameRes = R.string.friend_name_natalia,
        messageRes = R.string.friend_message_natalia
    )
)

private val searchFriends = listOf(
    SearchFriend(R.drawable.profile_1, R.string.friend_name_mona),
    SearchFriend(R.drawable.profile_7, R.string.friend_name_mark),
    SearchFriend(R.drawable.profile_8, R.string.friend_name_patricia),
    SearchFriend(R.drawable.profile_3, R.string.friend_name_mary),
    SearchFriend(R.drawable.profile_10, R.string.friend_name_linda),
    SearchFriend(R.drawable.profile_1, R.string.friend_name_mona),
    SearchFriend(R.drawable.profile_7, R.string.friend_name_mark),
    SearchFriend(R.drawable.profile_8, R.string.friend_name_patricia),
    SearchFriend(R.drawable.profile_3, R.string.friend_name_mary),
    SearchFriend(R.drawable.profile_10, R.string.friend_name_linda)
)

@Composable
fun FriendListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onAddFriendClick: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var isSearchMode by remember { mutableStateOf(false) }
    val searchFriendItems = listOf(
        SearchFriendItem(R.drawable.profile_1, stringResource(R.string.friend_name_mona)),
        SearchFriendItem(R.drawable.profile_7, stringResource(R.string.friend_name_mark)),
        SearchFriendItem(R.drawable.profile_8, stringResource(R.string.friend_name_patricia)),
        SearchFriendItem(R.drawable.profile_3, stringResource(R.string.friend_name_mary)),
        SearchFriendItem(R.drawable.profile_10, stringResource(R.string.friend_name_linda)),
        SearchFriendItem(R.drawable.profile_1, stringResource(R.string.friend_name_mona)),
        SearchFriendItem(R.drawable.profile_7, stringResource(R.string.friend_name_mark)),
        SearchFriendItem(R.drawable.profile_8, stringResource(R.string.friend_name_patricia)),
        SearchFriendItem(R.drawable.profile_3, stringResource(R.string.friend_name_mary)),
        SearchFriendItem(R.drawable.profile_10, stringResource(R.string.friend_name_linda))
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberFriendListMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val filteredSearchFriends = remember(query, searchFriendItems) {
            if (query.isBlank()) {
                searchFriendItems
            } else {
                searchFriendItems.filter { friend ->
                    friend.name.contains(query.trim(), ignoreCase = true)
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = metrics.horizontalPadding)
            ) {
                item {
                    Spacer(modifier = Modifier.height(metrics.topSpacing))
                }

                item {
                    if (isSearchMode) {
                        SearchModeHeader(
                            query = query,
                            onQueryChange = { query = it },
                            metrics = metrics,
                            onBackClick = {
                                if (query.isBlank()) {
                                    isSearchMode = false
                                } else {
                                    query = ""
                                }
                            },
                            onSearchFocus = { isSearchMode = true }
                        )
                    } else {
                        TopBar(
                            metrics = metrics,
                            onBackClick = onBackClick,
                            onEditClick = onEditClick,
                            onAddFriendClick = onAddFriendClick
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(metrics.sectionSpacing))
                }

                if (isSearchMode) {
                    itemsIndexed(filteredSearchFriends) { index, friend ->
                        SearchFriendRow(
                            friend = friend,
                            metrics = metrics
                        )

                        if (index != filteredSearchFriends.lastIndex) {
                            Spacer(modifier = Modifier.height(metrics.searchResultSpacing))
                        }
                    }
                } else {
                    item {
                        SearchBar(
                            value = query,
                            onValueChange = { query = it },
                            metrics = metrics,
                            onSearchFocus = { isSearchMode = true }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(metrics.sectionSpacing))
                    }

                    item {
                        HighlightFriendsSection(metrics = metrics)
                    }

                    item {
                        Spacer(modifier = Modifier.height(metrics.listTopSpacing))
                    }

                    itemsIndexed(friendMessages) { index, friend ->
                        FriendMessageRow(
                            friend = friend,
                            metrics = metrics
                        )

                        if (index != friendMessages.lastIndex) {
                            Spacer(modifier = Modifier.height(metrics.rowSpacing))
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    metrics: FriendListMetrics,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onAddFriendClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleActionButton(
            size = metrics.topButtonsSize,
            iconSize = metrics.topButtonsIconSize,
            background = MaterialTheme.colorScheme.surface,
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.topButtonsIconSize)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(metrics.topButtonsGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleActionButton(
                size = metrics.topButtonsSize,
                iconSize = metrics.topButtonsIconSize,
                background = MaterialTheme.colorScheme.surface,
                onClick = onEditClick
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.cd_edit),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(metrics.topButtonsIconSize)
                )
            }

            CircleActionButton(
                size = metrics.topButtonsSize,
                iconSize = metrics.topButtonsIconSize,
                background = MaterialTheme.colorScheme.primary,
                onClick = onAddFriendClick
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAddAlt1,
                    contentDescription = stringResource(R.string.cd_add_friend),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(metrics.topButtonsIconSize)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    metrics: FriendListMetrics,
    onSearchFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.searchHeight)
            .clip(RoundedCornerShape(metrics.searchCorner))
            .background(colorScheme.surface)
            .border(
                width = 1.dp,
                color = colorScheme.outline.copy(alpha = 0.18f),
                shape = RoundedCornerShape(metrics.searchCorner)
            )
            .padding(horizontal = metrics.searchHorizontalPadding)
            .clickable(onClick = onSearchFocus),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = colorScheme.onSurface,
                fontSize = metrics.searchFontSize.sp
            ),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    if (it.isFocused) onSearchFocus()
                },
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.friend_list_search_hint),
                            color = colorScheme.onSurface.copy(alpha = 0.45f),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = metrics.searchFontSize.sp
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = stringResource(R.string.cd_search),
            modifier = Modifier.size(metrics.searchIconSize)
        )
    }
}

@Composable
private fun SearchModeHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    metrics: FriendListMetrics,
    onBackClick: () -> Unit,
    onSearchFocus: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(metrics.topButtonsGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleActionButton(
            size = metrics.topButtonsSize,
            iconSize = metrics.topButtonsIconSize,
            background = MaterialTheme.colorScheme.surface,
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.topButtonsIconSize)
            )
        }

        SearchBar(
            value = query,
            onValueChange = onQueryChange,
            metrics = metrics,
            onSearchFocus = onSearchFocus,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HighlightFriendsSection(
    metrics: FriendListMetrics
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = metrics.highlightSpacingTop)
            .height(metrics.highlightSectionHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        highlightFriends.forEachIndexed { index, friend ->
            HighlightFriendCard(
                friend = friend,
                isCenter = index == 1,
                metrics = metrics,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HighlightFriendCard(
    friend: HighlightFriend,
    isCenter: Boolean,
    metrics: FriendListMetrics,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            SuggestionBubble(
                text = stringResource(friend.bubbleTextRes),
                metrics = metrics,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .align(Alignment.TopCenter)
            )

            StoryAvatar(
                imageRes = friend.imageRes,
                size = if (isCenter) metrics.centerAvatarSize else metrics.highlightAvatarSize,
                ringColors = ringColorsForAccent(
                    accent = friend.ringAccent,
                    colorScheme = colorScheme,
                    isCenter = isCenter
                ),
                modifier = Modifier
                    .padding(top = metrics.highlightBubbleMinHeight - 8.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun SuggestionBubble(
    text: String,
    metrics: FriendListMetrics,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colorScheme.surface)
            .border(
                1.dp,
                colorScheme.outline.copy(alpha = 0.14f),
                RoundedCornerShape(16.dp)
            )
            .padding(
                horizontal = metrics.bubbleHorizontalPadding,
                vertical = metrics.bubbleVerticalPadding
            )
            .height(metrics.highlightBubbleMinHeight),
    ) {
        Text(
            text = text,
            color = colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = metrics.messageFontSize.sp,
                lineHeight = metrics.messageLineHeight.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StoryAvatar(
    imageRes: Int,
    size: Dp,
    ringColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Brush.linearGradient(ringColors))
                .padding(size * 0.06f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(size * 0.055f)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
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
private fun FriendMessageRow(
    friend: FriendMessage,
    metrics: FriendListMetrics
) {
    val colorScheme = MaterialTheme.colorScheme
    val name = stringResource(friend.nameRes)
    val message = stringResource(friend.messageRes)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = friend.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(metrics.rowAvatarSize)
                .clip(CircleShape)
        )

        Box(
            modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(metrics.rowBubbleCorner))
            .defaultMinSize(minHeight = metrics.rowBubbleMinHeight)
            .background(
                if (friend.isHighlighted) {
                    Brush.horizontalGradient(
                            colors = listOf(colorScheme.primary, colorScheme.secondary)
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(colorScheme.surface, colorScheme.surface)
                        )
                    }
                )
                .border(
                    width = if (friend.isHighlighted) 0.dp else 1.dp,
                    color = colorScheme.outline.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(metrics.rowBubbleCorner)
                )
                .padding(
                    horizontal = metrics.rowBubblePaddingHorizontal,
                    vertical = metrics.rowBubblePaddingVertical
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        color = if (friend.isHighlighted) colorScheme.onPrimary else colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = metrics.nameFontSize.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = message,
                        color = if (friend.isHighlighted) {
                            colorScheme.onPrimary.copy(alpha = 0.88f)
                        } else {
                            colorScheme.onSurface.copy(alpha = 0.65f)
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = metrics.messageFontSize.sp
                        ),
                        maxLines = if (friend.isHighlighted) 2 else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!friend.isHighlighted) {
                    Text(
                        text = message,
                        color = colorScheme.onSurface.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = metrics.messageFontSize.sp,
                            lineHeight = metrics.messageLineHeight.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchFriendRow(
    friend: SearchFriendItem,
    metrics: FriendListMetrics
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = friend.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(metrics.rowAvatarSize)
                .clip(CircleShape)
        )

        Text(
            text = friend.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = metrics.nameFontSize.sp
            )
        )
    }
}

@Composable
private fun CircleActionButton(
    size: Dp,
    iconSize: Dp,
    background: Color,
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
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(iconSize),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

private fun ringColorsForAccent(
    accent: FriendRingAccent,
    colorScheme: androidx.compose.material3.ColorScheme,
    isCenter: Boolean
): List<Color> {
    return if (isCenter) {
        listOf(
            colorScheme.secondary,
            colorScheme.primary,
            colorScheme.tertiary
        )
    } else {
        when (accent) {
            FriendRingAccent.Primary -> listOf(
                colorScheme.primary,
                colorScheme.primary.copy(alpha = 0.82f),
                colorScheme.tertiary
            )
            FriendRingAccent.Secondary -> listOf(
                colorScheme.secondary,
                colorScheme.secondary.copy(alpha = 0.82f),
                colorScheme.primary
            )
            FriendRingAccent.Tertiary -> listOf(
                colorScheme.tertiary,
                colorScheme.tertiary.copy(alpha = 0.82f),
                colorScheme.secondary
            )
        }
    }
}

@Composable
private fun rememberFriendListMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): FriendListMetrics {
    val widthRatio = (maxWidth / 390.dp).coerceIn(0.86f, 1.18f)
    val heightRatio = (maxHeight / 844.dp).coerceIn(0.82f, 1.18f)
    val compactness = min(widthRatio, heightRatio)
    val isCompactHeight = maxHeight < 760.dp

    return FriendListMetrics(
        horizontalPadding = (28.dp * widthRatio).coerceIn(18.dp, 32.dp),
        topSpacing = ((28.dp * heightRatio) * if (isCompactHeight) 0.7f else 1f).coerceIn(16.dp, 34.dp),
        topButtonsSize = (48.dp * compactness).coerceIn(42.dp, 52.dp),
        topButtonsIconSize = (22.dp * compactness).coerceIn(18.dp, 24.dp),
        topButtonsGap = (10.dp * widthRatio).coerceIn(8.dp, 14.dp),
        searchHeight = (44.dp * compactness).coerceIn(40.dp, 48.dp),
        searchCorner = (14.dp * compactness).coerceIn(12.dp, 18.dp),
        searchHorizontalPadding = (16.dp * widthRatio).coerceIn(12.dp, 18.dp),
        searchIconSize = (22.dp * compactness).coerceIn(18.dp, 24.dp),
        searchFontSize = (14f * compactness).coerceIn(12f, 15f),
        highlightSpacingTop = ((18.dp * heightRatio) * if (isCompactHeight) 0.75f else 1f).coerceIn(10.dp, 22.dp),
        highlightSectionHeight = ((168.dp * compactness) * if (isCompactHeight) 0.92f else 1f).coerceIn(138.dp, 180.dp),
        highlightAvatarSize = (84.dp * compactness).coerceIn(70.dp, 90.dp),
        centerAvatarSize = (104.dp * compactness).coerceIn(84.dp, 110.dp),
        highlightBubbleMinHeight = (46.dp * compactness).coerceIn(38.dp, 50.dp),
        bubbleHorizontalPadding = (12.dp * compactness).coerceIn(9.dp, 14.dp),
        bubbleVerticalPadding = (9.dp * compactness).coerceIn(7.dp, 11.dp),
        listTopSpacing = ((18.dp * compactness) * if (isCompactHeight) 0.85f else 1f).coerceIn(10.dp, 20.dp),
        rowSpacing = (10.dp * compactness).coerceIn(8.dp, 12.dp),
        sectionSpacing = (16.dp * compactness).coerceIn(12.dp, 18.dp),
        rowAvatarSize = (62.dp * compactness).coerceIn(52.dp, 66.dp),
        rowBubbleMinHeight = (72.dp * compactness).coerceIn(62.dp, 80.dp),
        rowBubbleCorner = (18.dp * compactness).coerceIn(14.dp, 20.dp),
        rowBubblePaddingHorizontal = (14.dp * compactness).coerceIn(12.dp, 16.dp),
        rowBubblePaddingVertical = (12.dp * compactness).coerceIn(10.dp, 14.dp),
        nameFontSize = (15f * compactness).coerceIn(13f, 16f),
        messageFontSize = (13.5f * compactness).coerceIn(11.5f, 14f),
        messageLineHeight = (18f * compactness).coerceIn(15f, 19f),
        searchResultSpacing = (20.dp * compactness).coerceIn(16.dp, 24.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true, widthDp = 390, heightDp = 844)
@Composable
private fun FriendListScreenPreview() {
    MaterialTheme {
        FriendListScreen()
    }
}
