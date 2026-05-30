package com.example.ibook.screens.store

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.ibook.R
import com.example.ibook.screens.collection_details.CollectionDetailsBookListRow
import com.example.ibook.screens.collection_details.CollectionDetailsBookTagUiState
import com.example.ibook.screens.collection_details.CollectionDetailsBookUiState
import com.example.ibook.ui.theme.ColorCategoryArts
import com.example.ibook.ui.theme.ColorCategoryBiographies
import com.example.ibook.ui.theme.ColorCategoryBusiness
import com.example.ibook.ui.theme.ColorCategoryComic
import com.example.ibook.ui.theme.ColorCategoryCooking
import com.example.ibook.ui.theme.ColorCategoryEducation
import com.example.ibook.ui.theme.ColorCategoryFantasy
import com.example.ibook.ui.theme.ColorCategoryHealth
import com.example.ibook.ui.theme.ColorCategoryHistory
import com.example.ibook.ui.theme.ColorCategoryKids
import com.example.ibook.ui.theme.ColorCategoryMedical
import com.example.ibook.ui.theme.ColorCategorySelfHelp
import com.example.ibook.ui.theme.ColorCategorySport
import com.example.ibook.ui.theme.ColorCategoryTravel
import com.example.ibook.ui.theme.ColorPrimary
import com.example.ibook.ui.theme.ColorSecondary
import com.example.ibook.ui.theme.ColorTertiary
import com.example.ibook.ui.theme.IBookTheme

private data class StoreMetrics(
    val horizontalPadding: Dp,
    val smallGap: Dp,
    val mediumGap: Dp,
    val headerHeight: Dp,
    val headerCorner: Dp,
    val avatarSize: Dp,
    val headerTitleSize: TextUnit,
    val headerNameSize: TextUnit,
    val ebookPillHeight: Dp,
    val ebookTextSize: TextUnit,
    val promoTopPadding: Dp,
    val promoCardWidth: Dp,
    val promoCardHeight: Dp,
    val promoCorner: Dp,
    val promoCoverWidth: Dp,
    val promoDiscountSize: TextUnit,
    val promoButtonHeight: Dp,
    val sectionTitleSize: TextUnit,
    val categoryTileSize: Dp,
    val categoryIconSize: Dp,
    val categoryLabelSize: TextUnit,
    val categorySectionTopGap: Dp,
    val categoryToListGap: Dp,
    val bookCoverWidth: Dp,
    val bookCoverHeight: Dp,
    val bookGap: Dp,
    val bookTitleSize: TextUnit,
    val bookAuthorSize: TextUnit,
    val chipHeight: Dp,
    val chipTextSize: TextUnit,
    val rowSpacing: Dp,
    val bottomNavSpace: Dp
)

data class StoreBook(
    val title: String,
    val author: String,
    @param:DrawableRes val coverRes: Int,
    val views: String,
    val tags: List<String>
)

private data class StoreCategory(
    val name: String,
    @param:DrawableRes val iconRes: Int,
    val backgroundColor: Color
)

private data class StorePromotion(
    @param:DrawableRes val firstCoverRes: Int,
    @param:DrawableRes val secondCoverRes: Int,
    val discountText: String
)

private val StorePurple = ColorPrimary
private val StoreOnline = Color(0xFFFF5A73)

private val storePromotions = listOf(
    StorePromotion(
        firstCoverRes = R.drawable.book_cover,
        secondCoverRes = R.drawable.book_cover_2,
        discountText = "20%\nOFF"
    ),
    StorePromotion(
        firstCoverRes = R.drawable.book_cover_3,
        secondCoverRes = R.drawable.book_cover_4,
        discountText = "15%\nOFF"
    )
)

