package com.example.dz.presentation.store.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicBrowseTileStyles
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicFilterPill
import com.example.dz.designsystem.components.organic.OrganicGenreTile
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.components.organic.OrganicSearchBar
import com.example.dz.designsystem.components.organic.OrganicSectionLabel
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.domain.model.Category
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.browse_all_categories
import dz.shared.generated.resources.browse_book_count
import dz.shared.generated.resources.browse_mood
import dz.shared.generated.resources.browse_mood_categories
import dz.shared.generated.resources.browse_mood_cosy
import dz.shared.generated.resources.browse_mood_epic
import dz.shared.generated.resources.browse_mood_short
import dz.shared.generated.resources.browse_mood_unsettling
import dz.shared.generated.resources.browse_search_hint
import dz.shared.generated.resources.browse_sort_az
import dz.shared.generated.resources.browse_sort_az_description
import dz.shared.generated.resources.browse_title
import dz.shared.generated.resources.nav_back
import org.jetbrains.compose.resources.stringResource

/**
 * Browse — the handoff's Categories screen: a way into search, a row of moods, and every genre as
 * a tile with how many books it holds.
 *
 * Layout from `dz-all-screens.html` (`#scr-categories`): an 18dp rhythm between sections on a 24dp
 * gutter, with the genre grid 12dp apart both ways.
 *
 * Pushed from Store, so it carries a back circle and no tab bar. The frame draws the bar with Store
 * lit; on a device the back stack is what says where this sits, and a bar here would take a tap on
 * Store to mean "restore the Store stack" — which lands right back on Browse.
 *
 * No mood is chosen on arrival. The frame shows Cosy lit over a heading that says "All categories",
 * which cannot both be true: with a mood on, the grid is that mood's genres, and the heading says so.
 */
