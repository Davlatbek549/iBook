package com.example.ibook.screens.search

import androidx.annotation.DrawableRes
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
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
import com.example.ibook.ui.theme.ColorText
import com.example.ibook.ui.theme.ColorTextStrongLight
import com.example.ibook.ui.theme.IBookTheme

private data class SearchMetrics(
    val horizontalPadding: Dp,
    val searchBarTopMargin: Dp,
    val searchBarHeight: Dp,
    val searchBarRadius: Dp,
    val searchIconSize: Dp,
    val searchTextSize: TextUnit,
    val categoryTitleTopMargin: Dp,
    val categoryTitleSize: TextUnit,
    val categoryGridTopGap: Dp,
    val tileCorner: Dp,
    val labelSize: TextUnit,
    val tileToLabelGap: Dp,
    val rowVerticalGap: Dp,
    val columnGap: Dp,
    val focusedBackButtonSize: Dp,
    val focusedSearchGap: Dp,
    val focusedTrendingTopGap: Dp,
    val trendingTitleSize: TextUnit,
    val trendingItemSize: TextUnit,
    val trendingItemGap: Dp,
    val resultCoverWidth: Dp,
    val resultCoverHeight: Dp,
    val resultBookGap: Dp,
    val resultRowGap: Dp,
    val resultTitleSize: TextUnit,
    val resultAuthorSize: TextUnit,
    val resultChipHeight: Dp,
    val resultChipTextSize: TextUnit,
    val bottomNavSpace: Dp
)

private data class SearchCategory(
    val name: String,
    @param:DrawableRes val imageRes: Int,
    val backgroundColor: Color
)

private data class SearchResultBook(
    val title: String,
    val author: String,
    @param:DrawableRes val coverRes: Int,
    val tags: List<String>
)

private val searchCategories = listOf(
    SearchCategory("Arts", R.drawable.img_arts, ColorCategoryArts),
    SearchCategory("Biographies", R.drawable.img_biography, ColorCategoryBiographies),
    SearchCategory("Business", R.drawable.img_business, ColorCategoryBusiness),
    SearchCategory("Comic", R.drawable.img_commic, ColorCategoryComic),
    SearchCategory("Cooking", R.drawable.img_cooking, ColorCategoryCooking),
    SearchCategory("Edu", R.drawable.img_edu, ColorCategoryEducation),
    SearchCategory("Health", R.drawable.img_health, ColorCategoryHealth),
    SearchCategory("History", R.drawable.img_history, ColorCategoryHistory),
    SearchCategory("Horror", R.drawable.img_horror, ColorTertiary),
    SearchCategory("Kid", R.drawable.img_kid, ColorCategoryKids),
    SearchCategory("Medical", R.drawable.img_medical, ColorCategoryMedical),
    SearchCategory("Romance", R.drawable.img_romance, ColorSecondary),
    SearchCategory("Fantasy", R.drawable.img_fantasy, ColorCategoryFantasy),
    SearchCategory("Self-Help", R.drawable.img_self_help, ColorCategorySelfHelp),
    SearchCategory("Sport", R.drawable.img_sport, ColorCategorySport),
    SearchCategory("Travel", R.drawable.img_travel, ColorCategoryTravel)
)

private val trendingSearches = listOf(
    "Stephen king",
    "The Secret",
    "The great gatsby",
    "1984",
    "The four agreements",
    "A promised land",
    "Caste",
    "The power of habit",
    "Leadership"
)

