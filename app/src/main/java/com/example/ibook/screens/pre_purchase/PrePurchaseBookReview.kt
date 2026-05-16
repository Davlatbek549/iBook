package com.example.ibook.screens.pre_purchase

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ShoppingBag
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.ibook.ui.theme.ColorCategoryFantasy
import com.example.ibook.ui.theme.ColorTertiary
import com.example.ibook.ui.theme.IBookTheme
import kotlin.math.min

data class PrePurchaseBookUiState(
    val title: String,
    val author: String,
    val reads: String,
    val rating: String,
    val reviews: String,
    val overview: String,
    val price: String,
    val currentTag: Int = 1,
    val totalTags: Int = 4
)

private data class PrePurchaseMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val topButtonSize: Dp,
    val topIconSize: Dp,
    val coverTopSpacing: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val coverCorner: Dp,
    val titleTopSpacing: Dp,
    val titleFontSize: TextUnit,
    val authorFontSize: TextUnit,
    val authorTopSpacing: Dp,
    val statsTopSpacing: Dp,
    val statsHeight: Dp,
    val statsCorner: Dp,
    val statLabelFontSize: TextUnit,
    val statValueFontSize: TextUnit,
    val statsSheetOverlap: Dp,
    val sheetCorner: Dp,
    val sheetHorizontalPadding: Dp,
    val sheetTopPadding: Dp,
    val sectionTitleFontSize: TextUnit,
    val bodyFontSize: TextUnit,
    val bodyLineHeight: TextUnit,
    val chipTextSize: TextUnit,
    val bottomWhiteHeight: Dp,
    val actionPanelHeight: Dp,
    val actionPanelCorner: Dp,
    val actionPanelWidth: Dp,
    val actionIconSize: Dp,
    val actionTextSize: TextUnit,
    val heartIconSize: Dp
)

@Composable
fun PrePurchaseScreen(
    modifier: Modifier = Modifier,
    showTagsBookAction: Boolean = false,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onViewSampleClick: () -> Unit = {},
    onPurchaseClick: () -> Unit = {},
    onTagsBookClick: () -> Unit = {}
) {
    val book = PrePurchaseBookUiState(
        title = stringResource(R.string.reading_book_title),
        author = stringResource(R.string.reading_book_author),
        reads = stringResource(R.string.book_review_reads_value),
        rating = stringResource(R.string.book_review_rating_value),
        reviews = stringResource(R.string.book_review_review_value),
        overview = stringResource(R.string.book_review_overview_body),
        price = stringResource(R.string.pre_purchase_price)
    )

    PrePurchaseScreen(
        book = book,
        modifier = modifier,
        showTagsBookAction = showTagsBookAction,
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onFavoriteClick = onFavoriteClick,
        onViewSampleClick = onViewSampleClick,
        onPurchaseClick = onPurchaseClick,
        onTagsBookClick = onTagsBookClick
    )
}

