package com.example.dz.presentation.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicFilterPills
import com.example.dz.designsystem.components.organic.OrganicListRowCard
import com.example.dz.designsystem.components.organic.OrganicProgressDonut
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.components.organic.OrganicSectionHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.domain.model.Collection
import com.example.dz.domain.model.LibraryBook
import com.example.dz.presentation.common.uniqueLazyKeys
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.library_book_count
import dz.shared.generated.resources.library_collections
import dz.shared.generated.resources.library_empty_finished
import dz.shared.generated.resources.library_empty_reading
import dz.shared.generated.resources.library_empty_to_read
import dz.shared.generated.resources.library_pages
import dz.shared.generated.resources.library_pages_left
import dz.shared.generated.resources.library_tab_finished
import dz.shared.generated.resources.library_tab_reading
import dz.shared.generated.resources.library_tab_to_read
import dz.shared.generated.resources.library_your_shelf
import dz.shared.generated.resources.home_search
import org.jetbrains.compose.resources.stringResource

/**
 * Library — the reader's own shelf, in three states.
 *
 * The handoff draws two of them (Reading and Finished) and names the third; they are the same
 * screen with a different filter, so this is one composable rather than two. A row carries the
 * badge its state has something to say with: a progress ring while a book is open, a tick once it
 * is done, and nothing at all for one not started.
 *
 * Layout from `dz-all-screens.html`: a 20dp-gapped column on a 24dp gutter.
 */
@Composable
fun LibraryScreen(
    uiState: LibraryUiState = LibraryUiState(),
    onFilterSelect: (LibraryFilter) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onCollectionClick: (String) -> Unit = {},
    onCollectionsClick: () -> Unit = {},
) {
    val shelf = uiState.shelf
    val shelfKeys = shelf.uniqueLazyKeys { it.book.id }
    val filters = listOf(LibraryFilter.READING, LibraryFilter.TO_READ, LibraryFilter.FINISHED)
    val filterLabels = listOf(
        stringResource(Res.string.library_tab_reading),
        stringResource(Res.string.library_tab_to_read),
        stringResource(Res.string.library_tab_finished),
    )

    OrganicScreen {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            // Rows sit 12dp apart. Sections want 20, so the items that start one add the
            // difference themselves rather than every row paying for the wider gap.
            verticalArrangement = Arrangement.spacedBy(ROW_GAP)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = stringResource(Res.string.library_your_shelf),
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    trailing = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.Search,
                            onClick = onSearchClick,
                            contentDescription = stringResource(Res.string.home_search),
                        )
                    },
                )
            }

            item(key = "filters") {
                OrganicFilterPills(
                    labels = filterLabels,
                    selectedIndex = filters.indexOf(uiState.filter),
                    onSelect = { onFilterSelect(filters[it]) },
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER, vertical = SECTION_EXTRA),
                )
            }

            if (shelf.isEmpty()) {
                item(key = "empty") {
                    ShelfEmptyNote(
                        filter = uiState.filter,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    )
                }
            } else {
                // The 20dp column gap is the rhythm between sections; rows sit 12dp apart, so the
                // spacing is applied per row rather than by the list.
                items(shelf.size, key = { shelfKeys[it] }) { index ->
                    val entry = shelf[index]
                    OrganicListRowCard(
                        title = entry.book.title,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        author = entry.book.authors.firstOrNull()?.name,
                        meta = entry.metaLine(),
                        onClick = { onBookClick(entry.book.id) },
                        coverUrl = entry.book.coverUrl,
                        trailing = { ShelfBadge(entry) },
                    )
                }
            }

            if (uiState.collections.isNotEmpty()) {
                item(key = "collections-header") {
                    OrganicSectionHeader(
                        title = stringResource(Res.string.library_collections),
                        modifier = Modifier.padding(
                            start = ORGANIC_GUTTER,
                            end = ORGANIC_GUTTER,
                            top = SECTION_EXTRA,
                        ),
                        onActionClick = onCollectionsClick,
                    )
                }
                item(key = "collections") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ORGANIC_GUTTER),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // The design shows two side by side; more than that would need a carousel
                        // it does not draw, so the rest live on the Collections screen.
                        uiState.collections.take(COLLECTIONS_SHOWN).forEachIndexed { index, collection ->
                            CollectionTile(
                                collection = collection,
                                modifier = Modifier.weight(1f),
                                tint = collectionTints[index % collectionTints.size],
                                onClick = { onCollectionClick(collection.id) },
                            )
                        }
                        // One collection would otherwise stretch to the full width, which reads as
                        // a banner rather than as one of a pair.
                        if (uiState.collections.size < COLLECTIONS_SHOWN) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

