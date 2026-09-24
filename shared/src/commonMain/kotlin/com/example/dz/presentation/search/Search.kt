package com.example.dz.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicFieldLabel
import com.example.dz.designsystem.components.organic.OrganicListRow
import com.example.dz.designsystem.components.organic.OrganicRowChevron
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.components.organic.OrganicSearchField
import com.example.dz.designsystem.components.organic.OrganicSkeleton
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.domain.model.Book
import com.example.dz.presentation.common.priceLabel
import com.example.dz.presentation.common.uniqueLazyKeys
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.nav_back
import dz.shared.generated.resources.search_clear
import dz.shared.generated.resources.search_field_hint
import dz.shared.generated.resources.search_in_library
import dz.shared.generated.resources.search_no_results
import dz.shared.generated.resources.search_recent
import dz.shared.generated.resources.search_result_one
import dz.shared.generated.resources.search_results
import dz.shared.generated.resources.search_searching
import dz.shared.generated.resources.search_title
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.stringResource

/**
 * Search — a live query over the catalogue, the reader's recent ones, and what it found.
 *
 * Layout from `dz-all-screens.html` (`#scr-search`): a 22dp rhythm on a 24dp gutter. Search is no
 * longer a tab, so it wears the back circle Browse and Collections do, and the tab bar the frame
 * draws is not here (see the note in the nav graph).
 *
 * The field opens focused, once. Arriving here is asking to type; coming back from a result is not,
 * so the keyboard does not climb over the results a second time.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState = SearchUiState(),
    onEvent: (SearchEvent) -> Unit = {},
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var focusedOnArrival by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!focusedOnArrival) {
            focusRequester.requestFocus()
            focusedOnArrival = true
        }
    }

    val books = uiState.books
    val bookKeys = books.uniqueLazyKeys { it.id }
    val listState = rememberLazyListState()

    // Scrolling the results is reading them, and a keyboard over half of them is in the way. It
    // only drops on a real drag, so the list settling after a search does not close it.
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { it }
            .collect { keyboard?.hide() }
    }
    val resultsFor = uiState.resultsFor
    val errorMessage = uiState.errorMessage

    OrganicScreen {
        Column(modifier = Modifier.fillMaxSize()) {
            // Pinned above the list rather than scrolling with it: a lazy list disposes what scrolls
            // out of view, and a focused field disposed that way loses focus without reliably
            // saying so.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = ORGANIC_GUTTER, end = ORGANIC_GUTTER, top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(SECTION_GAP)
            ) {
                OrganicScreenHeader(
                    title = stringResource(Res.string.search_title),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = { onEvent(SearchEvent.BackClicked) },
                            contentDescription = stringResource(Res.string.nav_back),
                            size = 38.dp,
                            iconSize = 18.dp,
                        )
                    },
                )
                OrganicSearchField(
                    value = uiState.query,
                    onValueChange = {
                        onEvent(SearchEvent.QueryChanged(it))
                        onEvent(SearchEvent.SearchClicked)
                    },
                    placeholder = stringResource(Res.string.search_field_hint),
                    focusRequester = focusRequester,
                    onSearch = {
                        onEvent(SearchEvent.SearchSubmitted)
                        keyboard?.hide()
                    },
                    // Clearing leaves the box ready for the next query rather than dismissing the
                    // keyboard, which is what a reader starting over wants.
                    onClear = { onEvent(SearchEvent.QueryChanged("")) },
                    clearContentDescription = stringResource(Res.string.search_clear),
                )
            }

            // Lazy, so a long page of results composes only the rows on screen — and only those rows
            // start loading their covers, instead of every result's cover being fetched at once.
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = ORGANIC_GUTTER,
                    end = ORGANIC_GUTTER,
                    top = SECTION_GAP,
                    bottom = ORGANIC_TAB_BAR_CLEARANCE,
                ),
                verticalArrangement = Arrangement.spacedBy(ROW_GAP)
            ) {
                if (uiState.recentSearches.isNotEmpty()) {
                    item(key = "recent", contentType = "recent") {
                        Column(
                            // Tops the 12dp row gap up to the 22dp the frame leaves under Recent.
                            modifier = Modifier.padding(bottom = SECTION_GAP - ROW_GAP),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OrganicFieldLabel(text = stringResource(Res.string.search_recent))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                uiState.recentSearches.forEach { recent ->
                                    RecentChip(
                                        text = recent,
                                        onClick = {
                                            onEvent(SearchEvent.RecentSearchClicked(recent))
                                            keyboard?.hide()
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

                if (uiState.hasQuery) {
                    when {
                        errorMessage != null -> item(key = "error", contentType = "note") {
                            SearchNote(text = errorMessage)
                        }

                        // A row of grey shapes says "results are coming, and this is what they will
                        // look like"; the word alone leaves the screen blank under it.
                        uiState.isLoading -> {
                            item(key = "status", contentType = "label") {
                                OrganicFieldLabel(text = stringResource(Res.string.search_searching))
                            }
                            items(SEARCHING_SKELETON_ROWS, key = { "skeleton-$it" }, contentType = { "skeleton" }) {
                                ResultRowSkeleton()
                            }
                        }

                        resultsFor != null && books.isEmpty() -> item(key = "empty", contentType = "note") {
                            SearchNote(text = stringResource(Res.string.search_no_results, resultsFor))
                        }

                        resultsFor != null -> item(key = "status", contentType = "label") {
                            OrganicFieldLabel(
                                text = if (books.size == 1) {
                                    stringResource(Res.string.search_result_one)
                                } else {
                                    stringResource(Res.string.search_results, books.size)
                                }
                            )
                        }
                    }

                    // The last answer stays under "Searching…" while the next is on its way, so the
                    // list does not blank and jump on every pause in typing.
                    if (errorMessage == null && resultsFor != null && !uiState.isLoading) {
                        items(books.size, key = { "book:" + bookKeys[it] }, contentType = { "book" }) { index ->
                            val book = books[index]
                            ResultRow(
                                book = book,
                                inLibrary = uiState.isInLibrary(book),
                                onClick = { onEvent(SearchEvent.BookClicked(book.id)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * A result: the 52 × 76 cover of a library row, bare rather than carded, ending in a chevron. Under
 * the author it says the one thing worth knowing before opening it — already yours, or what it
 * costs.
 */