@Composable
fun PrePurchaseScreen(
    book: PrePurchaseBookUiState,
    modifier: Modifier = Modifier,
    showTagsBookAction: Boolean = false,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onViewSampleClick: () -> Unit = {},
    onPurchaseClick: () -> Unit = {},
    onTagsBookClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberPrePurchaseMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Box(modifier = Modifier.fillMaxSize()) {
            PrePurchaseBackground()

            TopActions(
                metrics = metrics,
                onBackClick = onBackClick,
                onShareClick = onShareClick,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(3f)
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(metrics.coverTopSpacing))

                MurderBoardCover(
                    metrics = metrics,
                    modifier = Modifier.zIndex(1f)
                )

                Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

                Text(
                    text = book.title,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = metrics.titleFontSize,
                        lineHeight = metrics.titleFontSize * 1.22f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.authorTopSpacing))

                Text(
                    text = book.author,
                    color = Color.White.copy(alpha = 0.82f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.authorFontSize,
                        lineHeight = metrics.authorFontSize * 1.25f,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.statsTopSpacing))

                StatsPanel(
                    book = book,
                    metrics = metrics,
                    modifier = Modifier.zIndex(0f)
                )

                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .zIndex(1f)
                ) {
                    BookInfoSheet(
                        book = book,
                        metrics = metrics,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .requiredHeight(maxHeight + metrics.statsSheetOverlap)
                    )
                }
            }

            BottomPrePurchaseBar(
                book = book,
                metrics = metrics,
                showTagsBookAction = showTagsBookAction,
                onFavoriteClick = onFavoriteClick,
                onViewSampleClick = onViewSampleClick,
                onPurchaseClick = onPurchaseClick,
                onTagsBookClick = onTagsBookClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun PrePurchaseBookReview(
    modifier: Modifier = Modifier,
    showTagsBookAction: Boolean = false,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onViewSampleClick: () -> Unit = {},
    onPurchaseClick: () -> Unit = {},
    onTagsBookClick: () -> Unit = {}
) {
    PrePurchaseScreen(
        modifier = modifier,
        showTagsBookAction = showTagsBookAction,
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onFavoriteClick = onFavoriteClick,
        onViewSampleClick = onViewSampleClick,
        onPurchaseClick = onPurchaseClick,
        onTagsBookClick = onTagsBookClick
    )
}

@Composable
private fun PrePurchaseBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF123642))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.18f
                    scaleY = 1.18f
                }
                .blur(42.dp)
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFA8464D),
                        Color(0xFF0D7C86),
                        Color(0xFF142238)
                    )
                )
            )
            drawCircle(
                color = Color(0xFFB95A4F).copy(alpha = 0.62f),
                radius = size.width * 0.48f,
                center = Offset(size.width * 0.54f, size.height * 0.18f)
            )
            drawCircle(
                color = Color(0xFF9EC9C6).copy(alpha = 0.46f),
                radius = size.width * 0.42f,
                center = Offset(size.width * 0.27f, size.height * 0.38f)
            )
            drawCircle(
                color = Color(0xFF071B26).copy(alpha = 0.58f),
                radius = size.width * 0.42f,
                center = Offset(size.width * 0.22f, size.height * 0.82f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.27f))
        )
    }
}

@Composable
private fun TopActions(
    metrics: PrePurchaseMetrics,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = metrics.horizontalPadding,
                top = metrics.topSpacing,
                end = metrics.horizontalPadding
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleButton(
            onClick = onBackClick,
            size = metrics.topButtonSize
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(R.string.cd_back),
                tint = Color.White,
                modifier = Modifier.size(metrics.topIconSize * 0.82f)
            )
        }

        CircleButton(
            onClick = onShareClick,
            size = metrics.topButtonSize
        ) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = stringResource(R.string.cd_share),
                tint = Color.White,
                modifier = Modifier.size(metrics.topIconSize)
            )
        }
    }
}

@Composable
private fun MurderBoardCover(
    metrics: PrePurchaseMetrics,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = metrics.coverWidth, height = metrics.coverHeight)
            .shadow(
                elevation = 22.dp,
                shape = RoundedCornerShape(metrics.coverCorner),
                clip = false
            )
            .clip(RoundedCornerShape(metrics.coverCorner))
            .background(Color(0xFF07131B))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCoverArt()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = metrics.coverWidth * 0.06f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(metrics.coverHeight * 0.02f))

            Text(
                text = stringResource(R.string.pre_purchase_cover_title),
                color = Color(0xFFFF6A62),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = metrics.coverWidth.value.times(0.155f).sp,
                    lineHeight = metrics.coverWidth.value.times(0.15f).sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.pre_purchase_cover_author),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = metrics.coverWidth.value.times(0.125f).sp,
                    lineHeight = metrics.coverWidth.value.times(0.145f).sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.coverHeight * 0.015f))
        }
    }
}

