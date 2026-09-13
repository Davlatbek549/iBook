package com.example.dz.presentation.collections.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicSectionLabel
import com.example.dz.designsystem.components.organic.shelfColorAt
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.presentation.common.uniqueLazyKeys
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.collection_edit
import dz.shared.generated.resources.collection_empty_books
import dz.shared.generated.resources.collection_in_this
import dz.shared.generated.resources.collection_read_next
import dz.shared.generated.resources.library_book_count
import dz.shared.generated.resources.nav_back
import org.jetbrains.compose.resources.stringResource

/**
 * Collection detail — one shelf, washed in the colour the reader gave it.
 *
 * The header is the screen's whole identity: the shelf's own colour as a ground, its covers fanned
 * out, and the one action worth having at the top of it. That wash is why a shelf colour is worth
 * storing — picking a swatch tints a screen, not a dot.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun CollectionDetails(
    uiState: CollectionDetailsUiState = CollectionDetailsUiState(),
    onEvent: (CollectionDetailsEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val shelf = shelfColorAt(uiState.colorIndex)
    val keys = uiState.books.uniqueLazyKeys { it.id }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(OrganicColors.bg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = ORGANIC_TAB_BAR_CLEARANCE)
        ) {
            item(key = "header") {
                ShelfHeader(uiState = uiState, shelfInk = shelf.ink, shelfGround = shelf.ground,
                    shelfSwatch = shelf.swatch, onEvent = onEvent)
            }

            item(key = "list-label") {
                OrganicSectionLabel(
                    text = stringResource(Res.string.collection_in_this),
                    modifier = Modifier.padding(
                        start = ORGANIC_GUTTER,
                        end = ORGANIC_GUTTER,
                        top = 20.dp,
                        bottom = 14.dp,
                    ),
                )
            }

            if (uiState.books.isEmpty()) {
                item(key = "empty") {
                    Text(
                        text = stringResource(Res.string.collection_empty_books),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 13.sp,
                        color = OrganicColors.neutral700
                    )
                }
            }

            items(uiState.books.size, key = { keys[it] }) { index ->
                ShelfBookRow(
                    position = index + 1,
                    book = uiState.books[index],
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER, vertical = 7.dp),
                    onClick = { onEvent(CollectionDetailsEvent.BookClicked(uiState.books[index].id)) },
                )
            }
        }
    }
}

/**
 * The coloured block at the top. Its bottom corners are rounded and its top are not — it runs up
 * under the status bar, so it reads as the page's own head rather than as a card sitting on it.
 */
@Composable
private fun ShelfHeader(
    uiState: CollectionDetailsUiState,
    shelfInk: Color,
    shelfGround: Color,
    shelfSwatch: Color,
    onEvent: (CollectionDetailsEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
            .background(shelfGround)
            // The colour runs up behind the status bar — the design butts it against the top of
            // the frame — while the content it holds starts below.
            .statusBarsPadding()
            .padding(start = ORGANIC_GUTTER, end = ORGANIC_GUTTER, bottom = 24.dp)
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OrganicCircleIconButton(
                icon = OrganicIcons.ChevronLeft,
                onClick = { onEvent(CollectionDetailsEvent.BackClicked) },
                contentDescription = stringResource(Res.string.nav_back),
                size = 38.dp,
                iconSize = 17.dp,
                background = OrganicColors.bg,
                tint = shelfInk,
            )
            Box(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(Res.string.collection_edit),
                modifier = Modifier
                    .height(38.dp)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(OrganicColors.bg)
                    .clickable(role = Role.Button) { onEvent(CollectionDetailsEvent.EditClicked) }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = shelfInk
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Bigger than the stack on the Collections list, and its back card takes the shelf's
            // own swatch so the colour reads even on a shelf whose covers are all dark.
            Box(modifier = Modifier.width(104.dp).height(126.dp)) {
                Box(
                    modifier = Modifier
                        .offset(x = 22.dp, y = 8.dp)
                        .width(74.dp)
                        .height(112.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(shelfSwatch)
                )
                uiState.books.getOrNull(1)?.let { second ->
                    OrganicBookCover(
                        title = second.title,
                        modifier = Modifier.offset(x = 11.dp, y = 4.dp),
                        coverUrl = second.coverUrl,
                        width = 76.dp,
                        height = 118.dp,
                        cornerRadius = 13.dp,
                    )
                }
                uiState.books.firstOrNull()?.let { first ->
                    OrganicBookCover(
                        title = first.title,
                        modifier = Modifier.shadow(
                            elevation = 14.dp,
                            shape = RoundedCornerShape(14.dp),
                            ambientColor = OrganicColors.shadow.copy(alpha = 0.22f),
                            spotColor = OrganicColors.shadow.copy(alpha = 0.22f)
                        ),
                        coverUrl = first.coverUrl,
                        width = 78.dp,
                        height = 122.dp,
                        cornerRadius = 14.dp,
                    )
                }
            }
            Column(
                modifier = Modifier.padding(bottom = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = uiState.title,
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp,
                    lineHeight = 31.5.sp,
                    color = shelfInk,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    // Pages only when the books on the shelf actually say how long they are.
                    // The design reads "12 books · 4,208 pages". A collection stores a book's
                    // title, author and cover — not its length — so the count is what is true.
                    text = stringResource(Res.string.library_book_count, uiState.bookCount),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 13.sp,
                    color = shelfInk.copy(alpha = 0.8f)
                )
            }
        }

        if (uiState.description.isNotBlank()) {
            Text(
                text = uiState.description,
                fontFamily = organicBodyFontFamily(),
                fontSize = 14.sp,
                lineHeight = 22.4.sp,
                color = shelfInk.copy(alpha = 0.85f)
            )
        }

        if (uiState.books.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(OrganicShape.pill),
                            ambientColor = OrganicColors.shadow.copy(alpha = 0.16f),
                            spotColor = OrganicColors.shadow.copy(alpha = 0.16f)
                        )
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .background(OrganicColors.accent)
                        .clickable(role = Role.Button) {
                            uiState.books.firstOrNull()?.let {
                                onEvent(CollectionDetailsEvent.BookClicked(it.id))
                            }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.collection_read_next),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
                OrganicCircleIconButton(
                    icon = OrganicIcons.Plus,
                    onClick = { onEvent(CollectionDetailsEvent.AddBooksClicked) },
                    contentDescription = stringResource(Res.string.collection_edit),
                    size = 52.dp,
                    iconSize = 20.dp,
                    background = OrganicColors.bg,
                    tint = shelfInk,
                )
            }
        }
    }
}

/** A numbered row: where the book sits on the shelf, its cover, and who wrote it. */
@Composable
private fun ShelfBookRow(
    position: Int,
    book: CollectionDetailsBookUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = position.toString(),
            modifier = Modifier.width(18.dp),
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = OrganicColors.neutral500
        )
        OrganicBookCover(
            title = book.title,
            coverUrl = book.coverUrl,
            width = 46.dp,
            height = 68.dp,
            cornerRadius = 9.dp,
            elevated = true,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = book.title,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = OrganicColors.text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.author,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun CollectionDetailsPreview() {
    CollectionDetails()
}