@Composable
private fun ResultRow(
    book: Book,
    inLibrary: Boolean,
    onClick: () -> Unit,
) {
    OrganicListRow(
        title = book.title,
        author = book.authors.firstOrNull()?.name,
        meta = if (inLibrary) stringResource(Res.string.search_in_library) else book.priceLabel(),
        coverUrl = book.coverUrl,
        coverWidth = 52.dp,
        coverHeight = 76.dp,
        onClick = onClick,
        trailing = { OrganicRowChevron() },
    )
}

/** The shape of a result before it arrives: cover, title, author. */
@Composable
private fun ResultRowSkeleton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganicSkeleton(
            modifier = Modifier
                .width(52.dp)
                .height(76.dp),
            cornerRadius = 10.dp,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OrganicSkeleton(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(17.dp)
            )
            OrganicSkeleton(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(12.dp)
            )
        }
    }
}

/** A recent query as a neutral pill; tapping it runs it again. */
@Composable
private fun RecentChip(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral200)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 9.dp),
        fontFamily = organicBodyFontFamily(),
        fontSize = 13.sp,
        color = OrganicColors.text,
        maxLines = 1
    )
}

/**
 * What an empty or failed search says. The handoff draws neither, so this follows Library's empty
 * note — a filled card and a plain sentence that says what to try.
 */
@Composable
private fun SearchNote(text: String) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        background = OrganicColors.neutral100,
        contentPadding = PaddingValues(18.dp),
    ) {
        Text(
            text = text,
            fontFamily = organicBodyFontFamily(),
            fontSize = 13.sp,
            lineHeight = 19.5.sp,
            color = OrganicColors.neutral700
        )
    }
}

/** Enough grey rows to fill the space the first answers will take, and no more. */
private const val SEARCHING_SKELETON_ROWS = 4

private val SECTION_GAP = 22.dp
private val ROW_GAP = 12.dp

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
