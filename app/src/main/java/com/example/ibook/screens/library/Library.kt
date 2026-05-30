package com.example.ibook.screens.library

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.ColorCategoryArts
import com.example.ibook.ui.theme.ColorCategoryComic
import com.example.ibook.ui.theme.ColorCategoryFantasy
import com.example.ibook.ui.theme.ColorPrimary
import com.example.ibook.ui.theme.ColorSecondary
import com.example.ibook.ui.theme.IBookTheme

private data class LibraryMetrics(
    val horizontalPadding: Dp,
    val smallGap: Dp,
    val mediumGap: Dp,
    val largeGap: Dp,
    val settingsButtonSize: Dp,
    val titleSize: TextUnit,
    val sectionTitleSize: TextUnit,
    val subtitleSize: TextUnit,
    val bodySize: TextUnit,
    val chipHeight: Dp,
    val chipTextSize: TextUnit,
    val collectionCorner: Dp,
    val collectionCoverWidth: Dp,
    val collectionCoverHeight: Dp,
    val collectionRowHeight: Dp,
    val gridCoverWidth: Dp,
    val gridCoverHeight: Dp,
    val gridCardHeight: Dp,
    val bottomNavSpace: Dp
)

data class LibraryBook(
    val title: String,
    val author: String,
    @param:DrawableRes val coverRes: Int,
    val tags: List<String> = emptyList(),
    val progress: String? = null
)

data class LibraryCollection(
    val title: String,
    val books: List<LibraryBook>
)

private val LibraryPurple = ColorPrimary

private val featuredCollection = LibraryCollection(
    title = "Collections",
    books = listOf(
        LibraryBook(
            title = "Murder Board",
            author = "By Brian Shea",
            coverRes = R.drawable.book_cover,
            tags = listOf("Horror", "Romantic")
        ),
        LibraryBook(
            title = "A Promised Land",
            author = "By Barack Obama",
            coverRes = R.drawable.book_cover_4,
            tags = listOf("Horror", "Romantic")
        )
    )
)

private val libraryBooks = listOf(
    LibraryBook("The Queen's Gambit", "By Walter Tevis", R.drawable.book_cover_2, tags = listOf("Sample")),
    LibraryBook("The Vanishing Half", "Brit Bennett", R.drawable.book_cover_3, tags = listOf("Sample")),
    LibraryBook("The Book of Two...", "By Jodi Picoult", R.drawable.book_cover_4, tags = listOf("Sample")),
    LibraryBook("Rodham", "By Curtis Sittenfeld", R.drawable.book_cove_6, progress = "30%"),
    LibraryBook("Caste", "By Isabel Wilkerson", R.drawable.book_cover, progress = "30%"),
    LibraryBook("Invisible Girl", "By Lisa Jewell", R.drawable.book_cover_3, progress = "30%"),
    LibraryBook("Murder Board", "By Brian Shea", R.drawable.book_cover, progress = "30%"),
    LibraryBook("Invisible Girl", "By Lisa Jewell", R.drawable.book_cover_3, progress = "30%"),
    LibraryBook("Friends and Strangers", "By J. Courtney Sullivan", R.drawable.book_cover_4, progress = "30%"),
    LibraryBook("Axiom's End", "By Lindsay Ellis", R.drawable.book_cover_2, progress = "30%")
)

@Composable
fun Library(
    modifier: Modifier = Modifier,
    collection: LibraryCollection = featuredCollection,
    books: List<LibraryBook> = libraryBooks,
    onSettingsClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onBookClick: (LibraryBook) -> Unit = {}
) {
    LibraryScreen(
        modifier = modifier,
        collection = collection,
        books = books,
        onSettingsClick = onSettingsClick,
        onSortClick = onSortClick,
        onBookClick = onBookClick
    )
}

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    collection: LibraryCollection = featuredCollection,
    books: List<LibraryBook> = libraryBooks,
    onSettingsClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onBookClick: (LibraryBook) -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LibraryPurple)
    ) {
        val metrics = rememberLibraryMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(
                start = metrics.horizontalPadding,
                end = metrics.horizontalPadding,
                top = metrics.largeGap,
                bottom = metrics.bottomNavSpace
            )
        ) {
            item {
                LibraryHeader(
                    metrics = metrics,
                    onSettingsClick = onSettingsClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(metrics.largeGap))
                LibrarySectionTitle(stringResource(R.string.library_collections), metrics = metrics)
                Spacer(modifier = Modifier.height(metrics.mediumGap))
                LibraryCollectionCard(
                    collection = collection,
                    metrics = metrics,
                    onBookClick = onBookClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(metrics.largeGap))
                LibraryAllBooksHeader(
                    metrics = metrics,
                    onSortClick = onSortClick
                )
                Spacer(modifier = Modifier.height(metrics.mediumGap))
            }

            books.chunked(2).forEach { rowBooks ->
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(metrics.mediumGap)
                    ) {
                        rowBooks.forEach { book ->
                            LibraryBookGridCard(
                                book = book,
                                metrics = metrics,
                                onClick = { onBookClick(book) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (rowBooks.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(metrics.largeGap))
                }
            }
        }
    }
}