private fun DrawScope.drawCoverArt() {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF161824),
                Color(0xFF14A0A7),
                Color(0xFF0A1B25)
            )
        )
    )

    drawCircle(
        color = Color(0xFFE8F6F3).copy(alpha = 0.62f),
        radius = size.minDimension * 0.24f,
        center = Offset(size.width * 0.17f, size.height * 0.47f)
    )

    drawRect(
        color = Color(0xFF121C28).copy(alpha = 0.64f),
        topLeft = Offset(0f, size.height * 0.51f),
        size = Size(size.width, size.height * 0.28f)
    )

    drawCityBlock(x = 0.28f, width = 0.12f, height = 0.22f)
    drawCityBlock(x = 0.42f, width = 0.1f, height = 0.28f)
    drawCityBlock(x = 0.54f, width = 0.16f, height = 0.18f)
    drawCityBlock(x = 0.73f, width = 0.1f, height = 0.2f)

    val tower = Path().apply {
        moveTo(size.width * 0.49f, size.height * 0.26f)
        lineTo(size.width * 0.54f, size.height * 0.26f)
        lineTo(size.width * 0.58f, size.height * 0.62f)
        lineTo(size.width * 0.45f, size.height * 0.62f)
        close()
    }
    drawPath(tower, Color(0xFF45331D))
    drawRoundRect(
        color = Color(0xFFFFBC57),
        topLeft = Offset(size.width * 0.47f, size.height * 0.35f),
        size = Size(size.width * 0.09f, size.height * 0.08f),
        cornerRadius = CornerRadius(4f, 4f)
    )

    drawCircle(
        color = Color(0xFF15242C),
        radius = size.width * 0.045f,
        center = Offset(size.width * 0.27f, size.height * 0.61f)
    )
    drawRoundRect(
        color = Color(0xFF15242C),
        topLeft = Offset(size.width * 0.245f, size.height * 0.64f),
        size = Size(size.width * 0.05f, size.height * 0.17f),
        cornerRadius = CornerRadius(8f, 8f)
    )

    drawRect(
        color = Color(0xFFEAEEF0).copy(alpha = 0.68f),
        topLeft = Offset(0f, size.height * 0.77f),
        size = Size(size.width, size.height * 0.05f)
    )

    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.7f),
                Color.Transparent
            )
        ),
        topLeft = Offset(0f, 0f),
        size = Size(size.width * 0.12f, size.height)
    )

    drawRect(
        color = Color.Black.copy(alpha = 0.34f),
        topLeft = Offset(size.width * 0.035f, 0f),
        size = Size(size.width * 0.022f, size.height)
    )
}

private fun DrawScope.drawCityBlock(
    x: Float,
    width: Float,
    height: Float
) {
    val left = size.width * x
    val top = size.height * (0.62f - height)
    val blockWidth = size.width * width
    val blockHeight = size.height * height

    drawRect(
        color = Color(0xFF1B2730),
        topLeft = Offset(left, top),
        size = Size(blockWidth, blockHeight)
    )

    repeat(4) { row ->
        repeat(3) { col ->
            drawRect(
                color = Color(0xFFFFB74E).copy(alpha = 0.8f),
                topLeft = Offset(
                    left + blockWidth * (0.18f + col * 0.25f),
                    top + blockHeight * (0.12f + row * 0.19f)
                ),
                size = Size(blockWidth * 0.08f, blockHeight * 0.05f)
            )
        }
    }
}