private val storeCategories = listOf(
    StoreCategory("Arts", R.drawable.img_arts, ColorCategoryArts),
    StoreCategory("Biographies", R.drawable.img_biography, ColorCategoryBiographies),
    StoreCategory("Business", R.drawable.img_business, ColorCategoryBusiness),
    StoreCategory("Comic", R.drawable.img_commic, ColorCategoryComic),
    StoreCategory("Cooking", R.drawable.img_cooking, ColorCategoryCooking),
    StoreCategory("Edu", R.drawable.img_edu, ColorCategoryEducation),
    StoreCategory("Health", R.drawable.img_health, ColorCategoryHealth),
    StoreCategory("History", R.drawable.img_history, ColorCategoryHistory),
    StoreCategory("Horror", R.drawable.img_horror, ColorTertiary),
    StoreCategory("Kid", R.drawable.img_kid, ColorCategoryKids),
    StoreCategory("Medical", R.drawable.img_medical, ColorCategoryMedical),
    StoreCategory("Romance", R.drawable.img_romance, ColorSecondary),
    StoreCategory("Fantasy", R.drawable.img_fantasy, ColorCategoryFantasy),
    StoreCategory("Self-Help", R.drawable.img_self_help, ColorCategorySelfHelp),
    StoreCategory("Sport", R.drawable.img_sport, ColorCategorySport),
    StoreCategory("Travel", R.drawable.img_travel, ColorCategoryTravel)
)

private val storeBooks = listOf(
    StoreBook(
        title = "Fifty Words for Rain",
        author = "By Asha Lemmie",
        coverRes = R.drawable.book_cover,
        views = "1320",
        tags = listOf("Horror", "Fantasy")
    ),
    StoreBook(
        title = "Ready Player Two",
        author = "By Ernest Cline",
        coverRes = R.drawable.book_cover_2,
        views = "1320",
        tags = listOf("Horror", "Fantasy")
    ),
    StoreBook(
        title = "Untamed",
        author = "By Glennon Doyle",
        coverRes = R.drawable.book_cover_3,
        views = "1320",
        tags = listOf("Horror", "Fantasy")
    ),
    StoreBook(
        title = "The Searcher",
        author = "By Tana French",
        coverRes = R.drawable.book_cove_6,
        views = "1320",
        tags = listOf("Horror", "Fantasy")
    )
)

@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    books: List<StoreBook> = storeBooks,
    onViewMoreClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onBookClick: (StoreBook) -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberStoreMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 0.dp)
        ) {
            item {
                StoreHeader(metrics = metrics)
            }

            item {
                StoreBody(
                    metrics = metrics,
                    books = books,
                    onViewMoreClick = onViewMoreClick,
                    onCategoryClick = onCategoryClick,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

@Composable
private fun StoreHeader(metrics: StoreMetrics) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.headerHeight)
            .clip(storeHeaderShape(metrics))
            .background(StorePurple)
            .statusBarsPadding()
            .padding(horizontal = metrics.horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StoreProfileImage(metrics = metrics)

                Spacer(modifier = Modifier.width(metrics.smallGap))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hi.",
                        color = colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = metrics.headerTitleSize,
                            lineHeight = metrics.headerTitleSize * 1.12f,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        )
                    )
                    Text(
                        text = "JollyDesigner78",
                        color = colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = metrics.headerNameSize,
                            lineHeight = metrics.headerNameSize * 1.16f,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(metrics.mediumGap))

            StoreEbookPill(metrics = metrics)
        }
    }
}

@Composable
private fun StoreProfileImage(metrics: StoreMetrics) {
    val colorScheme = MaterialTheme.colorScheme
    val avatarShape = RoundedCornerShape((metrics.avatarSize * 0.24f).coerceIn(10.dp, 14.dp))
    val indicatorOuterSize = (metrics.avatarSize * 0.29f).coerceIn(11.dp, 15.dp)

    Box(
        modifier = Modifier.size(metrics.avatarSize + 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.profile_1),
            contentDescription = stringResource(R.string.cd_profile),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(metrics.avatarSize)
                .border(3.dp, colorScheme.onPrimary, avatarShape)
                .clip(avatarShape)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 1.dp, y = (-1).dp)
                .size(indicatorOuterSize)
                .clip(CircleShape)
                .background(colorScheme.onPrimary),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(indicatorOuterSize * 0.62f)
                    .clip(CircleShape)
                    .background(StoreOnline)
            )
        }
    }
}