@Composable
private fun LibraryHeader(
    metrics: LibraryMetrics,
    onSettingsClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(metrics.settingsButtonSize)
                    .clip(CircleShape)
                    .background(colorScheme.onPrimary.copy(alpha = 0.12f))
                    .clickable(onClick = onSettingsClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.cd_library_settings),
                    tint = colorScheme.onPrimary,
                    modifier = Modifier.size(metrics.settingsButtonSize * 0.45f)
                )
            }
        }

        Spacer(modifier = Modifier.height(metrics.mediumGap))

        Text(
            text = stringResource(R.string.library_title),
            color = colorScheme.onPrimary,
            maxLines = 1,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = metrics.titleSize,
                lineHeight = metrics.titleSize * 1.12f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.library_subtitle),
            color = colorScheme.onPrimary.copy(alpha = 0.82f),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = metrics.subtitleSize,
                lineHeight = metrics.subtitleSize * 1.55f,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun LibraryCollectionCard(
    collection: LibraryCollection,
    metrics: LibraryMetrics,
    onBookClick: (LibraryBook) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = metrics.horizontalPadding)
                .height(metrics.mediumGap)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = metrics.collectionCorner, topEnd = metrics.collectionCorner))
                .background(colorScheme.surface.copy(alpha = 0.28f))
        )

        Column(
            modifier = Modifier
                .padding(top = metrics.smallGap)
                .fillMaxWidth()
                .clip(RoundedCornerShape(metrics.collectionCorner))
                .background(colorScheme.surface)
                .padding(horizontal = metrics.mediumGap, vertical = metrics.mediumGap),
            verticalArrangement = Arrangement.spacedBy(metrics.mediumGap)
        ) {
            collection.books.forEach { book ->
                LibraryCollectionBookRow(
                    book = book,
                    metrics = metrics,
                    onClick = { onBookClick(book) }
                )
            }
        }
    }
}

@Composable
private fun LibraryCollectionBookRow(
    book: LibraryBook,
    metrics: LibraryMetrics,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.collectionRowHeight)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(book.coverRes),
            contentDescription = stringResource(R.string.cd_book_cover, book.title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(metrics.collectionCoverWidth)
                .height(metrics.collectionCoverHeight)
                .shadow(8.dp, RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
        )

        Spacer(modifier = Modifier.width(metrics.mediumGap))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = book.title,
                color = colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.bodySize * 1.05f,
                    lineHeight = metrics.bodySize * 1.24f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = book.author,
                color = colorScheme.onSurface.copy(alpha = 0.62f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = metrics.subtitleSize,
                    lineHeight = metrics.subtitleSize * 1.2f,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.smallGap))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                book.tags.forEach { tag ->
                    LibraryChip(text = tag, color = colorForLibraryTag(tag), metrics = metrics)
                }
            }
        }
    }
}

@Composable
private fun LibraryAllBooksHeader(
    metrics: LibraryMetrics,
    onSortClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LibrarySectionTitle(stringResource(R.string.library_all_book), metrics = metrics)

        Box(
                modifier = Modifier
                    .size(metrics.settingsButtonSize)
                    .clip(CircleShape)
                .background(colorScheme.onPrimary.copy(alpha = 0.12f))
                .clickable(onClick = onSortClick),
            contentAlignment = Alignment.Center
        ) {
            SortGlyph(
                color = colorScheme.onPrimary,
                modifier = Modifier.size(metrics.settingsButtonSize * 0.48f)
            )
        }
    }
}