private val foundSearchBooks = listOf(
    SearchResultBook(
        title = "Murder Board",
        author = "By Brian Shea",
        coverRes = R.drawable.book_cover,
        tags = listOf("Horror", "Romatic")
    ),
    SearchResultBook(
        title = "Murder Board",
        author = "By Brian Shea",
        coverRes = R.drawable.book_cover,
        tags = listOf("Horror", "Romatic")
    ),
    SearchResultBook(
        title = "Murder Board",
        author = "By Brian Shea",
        coverRes = R.drawable.book_cover,
        tags = listOf("Horror", "Romatic")
    )
)

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onSearchFocusChange: (Boolean) -> Unit = {},
    onCategoryClick: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val metrics = rememberSearchMetrics(maxWidth, maxHeight)
        val focusedSearchRequester = remember { FocusRequester() }

        fun closeFocusedSearch() {
            isSearchFocused = false
            onSearchFocusChange(false)
            focusManager.clearFocus()
        }

        BackHandler(enabled = isSearchFocused) {
            closeFocusedSearch()
        }

        LaunchedEffect(isSearchFocused) {
            if (isSearchFocused) {
                focusedSearchRequester.requestFocus()
            }
        }

        SearchCategoryContent(
            query = query,
            onQueryChange = { query = it },
            metrics = metrics,
            onSearchFocus = {
                isSearchFocused = true
                onSearchFocusChange(true)
            },
            onCategoryClick = onCategoryClick,
            inputEnabled = !isSearchFocused,
            modifier = Modifier
                .fillMaxSize()
                .then(if (isSearchFocused) Modifier.blur(22.dp) else Modifier)
        )

        if (isSearchFocused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF6F6F6F).copy(alpha = 0.78f))
            )

            FocusedSearchContent(
                query = query,
                onQueryChange = { query = it },
                metrics = metrics,
                focusRequester = focusedSearchRequester,
                onBackClick = ::closeFocusedSearch,
                onSearchClick = {
                    if (query.isNotBlank()) {
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun SearchCategoryContent(
    query: String,
    onQueryChange: (String) -> Unit,
    metrics: SearchMetrics,
    onSearchFocus: () -> Unit,
    onCategoryClick: (String) -> Unit,
    inputEnabled: Boolean,
    modifier: Modifier = Modifier
) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(
                start = metrics.horizontalPadding,
                end = metrics.horizontalPadding,
                bottom = metrics.bottomNavSpace
            )
        ) {
            item {
                Spacer(Modifier.height(metrics.searchBarTopMargin))
                SearchInputBar(
                    query = query,
                    onQueryChange = onQueryChange,
                    metrics = metrics,
                    enabled = inputEnabled,
                    onFocusChanged = { isFocused ->
                        if (isFocused) {
                            onSearchFocus()
                        }
                    }
                )
                Spacer(Modifier.height(metrics.categoryTitleTopMargin))
                Text(
                    text = "Category",
                    color = ColorTextStrongLight,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = metrics.categoryTitleSize,
                        lineHeight = metrics.categoryTitleSize * 1.2f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
                Spacer(Modifier.height(metrics.categoryGridTopGap))
            }

            searchCategories.chunked(4).forEach { rowCategories ->
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(metrics.columnGap)
                    ) {
                        rowCategories.forEach { category ->
                            CategoryGridItem(
                                category = category,
                                metrics = metrics,
                                onClick = { onCategoryClick(category.name) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill remaining slots in last row so columns stay equal-width
                        repeat(4 - rowCategories.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(Modifier.height(metrics.rowVerticalGap))
                }
            }
        }
}

@Composable
private fun SearchInputBar(
    query: String,
    onQueryChange: (String) -> Unit,
    metrics: SearchMetrics,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    focusRequester: FocusRequester? = null,
    onSearchClick: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {}
) {
    val barBackground = Color(0xFFF0F2F6)
    val focusRequesterModifier = focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .then(focusRequesterModifier)
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            }
            .height(metrics.searchBarHeight)
            .clip(RoundedCornerShape(metrics.searchBarRadius))
            .background(barBackground)
            .padding(horizontal = metrics.searchBarRadius),
        singleLine = true,
        cursorBrush = SolidColor(ColorPrimary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = metrics.searchTextSize,
            color = ColorTextStrongLight,
            letterSpacing = 0.sp
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Enter the Title or Authors",
                            color = ColorText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = metrics.searchTextSize,
                                letterSpacing = 0.sp
                            )
                        )
                    }
                    innerTextField()
                }
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = ColorPrimary,
                    modifier = Modifier
                        .size(metrics.searchIconSize)
                        .clip(CircleShape)
                        .clickable(onClick = onSearchClick)
                )
            }
        }
    )
}

@Composable
private fun FocusedSearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    metrics: SearchMetrics,
    focusRequester: FocusRequester,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .imePadding()
            .padding(horizontal = metrics.horizontalPadding)
    ) {
        Spacer(Modifier.height(metrics.searchBarTopMargin))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(metrics.focusedBackButtonSize)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.13f))
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(metrics.focusedBackButtonSize * 0.36f)
                )
            }

            Spacer(Modifier.width(metrics.focusedSearchGap))

            SearchInputBar(
                query = query,
                onQueryChange = onQueryChange,
                metrics = metrics,
                focusRequester = focusRequester,
                onSearchClick = onSearchClick,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(metrics.focusedTrendingTopGap))

        Text(
            text = "Trending",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.trendingTitleSize,
                lineHeight = metrics.trendingTitleSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(Modifier.height(metrics.trendingItemGap))

        if (query.isBlank()) {
            trendingSearches.forEach { term ->
                Text(
                    text = term,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = metrics.trendingItemSize,
                        lineHeight = metrics.trendingItemSize * 1.18f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
                Spacer(Modifier.height(metrics.trendingItemGap))
            }
        } else {
            foundSearchBooks.forEach { book ->
                SearchResultRow(book = book, metrics = metrics)
                Spacer(Modifier.height(metrics.resultRowGap))
            }
        }
    }
}

