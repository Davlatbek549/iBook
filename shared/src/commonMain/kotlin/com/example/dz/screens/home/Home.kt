package com.example.dz.screens.home

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.dz.navigation.CustomBottomBar
import com.example.dz.screens.category_detail.CategoryDetailScreen
import com.example.dz.screens.collection_details.CollectionDetailsBookListRow
import com.example.dz.screens.collection_details.CollectionDetailsBookTagUiState
import com.example.dz.screens.collection_details.CollectionDetailsBookUiState
import com.example.dz.screens.library.Library
import com.example.dz.screens.reading.ReadingStopwatchArt
import com.example.dz.screens.reading.ReadingTrophyArt
import com.example.dz.screens.search.SearchScreen
import com.example.dz.screens.store.StoreScreen
import com.example.dz.theme.ColorCategoryArts
import com.example.dz.theme.ColorCategoryBiographies
import com.example.dz.theme.ColorCategoryBusiness
import com.example.dz.theme.ColorCategoryComic
import com.example.dz.theme.ColorCategoryCooking
import com.example.dz.theme.ColorCategoryEducation
import com.example.dz.theme.ColorCategoryFantasy
import com.example.dz.theme.ColorCategoryTravel
import com.example.dz.theme.ColorPrimary
import com.example.dz.theme.ColorProfileAround
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class HomeMetrics(
    val horizontalPadding: Dp,
    val smallGap: Dp,
    val mediumGap: Dp,
    val largeGap: Dp,
    val headerHeight: Dp,
    val headerCorner: Dp,
    val avatarSize: Dp,
    val coinPillHeight: Dp,
    val sectionTitleSize: TextUnit,
    val tinyTextSize: TextUnit,
    val keepCardHeight: Dp,
    val keepCardCorner: Dp,
    val keepCoverWidth: Dp,
    val progressSize: Dp,
    val categoryHeight: Dp,
    val categoryIconTile: Dp,
    val categoryIconSize: Dp,
    val bookCoverWidth: Dp,
    val bookCoverHeight: Dp,
    val bookRowHeight: Dp,
    val bookTitleSize: TextUnit,
    val bookAuthorSize: TextUnit,
    val chipHeight: Dp,
    val chipTextSize: TextUnit,
    val authorAvatarSize: Dp,
    val goalCardHeight: Dp,
    val goalArtSize: Dp,
    val goalButtonHeight: Dp,
    val bottomNavSpace: Dp
)

data class HomeBook(
    val title: String,
    val author: String,
    val coverRes: DrawableResource,
    val rating: String = "13.20",
    val tags: List<String>
)

data class HomeAuthor(
    val name: String,
    val avatarRes: DrawableResource,
    val tags: List<String>
)

data class HomeCategory(
    val name: String,
    val iconRes: DrawableResource,
    val backgroundColor: Color
)

private val HomePurple = ColorPrimary
private val HomeOnlineGreen = ColorProfileAround

private val homeCategories = listOf(
    HomeCategory("Arts", Res.drawable.img_arts, ColorCategoryArts),
    HomeCategory("Biography", Res.drawable.img_biography, ColorCategoryBiographies),
    HomeCategory("Business", Res.drawable.img_business, ColorCategoryBusiness),
    HomeCategory("Comic", Res.drawable.img_horror, ColorCategoryComic),
    HomeCategory("Comedy", Res.drawable.img_cooking, ColorCategoryCooking)
)

private val trendBooks = listOf(
    HomeBook("Dacon King Carr: A Novel", "by James McBride", Res.drawable.book_cover, tags = listOf("History", "Worker")),
    HomeBook("The Archer", "by Paulo Coelho", Res.drawable.book_cover_2, tags = listOf("Fantasy", "Comic")),
    HomeBook("Red at the Bone", "by Jacqueline Woodson", Res.drawable.book_cover_3, tags = listOf("History", "Sci")),
    HomeBook("Bestiary", "by K-Ming Chang", Res.drawable.book_cover_4, tags = listOf("History", "Sci-fi")),
    HomeBook("The Undocumented Americans", "by Karla Cornejo Villavicencio", Res.drawable.book_cove_6, tags = listOf("Cartoon", "History"))
)