@Composable
private fun LibraryBookGridCard(
    book: LibraryBook,
    metrics: LibraryMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .height(metrics.gridCardHeight)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .padding(top = metrics.gridCoverHeight * 0.56f)
                .fillMaxWidth()
                .height(metrics.gridCardHeight - metrics.gridCoverHeight * 0.56f)
                .clip(RoundedCornerShape(22.dp))
                .background(colorScheme.surface)
                .padding(
                    start = metrics.smallGap,
                    end = metrics.smallGap,
                    bottom = metrics.smallGap
                )
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = book.title,
                    color = colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = metrics.bodySize,
                        lineHeight = metrics.bodySize * 1.15f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = book.author,
                    color = colorScheme.onSurface.copy(alpha = 0.62f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = metrics.subtitleSize,
                        lineHeight = metrics.subtitleSize * 1.16f,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.smallGap))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (book.tags.isNotEmpty()) {
                        LibraryChip(
                            text = book.tags.first(),
                            color = LibraryPurple,
                            metrics = metrics
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    book.progress?.let { progress ->
                        Text(
                            text = progress,
                            color = colorScheme.onSurface.copy(alpha = 0.62f),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = metrics.chipTextSize,
                                lineHeight = metrics.chipTextSize,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.sp
                            )
                        )
                    }
                }
            }
        }

        Image(
            painter = painterResource(book.coverRes),
            contentDescription = stringResource(R.string.cd_book_cover, book.title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .width(metrics.gridCoverWidth)
                .height(metrics.gridCoverHeight)
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun LibraryChip(
    text: String,
    color: Color,
    metrics: LibraryMetrics
) {
    Box(
        modifier = Modifier
            .height(metrics.chipHeight)
            .clip(CircleShape)
            .background(color)
            .padding(horizontal = metrics.smallGap),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = metrics.chipTextSize,
                lineHeight = metrics.chipTextSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun SortGlyph(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val up = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.16f)
            lineTo(size.width * 0.25f, size.height * 0.42f)
            lineTo(size.width * 0.75f, size.height * 0.42f)
            close()
        }
        val down = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.84f)
            lineTo(size.width * 0.25f, size.height * 0.58f)
            lineTo(size.width * 0.75f, size.height * 0.58f)
            close()
        }
        drawPath(path = up, color = color)
        drawPath(path = down, color = color)
        drawLine(
            color = color.copy(alpha = 0.65f),
            start = Offset(size.width * 0.5f, size.height * 0.43f),
            end = Offset(size.width * 0.5f, size.height * 0.57f),
            strokeWidth = size.minDimension * 0.08f
        )
    }
}

@Composable
private fun LibrarySectionTitle(
    text: String,
    metrics: LibraryMetrics
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = metrics.sectionTitleSize,
            lineHeight = metrics.sectionTitleSize * 1.2f,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        )
    )
}

private fun colorForLibraryTag(tag: String): Color {
    return when (tag.lowercase()) {
        "horror" -> ColorCategoryComic
        "romantic" -> ColorSecondary
        "sample" -> ColorPrimary
        "arts" -> ColorCategoryArts
        else -> ColorCategoryFantasy
    }
}

@Composable
private fun rememberLibraryMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): LibraryMetrics {
    return remember(maxWidth, maxHeight) {
        val horizontalPadding = (maxWidth * 0.072f).coerceIn(20.dp, 30.dp)
        val smallGap = (maxWidth * 0.024f).coerceIn(7.dp, 11.dp)
        val mediumGap = (maxWidth * 0.045f).coerceIn(12.dp, 18.dp)
        val largeGap = (maxWidth * 0.065f).coerceIn(20.dp, 30.dp)
        val collectionCoverWidth = (maxWidth * 0.21f).coerceIn(62.dp, 82.dp)
        val gridCoverWidth = (maxWidth * 0.31f).coerceIn(96.dp, 126.dp)
        val titleSize = (maxWidth.value * 0.078f).coerceIn(25f, 32f).sp
        val sectionTitleSize = (maxWidth.value * 0.043f).coerceIn(15f, 18f).sp
        val bodySize = (maxWidth.value * 0.036f).coerceIn(13f, 16f).sp
        val subtitleSize = (maxWidth.value * 0.027f).coerceIn(9.5f, 12f).sp
        val chipTextSize = (maxWidth.value * 0.023f).coerceIn(8f, 10f).sp

        LibraryMetrics(
            horizontalPadding = horizontalPadding,
            smallGap = smallGap,
            mediumGap = mediumGap,
            largeGap = largeGap,
            settingsButtonSize = (maxWidth * 0.095f).coerceIn(34.dp, 42.dp),
            titleSize = titleSize,
            sectionTitleSize = sectionTitleSize,
            subtitleSize = subtitleSize,
            bodySize = bodySize,
            chipHeight = (maxWidth * 0.045f).coerceIn(16.dp, 20.dp),
            chipTextSize = chipTextSize,
            collectionCorner = (maxWidth * 0.07f).coerceIn(24.dp, 32.dp),
            collectionCoverWidth = collectionCoverWidth,
            collectionCoverHeight = collectionCoverWidth * 1.35f,
            collectionRowHeight = (collectionCoverWidth * 1.45f).coerceIn(88.dp, 112.dp),
            gridCoverWidth = gridCoverWidth,
            gridCoverHeight = gridCoverWidth * 1.38f,
            gridCardHeight = gridCoverWidth * 1.88f + 40.dp,
            bottomNavSpace = (maxHeight * 0.14f).coerceIn(112.dp, 150.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LibraryPreview() {
    IBookTheme {
        Box(modifier = Modifier.navigationBarsPadding()) {
            Library()
        }
    }
}