@Composable
private fun SearchResultRow(
    book: SearchResultBook,
    metrics: SearchMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.resultCoverHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(book.coverRes),
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(metrics.resultCoverWidth)
                .height(metrics.resultCoverHeight)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(Modifier.width(metrics.resultBookGap))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = book.title,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.resultTitleSize,
                    lineHeight = metrics.resultTitleSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = book.author,
                color = Color.White.copy(alpha = 0.82f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = metrics.resultAuthorSize,
                    lineHeight = metrics.resultAuthorSize * 1.2f,
                    letterSpacing = 0.sp
                )
            )

            Spacer(Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                book.tags.forEach { tag ->
                    SearchResultChip(
                        text = tag,
                        color = colorForSearchResultTag(tag),
                        metrics = metrics
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultChip(
    text: String,
    color: Color,
    metrics: SearchMetrics
) {
    Box(
        modifier = Modifier
            .height(metrics.resultChipHeight)
            .clip(CircleShape)
            .background(color)
            .padding(horizontal = metrics.focusedSearchGap),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = metrics.resultChipTextSize,
                lineHeight = metrics.resultChipTextSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

private fun colorForSearchResultTag(tag: String): Color {
    return when (tag.lowercase()) {
        "horror" -> ColorPrimary
        "romatic", "romantic" -> ColorSecondary
        else -> ColorTertiary
    }
}

@Composable
private fun CategoryGridItem(
    category: SearchCategory,
    metrics: SearchMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(metrics.tileCorner))
                .background(category.backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(category.imageRes),
                contentDescription = category.name,
                contentScale = ContentScale.Fit,
                // 58% of tile gives large, clear images with breathing room around edges
                modifier = Modifier.fillMaxSize(0.9f)
            )
        }
        Spacer(Modifier.height(metrics.tileToLabelGap))
        Text(
            text = category.name,
            color = ColorTextStrongLight,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = metrics.labelSize,
                lineHeight = metrics.labelSize * 1.2f,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberSearchMetrics(maxWidth: Dp, maxHeight: Dp): SearchMetrics {
    return remember(maxWidth, maxHeight) {
        val horizontalPadding = 24.dp
        val columnGap = 18.dp
        val tileWidth = (maxWidth - horizontalPadding * 2 - columnGap * 3) / 4

        SearchMetrics(
            horizontalPadding = horizontalPadding,
            searchBarTopMargin = (maxHeight * 0.095f).coerceIn(40.dp, 50.dp),
            searchBarHeight = (maxWidth * 0.178f).coerceIn(50.dp, 60.dp),
            searchBarRadius = (maxWidth * 0.056f).coerceIn(18.dp, 24.dp),
            searchIconSize = (maxWidth * 0.077f).coerceIn(20.dp, 24.dp),
            searchTextSize = (maxWidth.value * 0.042f).coerceIn(14f, 18f).sp,
            categoryTitleTopMargin = (maxHeight * 0.105f).coerceIn(30.dp, 40.dp),
            categoryTitleSize = (maxWidth.value * 0.065f).coerceIn(22f, 25f).sp,
            categoryGridTopGap = (maxWidth * 0.095f).coerceIn(20.dp, 25.dp),
            tileCorner = 20.dp,
            labelSize = (tileWidth.value * 0.185f).coerceIn(11f, 14f).sp,
            tileToLabelGap = 10.dp,
            rowVerticalGap = 32.dp,
            columnGap = columnGap,
            focusedBackButtonSize = (maxWidth * 0.108f).coerceIn(42.dp, 56.dp),
            focusedSearchGap = (maxWidth * 0.038f).coerceIn(12.dp, 18.dp),
            focusedTrendingTopGap = (maxHeight * 0.085f).coerceIn(58.dp, 82.dp),
            trendingTitleSize = (maxWidth.value * 0.05f).coerceIn(18f, 22f).sp,
            trendingItemSize = (maxWidth.value * 0.065f).coerceIn(24f, 32f).sp,
            trendingItemGap = (maxHeight * 0.023f).coerceIn(16.dp, 22.dp),
            resultCoverWidth = (maxWidth * 0.215f).coerceIn(78.dp, 94.dp),
            resultCoverHeight = (maxWidth * 0.29f).coerceIn(108.dp, 126.dp),
            resultBookGap = (maxWidth * 0.055f).coerceIn(20.dp, 28.dp),
            resultRowGap = (maxHeight * 0.028f).coerceIn(22.dp, 30.dp),
            resultTitleSize = (maxWidth.value * 0.053f).coerceIn(20f, 24f).sp,
            resultAuthorSize = (maxWidth.value * 0.037f).coerceIn(14f, 17f).sp,
            resultChipHeight = (maxWidth * 0.04f).coerceIn(20.dp, 24.dp),
            resultChipTextSize = (maxWidth.value * 0.027f).coerceIn(10f, 12f).sp,
            bottomNavSpace = (maxHeight * 0.14f).coerceIn(112.dp, 150.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    IBookTheme {
        SearchScreen()
    }
}