@Composable
private fun StoreEbookPill(metrics: StoreMetrics) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .height(metrics.ebookPillHeight)
            .clip(CircleShape)
            .background(colorScheme.surface)
            .padding(horizontal = metrics.smallGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_premium),
            contentDescription = null,
            tint = StorePurple,
            modifier = Modifier.size(metrics.ebookPillHeight * 0.52f)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "12",
            color = StorePurple,
            maxLines = 1,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = metrics.ebookTextSize,
                lineHeight = metrics.ebookTextSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "Ebook",
            color = StorePurple,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = metrics.ebookTextSize * 0.72f,
                lineHeight = metrics.ebookTextSize,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun StoreBody(
    metrics: StoreMetrics,
    books: List<StoreBook>,
    onViewMoreClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onBookClick: (StoreBook) -> Unit
) {
    StoreCurvedSurface(
        surfaceColor = MaterialTheme.colorScheme.background,
        surroundColor = StorePurple,
        shape = storeBodyShape(metrics)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = metrics.promoTopPadding, bottom = metrics.bottomNavSpace)
        ) {
            PromotionCarousel(metrics = metrics, onViewMoreClick = onViewMoreClick)

            Spacer(modifier = Modifier.height(metrics.categorySectionTopGap))

            StoreSectionTitle(
                text = "Discover something new",
                metrics = metrics,
                modifier = Modifier.padding(horizontal = metrics.horizontalPadding)
            )

            Spacer(modifier = Modifier.height(metrics.mediumGap))

            StoreCategoryCarousel(metrics = metrics, onCategoryClick = onCategoryClick)

            Spacer(modifier = Modifier.height(metrics.categoryToListGap))

            books.forEach { book ->
                StoreBookRow(
                    book = book,
                    metrics = metrics,
                    onClick = { onBookClick(book) }
                )
                Spacer(modifier = Modifier.height(metrics.rowSpacing))
            }
        }
    }
}

@Composable
private fun PromotionCarousel(
    metrics: StoreMetrics,
    onViewMoreClick: () -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = metrics.horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(metrics.mediumGap)
    ) {
        items(storePromotions.size) { index ->
            PromotionCard(
                promotion = storePromotions[index],
                metrics = metrics,
                onViewMoreClick = onViewMoreClick
            )
        }
    }
}

