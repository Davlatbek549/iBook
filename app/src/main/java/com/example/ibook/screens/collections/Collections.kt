package com.example.ibook.screens.collections

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Visibility
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.ColorCategoryFantasy
import com.example.ibook.ui.theme.ColorPrimary
import com.example.ibook.ui.theme.ColorTertiary
import com.example.ibook.ui.theme.IBookTheme

private val CollectionPanelText = Color(0xFF68686B)
private val CollectionMutedText = Color(0xFFAAAAB0)
private val CollectionButtonBackground = Color(0xFF5748E7)

private data class CollectionMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val topButtonSize: Dp,
    val topIconSize: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val subtitleSize: TextUnit,
    val subtitleLineHeight: TextUnit,
    val firstSectionTopSpacing: Dp,
    val sectionGap: Dp,
    val sectionTitleSize: TextUnit,
    val cardTopSpacing: Dp,
    val cardHeight: Dp,
    val cardCorner: Dp,
    val cardHorizontalPadding: Dp,
    val cardTopPadding: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val bookGap: Dp,
    val bookRowSpacing: Dp,
    val bookTitleSize: TextUnit,
    val bookTitleLineHeight: TextUnit,
    val bookAuthorSize: TextUnit,
    val bookAuthorLineHeight: TextUnit,
    val readsSize: TextUnit,
    val chipWidth: Dp,
    val chipHeight: Dp,
    val chipTextSize: TextUnit,
    val emptyIllustrationTopSpacing: Dp,
    val emptyIllustrationSize: Dp,
    val emptyFileWidth: Dp,
    val emptyFileHeight: Dp,
    val emptySearchSize: Dp,
    val emptyTextTopSpacing: Dp,
    val emptyTextSize: TextUnit,
    val emptyButtonTopSpacing: Dp,
    val emptyButtonHeight: Dp,
    val emptyButtonCorner: Dp,
    val emptyButtonTextSize: TextUnit
)

data class CollectionSection(
    val title: String,
    val books: List<CollectionBook>
)

data class CollectionBook(
    val title: String,
    val author: String,
    val reads: String,
    val coverRes: Int
)

private val collectionSections = listOf(
    CollectionSection(
        title = "Self help book",
        books = listOf(
            CollectionBook(
                title = "Olive, Again",
                author = "Elizabeth Strout",
                reads = "1320",
                coverRes = R.drawable.book_cover_3
            ),
            CollectionBook(
                title = "How Much of These Hills Is Gold",
                author = "By C Pam Zhang",
                reads = "1320",
                coverRes = R.drawable.book_cover_4
            ),
            CollectionBook(
                title = "Mexican Gothic",
                author = "By Silvia Moreno-Garcia",
                reads = "1320",
                coverRes = R.drawable.book_cover
            )
        )
    ),
    CollectionSection(
        title = "Viet Nam book",
        books = listOf(
            CollectionBook(
                title = "The Searcher",
                author = "By TANA FRENCH",
                reads = "1320",
                coverRes = R.drawable.book_cove_6
            ),
            CollectionBook(
                title = "The Immortalists",
                author = "By Chloe Benjamin",
                reads = "1320",
                coverRes = R.drawable.book_cover_2
            )
        )
    )
)

@Composable
fun Collections(
    modifier: Modifier = Modifier,
    sections: List<CollectionSection> = collectionSections,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNewCollectionClick: () -> Unit = {}
) {
    CollectionsScreen(
        modifier = modifier,
        sections = sections,
        onBackClick = onBackClick,
        onSettingsClick = onSettingsClick,
        onNewCollectionClick = onNewCollectionClick
    )
}

@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    sections: List<CollectionSection> = collectionSections,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNewCollectionClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(ColorPrimary)
    ) {
        val metrics = rememberCollectionMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            CollectionTopActions(
                metrics = metrics,
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick
            )

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = "Collections",
                color = Color.White,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Synthesize favorite books your way. Everyone can see and share this collection",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = metrics.subtitleSize,
                    lineHeight = metrics.subtitleLineHeight,
                    letterSpacing = 0.sp
                )
            )

            if (sections.isEmpty()) {
                CollectionsEmptyState(
                    metrics = metrics,
                    onNewCollectionClick = onNewCollectionClick
                )
            } else {
                Spacer(modifier = Modifier.height(metrics.firstSectionTopSpacing))

                sections.forEachIndexed { index, section ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(metrics.sectionGap))
                    }

                    CollectionSectionContent(
                        section = section,
                        metrics = metrics
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun CollectionTopActions(
    metrics: CollectionMetrics,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CollectionCircleButton(
            metrics = metrics,
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(metrics.topIconSize)
            )
        }

        CollectionCircleButton(
            metrics = metrics,
            onClick = onSettingsClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_three_vertical_dots),
                contentDescription = "Collection options",
                tint = Color.White,
                modifier = Modifier.size(metrics.topIconSize * 0.74f)
            )
        }
    }
}