/**
 * What the row says under the author.
 *
 * The design reads "18 min left" and "Finished in June". Neither is available: nothing estimates
 * reading time, and a library book carries no date it was finished. Pages remaining is the true
 * thing the data does support, and it answers the same question — how much is left.
 */
@Composable
private fun LibraryBook.metaLine(): String? = when (filter) {
    LibraryFilter.READING -> pagesLeft?.let { stringResource(Res.string.library_pages_left, it) }
    LibraryFilter.TO_READ -> book.pageCount?.let { stringResource(Res.string.library_pages, it) }
    LibraryFilter.FINISHED -> null
}

/** A ring while a book is open, a tick once it is done, nothing before it is started. */
@Composable
private fun ShelfBadge(entry: LibraryBook) {
    when (entry.filter) {
        LibraryFilter.READING -> OrganicProgressDonut(
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

        LibraryFilter.FINISHED -> Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(OrganicColors.accent2),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = OrganicIcons.Check,
                contentDescription = stringResource(Res.string.library_tab_finished),
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        LibraryFilter.TO_READ -> Unit
    }
}

/**
 * What an empty shelf says. The handoff draws no empty state for Library, so this follows its
 * nearest pattern — a filled card and a plain sentence — rather than inventing artwork.
 */
@Composable
private fun ShelfEmptyNote(
    filter: LibraryFilter,
    modifier: Modifier = Modifier,
) {
    OrganicCard(
        modifier = modifier.fillMaxWidth(),
        background = OrganicColors.neutral100,
        contentPadding = PaddingValues(18.dp),
    ) {
        Text(
            text = stringResource(
                when (filter) {
                    LibraryFilter.READING -> Res.string.library_empty_reading
                    LibraryFilter.TO_READ -> Res.string.library_empty_to_read
                    LibraryFilter.FINISHED -> Res.string.library_empty_finished
                }
            ),
            fontFamily = organicBodyFontFamily(),
            fontSize = 13.sp,
            lineHeight = 19.5.sp,
            color = OrganicColors.neutral700
        )
    }
}

/** A shelf the reader built: a coloured disc, the name, and how many books are on it. */
@Composable
private fun CollectionTile(
    collection: Collection,
    tint: CollectionTint,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OrganicCard(
        modifier = modifier,
        background = tint.background,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(tint.disc)
            )
            Text(
                text = collection.title,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = tint.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(Res.string.library_book_count, collection.books.size),
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = tint.subtitle
            )
        }
    }
}

/** One collection tile's colours: ground, disc, title, count. */
private data class CollectionTint(
    val background: Color,
    val disc: Color,
    val text: Color,
    val subtitle: Color,
)

/** The design alternates sage and terracotta between the two tiles. */
private val collectionTints = listOf(
    CollectionTint(
        background = OrganicColors.accent2_200,
        disc = OrganicColors.accent2_400,
        text = OrganicColors.accent2_900,
        subtitle = OrganicColors.accent2_800,
    ),
    CollectionTint(
        background = OrganicColors.accent200,
        disc = OrganicColors.accent400,
        text = OrganicColors.accent900,
        subtitle = OrganicColors.accent800,
    ),
)

private val ROW_GAP = 12.dp

/** Tops up the 12dp row gap to the 20dp the design puts between sections. */
private val SECTION_EXTRA = 8.dp
private const val COLLECTIONS_SHOWN = 2

@Preview
@Composable
fun LibraryScreenPreview() {
    LibraryScreen()
}