@Composable
private fun PromotionCard(
    promotion: StorePromotion,
    metrics: StoreMetrics,
    onViewMoreClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(metrics.promoCorner)

    Box(
        modifier = Modifier
            .width(metrics.promoCardWidth)
            .height(metrics.promoCardHeight)
            .clip(cardShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF74D9FF),
                        Color(0xFF4357FF),
                        Color(0xFFEBD4E3),
                        Color(0xFFFFCE58)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .clickable(onClick = onViewMoreClick)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.06f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.44f, size.height * 0.16f),
                    radius = size.width * 0.48f
                ),
                radius = size.width * 0.48f,
                center = Offset(size.width * 0.44f, size.height * 0.16f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFD96C).copy(alpha = 0.9f),
                        Color(0xFFFFD96C).copy(alpha = 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.92f, size.height * 0.54f),
                    radius = size.width * 0.46f
                ),
                radius = size.width * 0.46f,
                center = Offset(size.width * 0.92f, size.height * 0.54f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF8A35D2).copy(alpha = 0.5f),
                        Color(0xFF8A35D2).copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.68f, size.height * 0.92f),
                    radius = size.width * 0.26f
                ),
                radius = size.width * 0.26f,
                center = Offset(size.width * 0.68f, size.height * 0.92f)
            )
        }

        Image(
            painter = painterResource(promotion.firstCoverRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = metrics.mediumGap, y = metrics.smallGap)
                .width(metrics.promoCoverWidth)
                .height(metrics.promoCoverWidth * 1.36f)
                .graphicsLayer(rotationZ = -5f)
                .shadow(10.dp, RoundedCornerShape(7.dp))
                .clip(RoundedCornerShape(7.dp))
                .zIndex(1f)
        )

        Image(
            painter = painterResource(promotion.secondCoverRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = metrics.mediumGap + metrics.promoCoverWidth * 0.78f, y = metrics.smallGap * 1.2f)
                .width(metrics.promoCoverWidth)
                .height(metrics.promoCoverWidth * 1.36f)
                .graphicsLayer(rotationZ = 3f)
                .shadow(10.dp, RoundedCornerShape(7.dp))
                .clip(RoundedCornerShape(7.dp))
                .zIndex(0.8f)
        )

        Text(
            text = promotion.discountText,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = metrics.promoDiscountSize,
                lineHeight = metrics.promoDiscountSize * 0.96f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = metrics.mediumGap, end = metrics.mediumGap)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(metrics.promoCardWidth * 0.38f)
                .height(metrics.promoButtonHeight)
                .clip(
                    RoundedCornerShape(
                        topStart = metrics.promoButtonHeight,
                        topEnd = 0.dp,
                        bottomEnd = metrics.promoCorner,
                        bottomStart = 0.dp
                    )
                )
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFA45BDB).copy(alpha = 0.92f),
                            Color(0xFFFFC44E).copy(alpha = 0.9f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VIEW MORE",
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = metrics.chipTextSize * 1.35f,
                    lineHeight = metrics.chipTextSize * 1.35f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun StoreCategoryCarousel(
    metrics: StoreMetrics,
    onCategoryClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = metrics.horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(metrics.mediumGap - 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        items(storeCategories.size) { index ->
            val category = storeCategories[index]
            StoreCategoryItem(
                category = category,
                metrics = metrics,
                onClick = { onCategoryClick(category.name) }
            )
        }
    }
}

@Composable
private fun StoreCategoryItem(
    category: StoreCategory,
    metrics: StoreMetrics,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(metrics.categoryTileSize + metrics.smallGap * 1.6f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(metrics.categoryTileSize)
                .clip(RoundedCornerShape(metrics.categoryTileSize * 0.28f))
                .background(category.backgroundColor)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(category.iconRes),
                contentDescription = category.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(metrics.categoryIconSize)
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = category.name,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = metrics.categoryLabelSize,
                lineHeight = metrics.categoryLabelSize * 1.15f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun StoreBookRow(
    book: StoreBook,
    metrics: StoreMetrics,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    CollectionDetailsBookListRow(
        book = CollectionDetailsBookUiState(
            imageRes = book.coverRes,
            title = book.title,
            author = book.author,
            views = book.views
        ),
        tags = book.tags.take(2).map { tag ->
            CollectionDetailsBookTagUiState(text = tag, color = colorForStoreTag(tag))
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.bookCoverHeight)
            .padding(horizontal = metrics.horizontalPadding)
            .clickable(onClick = onClick),
        coverWidth = metrics.bookCoverWidth,
        coverHeight = metrics.bookCoverHeight,
        coverCorner = 7.dp,
        bookGap = metrics.bookGap,
        bookTopPadding = 4.dp,
        titleSize = metrics.bookTitleSize,
        titleLineHeight = metrics.bookTitleSize * 1.18f,
        authorSize = metrics.bookAuthorSize,
        authorLineHeight = metrics.bookAuthorSize * 1.24f,
        viewsSize = metrics.bookAuthorSize * 1.03f,
        chipHeight = metrics.chipHeight,
        chipTextSize = metrics.chipTextSize,
        chipHorizontalPadding = metrics.mediumGap,
        menuIconSize = metrics.chipHeight * 1.34f,
        titleColor = colorScheme.onSurface.copy(alpha = 0.72f),
        authorColor = colorScheme.onSurface.copy(alpha = 0.66f),
        viewsColor = colorScheme.onSurface.copy(alpha = 0.36f),
        actionTint = StorePurple,
        menuTint = colorScheme.onSurface.copy(alpha = 0.64f),
        onOptionsClick = onClick
    )
}

@Composable
private fun StoreSectionTitle(
    text: String,
    metrics: StoreMetrics,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = metrics.sectionTitleSize,
            lineHeight = metrics.sectionTitleSize * 1.2f,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        ),
        modifier = modifier
    )
}

@Composable
private fun StoreCurvedSurface(
    surfaceColor: Color,
    surroundColor: Color,
    shape: RoundedCornerShape,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(surroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(surfaceColor)
        ) {
            content()
        }
    }
}

private fun colorForStoreTag(tag: String): Color {
    return when (tag.lowercase()) {
        "horror" -> ColorTertiary
        "fantasy" -> ColorCategoryFantasy
        else -> StorePurple
    }
}

