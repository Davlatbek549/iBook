package com.example.dz.screens.category_detail

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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.ColorCategoryFantasy
import com.example.dz.theme.ColorPrimary
import com.example.dz.theme.ColorTertiary
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class CategoryDetailMetrics(
    val horizontalPadding: Dp,
    val headerHeight: Dp,
    val headerCorner: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val titleTopGap: Dp,
    val titleSize: TextUnit,
    val descriptionTopGap: Dp,
    val descriptionSize: TextUnit,
    val descriptionLineHeight: TextUnit,
    val listTopPadding: Dp,
    val rowSpacing: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val coverCorner: Dp,
    val bookGap: Dp,
    val titleTextSize: TextUnit,
    val authorTextSize: TextUnit,
    val viewsTextSize: TextUnit,
    val chipHeight: Dp,
    val chipTextSize: TextUnit,
    val chipHorizontalPadding: Dp,
    val menuIconSize: Dp,
    val bottomPadding: Dp
)

data class CategoryDetailBook(
    val title: String,
    val author: String,
    val coverRes: DrawableResource,
    val views: String = "1320",
    val tags: List<String> = listOf("Horror", "Fantasy")
)

private data class CategoryDetailBookTagUiState(
    val text: String,
    val color: Color
)

private val HorrorHeaderBackground = Color(0xFF1C0C36)
private val CategoryDetailBackground = Color(0xFFEFF3FA)

private const val HorrorDescription =
    "Horror fiction is fiction in any medium intended to scare, " +
        "unsettle, or horrify the audience. Historically, the cause of " +
        "the \"horror\" experience has often been the intrusion of a " +
        "supernatural element into everyday human experience. " +
        "Since the 1960s, any work of fiction with a morbid, " +
        "gruesome, surreal, or exceptionally suspenseful or " +
        "frightening theme has come to be called \"horror\". Horror " +
        "fiction often overlaps science fiction or fantasy, all three " +
        "of which categories are sometimes placed under the " +
        "umbrella classification speculative fiction."

private val horrorBooks = listOf(
    CategoryDetailBook(
        title = "Lord of the Flies",
        author = "By William Golding",
        coverRes = Res.drawable.book_cover
    ),
    CategoryDetailBook(
        title = "We Need to Talk About Kevin",
        author = "By Lionel Shriver",
        coverRes = Res.drawable.book_cover_2
    ),
    CategoryDetailBook(
        title = "Carrion Comfort",
        author = "By Dan Simmons",
        coverRes = Res.drawable.book_cove_6
    ),
    CategoryDetailBook(
        title = "Pet Sematary",
        author = "By Stephen King",
        coverRes = Res.drawable.book_cover_4
    ),
    CategoryDetailBook(
        title = "Invisible Girl",
        author = "By Lisa Jewell",
        coverRes = Res.drawable.book_cover_3
    )
)

@Composable
fun CategoryDetailScreen(
    modifier: Modifier = Modifier,
    title: String = "Horror",
    description: String = HorrorDescription,
    books: List<CategoryDetailBook> = horrorBooks,
    onBackClick: () -> Unit = {},
    onBookClick: (CategoryDetailBook) -> Unit = {},
    onOptionsClick: (CategoryDetailBook) -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(CategoryDetailBackground)
    ) {
        val metrics = rememberCategoryDetailMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = metrics.bottomPadding)
        ) {
            item {
                CategoryDetailHeader(
                    title = title,
                    description = description,
                    metrics = metrics,
                    onBackClick = onBackClick
                )
            }

            item {
                CategoryDetailBookSection(
                    books = books,
                    metrics = metrics,
                    onBookClick = onBookClick,
                    onOptionsClick = onOptionsClick
                )
            }
        }
    }
}

