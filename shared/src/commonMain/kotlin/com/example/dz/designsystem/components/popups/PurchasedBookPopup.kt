package com.example.dz.designsystem.components.popups

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.theme.ColorCategoryFantasy
import com.example.dz.designsystem.theme.ColorPrimary
import com.example.dz.designsystem.theme.ColorTertiary
import com.example.dz.designsystem.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.category_fantasy
import dz.shared.generated.resources.category_horror
import dz.shared.generated.resources.cd_book_cover
import dz.shared.generated.resources.cd_search_purchased_books
import dz.shared.generated.resources.done
import dz.shared.generated.resources.ic_done
import dz.shared.generated.resources.ic_search
import dz.shared.generated.resources.ic_visibility_on
import dz.shared.generated.resources.popup_purchased_book_title
import dz.shared.generated.resources.popup_purchased_search_hint
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class PurchasedBookUiState(
    val id: String,
    val imageRes: DrawableResource,
    val title: String,
    val author: String,
    val views: String
)

private data class PurchasedBookPopupMetrics(
    val horizontalPadding: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val cardTopSpacing: Dp,
    val cardHeight: Dp,
    val cardCorner: Dp,
    val cardHorizontalPadding: Dp,
    val searchHeight: Dp,
    val searchCorner: Dp,
    val searchFontSize: TextUnit,
    val searchIconSize: Dp,
    val listTopSpacing: Dp,
    val rowSpacing: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val coverCorner: Dp,
    val bookGap: Dp,
    val bookTitleSize: TextUnit,
    val bookTitleLineHeight: TextUnit,
    val authorSize: TextUnit,
    val authorLineHeight: TextUnit,
    val viewsSize: TextUnit,
    val chipWidth: Dp,
    val chipHeight: Dp,
    val chipTextSize: TextUnit,
    val selectSize: Dp,
    val doneHeight: Dp,
    val doneBottomSpacing: Dp,
    val doneCorner: Dp
)

private val previewPurchasedBooks = listOf(
    PurchasedBookUiState(
        id = "mexican-gothic",
        imageRes = Res.drawable.book_cover,
        title = "Mexican Gothic",
        author = "By Silvia Moreno-Garcia",
        views = "1320"
    ),
    PurchasedBookUiState(
        id = "immortalists",
        imageRes = Res.drawable.book_cover_2,
        title = "The Immortalists",
        author = "By Chloe Benjamin",
        views = "1320"
    ),
    PurchasedBookUiState(
        id = "olive-again",
        imageRes = Res.drawable.book_cover_3,
        title = "Olive, Again",
        author = "Elizabeth Strout",
        views = "1320"
    ),
    PurchasedBookUiState(
        id = "gold-hills",
        imageRes = Res.drawable.book_cover_4,
        title = "How Much of These Hills Is Gold",
        author = "By C Pam Zhang",
        views = "1320"
    )
)

@Composable
fun PurchasedBookPopup(
    books: List<PurchasedBookUiState>,
    selectedBookIds: Set<String>,
    searchQuery: TextFieldValue,
    onSearchQueryChange: (TextFieldValue) -> Unit,
    onBookToggle: (PurchasedBookUiState) -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberPurchasedBookPopupMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme
        val filteredBooks = remember(books, searchQuery.text) {
            val query = searchQuery.text.trim()
            if (query.isEmpty()) {
                books
            } else {
                books.filter { book ->
                    book.title.contains(query, ignoreCase = true) ||
                            book.author.contains(query, ignoreCase = true)
                }
            }
        }

        PurchasedBookPopupBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = metrics.horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = stringResource(Res.string.popup_purchased_book_title),
                color = colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize * 1.1f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.cardTopSpacing))

            PurchasedBookCard(
                books = filteredBooks,
                selectedBookIds = selectedBookIds,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onBookToggle = onBookToggle,
                metrics = metrics,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            PurchasedBookDoneButton(
                metrics = metrics,
                onClick = onDoneClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(metrics.doneBottomSpacing))
        }
    }
}

