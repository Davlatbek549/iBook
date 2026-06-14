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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class CategoryDetailBook(
    val title: String,
    val author: String,
    val coverRes: DrawableResource,
    val views: String = "",
    val tags: List<String> = emptyList(),
    val rating: String = "4.5",
    val price: String = "12.99",
    val bookmarked: Boolean = false
)

private val defaultBooks = listOf(
    CategoryDetailBook("Mexican Gothic", "Silvia Moreno-Garcia", Res.drawable.book_cover, tags = listOf("Literary"), rating = "4.6", price = "12.99"),
    CategoryDetailBook("Red at the Bone", "Jacqueline Woodson", Res.drawable.book_cover_3, tags = listOf("Literary"), rating = "4.5", price = "10.00", bookmarked = true),
    CategoryDetailBook("Olive, Again", "Elizabeth Strout", Res.drawable.olive_again_book, tags = listOf("Quiet"), rating = "4.3", price = "13.00"),
    CategoryDetailBook("Bestiary", "K-Ming Chang", Res.drawable.book_cover_4, tags = listOf("Myth"), rating = "4.2", price = "9.20")
)

private const val DefaultDescription = "412 books · quiet, character-first novels that stay with you."

@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    title: String = "Literary fiction",
    description: String = DefaultDescription,
    books: List<CategoryDetailBook> = defaultBooks,
    onBackClick: () -> Unit = {},
    onBookClick: (CategoryDetailBook) -> Unit = {},
    onOptionsClick: (CategoryDetailBook) -> Unit = {}
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
            InkIconButton(icon = InkIcons.Back, onClick = onBackClick, colors = colors)
            Spacer(modifier = Modifier.weight(1f))
            InkIconButton(icon = InkIcons.Search, onClick = {}, colors = colors)
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
                text = title,
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 30.sp,
                lineHeight = 33.sp,
                color = colors.ink
            )
            Text(
                text = description,
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
            Box(modifier = Modifier.weight(1f)) {
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
            books.forEachIndexed { i, book ->
                InkBookRow(
                    cover = book.coverRes,
                    title = book.title,
                    author = book.author,
                    modifier = Modifier.clickable { onBookClick(book) },
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
                                text = "· ${book.tags.firstOrNull().orEmpty()}",
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
                                    .clickable { onOptionsClick(book) }
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
