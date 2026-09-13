package com.example.dz.presentation.book.pre_purchase

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicCoverCard
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicSectionHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.presentation.common.uniqueLazyKeys
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_continue_reading
import dz.shared.generated.resources.book_download
import dz.shared.generated.resources.book_downloaded
import dz.shared.generated.resources.book_file_to_shelf
import dz.shared.generated.resources.book_more_like_this
import dz.shared.generated.resources.book_pages
import dz.shared.generated.resources.book_read_again
import dz.shared.generated.resources.book_read_free
import dz.shared.generated.resources.book_start_reading
import dz.shared.generated.resources.nav_back
import org.jetbrains.compose.resources.stringResource

/**
 * Book detail — the cover centred, what the book is, and one thing to do with it.
 *
 * Everything about the layout is symmetrical until the description, which is where the design stops
 * centring and starts reading like a page. Geometry from `dz-all-screens.html`.
 *
 * The primary button says what is actually true of this book for this reader: buy it, start it,
 * carry on, or read it again.
 */
@Composable
fun PrePurchaseScreen(
    uiState: PrePurchaseUiState = PrePurchaseUiState(),
    onEvent: (PrePurchaseEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val relatedKeys = uiState.relatedBooks.uniqueLazyKeys { it.id }

    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(key = "bar") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OrganicCircleIconButton(
                        icon = OrganicIcons.ChevronLeft,
                        onClick = { onEvent(PrePurchaseEvent.BackClicked) },
                        contentDescription = stringResource(Res.string.nav_back),
                    )
                    Box(modifier = Modifier.weight(1f))
                    OrganicCircleIconButton(
                        icon = OrganicIcons.Plus,
                        onClick = { onEvent(PrePurchaseEvent.BookmarkClicked) },
                        contentDescription = stringResource(Res.string.book_file_to_shelf),
                        background = OrganicColors.accent200,
                        tint = OrganicColors.accent800,
                    )
                }
            }

            item(key = "cover") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OrganicBookCover(
                        title = uiState.title,
                        coverUrl = uiState.coverUrl,
                        width = 132.dp,
                        height = 194.dp,
                        cornerRadius = 20.dp,
                        elevated = true,
                    )
                    Text(
                        text = uiState.title,
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 26.sp,
                        lineHeight = 29.9.sp,
                        color = OrganicColors.text,
                        textAlign = TextAlign.Center
                    )
                    if (uiState.author.isNotBlank()) {
                        Text(
                            text = uiState.author,
                            modifier = Modifier.clickable(role = Role.Button) {
                                onEvent(PrePurchaseEvent.AuthorClicked)
                            },
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 14.sp,
                            color = OrganicColors.neutral700,
                            textAlign = TextAlign.Center
                        )
                    }
                    MetaPills(uiState = uiState)
                }
            }

            if (uiState.overview.isNotBlank()) {
                item(key = "overview") {
                    Text(
                        text = uiState.overview,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 15.sp,
                        lineHeight = 24.75.sp,
                        color = OrganicColors.neutral800
                    )
                }
            }

            item(key = "actions") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrimaryAction(
                        uiState = uiState,
                        modifier = Modifier.weight(1f),
                        onClick = { onEvent(PrePurchaseEvent.PrimaryActionClicked) },
                    )
                    DownloadAction(
                        isDownloaded = uiState.isDownloaded,
                        isDownloading = uiState.isDownloading,
                        onClick = { onEvent(PrePurchaseEvent.DownloadClicked) },
                    )
                }
            }

            if (uiState.relatedBooks.isNotEmpty()) {
                item(key = "related-header") {
                    OrganicSectionHeader(
                        title = stringResource(Res.string.book_more_like_this),
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    )
                }
                item(key = "related") {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = ORGANIC_GUTTER),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.relatedBooks.size, key = { relatedKeys[it] }) { index ->
                            val related = uiState.relatedBooks[index]
                            OrganicCoverCard(
                                title = related.title,
                                author = "",
                                coverUrl = related.coverUrl,
                                onClick = { onEvent(PrePurchaseEvent.RelatedBookClicked(related.id)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Rating, length and genre as pills — each one only when the source actually carries it. An
 * unrated book shows two pills rather than a made-up score.
 */
@Composable
private fun MetaPills(uiState: PrePurchaseUiState) {
    val pills = buildList {
        uiState.rating?.let { add("★ $it" to true) }
        uiState.pages?.let { add(stringResource(Res.string.book_pages, it) to false) }
        uiState.genre?.let { add(it to false) }
    }
    if (pills.isEmpty()) return

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        pills.forEach { (label, isRating) ->
            Text(
                text = label,
                modifier = Modifier
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(
                        if (isRating) OrganicColors.accent2_200 else OrganicColors.neutral200
                    )
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = if (isRating) OrganicColors.accent2_900 else OrganicColors.text,
                maxLines = 1
            )
        }
    }
}

/** Buy, start, carry on, or read again — whichever this book actually is for this reader. */
@Composable
private fun PrimaryAction(
    uiState: PrePurchaseUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val label = when {
        uiState.ownership == BookOwnership.IN_PROGRESS -> stringResource(Res.string.book_continue_reading)
        uiState.ownership == BookOwnership.FINISHED -> stringResource(Res.string.book_read_again)
        uiState.ownership == BookOwnership.NOT_STARTED -> stringResource(Res.string.book_start_reading)
        uiState.isFree -> stringResource(Res.string.book_read_free)
        // Only a book that must be paid for shows a price, and only if the source gave one.
        else -> uiState.price ?: stringResource(Res.string.book_start_reading)
    }

    Row(
        modifier = modifier
            .height(58.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(OrganicShape.pill),
                ambientColor = OrganicColors.shadow.copy(alpha = 0.16f),
                spotColor = OrganicColors.shadow.copy(alpha = 0.16f)
            )
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.accent)
            .clickable(role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

/**
 * The outlined circle beside the primary action. It is the one place the Organic system draws a
 * hairline, and the design draws it that way: an outline reads as secondary next to a filled pill.
 */
@Composable
private fun DownloadAction(
    isDownloaded: Boolean,
    isDownloading: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(if (isDownloaded) OrganicColors.accent2_200 else Color.Transparent)
            .then(
                if (isDownloaded) {
                    Modifier
                } else {
                    Modifier.border(1.dp, OrganicColors.neutral400, RoundedCornerShape(OrganicShape.pill))
                }
            )
            .clickable(enabled = !isDownloading && !isDownloaded, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when {
            isDownloading -> CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = OrganicColors.neutral800,
                strokeWidth = 2.dp
            )

            isDownloaded -> Icon(
                imageVector = OrganicIcons.Check,
                contentDescription = stringResource(Res.string.book_downloaded),
                tint = OrganicColors.accent2_900,
                modifier = Modifier.size(20.dp)
            )

            else -> Icon(
                imageVector = OrganicIcons.Download,
                contentDescription = stringResource(Res.string.book_download),
                tint = OrganicColors.neutral800,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview
@Composable
fun PrePurchaseScreenPreview() {
    PrePurchaseScreen()
}
