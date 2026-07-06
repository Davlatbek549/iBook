package com.example.dz.presentation.book.category_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkBookRow
import com.example.dz.designsystem.components.ink.InkChip
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.category_chip_all
import dz.shared.generated.resources.category_chip_award
import dz.shared.generated.resources.category_chip_new
import dz.shared.generated.resources.category_chip_under
import dz.shared.generated.resources.category_sorted_by
import dz.shared.generated.resources.olive_again_book
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val previewBooks = listOf(
    CategoryDetailBookUi(id = "mexican-gothic", title = "Mexican Gothic", author = "Silvia Moreno-Garcia", coverRes = Res.drawable.book_cover, tag = "Literary", rating = "4.6", price = "12.99"),
    CategoryDetailBookUi(id = "red-at-the-bone", title = "Red at the Bone", author = "Jacqueline Woodson", coverRes = Res.drawable.book_cover_3, tag = "Literary", rating = "4.5", price = "10.00", bookmarked = true),
    CategoryDetailBookUi(id = "olive-again", title = "Olive, Again", author = "Elizabeth Strout", coverRes = Res.drawable.olive_again_book, tag = "Quiet", rating = "4.3", price = "13.00"),
    CategoryDetailBookUi(id = "bestiary", title = "Bestiary", author = "K-Ming Chang", coverRes = Res.drawable.book_cover_4, tag = "Myth", rating = "4.2", price = "9.20")
)

private val previewUiState = CategoryDetailUiState(
    categoryId = "literary-fiction",
    title = "Literary fiction",
    description = "412 books · quiet, character-first novels that stay with you.",
    books = previewBooks
)

@Composable
fun CategoryDetailScreen(
    uiState: CategoryDetailUiState = previewUiState,
    onEvent: (CategoryDetailEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        // top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 4.dp)
        ) {
            InkIconButton(icon = InkIcons.Back, onClick = { onEvent(CategoryDetailEvent.BackClicked) }, colors = colors)
            Spacer(modifier = Modifier.weight(1f))
            InkIconButton(icon = InkIcons.Search, onClick = { onEvent(CategoryDetailEvent.SearchClicked) }, colors = colors)
        }

        // header
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 20.dp)) {
            Text(
                text = "Category 01",
                fontFamily = displayFont,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp,
                color = colors.accent
            )
            Text(
                text = uiState.title,
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 30.sp,
                lineHeight = 33.sp,
                color = colors.ink
            )
            Text(
                text = uiState.description,
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = bodyFont,
                fontSize = 13.sp,
                lineHeight = 21.sp,
                color = colors.muted
            )
        }

        // filter chips
        Row(
            modifier = Modifier
                .padding(top = 18.dp)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InkChip(text = stringResource(Res.string.category_chip_all), solid = true, colors = colors)
            InkChip(text = stringResource(Res.string.category_chip_new), colors = colors)
            InkChip(text = stringResource(Res.string.category_chip_award), colors = colors)
            InkChip(text = stringResource(Res.string.category_chip_under), colors = colors)
        }

        // sort row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onEvent(CategoryDetailEvent.SortClicked) }
            ) {
                InkLabel(text = stringResource(Res.string.category_sorted_by), colors = colors)
            }
            Icon(
                imageVector = InkIcons.ChevronDown,
                contentDescription = null,
                tint = colors.muted,
                modifier = Modifier.size(14.dp)
            )
        }

        // book list
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 4.dp)) {
            uiState.books.forEachIndexed { i, book ->
                InkBookRow(
                    cover = book.coverRes,
                    coverUrl = book.coverUrl,
                    title = book.title,
                    author = book.author,
                    modifier = Modifier.clickable { onEvent(CategoryDetailEvent.BookClicked(book.id)) },
                    showDivider = i > 0,
                    meta = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = InkIcons.Star,
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = book.rating,
                                fontFamily = bodyFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = colors.inkSoft
                            )
                            Text(
                                text = "· ${book.tag}",
                                fontFamily = bodyFont,
                                fontSize = 11.sp,
                                color = colors.muted
                            )
                        }
                    },
                    trailing = {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "$${book.price}",
                                fontFamily = displayFont,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = colors.ink
                            )
                            Icon(
                                imageVector = InkIcons.Bookmark,
                                contentDescription = null,
                                tint = if (book.bookmarked) colors.accent else colors.muted,
                                modifier = Modifier
                                    .size(15.dp)
                                    .clickable { onEvent(CategoryDetailEvent.BookmarkClicked(book.id)) }
                            )
                        }
                    },
                    colors = colors
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun CategoryDetailScreenPreview() {
    CategoryDetailScreen()
}
