package com.example.dz.presentation.book.category_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicFilterPill
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.category_chip_all
import dz.shared.generated.resources.category_chip_award
import dz.shared.generated.resources.category_chip_new
import dz.shared.generated.resources.category_chip_under
import dz.shared.generated.resources.category_sorted_by
import org.jetbrains.compose.resources.stringResource

private val previewBooks = listOf(
    CategoryDetailBookUi(id = "mexican-gothic", title = "Mexican Gothic", author = "Silvia Moreno-Garcia", tag = "Literary", rating = "4.6", price = "$12.99"),
    CategoryDetailBookUi(id = "red-at-the-bone", title = "Red at the Bone", author = "Jacqueline Woodson", tag = "Literary", rating = "4.5", price = "$10.00", bookmarked = true),
    CategoryDetailBookUi(id = "olive-again", title = "Olive, Again", author = "Elizabeth Strout", tag = "Quiet", rating = "4.3", price = "$13.00"),
    CategoryDetailBookUi(id = "bestiary", title = "Bestiary", author = "K-Ming Chang", tag = "Myth", rating = "4.2", price = "$9.20")
)

private val previewUiState = CategoryDetailUiState(
    categoryId = "literary-fiction",
    title = "Literary fiction",
    description = "412 books · quiet, character-first novels that stay with you.",
    books = previewBooks
)

/**
 * Category detail — one category's book list, in the Organic design system. No screen in the
 * handoff matches this one-to-one (`dz-all-screens.html`'s "Categories" frame is the browse grid
 * that opens it, not this list), so it's restyled with the same Organic primitives and layout
 * every other book-list screen this port has used, rather than a literal new mockup. The filter
 * chips and sort row carry forward unwired from the old screen — same fidelity, not new scope.
 */
@Composable
fun CategoryDetailScreen(
    uiState: CategoryDetailUiState = previewUiState,
    onEvent: (CategoryDetailEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = uiState.title,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = { onEvent(CategoryDetailEvent.BackClicked) },
                            contentDescription = "Back",
                            size = 38.dp
                        )
                    },
                    trailing = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.Search,
                            onClick = { onEvent(CategoryDetailEvent.SearchClicked) },
                            contentDescription = "Search",
                            size = 38.dp
                        )
                    }
                )
            }

            if (uiState.description.isNotBlank()) {
                item(key = "description") {
                    Text(
                        text = uiState.description,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = OrganicColors.neutral700
                    )
                }
            }

            item(key = "chips") {
                Row(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OrganicFilterPill(label = stringResource(Res.string.category_chip_all), selected = true, onClick = {})
                    OrganicFilterPill(label = stringResource(Res.string.category_chip_new), selected = false, onClick = {})
                    OrganicFilterPill(label = stringResource(Res.string.category_chip_award), selected = false, onClick = {})
                    OrganicFilterPill(label = stringResource(Res.string.category_chip_under), selected = false, onClick = {})
                }
            }

            item(key = "sort") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER)
                        .clickable(role = Role.Button) { onEvent(CategoryDetailEvent.SortClicked) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.category_sorted_by),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 13.sp,
                        color = OrganicColors.neutral700
                    )
                    Icon(
                        imageVector = OrganicIcons.ChevronDown,
                        contentDescription = null,
                        tint = OrganicColors.neutral600,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            itemsIndexed(items = uiState.books, key = { _, book -> book.id }) { index, book ->
                Column(modifier = Modifier.padding(horizontal = ORGANIC_GUTTER)) {
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(bottom = 14.dp),
                            thickness = 1.dp,
                            color = OrganicColors.neutral200
                        )
                    }
                    CategoryBookRow(
                        book = book,
                        onClick = { onEvent(CategoryDetailEvent.BookClicked(book.id)) },
                        onBookmarkClick = { onEvent(CategoryDetailEvent.BookmarkClicked(book.id)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryBookRow(
    book: CategoryDetailBookUi,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        OrganicBookCover(title = book.title, coverUrl = book.coverUrl, width = 64.dp, height = 92.dp, cornerRadius = 12.dp)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = book.title,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = OrganicColors.text
            )
            Text(
                text = book.author,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Icon(
                    imageVector = OrganicIcons.StarFilled,
                    contentDescription = null,
                    tint = OrganicColors.accent700,
                    modifier = Modifier.size(11.dp)
                )
                Text(
                    text = book.rating,
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = OrganicColors.neutral800
                )
                if (book.tag.isNotBlank()) {
                    Text(
                        text = "· ${book.tag}",
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 11.sp,
                        color = OrganicColors.neutral600
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = book.price,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = OrganicColors.text
            )
            Icon(
                imageVector = if (book.bookmarked) OrganicIcons.BookmarkFilled else OrganicIcons.Bookmark,
                contentDescription = "Bookmark",
                tint = if (book.bookmarked) OrganicColors.accent else OrganicColors.neutral500,
                modifier = Modifier
                    .size(16.dp)
                    .clickable(role = Role.Button, onClick = onBookmarkClick)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 900)
@Composable
private fun CategoryDetailScreenPreview() {
    CategoryDetailScreen()
}