@Composable
private fun CategoryDetailHeader(
    title: String,
    description: String,
    metrics: CategoryDetailMetrics,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.headerHeight)
            .clip(RoundedCornerShape(bottomEnd = metrics.headerCorner))
            .background(HorrorHeaderBackground)
    ) {
        HeaderGradientArt()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = metrics.horizontalPadding),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(26.dp))

            Box(
                modifier = Modifier
                    .size(metrics.backButtonSize)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.16f))
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = stringResource(Res.string.cd_back),
                    tint = Color.White,
                    modifier = Modifier.size(metrics.backIconSize)
                )
            }

            Spacer(modifier = Modifier.height(metrics.titleTopGap))

            Text(
                text = title,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize * 1.1f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.descriptionTopGap))

            Text(
                text = description,
                color = Color.White.copy(alpha = 0.94f),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = metrics.descriptionSize,
                    lineHeight = metrics.descriptionLineHeight,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun HeaderGradientArt() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(20.dp)
    ) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF8C3DFF).copy(alpha = 0.78f),
                    Color(0xFF5E22B9).copy(alpha = 0.36f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.16f, size.height * 0.15f),
                radius = size.width * 0.55f
            ),
            radius = size.width * 0.55f,
            center = Offset(size.width * 0.16f, size.height * 0.15f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFA95EFF).copy(alpha = 0.76f),
                    Color(0xFF6429CF).copy(alpha = 0.42f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.95f, size.height * 0.36f),
                radius = size.width * 0.54f
            ),
            radius = size.width * 0.54f,
            center = Offset(size.width * 0.95f, size.height * 0.36f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF8E45FF).copy(alpha = 0.48f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.42f, size.height * 0.36f),
                radius = size.width * 0.42f
            ),
            radius = size.width * 0.42f,
            center = Offset(size.width * 0.42f, size.height * 0.36f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.7f),
                    Color.Black.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.58f, size.height * 0.98f),
                radius = size.width * 0.48f
            ),
            radius = size.width * 0.48f,
            center = Offset(size.width * 0.58f, size.height * 0.98f)
        )
    }
}

@Composable
private fun CategoryDetailBookSection(
    books: List<CategoryDetailBook>,
    metrics: CategoryDetailMetrics,
    onBookClick: (CategoryDetailBook) -> Unit,
    onOptionsClick: (CategoryDetailBook) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(HorrorHeaderBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = metrics.headerCorner))
                .background(CategoryDetailBackground)
                .padding(top = metrics.listTopPadding)
        ) {
            books.forEach { book ->
                CategoryDetailBookRow(
                    book = book,
                    metrics = metrics,
                    onClick = { onBookClick(book) },
                    onOptionsClick = { onOptionsClick(book) }
                )
                Spacer(modifier = Modifier.height(metrics.rowSpacing))
            }
        }
    }
}