@Composable
fun PurchasedBookPopup(
    books: List<PurchasedBookUiState>,
    selectedBookIds: Set<String>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBookToggle: (PurchasedBookUiState) -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PurchasedBookPopup(
        books = books,
        selectedBookIds = selectedBookIds,
        searchQuery = TextFieldValue(searchQuery),
        onSearchQueryChange = { onSearchQueryChange(it.text) },
        onBookToggle = onBookToggle,
        onDoneClick = onDoneClick,
        modifier = modifier
    )
}

@Composable
private fun PurchasedBookPopupBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF858585))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.16f
                    scaleY = 1.16f
                }
                .blur(38.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.12f),
                            ColorPrimary.copy(alpha = 0.14f),
                            Color.Black.copy(alpha = 0.22f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.28f))
        )
    }
}

@Composable
private fun PurchasedBookCard(
    books: List<PurchasedBookUiState>,
    selectedBookIds: Set<String>,
    searchQuery: TextFieldValue,
    onSearchQueryChange: (TextFieldValue) -> Unit,
    onBookToggle: (PurchasedBookUiState) -> Unit,
    metrics: PurchasedBookPopupMetrics,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .height(metrics.cardHeight)
            .clip(RoundedCornerShape(metrics.cardCorner))
            .background(colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = metrics.cardHorizontalPadding,
                    top = 24.dp,
                    end = metrics.cardHorizontalPadding
                )
        ) {
            PurchasedBookSearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                metrics = metrics
            )

            Spacer(modifier = Modifier.height(metrics.listTopSpacing))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(metrics.rowSpacing)
            ) {
                books.forEach { book ->
                    PurchasedBookRow(
                        book = book,
                        isSelected = book.id in selectedBookIds,
                        onClick = { onBookToggle(book) },
                        metrics = metrics
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(62.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorScheme.surface.copy(alpha = 0f),
                            colorScheme.surface.copy(alpha = 0.98f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun PurchasedBookSearchField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    metrics: PurchasedBookPopupMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(ColorPrimary),
        textStyle = MaterialTheme.typography.bodyLarge.merge(
            TextStyle(
                color = colorScheme.onSurface,
                fontSize = metrics.searchFontSize,
                lineHeight = metrics.searchFontSize * 1.25f,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(metrics.searchHeight)
                    .clip(RoundedCornerShape(metrics.searchCorner))
                    .background(colorScheme.outline.copy(alpha = 0.12f))
                    .padding(start = 22.dp, end = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.popup_purchased_search_hint),
                            color = colorScheme.onSurface.copy(alpha = 0.42f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = metrics.searchFontSize,
                                lineHeight = metrics.searchFontSize * 1.25f,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 0.sp
                            )
                        )
                    }
                    innerTextField()
                }

                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = stringResource(Res.string.cd_search_purchased_books),
                    tint = ColorPrimary,
                    modifier = Modifier.size(metrics.searchIconSize)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PurchasedBookRow(
    book: PurchasedBookUiState,
    isSelected: Boolean,
    onClick: () -> Unit,
    metrics: PurchasedBookPopupMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(book.imageRes),
            contentDescription = stringResource(Res.string.cd_book_cover, book.title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(metrics.coverWidth)
                .height(metrics.coverHeight)
                .shadow(8.dp, RoundedCornerShape(metrics.coverCorner))
                .clip(RoundedCornerShape(metrics.coverCorner))
        )

        Spacer(modifier = Modifier.width(metrics.bookGap))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 7.dp)
        ) {
            Text(
                text = book.title,
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.bookTitleSize,
                    lineHeight = metrics.bookTitleLineHeight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = book.author,
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.authorSize,
                    lineHeight = metrics.authorLineHeight,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.ic_visibility_on),
                    contentDescription = null,
                    tint = ColorPrimary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = book.views,
                    color = colorScheme.onSurface.copy(alpha = 0.42f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.viewsSize,
                        lineHeight = metrics.viewsSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PurchasedBookGenreChip(
                    text = stringResource(Res.string.category_horror),
                    color = ColorTertiary,
                    metrics = metrics
                )
                PurchasedBookGenreChip(
                    text = stringResource(Res.string.category_fantasy),
                    color = ColorCategoryFantasy,
                    metrics = metrics
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        PurchasedBookSelectionMark(
            isSelected = isSelected,
            metrics = metrics,
            modifier = Modifier.padding(top = 61.dp)
        )
    }
}

@Composable
private fun PurchasedBookSelectionMark(
    isSelected: Boolean,
    metrics: PurchasedBookPopupMetrics,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .size(metrics.selectSize)
            .clip(CircleShape)
            .background(if (isSelected) ColorPrimary else colorScheme.surface)
            .border(
                width = if (isSelected) 0.dp else 2.dp,
                color = if (isSelected) Color.Transparent else ColorPrimary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(Res.drawable.ic_done),
                contentDescription = null,
                tint = colorScheme.onPrimary,
                modifier = Modifier.size(metrics.selectSize * 0.62f)
            )
        }
    }
}

@Composable
private fun PurchasedBookGenreChip(
    text: String,
    color: Color,
    metrics: PurchasedBookPopupMetrics
) {
    Box(
        modifier = Modifier
            .width(metrics.chipWidth)
            .height(metrics.chipHeight)
            .clip(RoundedCornerShape(50))
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = metrics.chipTextSize,
                lineHeight = metrics.chipTextSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun PurchasedBookDoneButton(
    metrics: PurchasedBookPopupMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(metrics.doneHeight)
            .clip(RoundedCornerShape(metrics.doneCorner))
            .background(ColorPrimary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.done),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberPurchasedBookPopupMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): PurchasedBookPopupMetrics {
    val compact = maxWidth < 370.dp
    val short = maxHeight < 760.dp

    return remember(maxWidth, maxHeight) {
        PurchasedBookPopupMetrics(
            horizontalPadding = if (compact) 24.dp else 29.dp,
            titleTopSpacing = if (short) 82.dp else 114.dp,
            titleSize = if (compact) 33.sp else 36.sp,
            cardTopSpacing = if (short) 34.dp else 42.dp,
            cardHeight = if (short) 435.dp else 482.dp,
            cardCorner = 28.dp,
            cardHorizontalPadding = if (compact) 18.dp else 21.dp,
            searchHeight = 46.dp,
            searchCorner = 18.dp,
            searchFontSize = if (compact) 13.sp else 14.sp,
            searchIconSize = 26.dp,
            listTopSpacing = 26.dp,
            rowSpacing = if (compact) 23.dp else 27.dp,
            coverWidth = if (compact) 76.dp else 84.dp,
            coverHeight = if (compact) 99.dp else 109.dp,
            coverCorner = 8.dp,
            bookGap = if (compact) 18.dp else 21.dp,
            bookTitleSize = if (compact) 18.sp else 19.sp,
            bookTitleLineHeight = if (compact) 21.sp else 23.sp,
            authorSize = if (compact) 13.sp else 14.sp,
            authorLineHeight = if (compact) 17.sp else 19.sp,
            viewsSize = if (compact) 16.sp else 17.sp,
            chipWidth = if (compact) 67.dp else 72.dp,
            chipHeight = 22.dp,
            chipTextSize = if (compact) 12.sp else 13.sp,
            selectSize = 23.dp,
            doneHeight = 68.dp,
            doneBottomSpacing = if (short) 24.dp else 26.dp,
            doneCorner = 18.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PurchasedBookPopupPreview() {
    DZTheme {
        PurchasedBookPopup(
            books = previewPurchasedBooks,
            selectedBookIds = setOf("mexican-gothic"),
            searchQuery = "",
            onSearchQueryChange = {},
            onBookToggle = {},
            onDoneClick = {}
        )
    }
}