@Composable
private fun StatsPanel(
    book: PrePurchaseBookUiState,
    metrics: PrePurchaseMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.statsHeight)
            .clip(
                RoundedCornerShape(
                    topStart = metrics.statsCorner,
                    topEnd = metrics.statsCorner
                )
            )
            .background(Color.White.copy(alpha = 0.24f))
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.24f),
                shape = RoundedCornerShape(
                    topStart = metrics.statsCorner,
                    topEnd = metrics.statsCorner
                )
            )
            .padding(horizontal = metrics.horizontalPadding),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            label = stringResource(R.string.book_review_reads),
            value = book.reads,
            metrics = metrics,
            modifier = Modifier.weight(1f)
        )
        StatItem(
            label = stringResource(R.string.book_review_rating),
            value = book.rating,
            metrics = metrics,
            modifier = Modifier.weight(1f)
        )
        StatItem(
            label = stringResource(R.string.book_review_review),
            value = book.reviews,
            metrics = metrics,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    metrics: PrePurchaseMetrics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.offset(y = (-15).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.statLabelFontSize,
                lineHeight = metrics.statLabelFontSize * 1.25f,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.statValueFontSize,
                lineHeight = metrics.statValueFontSize * 1.15f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun BookInfoSheet(
    book: PrePurchaseBookUiState,
    metrics: PrePurchaseMetrics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = metrics.sheetCorner,
                    topEnd = metrics.sheetCorner
                )
            )
            .background(Color(0xFFEFF3FA))
            .verticalScroll(rememberScrollState())
            .padding(
                start = metrics.sheetHorizontalPadding,
                top = metrics.sheetTopPadding,
                end = metrics.sheetHorizontalPadding,
                bottom = metrics.actionPanelHeight + 58.dp
            )
    ) {
        Text(
            text = stringResource(R.string.book_review_categories),
            color = Color(0xFF3E4147),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.sectionTitleFontSize,
                lineHeight = metrics.sectionTitleFontSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CategoryChip(
                text = stringResource(R.string.category_horror),
                color = ColorTertiary,
                metrics = metrics
            )
            CategoryChip(
                text = stringResource(R.string.category_fantasy),
                color = ColorCategoryFantasy,
                metrics = metrics
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.book_review_overview),
            color = Color(0xFF3E4147),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.sectionTitleFontSize,
                lineHeight = metrics.sectionTitleFontSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = book.overview,
            color = Color(0xFF8D9096),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = metrics.bodyFontSize,
                lineHeight = metrics.bodyLineHeight,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun CategoryChip(
    text: String,
    color: Color,
    metrics: PrePurchaseMetrics
) {
    Text(
        text = text,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 14.dp, vertical = 5.dp),
        style = MaterialTheme.typography.labelLarge.copy(
            fontSize = metrics.chipTextSize,
            lineHeight = metrics.chipTextSize * 1.15f,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        )
    )
}

@Composable
private fun BottomPrePurchaseBar(
    book: PrePurchaseBookUiState,
    metrics: PrePurchaseMetrics,
    showTagsBookAction: Boolean,
    onFavoriteClick: () -> Unit,
    onViewSampleClick: () -> Unit,
    onPurchaseClick: () -> Unit,
    onTagsBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actionPanelHeight = if (showTagsBookAction) {
        metrics.actionPanelHeight
    } else {
        (metrics.actionPanelHeight * 0.72f).coerceIn(96.dp, 122.dp)
    }
    val actionRowHeight = if (actionPanelHeight > 140.dp) 46.dp else 42.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(actionPanelHeight + metrics.bottomWhiteHeight)
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(metrics.bottomWhiteHeight)
                .clip(RoundedCornerShape(topStart = metrics.actionPanelCorner))
                .background(Color.White)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = metrics.sheetHorizontalPadding + 2.dp, bottom = 12.dp)
                .size(metrics.heartIconSize * 1.9f)
                .clickable(onClick = onFavoriteClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = stringResource(R.string.cd_favorite),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(metrics.heartIconSize)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(metrics.actionPanelWidth)
                .height(actionPanelHeight)
                .clip(RoundedCornerShape(topStart = metrics.actionPanelCorner))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ActionRow(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_book_open),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(metrics.actionIconSize)
                    )
                },
                text = stringResource(R.string.pre_purchase_view_sample),
                metrics = metrics,
                rowHeight = actionRowHeight,
                onClick = onViewSampleClick
            )

            ActionDivider()

            ActionRow(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_purchase_book),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(metrics.actionIconSize)
                    )
                },
                text = stringResource(R.string.pre_purchase_purchase, book.price),
                metrics = metrics,
                rowHeight = actionRowHeight,
                onClick = onPurchaseClick
            )

            if (showTagsBookAction) {
                ActionDivider()

                ActionRow(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_premium),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(metrics.actionIconSize)
                        )
                    },
                    text = stringResource(
                        R.string.pre_purchase_tags_book,
                        book.currentTag,
                        book.totalTags
                    ),
                    metrics = metrics,
                    rowHeight = actionRowHeight,
                    onClick = onTagsBookClick
                )
            }
        }
    }
}

@Composable
private fun ActionRow(
    icon: @Composable () -> Unit,
    text: String,
    metrics: PrePurchaseMetrics,
    rowHeight: Dp,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(rowHeight)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(metrics.actionIconSize + 2.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.actionTextSize,
                lineHeight = metrics.actionTextSize * 1.18f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun ActionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.36f))
    )
}