private fun storeHeaderShape(metrics: StoreMetrics) = RoundedCornerShape(
    bottomEnd = metrics.headerCorner
)

private fun storeBodyShape(metrics: StoreMetrics) = RoundedCornerShape(
    topStart = metrics.headerCorner
)

@Composable
private fun rememberStoreMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): StoreMetrics {
    return remember(maxWidth, maxHeight) {
        val horizontalPadding = (maxWidth * 0.071f).coerceIn(22.dp, 32.dp)
        val smallGap = (maxWidth * 0.026f).coerceIn(8.dp, 12.dp)
        val mediumGap = (maxWidth * 0.045f).coerceIn(15.dp, 22.dp)
        val promoCardWidth = (maxWidth * 0.805f).coerceIn(286.dp, 360.dp)
        val promoCardHeight = (promoCardWidth * 0.455f).coerceIn(130.dp, 164.dp)
        val categoryTileSize = (maxWidth * 0.17f).coerceIn(60.dp, 78.dp)
        val bookCoverWidth = (maxWidth * 0.205f).coerceIn(72.dp, 88.dp)
        val bookTitleSize = (maxWidth.value * 0.047f).coerceIn(16.5f, 20f).sp
        val bookAuthorSize = (maxWidth.value * 0.035f).coerceIn(12.5f, 15f).sp
        val chipTextSize = (maxWidth.value * 0.029f).coerceIn(10.5f, 12f).sp

        StoreMetrics(
            horizontalPadding = horizontalPadding,
            smallGap = smallGap,
            mediumGap = mediumGap,
            headerHeight = (maxHeight * 0.13f).coerceIn(116.dp, 148.dp),
            headerCorner = (maxWidth * 0.13f).coerceIn(46.dp, 76.dp),
            avatarSize = (maxWidth * 0.125f).coerceIn(44.dp, 56.dp),
            headerTitleSize = (maxWidth.value * 0.047f).coerceIn(17f, 22f).sp,
            headerNameSize = (maxWidth.value * 0.043f).coerceIn(15.5f, 20f).sp,
            ebookPillHeight = (maxWidth * 0.083f).coerceIn(30.dp, 38.dp),
            ebookTextSize = (maxWidth.value * 0.038f).coerceIn(13f, 16f).sp,
            promoTopPadding = (maxWidth * 0.095f).coerceIn(34.dp, 46.dp),
            promoCardWidth = promoCardWidth,
            promoCardHeight = promoCardHeight,
            promoCorner = (maxWidth * 0.057f).coerceIn(20.dp, 28.dp),
            promoCoverWidth = (promoCardHeight * 0.58f).coerceIn(76.dp, 96.dp),
            promoDiscountSize = (maxWidth.value * 0.096f).coerceIn(34f, 46f).sp,
            promoButtonHeight = (promoCardHeight * 0.22f).coerceIn(32.dp, 42.dp),
            sectionTitleSize = (maxWidth.value * 0.045f).coerceIn(17f, 22f).sp,
            categoryTileSize = categoryTileSize,
            categoryIconSize = categoryTileSize * 0.68f,
            categoryLabelSize = (maxWidth.value * 0.034f).coerceIn(12f, 15f).sp,
            categorySectionTopGap = (maxWidth * 0.105f).coerceIn(38.dp, 54.dp),
            categoryToListGap = (maxWidth * 0.105f).coerceIn(30.dp, 40.dp),
            bookCoverWidth = bookCoverWidth,
            bookCoverHeight = bookCoverWidth * 1.42f,
            bookGap = (maxWidth * 0.061f).coerceIn(22.dp, 30.dp),
            bookTitleSize = bookTitleSize,
            bookAuthorSize = bookAuthorSize,
            chipHeight = (maxWidth * 0.051f).coerceIn(19.dp, 24.dp),
            chipTextSize = chipTextSize,
            rowSpacing = (maxWidth * 0.07f).coerceIn(26.dp, 36.dp),
            bottomNavSpace = (maxHeight * 0.13f).coerceIn(112.dp, 150.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreScreenPreview() {
    IBookTheme {
        StoreScreen()
    }
}