private val loveBooks = listOf(
    HomeBook("A Burning", "by Megha Majumdar", Res.drawable.book_cover_2, tags = listOf("Fantasy", "Travel")),
    HomeBook("The Immortalists", "by Chloe Benjamin", Res.drawable.book_cover_3, tags = listOf("Romance", "History")),
    HomeBook("Olive, Again", "by Elizabeth Strout", Res.drawable.book_cover_4, tags = listOf("History", "Romance")),
    HomeBook("How Much These Hills Is Gold", "by C Pam Zhang", Res.drawable.book_cove_6, tags = listOf("Arts", "Sci")),
    HomeBook("Love in the Time of Cholera", "by Gabriel Garcia Marquez", Res.drawable.book_cover, tags = listOf("Romance", "Comic"))
)

private val moreBooks = listOf(
    HomeBook("The Searcher", "by Tana French", Res.drawable.book_cove_6, tags = listOf("Arts", "Romance")),
    HomeBook("The Penguin Book of Italian Short Stories", "by Jhumpa Lahiri", Res.drawable.book_cover_4, tags = listOf("Biography", "Business")),
    HomeBook("The Most Fun We Ever Had", "by Claire Lombardo", Res.drawable.book_cover_3, tags = listOf("Love", "Romance")),
    HomeBook("Just Mercy (Movie Tie-In Edition)", "by Bryan Stevenson", Res.drawable.book_cover_2, tags = listOf("Comic", "Romance")),
    HomeBook("Homegoing", "by Yaa Gyasi", Res.drawable.book_cover, tags = listOf("Fiction", "Sci-fi"))
)

private val popularAuthors = listOf(
    HomeAuthor("Maria Remy", Res.drawable.img_maria_renzy, listOf("History", "Romance")),
    HomeAuthor("RonaldPurfrill", Res.drawable.img_raunak_purohit, listOf("Arts", "Worker")),
    HomeAuthor("Malan Cerre", Res.drawable.img_masaa, listOf("Sci-fi", "Medical")),
    HomeAuthor("Neil Alvin", Res.drawable.img_neil_alvin, listOf("Adventure", "Comic")),
    HomeAuthor("Ysa Barreto", Res.drawable.img_yza_barretto, listOf("Heroic", "Biography"))
)

@Composable
fun Home() {
    var currentRoute by remember { mutableStateOf("home") }
    var isSearchFocused by remember { mutableStateOf(false) }
    val usesLightBottomBar = currentRoute == "books" || currentRoute == "library" || currentRoute == "store" || currentRoute == "search"
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(currentRoute) {
        if (currentRoute != "search") {
            isSearchFocused = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentRoute) {
                "home" -> HomeScreen()
                "books", "library" -> Library()
                "store" -> StoreScreen()
                "search" -> {
                    SearchScreen(
                        onSearchFocusChange = { isSearchFocused = it },
                        onCategoryClick = { category ->
                            if (category.equals("Horror", ignoreCase = true)) {
                                currentRoute = "category_detail"
                            }
                        }
                    )
                }
                "category_detail" -> {
                    CategoryDetailScreen(
                        onBackClick = {
                            currentRoute = "search"
                        }
                    )
                }
                else -> HomeScreen()
            }
        }

        if (!isSearchFocused && currentRoute != "category_detail") {
            CustomBottomBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    currentRoute = route
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .zIndex(2f),
                containerColor = if (usesLightBottomBar) colorScheme.surface else colorScheme.primary,
                selectedContentColor = if (usesLightBottomBar) {
                    colorScheme.primary
                } else {
                    colorScheme.onPrimary
                },
                unselectedContentColor = if (usesLightBottomBar) {
                    colorScheme.onSurface.copy(alpha = 0.34f)
                } else {
                    colorScheme.onPrimary.copy(alpha = 0.74f)
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onKeepReadingClick: () -> Unit = {},
    onViewAllCategoriesClick: () -> Unit = {},
    onBookClick: (HomeBook) -> Unit = {},
    onAuthorClick: (HomeAuthor) -> Unit = {},
    onGoalsKeepReadingClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberHomeMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = metrics.bottomNavSpace)
        ) {
            item {
                TopHomeHeader(metrics = metrics)
            }

            item {
                KeepReadingSection(
                    metrics = metrics,
                    onClick = onKeepReadingClick
                )
            }

            item {
                CategorySection(
                    metrics = metrics,
                    onViewAllClick = onViewAllCategoriesClick
                )
            }

            item {
                BookSection(
                    title = stringResource(Res.string.home_trending_book),
                    books = trendBooks,
                    metrics = metrics,
                    onBookClick = onBookClick
                )
            }

            item {
                PopularAuthorsSection(
                    authors = popularAuthors,
                    metrics = metrics,
                    onAuthorClick = onAuthorClick
                )
            }

            item {
                BookSection(
                    title = stringResource(Res.string.home_you_may_love),
                    books = loveBooks,
                    metrics = metrics,
                    onBookClick = onBookClick
                )
            }

            item {
                ReadingGoalsSection(
                    metrics = metrics,
                    onKeepReadingClick = onGoalsKeepReadingClick
                )
            }

            item {
                BookSection(
                    title = stringResource(Res.string.home_more_books),
                    books = moreBooks,
                    metrics = metrics,
                    onBookClick = onBookClick,
                    roundBottom = false
                )
            }
        }
    }
}