@Composable
private fun CollectionCircleButton(
    metrics: CollectionMetrics,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(metrics.topButtonSize)
            .clip(CircleShape)
            .background(CollectionButtonBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun CollectionsEmptyState(
    metrics: CollectionMetrics,
    onNewCollectionClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(metrics.emptyIllustrationTopSpacing))

        CollectionsEmptyIllustration(
            metrics = metrics,
            modifier = Modifier.size(metrics.emptyIllustrationSize)
        )

        Spacer(modifier = Modifier.height(metrics.emptyTextTopSpacing))

        Text(
            text = "No Collections Right Now!",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.emptyTextSize,
                lineHeight = metrics.emptyTextSize * 1.25f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(metrics.emptyButtonTopSpacing))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(metrics.emptyButtonHeight)
                .clip(RoundedCornerShape(metrics.emptyButtonCorner))
                .background(Color.White)
                .clickable(onClick = onNewCollectionClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "New Collection",
                color = ColorPrimary,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.emptyButtonTextSize,
                    lineHeight = metrics.emptyButtonTextSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CollectionsEmptyIllustration(
    metrics: CollectionMetrics,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCollectionsEmptyDecor()
        }

        Image(
            painter = painterResource(R.drawable.img_file),
            contentDescription = "Collection file",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(metrics.emptyFileWidth, metrics.emptyFileHeight)
                .offset(x = 34.dp, y = 1.dp)
        )

        Image(
            painter = painterResource(R.drawable.img_search),
            contentDescription = "Search collection",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(metrics.emptySearchSize)
                .offset(x = (-72).dp, y = 52.dp)
                .graphicsLayer {
                    rotationZ = -7f
                }
        )
    }
}

private fun DrawScope.drawCollectionsEmptyDecor() {
    drawCircle(
        color = Color(0xFF4F40EE).copy(alpha = 0.46f),
        radius = size.minDimension * 0.43f,
        center = Offset(size.width / 2f, size.height / 2f)
    )

    val purple = Color.White
    val mint = Color(0xFF5ED2C7)
    val blue = Color(0xFF77A8FF)
    val orange = Color(0xFFFFBD47)
    val peach = Color(0xFFFFBC93)
    val pink = Color(0xFFBE74FF)
    val cyan = Color(0xFF25B7F4)
    val green = Color(0xFF81F69B)

    drawCollectionRing(Offset(size.width * 0.16f, size.height * 0.2f), purple, size.minDimension * 0.031f)
    drawCollectionRing(Offset(size.width * 0.86f, size.height * 0.24f), purple, size.minDimension * 0.044f)
    drawCollectionRing(Offset(size.width * 0.11f, size.height * 0.76f), orange, size.minDimension * 0.036f)

    drawCollectionPlus(Offset(size.width * 0.88f, size.height * 0.52f), blue, size.minDimension * 0.022f)
    drawCollectionPlus(Offset(size.width * 0.86f, size.height * 0.72f), peach, size.minDimension * 0.022f)
    drawCollectionPlus(Offset(size.width * 0.31f, size.height * 0.88f), pink, size.minDimension * 0.022f)

    drawCollectionSquiggle(Offset(size.width * 0.12f, size.height * 0.43f), mint, size.minDimension * 0.025f)
    drawCollectionSquiggle(Offset(size.width * 0.73f, size.height * 0.45f), green, size.minDimension * 0.014f)

    drawCircle(color = Color(0xFFFF5DB1), radius = size.minDimension * 0.011f, center = Offset(size.width * 0.33f, size.height * 0.24f))
    drawCircle(color = cyan, radius = size.minDimension * 0.018f, center = Offset(size.width * 0.65f, size.height * 0.93f))
}

private fun DrawScope.drawCollectionRing(center: Offset, color: Color, radius: Float) {
    drawCircle(
        color = color,
        radius = radius,
        center = center,
        style = Stroke(width = radius * 0.42f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawCollectionPlus(center: Offset, color: Color, length: Float) {
    drawLine(
        color = color,
        start = Offset(center.x - length, center.y),
        end = Offset(center.x + length, center.y),
        strokeWidth = length * 0.58f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(center.x, center.y - length),
        end = Offset(center.x, center.y + length),
        strokeWidth = length * 0.58f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawCollectionSquiggle(start: Offset, color: Color, length: Float) {
    drawLine(
        color = color,
        start = start,
        end = Offset(start.x + length * 0.72f, start.y),
        strokeWidth = length * 0.45f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(start.x + length * 0.72f, start.y),
        end = Offset(start.x + length * 0.72f, start.y + length * 0.46f),
        strokeWidth = length * 0.45f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(start.x + length * 0.72f, start.y + length * 0.46f),
        end = Offset(start.x + length * 1.25f, start.y + length * 0.46f),
        strokeWidth = length * 0.45f,
        cap = StrokeCap.Round
    )
}

@Composable
private fun CollectionSectionContent(
    section: CollectionSection,
    metrics: CollectionMetrics
) {
    Text(
        text = section.title,
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontSize = metrics.sectionTitleSize,
            lineHeight = metrics.sectionTitleSize * 1.25f,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        )
    )

    Spacer(modifier = Modifier.height(metrics.cardTopSpacing))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.cardHeight)
            .clip(RoundedCornerShape(metrics.cardCorner))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = metrics.cardHorizontalPadding,
                    top = metrics.cardTopPadding,
                    end = metrics.cardHorizontalPadding
                ),
            verticalArrangement = Arrangement.spacedBy(metrics.bookRowSpacing)
        ) {
            section.books.forEach { book ->
                CollectionBookRow(
                    book = book,
                    metrics = metrics
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0.98f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun CollectionBookRow(
    book: CollectionBook,
    metrics: CollectionMetrics
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        BookCover(
            book = book,
            modifier = Modifier
                .width(metrics.coverWidth)
                .height(metrics.coverHeight)
        )

        Spacer(modifier = Modifier.width(metrics.bookGap))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp)
        ) {
            Text(
                text = book.title,
                color = CollectionPanelText,
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
                color = CollectionPanelText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.bookAuthorSize,
                    lineHeight = metrics.bookAuthorLineHeight,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(11.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = null,
                    tint = ColorPrimary,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = book.reads,
                    color = CollectionMutedText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.readsSize,
                        lineHeight = metrics.readsSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GenreChip(
                    text = "Horror",
                    color = ColorTertiary,
                    metrics = metrics
                )
                GenreChip(
                    text = "Fantasy",
                    color = ColorCategoryFantasy,
                    metrics = metrics
                )
            }
        }
    }
}

@Composable
private fun BookCover(
    book: CollectionBook,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(book.coverRes),
        contentDescription = "${book.title} book cover",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
private fun GenreChip(
    text: String,
    color: Color,
    metrics: CollectionMetrics
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
            color = Color.White,
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
private fun rememberCollectionMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): CollectionMetrics {
    val compact = maxWidth < 370.dp
    val short = maxHeight < 760.dp

    return remember(maxWidth, maxHeight) {
        CollectionMetrics(
            horizontalPadding = if (compact) 24.dp else 29.dp,
            topSpacing = if (short) 22.dp else 26.dp,
            topButtonSize = if (compact) 42.dp else 44.dp,
            topIconSize = if (compact) 32.dp else 34.dp,
            titleTopSpacing = if (short) 26.dp else 33.dp,
            titleSize = if (compact) 37.sp else 40.sp,
            subtitleSize = if (compact) 14.sp else 15.sp,
            subtitleLineHeight = if (compact) 23.sp else 25.sp,
            firstSectionTopSpacing = if (short) 44.dp else 53.dp,
            sectionGap = if (short) 26.dp else 34.dp,
            sectionTitleSize = if (compact) 20.sp else 21.sp,
            cardTopSpacing = 29.dp,
            cardHeight = if (short) 322.dp else 338.dp,
            cardCorner = 28.dp,
            cardHorizontalPadding = if (compact) 20.dp else 22.dp,
            cardTopPadding = 28.dp,
            coverWidth = if (compact) 78.dp else 84.dp,
            coverHeight = if (compact) 108.dp else 116.dp,
            bookGap = if (compact) 20.dp else 25.dp,
            bookRowSpacing = if (compact) 23.dp else 28.dp,
            bookTitleSize = if (compact) 19.sp else 20.sp,
            bookTitleLineHeight = if (compact) 22.sp else 24.sp,
            bookAuthorSize = if (compact) 14.sp else 15.sp,
            bookAuthorLineHeight = if (compact) 18.sp else 20.sp,
            readsSize = if (compact) 17.sp else 18.sp,
            chipWidth = if (compact) 72.dp else 76.dp,
            chipHeight = 24.dp,
            chipTextSize = if (compact) 13.sp else 14.sp,
            emptyIllustrationTopSpacing = if (short) 40.dp else 60.dp,
            emptyIllustrationSize = if (compact) 300.dp else 320.dp,
            emptyFileWidth = if (compact) 221.dp else 242.dp,
            emptyFileHeight = if (compact) 252.dp else 276.dp,
            emptySearchSize = if (compact) 133.dp else 146.dp,
            emptyTextTopSpacing = if (short) 20.dp else 40.dp,
            emptyTextSize = if (compact) 20.sp else 22.sp,
            emptyButtonTopSpacing = if (short) 90.dp else 120.dp,
            emptyButtonHeight = if (compact) 68.dp else 70.dp,
            emptyButtonCorner = 21.dp,
            emptyButtonTextSize = if (compact) 18.sp else 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionsScreenPreview() {
    IBookTheme {
        CollectionsScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyCollectionsScreenPreview() {
    IBookTheme {
        CollectionsScreen(sections = emptyList())
    }
}