@Composable
fun BrowseScreen(
    uiState: BrowseUiState = BrowseUiState(),
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoodClick: (BrowseMood) -> Unit = {},
    onSortClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
) {
    val shown = uiState.shownCategories

    OrganicScreen {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 12.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            // The grid's rows sit 12dp apart. Sections want 18, so the items that start one add
            // the difference themselves rather than every row paying for the wider gap.
            verticalArrangement = Arrangement.spacedBy(ROW_GAP)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = stringResource(Res.string.browse_title),
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = onBackClick,
                            contentDescription = stringResource(Res.string.nav_back),
                            size = 38.dp,
                            iconSize = 18.dp,
                        )
                    },
                )
            }

            item(key = "search") {
                OrganicSearchBar(
                    placeholder = stringResource(Res.string.browse_search_hint),
                    onClick = onSearchClick,
                    modifier = Modifier.padding(
                        start = ORGANIC_GUTTER,
                        end = ORGANIC_GUTTER,
                        top = SECTION_EXTRA,
                    ),
                )
            }

            item(key = "moods") {
                MoodRow(
                    selected = uiState.mood,
                    onMoodClick = onMoodClick,
                    modifier = Modifier.padding(top = SECTION_EXTRA),
                )
            }

            item(key = "categories-header") {
                CategoriesHeader(
                    mood = uiState.mood,
                    sortedAlphabetically = uiState.sort == BrowseSort.ALPHABETICAL,
                    onSortClick = onSortClick,
                    modifier = Modifier.padding(top = SECTION_EXTRA),
                )
            }

            val errorMessage = uiState.errorMessage
            if (shown.isEmpty() && errorMessage != null) {
                item(key = "error") {
                    OrganicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ORGANIC_GUTTER),
                        background = OrganicColors.neutral100,
                        contentPadding = PaddingValues(18.dp),
                    ) {
                        Text(
                            text = errorMessage,
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 13.sp,
                            lineHeight = 19.5.sp,
                            color = OrganicColors.neutral700
                        )
                    }
                }
            }

            // A two-column grid inside a LazyColumn: rows laid out in pairs rather than nesting a
            // LazyVerticalGrid, which cannot measure inside a vertical scroll. Same as Home's.
            items(
                count = (shown.size + 1) / 2,
                key = { "genre-row-$it" }
            ) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.spacedBy(ROW_GAP)
                ) {
                    for (column in 0..1) {
                        val index = row * 2 + column
                        val category = shown.getOrNull(index)
                        if (category == null) {
                            Box(modifier = Modifier.weight(1f))
                        } else {
                            GenreTile(
                                category = category,
                                // Styled by position rather than by genre, so the grid keeps the
                                // frame's run of colours however a mood or A–Z reshuffles it.
                                index = index,
                                bookCount = uiState.bookCounts[category.id],
                                onClick = { onCategoryClick(category.id) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * "In the mood for" over a sideways row of mood pills. The row scrolls rather than wraps: the frame
 * lets the last pill run off the edge, which is what tells a reader there are more.
 */
@Composable
private fun MoodRow(
    selected: BrowseMood?,
    onMoodClick: (BrowseMood) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OrganicSectionLabel(
            text = stringResource(Res.string.browse_mood),
            modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = ORGANIC_GUTTER),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            items(BrowseMood.entries.size, key = { BrowseMood.entries[it].name }) { index ->
                val mood = BrowseMood.entries[index]
                OrganicFilterPill(
                    label = mood.label(),
                    selected = mood == selected,
                    onClick = { onMoodClick(mood) },
                    horizontalPadding = 16.dp,
                    selectedFontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

/**
 * The grid's heading and its sort. "All categories" until a mood narrows it, then that mood's name.
 *
 * A–Z is a switch, and draws like one: off, it is the accent link the frame shows; on, it fills in
 * the accent the way a chosen pill does, so a reader can tell the order has changed.
 */
@Composable
private fun CategoriesHeader(
    mood: BrowseMood?,
    sortedAlphabetically: Boolean,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sortDescription = stringResource(Res.string.browse_sort_az_description)

    Row(
        modifier = modifier
            .fillMaxWidth()
            // The A–Z label carries 10dp of padding so its filled state has room; the row gives
            // that back on the right so the resting label still sits on the gutter.
            .padding(start = ORGANIC_GUTTER, end = ORGANIC_GUTTER - SORT_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganicSectionLabel(
            text = if (mood == null) {
                stringResource(Res.string.browse_all_categories)
            } else {
                stringResource(Res.string.browse_mood_categories, mood.label())
            },
            modifier = Modifier.weight(1f),
        )
        Text(
            text = stringResource(Res.string.browse_sort_az),
            modifier = Modifier
                .clip(RoundedCornerShape(OrganicShape.pill))
                .background(if (sortedAlphabetically) OrganicColors.accent else Color.Transparent)
                .toggleable(
                    value = sortedAlphabetically,
                    role = Role.Switch,
                    onValueChange = { onSortClick() }
                )
                .semantics { contentDescription = sortDescription }
                .padding(horizontal = SORT_PADDING, vertical = 4.dp),
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = if (sortedAlphabetically) Color.White else OrganicColors.accent700
        )
    }
}

/** One genre, dressed in the style its place in the grid calls for. */
@Composable
private fun GenreTile(
    category: Category,
    index: Int,
    bookCount: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val style = OrganicBrowseTileStyles[index % OrganicBrowseTileStyles.size]
    OrganicGenreTile(
        name = category.name,
        modifier = modifier,
        background = style.tint.background,
        decorationColor = style.tint.decoration,
        spineColor = style.tint.spine,
        textColor = style.tint.text,
        subtitle = bookCount?.let { stringResource(Res.string.browse_book_count, it.withThousands()) },
        subtitleColor = style.tint.subtitle,
        art = style.art,
        onClick = onClick,
    )
}

@Composable
private fun BrowseMood.label(): String = stringResource(
    when (this) {
        BrowseMood.COSY -> Res.string.browse_mood_cosy
        BrowseMood.UNSETTLING -> Res.string.browse_mood_unsettling
        BrowseMood.SHORT -> Res.string.browse_mood_short
        BrowseMood.EPIC -> Res.string.browse_mood_epic
    }
)

/** "2140" as the frame writes it, "2,140". The strings are English-only, so the separator is too. */
internal fun Int.withThousands(): String =
    toString().reversed().chunked(3).joinToString(",").reversed()

private val ROW_GAP = 12.dp

/** Tops up the 12dp row gap to the 18dp the design puts between sections. */
private val SECTION_EXTRA = 6.dp

private val SORT_PADDING = 10.dp

@Preview
@Composable
fun BrowseScreenPreview() {
    BrowseScreen()
}