@Composable
private fun TopHomeHeader(metrics: HomeMetrics) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.headerHeight)
            .clip(homeHeaderShape(metrics))
            .background(HomePurple)
            .statusBarsPadding()
            .padding(horizontal = metrics.horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfileHeaderImage(metrics = metrics)

                Spacer(modifier = Modifier.width(metrics.smallGap))

                Column {
                    Text(
                        text = stringResource(Res.string.home_hi),
                        color = colorScheme.onPrimary.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = metrics.tinyTextSize,
                            lineHeight = metrics.tinyTextSize * 1.2f,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.sp
                        )
                    )
                    Text(
                        text = stringResource(Res.string.home_username),
                        color = colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = metrics.tinyTextSize * 1.05f,
                            lineHeight = metrics.tinyTextSize * 1.25f,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .height(metrics.coinPillHeight)
                    .clip(CircleShape)
                    .background(colorScheme.surface)
                    .padding(horizontal = metrics.smallGap),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_premium),
                    contentDescription = null,
                    tint = HomePurple,
                    modifier = Modifier.size(metrics.coinPillHeight * 0.44f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.home_coin_count),
                    color = HomePurple,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = metrics.tinyTextSize * 1.4f,
                        lineHeight = metrics.tinyTextSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
                Text(
                    text = stringResource(Res.string.home_coin_label),
                    color = HomePurple,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = metrics.tinyTextSize * 0.82f,
                        lineHeight = metrics.tinyTextSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun ProfileHeaderImage(metrics: HomeMetrics) {
    val colorScheme = MaterialTheme.colorScheme
    val avatarShape = RoundedCornerShape((metrics.avatarSize * 0.34f).coerceIn(10.dp, 14.dp))
    val indicatorOuterSize = (metrics.avatarSize * 0.31f).coerceIn(9.dp, 12.dp)

    Box(
        modifier = Modifier.size(metrics.avatarSize + 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.profile_1),
            contentDescription = stringResource(Res.string.cd_profile),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(metrics.avatarSize)
                .border(3.dp, colorScheme.surface, avatarShape)
                .clip(avatarShape)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 1.dp, y = (-1).dp)
                .size(indicatorOuterSize)
                .clip(CircleShape)
                .background(colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(indicatorOuterSize * 0.64f)
                    .clip(CircleShape)
                    .background(HomeOnlineGreen)
            )
        }
    }
}

