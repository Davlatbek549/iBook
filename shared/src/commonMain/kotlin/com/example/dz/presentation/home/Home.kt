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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import com.example.dz.designsystem.components.organic.OrganicGenreTile
import com.example.dz.designsystem.components.organic.OrganicGoalCard
import com.example.dz.designsystem.components.organic.OrganicGenreTints
import com.example.dz.designsystem.components.organic.OrganicHeroCard
import com.example.dz.designsystem.components.organic.OrganicListRowCard
import com.example.dz.designsystem.components.organic.OrganicPersonRow
import com.example.dz.designsystem.components.organic.OrganicSectionLabel
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
import dz.shared.generated.resources.goal_finish_it
import dz.shared.generated.resources.goal_met
import dz.shared.generated.resources.goal_met_body
import dz.shared.generated.resources.goal_minutes_to_go
import dz.shared.generated.resources.goal_of_minutes
import dz.shared.generated.resources.goal_start_reading
import dz.shared.generated.resources.goal_streak_days
import dz.shared.generated.resources.home_browse
import dz.shared.generated.resources.home_editors_pick
import dz.shared.generated.resources.home_friends_reading
import dz.shared.generated.resources.home_greeting
import dz.shared.generated.resources.home_keep_going
import dz.shared.generated.resources.home_new_this_week
import dz.shared.generated.resources.home_one_of_them
import dz.shared.generated.resources.home_picked_for_you
import dz.shared.generated.resources.home_reading_a_book
import dz.shared.generated.resources.home_reading_right_now
import dz.shared.generated.resources.home_search
import dz.shared.generated.resources.home_see_all
import dz.shared.generated.resources.home_shelf_also_like
import dz.shared.generated.resources.home_shelf_something_different
import dz.shared.generated.resources.home_shelf_worth_a_look
import dz.shared.generated.resources.home_your_shelf
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
    onGoalClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val picked = uiState.books
    val newThisWeek = uiState.books.drop(PICKED_FOR_YOU_LIMIT).take(NEW_THIS_WEEK_LIMIT)
    val pickedShown = picked.take(PICKED_FOR_YOU_LIMIT)
    val newKeys = newThisWeek.uniqueLazyKeys { it.id }
    val shelfKeys = uiState.shelf.uniqueLazyKeys { it.book.id }
    val friendKeys = uiState.friendsReading.uniqueLazyKeys { it.id }
    // The genres that already have a carousel are skipped by the tile grid, so the same three
    // names do not appear twice a few hundred pixels apart.
    val shelvedIds = uiState.categoryShelves.map { it.category.id }.toSet()
    val genres = uiState.categories.filterNot { it.id in shelvedIds }.take(GENRE_LIMIT)
    val pickedForYouLabel = stringResource(Res.string.home_picked_for_you)
    val seeAllLabel = stringResource(Res.string.home_see_all)

    /**
     * Genre shelves are headed as recommendations rather than by genre name. Each is still a
     * genre slice underneath, so these say why you might read them and stop short of claiming
     * anything about other readers — nothing measures that yet.
     */
    val shelfHeadings = listOf(
        stringResource(Res.string.home_shelf_also_like),
        stringResource(Res.string.home_shelf_worth_a_look),
        stringResource(Res.string.home_shelf_something_different),
    )

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

            uiState.goal?.let { goal ->
                item(key = "goal") {
                    OrganicGoalCard(
                        minutesToday = goal.minutesToday,
                        targetMinutes = goal.targetMinutes,
                        progress = goal.progress,
                        title = if (goal.isMet) {
                            stringResource(Res.string.goal_met)
                        } else {
                            stringResource(Res.string.goal_minutes_to_go, goal.minutesRemaining)
                        },
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        subtitle = when {
                            goal.isMet -> stringResource(Res.string.goal_met_body, goal.minutesToday)
                            goal.streakDays > 0 -> stringResource(Res.string.goal_streak_days, goal.streakDays)
                            else -> stringResource(Res.string.goal_start_reading)
                        },
                        targetLabel = stringResource(Res.string.goal_of_minutes, goal.targetMinutes),
                        // Nothing to finish if there is no book open, and nothing to nudge if the
                        // goal is already met.
                        actionLabel = if (!goal.isMet && uiState.continueReading != null) {
                            stringResource(Res.string.goal_finish_it)
                        } else {
                            null
                        },
                        onActionClick = onKeepReadingClick,
                        onClick = onGoalClick,
                        ringSize = 116.dp,
                    )
                }
            }

            if (uiState.shelf.isNotEmpty()) {
                item(key = "shelf-header") {
                    OrganicSectionHeader(
                        title = stringResource(Res.string.home_your_shelf),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    )
                }
                items(uiState.shelf.size, key = { shelfKeys[it] }) { index ->
                    val entry = uiState.shelf[index]
                    OrganicListRowCard(
                        title = entry.book.title,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        author = entry.book.authors.firstOrNull()?.name,
                        coverUrl = entry.book.coverUrl,
                        onClick = { onBookClick(entry.book.id) },
                        trailing = {
                            OrganicProgressDonut(
                                progress = entry.progressPercent / 100f,
                                size = 44.dp,
                                innerSize = 34.dp,
                                trackColor = OrganicColors.neutral200,
                                innerColor = OrganicColors.neutral100,
                                label = {
                                    Text(
                                        text = entry.progressPercent.toString(),
                                        fontFamily = organicBodyFontFamily(),
                                        fontSize = 11.sp,
                                        color = OrganicColors.neutral800
                                    )
                                }
                            )
                        },
                    )
                }
            }

            bookCarousel(
                key = "picked",
                title = pickedForYouLabel,
                books = pickedShown,
                actionLabel = seeAllLabel,
                onActionClick = onSeeAllClick,
                onBookClick = onBookClick,
            )

            uiState.editorsPick?.let { pick ->
                item(key = "editors-pick") {
                    OrganicHeroCard(
                        title = pick.title,
                        kicker = stringResource(Res.string.home_editors_pick),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        author = pick.authors.firstOrNull()?.name,
                        price = pick.price,
                        coverUrl = pick.coverUrl,
                        onClick = { onBookClick(pick.id) },
                    )
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

            if (uiState.friendsReading.isNotEmpty()) {
                item(key = "friends-label") {
                    OrganicSectionLabel(
                        text = stringResource(Res.string.home_friends_reading),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    )
                }
                items(uiState.friendsReading.size, key = { friendKeys[it] }) { index ->
                    val friend = uiState.friendsReading[index]
                    OrganicPersonRow(
                        name = friend.name,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        subtitle = friend.currentBook?.title?.let {
                            stringResource(Res.string.home_reading_a_book, it)
                        },
                        avatarBackground = friendAvatarTints[index % friendAvatarTints.size],
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

            uiState.categoryShelves.forEachIndexed { index, shelf ->
                bookCarousel(
                    key = "shelf-${shelf.category.id}",
                    title = shelfHeadings[index % shelfHeadings.size],
                    books = shelf.books,
                    actionLabel = seeAllLabel,
                    onActionClick = { onCategoryClick(shelf.category.id) },
                    onBookClick = onBookClick,
                )
            }

            if (genres.isNotEmpty()) {
                item(key = "genres-label") {
                    OrganicSectionLabel(
                        text = stringResource(Res.string.home_browse),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    )
                }
                // A two-column grid inside a LazyColumn: the rows are laid out in pairs rather
                // than nesting a LazyVerticalGrid, which cannot measure inside a vertical scroll.
                items(
                    count = (genres.size + 1) / 2,
                    key = { "genre-row-$it" }
                ) { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ORGANIC_GUTTER),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (column in 0..1) {
                            val index = row * 2 + column
                            val category = genres.getOrNull(index)
                            if (category == null) {
                                Box(modifier = Modifier.weight(1f))
                            } else {
                                val tint = OrganicGenreTints[index % OrganicGenreTints.size]
                                OrganicGenreTile(
                                    name = category.name,
                                    modifier = Modifier.weight(1f),
                                    background = tint.background,
                                    decorationColor = tint.decoration,
                                    spineColor = tint.spine,
                                    textColor = tint.text,
                                    subtitleColor = tint.subtitle,
                                    onClick = onSeeAllClick,
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

/**
 * A titled row of covers that scrolls sideways — Picked for you, and one per genre.
 *
 * Lives in [LazyListScope] rather than a composable so the header and the row stay separate list
 * items: a section that is one item cannot be recycled, and Home now stacks several of these.
 * Labels arrive resolved because `stringResource` needs a composable and this is not one.
 */
private fun LazyListScope.bookCarousel(
    key: String,
    title: String,
    books: List<Book>,
    actionLabel: String,
    onActionClick: () -> Unit,
    onBookClick: (String) -> Unit,
) {
    if (books.isEmpty()) return
    val bookKeys = books.uniqueLazyKeys { it.id }

    item(key = "$key-header") {
        OrganicSectionHeader(
            title = title,
            modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
            actionLabel = actionLabel,
            onActionClick = onActionClick,
        )
    }
    item(key = "$key-carousel") {
        LazyRow(
            contentPadding = PaddingValues(horizontal = ORGANIC_GUTTER),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(books.size, key = { bookKeys[it] }) { index ->
                val book = books[index]
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
        decoration = {
            // `right:-52; top:-58; 150×150`, drawn rather than laid out so it cannot stretch the
            // card past the 128dp the design gives it.
            drawCircle(
                color = OrganicColors.accent300.copy(alpha = 0.55f),
                radius = 75.dp.toPx(),
                center = Offset(size.width - 23.dp.toPx(), 17.dp.toPx())
            )
        },
        onClick = onClick,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
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

/** Six tiles is three rows — enough to browse without turning Home into the Categories screen. */
private const val GENRE_LIMIT = 6

/** The design cycles friend avatars through sage, terracotta and neutral rather than one colour. */
private val friendAvatarTints = listOf(
    OrganicColors.accent2_600,
    OrganicColors.accent600,
    OrganicColors.neutral500,
)

/** Stands in for an initial while the profile is still loading. */
private const val FALLBACK_INITIAL = "•"

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
