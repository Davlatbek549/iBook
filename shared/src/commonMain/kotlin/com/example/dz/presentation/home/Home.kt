package com.example.dz.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicAvatar
import com.example.dz.designsystem.components.organic.OrganicAvatarStack
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicCoverCard
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicListRow
import com.example.dz.designsystem.components.organic.OrganicProgressDonut
import com.example.dz.designsystem.components.organic.OrganicRowChevron
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicSectionHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.LibraryBook
import com.example.dz.presentation.common.uniqueLazyKeys
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.home_greeting
import dz.shared.generated.resources.home_keep_going
import dz.shared.generated.resources.home_new_this_week
import dz.shared.generated.resources.home_one_of_them
import dz.shared.generated.resources.home_picked_for_you
import dz.shared.generated.resources.home_reading_right_now
import dz.shared.generated.resources.home_search
import dz.shared.generated.resources.home_see_all
import org.jetbrains.compose.resources.stringResource

/**
 * Home — the reading hub, and the screen the rest of the redesign takes its conventions from.
 *
 * Layout from the handoff's `dz-all-screens.html`: a 12dp-gapped column with a 24dp gutter, 16dp
 * of top padding, and enough bottom padding to clear the floating tab bar.
 *
 * Sections render only when they have something to say — a reader with nothing in progress gets no
 * Keep going card rather than an empty one. The handoff draws one full state; this follows its
 * nearest pattern rather than inventing empty-state art it never specified.
 */
@Composable
fun HomeScreen(
    uiState: HomeUiState = HomeUiState(),
    onKeepReadingClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSeeAllClick: () -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onPresenceClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val picked = uiState.books
    val newThisWeek = uiState.books.drop(PICKED_FOR_YOU_LIMIT).take(NEW_THIS_WEEK_LIMIT)
    val pickedShown = picked.take(PICKED_FOR_YOU_LIMIT)
    val pickedKeys = pickedShown.uniqueLazyKeys { it.id }
    val newKeys = newThisWeek.uniqueLazyKeys { it.id }

    OrganicScreen {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "greeting") {
                GreetingRow(
                    name = uiState.userName,
                    onAvatarClick = onProfileClick,
                    onSearchClick = onSearchClick,
                )
            }

            uiState.continueReading?.let { current ->
                item(key = "keep-going") {
                    KeepGoingCard(
                        libraryBook = current,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        onClick = onKeepReadingClick,
                    )
                }
            }

            if (picked.isNotEmpty()) {
                item(key = "picked-header") {
                    OrganicSectionHeader(
                        title = stringResource(Res.string.home_picked_for_you),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        actionLabel = stringResource(Res.string.home_see_all),
                        onActionClick = onSeeAllClick,
                    )
                }
                item(key = "picked-carousel") {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = ORGANIC_GUTTER),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(pickedShown.size, key = { pickedKeys[it] }) { index ->
                            val book = pickedShown[index]
                            OrganicCoverCard(
                                title = book.title,
                                author = book.authors.firstOrNull()?.name.orEmpty(),
                                coverUrl = book.coverUrl,
                                onClick = { onBookClick(book.id) },
                            )
                        }
                    }
                }
            }

            uiState.presence?.let { presence ->
                item(key = "presence") {
                    PresenceCard(
                        presence = presence,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        onClick = onPresenceClick,
                    )
                }
            }

            if (newThisWeek.isNotEmpty()) {
                item(key = "new-header") {
                    OrganicSectionHeader(
                        title = stringResource(Res.string.home_new_this_week),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        actionLabel = stringResource(Res.string.home_see_all),
                        onActionClick = onSeeAllClick,
                    )
                }
                items(newThisWeek.size, key = { newKeys[it] }) { index ->
                    val book = newThisWeek[index]
                    OrganicListRow(
                        title = book.title,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        author = book.authors.firstOrNull()?.name,
                        meta = book.price,
                        coverUrl = book.coverUrl,
                        onClick = { onBookClick(book.id) },
                        trailing = { OrganicRowChevron() },
                    )
                }
            }
        }
    }
}