@Composable
private fun CircleButton(
    onClick: () -> Unit,
    size: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.18f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun rememberPrePurchaseMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): PrePurchaseMetrics {
    val widthRatio = dimensionRatio(
        value = maxWidth,
        base = 390.dp,
        min = 0.86f,
        max = 1.18f
    )
    val heightRatio = dimensionRatio(
        value = maxHeight,
        base = 844.dp,
        min = 0.82f,
        max = 1.12f
    )
    val compactness = min(widthRatio, heightRatio)
    val isCompactHeight = maxHeight.value.isFinite() && maxHeight < 760.dp
    val spacingScale = if (isCompactHeight) 0.78f else 1f
    val coverWidth = (196.dp * compactness).coerceIn(168.dp, 206.dp)
    val actionPanelHeight = (if (isCompactHeight) 124.dp else 148.dp) * compactness

    return remember(maxWidth, maxHeight) {
        PrePurchaseMetrics(
            horizontalPadding = (29.dp * widthRatio).coerceIn(22.dp, 36.dp),
            topSpacing = (46.dp * spacingScale).coerceIn(40.dp, 52.dp),
            topButtonSize = (38.dp * compactness).coerceIn(34.dp, 44.dp),
            topIconSize = (23.dp * compactness).coerceIn(20.dp, 27.dp),
            coverTopSpacing = (58.dp * spacingScale).coerceIn(48.dp, 66.dp),
            coverWidth = coverWidth,
            coverHeight = coverWidth * 1.20f,
            coverCorner = (15.dp * compactness).coerceIn(12.dp, 18.dp),
            titleTopSpacing = (20.dp * spacingScale).coerceIn(14.dp, 22.dp),
            titleFontSize = (20f * compactness).coerceIn(18f, 24f).sp,
            authorFontSize = (14.5f * compactness).coerceIn(13f, 17f).sp,
            authorTopSpacing = (10.dp * spacingScale).coerceIn(6.dp, 12.dp),
            statsTopSpacing = (14.dp * spacingScale).coerceIn(10.dp, 18.dp),
            statsHeight = (110.dp * compactness).coerceIn(96.dp, 124.dp),
            statsCorner = (40.dp * compactness).coerceIn(32.dp, 48.dp),
            statLabelFontSize = (13.5f * compactness).coerceIn(12f, 16f).sp,
            statValueFontSize = (18f * compactness).coerceIn(16f, 21f).sp,
            statsSheetOverlap = 72.dp,
            sheetCorner = (43.dp * compactness).coerceIn(34.dp, 52.dp),
            sheetHorizontalPadding = (29.dp * widthRatio).coerceIn(24.dp, 34.dp),
            sheetTopPadding = (28.dp * spacingScale).coerceIn(22.dp, 34.dp),
            sectionTitleFontSize = (19.5f * compactness).coerceIn(17.5f, 22f).sp,
            bodyFontSize = (14.5f * compactness).coerceIn(13f, 16.5f).sp,
            bodyLineHeight = (24f * compactness).coerceIn(21f, 27f).sp,
            chipTextSize = (12.5f * compactness).coerceIn(11f, 14f).sp,
            bottomWhiteHeight = (78.dp * compactness).coerceIn(68.dp, 92.dp),
            actionPanelHeight = actionPanelHeight.coerceIn(118.dp, 168.dp),
            actionPanelCorner = (39.dp * compactness).coerceIn(30.dp, 46.dp),
            actionPanelWidth = (maxWidth * 0.75f).coerceIn(226.dp, 310.dp),
            actionIconSize = (20.dp * compactness).coerceIn(18.dp, 24.dp),
            actionTextSize = (15.5f * compactness).coerceIn(14f, 18.5f).sp,
            heartIconSize = (25.dp * compactness).coerceIn(22.dp, 30.dp)
        )
    }
}

private fun dimensionRatio(
    value: Dp,
    base: Dp,
    min: Float,
    max: Float
): Float {
    if (!value.value.isFinite() || !base.value.isFinite() || base.value == 0f) {
        return 1f
    }

    return (value / base).coerceIn(min, max)
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PrePurchaseScreenPreview() {
    IBookTheme(darkTheme = true) {
        PrePurchaseScreen(showTagsBookAction = false)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PrePurchaseScreenTaggedPreview() {
    IBookTheme(darkTheme = true) {
        PrePurchaseScreen(showTagsBookAction = true)
    }
}