@Composable
private fun KeepReadingSection(
    metrics: HomeMetrics,
    onClick: () -> Unit
) {
    HomeCurvedSurface(
        surfaceColor = MaterialTheme.colorScheme.background,
        surroundColor = HomePurple,
        shape = homeWhiteSectionShape(metrics)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = metrics.horizontalPadding)
                .padding(top = metrics.largeGap, bottom = metrics.largeGap)
        ) {
            HomeSectionTitle(stringResource(Res.string.home_keep_reading), metrics = metrics)
            Spacer(modifier = Modifier.height(metrics.smallGap))
            KeepReadingCard(metrics = metrics, onClick = onClick)
        }
    }
}

@Composable
private fun KeepReadingCard(
    metrics: HomeMetrics,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val onPrimary = colorScheme.onPrimary
    val cardShape = RoundedCornerShape(metrics.keepCardCorner)
    val coverWidth = metrics.keepCoverWidth * 1.12f
    val coverHeight = metrics.keepCardHeight * 0.94f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.keepCardHeight)
            .clip(cardShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFB160),
                        Color(0xFFFF673C),
                        Color(0xFFE93328),
                        Color(0xFFB92824)
                    )
                )
            )
            .clickable(onClick = onClick)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        onPrimary.copy(alpha = 0.34f),
                        onPrimary.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.24f, size.height * 0.24f),
                    radius = size.width * 0.42f
                ),
                radius = size.width * 0.42f,
                center = Offset(size.width * 0.24f, size.height * 0.24f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF5E1712).copy(alpha = 0.32f),
                        Color(0xFF5E1712).copy(alpha = 0.1f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.82f, size.height * 0.78f),
                    radius = size.width * 0.36f
                ),
                radius = size.width * 0.36f,
                center = Offset(size.width * 0.82f, size.height * 0.78f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = metrics.mediumGap,
                    top = metrics.smallGap,
                    end = metrics.mediumGap,
                    bottom = metrics.smallGap
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.book_cover),
            contentDescription = stringResource(Res.string.cd_featured_book),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .offset(x = -metrics.smallGap * 0.55f)
                    .width(coverWidth)
                    .height(coverHeight)
                    .shadow(10.dp, RoundedCornerShape(9.dp))
                    .clip(RoundedCornerShape(9.dp))
            )

            Spacer(modifier = Modifier.width(metrics.smallGap))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = metrics.smallGap * 0.35f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.home_featured_book),
                        color = onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = metrics.bookTitleSize * 1.18f,
                            lineHeight = metrics.bookTitleSize * 1.34f,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.home_featured_author),
                        color = onPrimary.copy(alpha = 0.84f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = metrics.bookAuthorSize * 1.05f,
                            lineHeight = metrics.bookAuthorSize * 1.25f,
                            letterSpacing = 0.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(metrics.mediumGap))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ReadingProgressCircle(
                        progress = 0.62f,
                        metrics = metrics
                    )
                }
            }
        }
    }
}