@Composable
private fun CategoryDetailBookRow(
    book: CategoryDetailBook,
    metrics: CategoryDetailMetrics,
    onClick: () -> Unit,
    onOptionsClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val tags = book.tags.take(2).map { tag ->
        CategoryDetailBookTagUiState(text = tag, color = colorForCategoryTag(tag))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.coverHeight)
            .padding(horizontal = metrics.horizontalPadding)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(book.coverRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = metrics.coverWidth, height = metrics.coverHeight)
                .clip(RoundedCornerShape(metrics.coverCorner))
        )

        Spacer(modifier = Modifier.width(metrics.bookGap))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 6.dp)
        ) {
            Text(
                text = book.title,
                color = colorScheme.onSurface.copy(alpha = 0.66f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.titleTextSize,
                    lineHeight = metrics.titleTextSize * 1.18f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = book.author,
                color = colorScheme.onSurface.copy(alpha = 0.66f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = metrics.authorTextSize,
                    lineHeight = metrics.authorTextSize * 1.22f,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(9.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tags.forEach { tag ->
                    CategoryDetailTagChip(
                        tag = tag,
                        metrics = metrics
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_visibility_on),
                    contentDescription = null,
                    tint = ColorPrimary,
                    modifier = Modifier.size(metrics.menuIconSize * 0.78f)
                )

                Text(
                    text = book.views,
                    color = colorScheme.onSurface.copy(alpha = 0.35f),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = metrics.viewsTextSize,
                        lineHeight = metrics.viewsTextSize * 1.2f,
                        letterSpacing = 0.sp
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .size(metrics.menuIconSize)
                .clickable(onClick = onOptionsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_three_vertical_dots),
                contentDescription = null,
                tint = colorScheme.onSurface.copy(alpha = 0.62f),
                modifier = Modifier.size(metrics.menuIconSize)
            )
        }
    }
}

@Composable
private fun CategoryDetailTagChip(
    tag: CategoryDetailBookTagUiState,
    metrics: CategoryDetailMetrics
) {
    Box(
        modifier = Modifier
            .height(metrics.chipHeight)
            .clip(RoundedCornerShape(metrics.chipHeight / 2))
            .background(tag.color.copy(alpha = 0.16f))
            .padding(horizontal = metrics.chipHorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag.text,
            color = tag.color,
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = metrics.chipTextSize,
                lineHeight = metrics.chipTextSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

private fun colorForCategoryTag(tag: String): Color {
    return when (tag.lowercase()) {
        "horror" -> ColorTertiary
        "fantasy" -> ColorCategoryFantasy
        else -> ColorPrimary
    }
}

@Composable
private fun rememberCategoryDetailMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): CategoryDetailMetrics {
    return remember(maxWidth, maxHeight) {
        val horizontalPadding = (maxWidth * 0.073f).coerceIn(24.dp, 34.dp)
        val coverWidth = (maxWidth * 0.21f).coerceIn(78.dp, 94.dp)
        val titleTextSize = (maxWidth.value * 0.044f).coerceIn(17f, 22f).sp
        val authorTextSize = (maxWidth.value * 0.034f).coerceIn(13f, 16f).sp
        val descriptionSize = (maxWidth.value * 0.035f).coerceIn(14f, 17f).sp

        CategoryDetailMetrics(
            horizontalPadding = horizontalPadding,
            headerHeight = (maxHeight * 0.427f).coerceIn(360.dp, 430.dp),
            headerCorner = (maxWidth * 0.116f).coerceIn(44.dp, 72.dp),
            backButtonSize = (maxWidth * 0.108f).coerceIn(44.dp, 56.dp),
            backIconSize = (maxWidth * 0.046f).coerceIn(18.dp, 24.dp),
            titleTopGap = (maxHeight * 0.041f).coerceIn(36.dp, 52.dp),
            titleSize = (maxWidth.value * 0.09f).coerceIn(38f, 52f).sp,
            descriptionTopGap = (maxHeight * 0.018f).coerceIn(16.dp, 24.dp),
            descriptionSize = descriptionSize,
            descriptionLineHeight = descriptionSize * 1.48f,
            listTopPadding = (maxHeight * 0.048f).coerceIn(34.dp, 50.dp),
            rowSpacing = (maxHeight * 0.03f).coerceIn(28.dp, 40.dp),
            coverWidth = coverWidth,
            coverHeight = coverWidth * 1.42f,
            coverCorner = 8.dp,
            bookGap = (maxWidth * 0.055f).coerceIn(20.dp, 30.dp),
            titleTextSize = titleTextSize,
            authorTextSize = authorTextSize,
            viewsTextSize = authorTextSize * 1.04f,
            chipHeight = (maxWidth * 0.044f).coerceIn(20.dp, 24.dp),
            chipTextSize = (maxWidth.value * 0.027f).coerceIn(10f, 12f).sp,
            chipHorizontalPadding = (maxWidth * 0.035f).coerceIn(12.dp, 18.dp),
            menuIconSize = (maxWidth * 0.044f).coerceIn(22.dp, 28.dp),
            bottomPadding = (maxHeight * 0.08f).coerceIn(72.dp, 110.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryDetailScreenPreview() {
    DZTheme {
        Box(modifier = Modifier.navigationBarsPadding()) {
            CategoryDetailScreen()
        }
    }
}