/** 46dp avatar, the greeting stacked beside it, and the search circle. */
@Composable
private fun GreetingRow(
    name: String?,
    onAvatarClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ORGANIC_GUTTER),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganicAvatar(
            name = name.orEmpty().ifBlank { FALLBACK_INITIAL },
            size = 46.dp,
            onClick = onAvatarClick,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                text = stringResource(Res.string.home_greeting),
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700
            )
            // The name is the heading here, so an unloaded profile leaves the greeting alone
            // rather than showing a placeholder where a person's name belongs.
            if (!name.isNullOrBlank()) {
                Text(
                    text = name,
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    lineHeight = 26.4.sp,
                    color = OrganicColors.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        OrganicCircleIconButton(
            icon = OrganicIcons.Search,
            onClick = onSearchClick,
            contentDescription = stringResource(Res.string.home_search),
        )
    }
}

/**
 * The Keep going card: cover, what you are in the middle of, and how far through you are.
 *
 * A 150dp accent-300 circle bleeds off the top-right corner, clipped by the card — the design's one
 * piece of decoration on this screen.
 */
@Composable
private fun KeepGoingCard(
    libraryBook: LibraryBook,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OrganicCard(
        modifier = modifier.fillMaxWidth(),
        background = OrganicColors.accent200,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 52.dp, y = (-74).dp)
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(OrganicColors.accent300.copy(alpha = 0.55f))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OrganicBookCover(
                    title = libraryBook.book.title,
                    coverUrl = libraryBook.book.coverUrl,
                    width = 66.dp,
                    height = 96.dp,
                    cornerRadius = 12.dp,
                    elevated = true,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OrganicKicker(
                        text = stringResource(Res.string.home_keep_going),
                        color = OrganicColors.accent800,
                    )
                    Text(
                        text = libraryBook.book.title,
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        lineHeight = 23.sp,
                        color = OrganicColors.accent900,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    // The design reads "18 min left in Chapter Four". Neither a chapter nor a
                    // time estimate exists in the domain — the reader tracks pages, not chapters —
                    // so this says what is true and keeps the line.
                    libraryBook.book.authors.firstOrNull()?.let { author ->
                        Text(
                            text = author.name,
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 12.sp,
                            color = OrganicColors.accent800,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                OrganicProgressDonut(
                    progress = libraryBook.progressPercent / 100f,
                    size = 58.dp,
                    innerSize = 44.dp,
                    innerColor = OrganicColors.accent200,
                    label = {
                        Text(
                            text = "${libraryBook.progressPercent}%",
                            fontFamily = organicHeadingFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = OrganicColors.accent900
                        )
                    }
                )
            }
        }
    }
}

/** The sage presence card — who is in a book right now. */
@Composable
private fun PresenceCard(
    presence: HomePresence,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OrganicCard(
        modifier = modifier.fillMaxWidth(),
        background = OrganicColors.accent2_200,
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OrganicAvatarStack(names = listOf(presence.firstName, "", ""))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(OrganicColors.accent2_700)
                    )
                    Text(
                        text = stringResource(
                            Res.string.home_reading_right_now,
                            presence.readerCount.toString()
                        ),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OrganicColors.accent2_900
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        val line = stringResource(Res.string.home_one_of_them, presence.firstName)
                        val nameEnd = presence.firstName.length
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(line.take(nameEnd))
                        }
                        append(line.drop(nameEnd))
                    },
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp,
                    color = OrganicColors.accent2_800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            OrganicRowChevron(tint = OrganicColors.accent2_800)
        }
    }
}

/** The carousel shows seven; what follows it fills the list below. */
private const val PICKED_FOR_YOU_LIMIT = 7
private const val NEW_THIS_WEEK_LIMIT = 2

/** Stands in for an initial while the profile is still loading. */
private const val FALLBACK_INITIAL = "•"

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
