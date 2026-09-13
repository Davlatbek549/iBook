package com.example.dz.presentation.collections.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.dz.designsystem.components.organic.OrganicRowChevron
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.components.organic.OrganicStackedCovers
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.presentation.common.uniqueLazyKeys
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.collections_empty
import dz.shared.generated.resources.collections_title
import dz.shared.generated.resources.library_book_count
import dz.shared.generated.resources.library_new_collection
import dz.shared.generated.resources.nav_back
import org.jetbrains.compose.resources.stringResource

/**
 * Collections — the shelves the reader built themselves.
 *
 * Each row wears the shelf's own books as its portrait: three covers fanned out, so a shelf is
 * recognisable before its name is read. Geometry from `dz-all-screens.html`.
 *
 * Creating a shelf goes to the edit screen with nothing in it, which is how the handoff routes it
 * and how the view model already works — there is no separate create form to keep in step.
 */
@Composable
fun CollectionsScreen(
    uiState: CollectionsUiState = CollectionsUiState(),
    onEvent: (CollectionsEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val keys = uiState.collections.uniqueLazyKeys { it.id }

    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 12.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = stringResource(Res.string.collections_title),
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER, vertical = 4.dp),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = { onEvent(CollectionsEvent.BackClicked) },
                            contentDescription = stringResource(Res.string.nav_back),
                            size = 38.dp,
                            iconSize = 17.dp,
                        )
                    },
                    trailing = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.Plus,
                            onClick = { onEvent(CollectionsEvent.NewCollectionClicked) },
                            contentDescription = stringResource(Res.string.library_new_collection),
                            size = 38.dp,
                            iconSize = 18.dp,
                            background = OrganicColors.accent,
                            tint = Color.White,
                        )
                    },
                )
            }

            if (uiState.collections.isEmpty() && !uiState.isLoading) {
                item(key = "empty") {
                    OrganicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ORGANIC_GUTTER),
                        contentPadding = PaddingValues(18.dp),
                    ) {
                        Text(
                            text = stringResource(Res.string.collections_empty),
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 13.sp,
                            lineHeight = 19.5.sp,
                            color = OrganicColors.neutral700
                        )
                    }
                }
            }

            items(uiState.collections.size, key = { keys[it] }) { index ->
                val collection = uiState.collections[index]
                CollectionRow(
                    collection = collection,
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    onClick = { onEvent(CollectionsEvent.CollectionClicked(collection.id)) },
                )
            }
        }
    }
}

@Composable
private fun CollectionRow(
    collection: CollectionUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OrganicCard(
        modifier = modifier.fillMaxWidth(),
        elevated = true,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OrganicStackedCovers(
                // The cover art keys the gradient behind it, so a shelf keeps its colours even
                // before any artwork loads.
                keys = collection.covers.indices.map { "${collection.id}-$it" }
                    .ifEmpty { listOf(collection.id) },
                coverUrls = collection.covers.map { it.coverUrl },
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = collection.name,
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    lineHeight = 22.sp,
                    color = OrganicColors.text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    // The design also reads "· 2 unread" and carries a Private/shared pill.
                    // Neither exists: a collection records a title, a note and its books, and
                    // nothing tracks who else can see it or what has been read inside it.
                    text = stringResource(Res.string.library_book_count, collection.bookCount),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 13.sp,
                    color = OrganicColors.neutral700
                )
            }
            OrganicRowChevron()
        }
    }
}

@Preview
@Composable
fun CollectionsScreenPreview() {
    CollectionsScreen()
}
