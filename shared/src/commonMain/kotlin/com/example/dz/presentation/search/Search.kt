package com.example.dz.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkField
import com.example.dz.designsystem.components.ink.InkBookRow
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkSectionTitle
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Category
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.olive_again_book
import dz.shared.generated.resources.search_browse_by_mood
import dz.shared.generated.resources.search_placeholder
import dz.shared.generated.resources.search_recent
import dz.shared.generated.resources.search_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource

private val moodCategories = listOf(
    "Literary fiction", "History", "Romance", "Essays",
    "Poetry", "Biography", "Fantasy", "Health"
)

private val searchCoverFallbacks = listOf(
    Res.drawable.book_cover,
    Res.drawable.book_cover_2,
    Res.drawable.book_cover_3,
    Res.drawable.book_cover_4,
    Res.drawable.olive_again_book
)

@Composable
fun SearchScreen(
    uiState: SearchUiState = SearchUiState(),
    onEvent: (SearchEvent) -> Unit = {},
    onSearchFocusChange: (Boolean) -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onBookClick: (bookId: String) -> Unit = {},
    onAuthorClick: (authorId: String) -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    val recentSearches = remember {
        listOf("olive again", "paulo coelho", "gothic novels").toMutableStateList()
    }
    val displayCategories = uiState.categories.ifEmpty {
        moodCategories.map { Category(id = it.toCategoryId(), name = it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 6.dp)) {
            Text(
                text = stringResource(Res.string.search_title),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = colors.ink
            )
            InkField(
                value = uiState.query,
                onValueChange = {
                    onEvent(SearchEvent.QueryChanged(it))
                    onEvent(SearchEvent.SearchClicked)
                },
                placeholder = stringResource(Res.string.search_placeholder),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .onFocusChanged { onSearchFocusChange(it.isFocused) },
                leadingIcon = InkIcons.Search,
                colors = colors
            )
        }

        if (recentSearches.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
                InkLabel(text = stringResource(Res.string.search_recent), colors = colors)
                Column(modifier = Modifier.padding(top = 6.dp)) {
                    recentSearches.forEachIndexed { i, recent ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onEvent(SearchEvent.QueryChanged(recent))
                                    onEvent(SearchEvent.SearchClicked)
                                }
                                .padding(vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = InkIcons.Search,
                                contentDescription = null,
                                tint = colors.muted,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = recent,
                                modifier = Modifier.weight(1f),
                                fontFamily = bodyFont,
                                fontSize = 13.5.sp,
                                color = colors.inkSoft
                            )
                            Icon(
                                imageVector = InkIcons.Close,
                                contentDescription = null,
                                tint = colors.muted,
                                modifier = Modifier
                                    .size(11.dp)
                                    .clickable { recentSearches.remove(recent) }
                            )
                        }
                        if (i < recentSearches.size - 1) {
                            HorizontalDivider(thickness = 1.dp, color = colors.line)
                        }
                    }
                }
            }
        }

        if (uiState.books.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
                InkSectionTitle(
                    text = "Results",
                    colors = colors
                )
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    uiState.books.forEachIndexed { index, book ->
                        SearchBookRow(
                            book = book,
                            cover = searchCoverFallbacks[index % searchCoverFallbacks.size],
                            showDivider = index > 0,
                            onClick = {
                                onEvent(SearchEvent.BookClicked(book.id))
                                onBookClick(book.id)
                            },
                            colors = colors
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
            InkSectionTitle(
                text = stringResource(Res.string.search_browse_by_mood),
                colors = colors
            )
            Column(
                modifier = Modifier.padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                displayCategories.chunked(2).forEachIndexed { rowIndex, rowCats ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        rowCats.forEachIndexed { colIndex, category ->
                            val index = rowIndex * 2 + colIndex
                            MoodCard(
                                number = index + 1,
                                name = category.name,
                                highlighted = index % 3 == 0,
                                onClick = {
                                    onEvent(SearchEvent.CategoryClicked(category.id))
                                    onCategoryClick(category.id)
                                },
                                modifier = Modifier.weight(1f),
                                colors = colors
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBookRow(
    book: Book,
    cover: DrawableResource,
    showDivider: Boolean,
    onClick: () -> Unit,
    colors: InkColors,
) {
    InkBookRow(
        cover = cover,
        coverUrl = book.coverUrl,
        title = book.title,
        author = book.authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        modifier = Modifier.clickable(onClick = onClick),
        showDivider = showDivider,
        meta = {
            Text(
                text = book.categories.firstOrNull()?.name.orEmpty(),
                fontFamily = inkBodyFontFamily(),
                fontSize = 11.sp,
                color = colors.muted
            )
        },
        colors = colors
    )
}

private fun String.toCategoryId(): String =
    trim()
        .lowercase()
        .replace(Regex("[^a-z0-9]+"), "-")
        .trim('-')
        .ifBlank { "category" }

@Composable
private fun MoodCard(
    number: Int,
    name: String,
    highlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: InkColors,
) {
    Column(
        modifier = modifier
            .inkCard(colors)
            .let { if (highlighted) it.background(colors.alt) else it }
            .clickable(onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Text(
            text = number.toString().padStart(2, '0'),
            fontFamily = inkDisplayFontFamily(),
            fontStyle = FontStyle.Italic,
            fontSize = 11.sp,
            color = colors.muted
        )
        Text(
            text = name,
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            color = colors.ink
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