@Composable
private fun ReadingProgressCircle(
    progress: Float,
    metrics: HomeMetrics
) {
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier.size(metrics.progressSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.minDimension * 0.12f
            drawCircle(
                color = onPrimary.copy(alpha = 0.28f),
                radius = size.minDimension / 2f - strokeWidth,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = onPrimary,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(
            text = stringResource(Res.string.home_featured_progress),
            color = onPrimary,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = metrics.tinyTextSize * 0.8f,
                lineHeight = metrics.tinyTextSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun CategorySection(
    metrics: HomeMetrics,
    onViewAllClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    HomeCurvedSurface(
        surfaceColor = HomePurple,
        surroundColor = MaterialTheme.colorScheme.background,
        shape = homePurpleSectionShape(metrics)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(metrics.categoryHeight)
                .padding(horizontal = metrics.horizontalPadding, vertical = metrics.mediumGap)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomeSectionTitle(stringResource(Res.string.home_category), color = colorScheme.onPrimary, metrics = metrics)
                Text(
                    text = stringResource(Res.string.home_view_all),
                    color = HomePurple,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colorScheme.surface)
                        .clickable(onClick = onViewAllClick)
                        .padding(horizontal = metrics.mediumGap, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = metrics.tinyTextSize * 0.9f,
                        lineHeight = metrics.tinyTextSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(metrics.mediumGap))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                homeCategories.forEach { category ->
                    CategoryItem(
                        category = category,
                        metrics = metrics
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: HomeCategory,
    metrics: HomeMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(metrics.categoryIconTile)
    ) {
        Box(
            modifier = Modifier
                .size(metrics.categoryIconTile)
                .clip(RoundedCornerShape(metrics.categoryIconTile * 0.22f))
                .background(category.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(category.iconRes),
                contentDescription = category.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(metrics.categoryIconSize)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = category.name,
            color = colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = metrics.tinyTextSize,
                lineHeight = metrics.tinyTextSize * 1.15f,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun BookSection(
    title: String,
    books: List<HomeBook>,
    metrics: HomeMetrics,
    onBookClick: (HomeBook) -> Unit,
    roundBottom: Boolean = true
) {
    HomeCurvedSurface(
        surfaceColor = MaterialTheme.colorScheme.background,
        surroundColor = HomePurple,
        shape = homeWhiteSectionShape(metrics, roundBottom = roundBottom)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = metrics.horizontalPadding)
                .padding(top = metrics.largeGap, bottom = metrics.mediumGap)
        ) {
            HomeSectionTitle(title, metrics = metrics)
            Spacer(modifier = Modifier.height(metrics.mediumGap))

            books.forEach { book ->
                BookRow(
                    book = book,
                    metrics = metrics,
                    onClick = { onBookClick(book) }
                )
                Spacer(modifier = Modifier.height(metrics.mediumGap))
            }
        }
    }
}

@Composable
private fun BookRow(
    book: HomeBook,
    metrics: HomeMetrics,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    CollectionDetailsBookListRow(
        book = CollectionDetailsBookUiState(
            imageRes = book.coverRes,
            title = book.title,
            author = book.author,
            views = book.rating
        ),
        tags = book.tags.take(2).map { tag ->
            CollectionDetailsBookTagUiState(text = tag, color = colorForTag(tag))
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.bookRowHeight)
            .clickable(onClick = onClick),
        coverWidth = metrics.bookCoverWidth,
        coverHeight = metrics.bookCoverHeight,
        coverCorner = 7.dp,
        bookGap = metrics.mediumGap,
        bookTopPadding = 3.dp,
        titleSize = metrics.bookTitleSize,
        titleLineHeight = metrics.bookTitleSize * 1.22f,
        authorSize = metrics.bookAuthorSize,
        authorLineHeight = metrics.bookAuthorSize * 1.24f,
        viewsSize = metrics.chipTextSize * 1.1f,
        chipHeight = metrics.chipHeight,
        chipTextSize = metrics.chipTextSize,
        chipHorizontalPadding = metrics.smallGap,
        menuIconSize = metrics.chipHeight * 1.28f,
        titleColor = colorScheme.onSurface,
        authorColor = colorScheme.onSurface.copy(alpha = 0.62f),
        viewsColor = HomePurple,
        actionTint = HomePurple,
        menuTint = colorScheme.onSurface.copy(alpha = 0.7f),
        onOptionsClick = onClick
    )
}

@Composable
private fun PopularAuthorsSection(
    authors: List<HomeAuthor>,
    metrics: HomeMetrics,
    onAuthorClick: (HomeAuthor) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    HomeCurvedSurface(
        surfaceColor = HomePurple,
        surroundColor = MaterialTheme.colorScheme.background,
        shape = homePurpleSectionShape(metrics)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = metrics.horizontalPadding, vertical = metrics.largeGap)
        ) {
            HomeSectionTitle(stringResource(Res.string.home_popular_authors), color = colorScheme.onPrimary, metrics = metrics)
            Spacer(modifier = Modifier.height(metrics.mediumGap))

            authors.forEach { author ->
                AuthorRow(
                    author = author,
                    metrics = metrics,
                    onClick = { onAuthorClick(author) }
                )
                Spacer(modifier = Modifier.height(metrics.mediumGap))
            }
        }
    }
}

@Composable
private fun AuthorRow(
    author: HomeAuthor,
    metrics: HomeMetrics,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(author.avatarRes),
            contentDescription = author.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(metrics.authorAvatarSize)
                .height(metrics.authorAvatarSize * 1.08f)
                .clip(RoundedCornerShape((metrics.authorAvatarSize * 0.28f).coerceIn(10.dp, 14.dp)))
        )

        Spacer(modifier = Modifier.width(metrics.mediumGap))

        Column {
            Text(
                text = author.name,
                color = colorScheme.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.bookTitleSize,
                    lineHeight = metrics.bookTitleSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                author.tags.forEach { tag ->
                    GenreChip(tag = tag, metrics = metrics)
                }
            }
        }
    }
}

@Composable
private fun ReadingGoalsSection(
    metrics: HomeMetrics,
    onKeepReadingClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    HomeCurvedSurface(
        surfaceColor = HomePurple,
        surroundColor = MaterialTheme.colorScheme.background,
        shape = homePurpleSectionShape(metrics)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = metrics.horizontalPadding, vertical = metrics.largeGap)
        ) {
            HomeSectionTitle(stringResource(Res.string.home_reading_goals), color = colorScheme.onPrimary, metrics = metrics)
            Spacer(modifier = Modifier.height(metrics.mediumGap))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(metrics.mediumGap)
            ) {
                ReadingGoalCard(
                    title = stringResource(Res.string.home_today_reading),
                    value = stringResource(Res.string.home_goal_today_value),
                    unit = stringResource(Res.string.home_goal_today_unit),
                    metrics = metrics,
                    art = { ReadingStopwatchArt(modifier = Modifier.size(metrics.goalArtSize)) },
                    modifier = Modifier.weight(1f)
                )
                ReadingGoalCard(
                    title = stringResource(Res.string.home_longest_streak),
                    value = stringResource(Res.string.home_goal_streak_value),
                    unit = stringResource(Res.string.home_goal_streak_unit),
                    metrics = metrics,
                    art = { ReadingTrophyArt(modifier = Modifier.size(metrics.goalArtSize)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(metrics.mediumGap))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(metrics.goalButtonHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.surface)
                    .clickable(onClick = onKeepReadingClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.home_keep_reading),
                    color = colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = metrics.bookAuthorSize * 1.08f,
                        lineHeight = metrics.bookAuthorSize * 1.28f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun ReadingGoalCard(
    title: String,
    value: String,
    unit: String,
    metrics: HomeMetrics,
    art: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .height(metrics.goalCardHeight)
            .clip(RoundedCornerShape(14.dp))
            .background(colorScheme.surface)
            .padding(metrics.mediumGap),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            color = colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = metrics.bookAuthorSize,
                lineHeight = metrics.bookAuthorSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            art()
        }

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                color = HomePurple,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.sectionTitleSize * 1.15f,
                    lineHeight = metrics.sectionTitleSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = unit,
                color = HomePurple,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = metrics.tinyTextSize * 0.9f,
                    lineHeight = metrics.tinyTextSize,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun GenreChip(
    tag: String,
    metrics: HomeMetrics
) {
    Box(
        modifier = Modifier
            .height(metrics.chipHeight)
            .clip(CircleShape)
            .background(colorForTag(tag))
            .padding(horizontal = metrics.smallGap),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
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
private fun HomeSectionTitle(
    text: String,
    metrics: HomeMetrics,
    color: Color? = null
) {
    Text(
        text = text,
        color = color ?: MaterialTheme.colorScheme.onSurface,
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

private fun colorForTag(tag: String): Color {
    return when (tag.lowercase()) {
        "history", "fantasy", "business", "romance", "love" -> ColorCategoryFantasy
        "worker", "horror", "arts", "heroic" -> ColorCategoryArts
        "travel" -> ColorCategoryTravel
        "sci", "sci-fi", "medical", "fiction" -> ColorCategoryEducation
        "comic", "cartoon", "adventure" -> ColorCategoryComic
        "biography" -> ColorCategoryCooking
        else -> HomePurple
    }
}

@Composable
private fun HomeCurvedSurface(
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

private fun homeHeaderShape(metrics: HomeMetrics) = RoundedCornerShape(
    bottomEnd = metrics.headerCorner
)

private fun homeWhiteSectionShape(
    metrics: HomeMetrics,
    roundBottom: Boolean = true
) = RoundedCornerShape(
    topStart = metrics.headerCorner,
    bottomStart = if (roundBottom) metrics.headerCorner else 0.dp
)

private fun homePurpleSectionShape(metrics: HomeMetrics) = RoundedCornerShape(
    topEnd = metrics.headerCorner,
    bottomEnd = metrics.headerCorner
)

@Composable
private fun rememberHomeMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): HomeMetrics {
    return remember(maxWidth, maxHeight) {
        val horizontalPadding = (maxWidth * 0.055f).coerceIn(19.dp, 28.dp)
        val smallGap = (maxWidth * 0.027f).coerceIn(9.dp, 13.dp)
        val mediumGap = (maxWidth * 0.042f).coerceIn(14.dp, 20.dp)
        val largeGap = (maxWidth * 0.068f).coerceIn(24.dp, 32.dp)
        val bookCoverWidth = (maxWidth * 0.19f).coerceIn(64.dp, 78.dp)
        val keepCardHeight = (maxWidth * 0.365f).coerceIn(130.dp, 150.dp)
        val categoryIconTile = (maxWidth * 0.137f).coerceIn(46.dp, 56.dp) + 10.dp
        val sectionTitleSize = (maxWidth.value * 0.038f).coerceIn(15f, 18f).sp
        val bookTitleSize = (maxWidth.value * 0.034f).coerceIn(13f, 16f).sp
        val bookAuthorSize = (maxWidth.value * 0.027f).coerceIn(10f, 12.5f).sp
        val tinyTextSize = (maxWidth.value * 0.025f).coerceIn(9f, 11.5f).sp

        HomeMetrics(
            horizontalPadding = horizontalPadding,
            smallGap = smallGap,
            mediumGap = mediumGap,
            largeGap = largeGap,
            headerHeight = (maxHeight * 0.112f).coerceIn(90.dp, 108.dp),
            headerCorner = (maxWidth * 0.085f).coerceIn(28.dp, 36.dp),
            avatarSize = (maxWidth * 0.088f).coerceIn(32.dp, 40.dp),
            coinPillHeight = (maxWidth * 0.057f).coerceIn(22.dp, 28.dp),
            sectionTitleSize = sectionTitleSize,
            tinyTextSize = tinyTextSize,
            keepCardHeight = keepCardHeight,
            keepCardCorner = (maxWidth * 0.04f).coerceIn(14.dp, 18.dp),
            keepCoverWidth = keepCardHeight * 0.57f,
            progressSize = (maxWidth * 0.112f).coerceIn(42.dp, 50.dp),
            categoryHeight = (maxWidth * 0.41f).coerceIn(154.dp, 182.dp),
            categoryIconTile = categoryIconTile,
            categoryIconSize = categoryIconTile * 0.74f,
            bookCoverWidth = bookCoverWidth,
            bookCoverHeight = bookCoverWidth * 1.45f,
            bookRowHeight = (bookCoverWidth * 1.58f).coerceIn(106.dp, 124.dp),
            bookTitleSize = bookTitleSize,
            bookAuthorSize = bookAuthorSize,
            chipHeight = (maxWidth * 0.048f).coerceIn(18.dp, 22.dp),
            chipTextSize = (maxWidth.value * 0.022f).coerceIn(8.5f, 10.5f).sp,
            authorAvatarSize = (maxWidth * 0.135f).coerceIn(48.dp, 60.dp),
            goalCardHeight = (maxWidth * 0.44f).coerceIn(152.dp, 178.dp),
            goalArtSize = (maxWidth * 0.19f).coerceIn(68.dp, 86.dp),
            goalButtonHeight = (maxWidth * 0.145f).coerceIn(52.dp, 62.dp),
            bottomNavSpace = (maxHeight * 0.12f).coerceIn(104.dp, 132.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    DZTheme {
        HomeScreen()
    }
}
