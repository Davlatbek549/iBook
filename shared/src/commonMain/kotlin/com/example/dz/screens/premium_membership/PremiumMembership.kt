package com.example.dz.screens.premium_membership

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.screens.reading.ReadingStopwatchArt
import com.example.dz.screens.reading.ReadingTrophyArt
import com.example.dz.theme.ColorCategoryFantasy
import com.example.dz.theme.ColorPrimary
import com.example.dz.theme.ColorTertiary
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class PremiumMembershipMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val topIconButtonSize: Dp,
    val topIconSize: Dp,
    val profileTopSpacing: Dp,
    val avatarSize: Dp,
    val badgeSize: Dp,
    val usernameSize: TextUnit,
    val statValueSize: TextUnit,
    val statLabelSize: TextUnit,
    val sectionTopSpacing: Dp,
    val sectionTitleSize: TextUnit,
    val goalTopSpacing: Dp,
    val goalCardHeight: Dp,
    val goalCardCorner: Dp,
    val goalTitleSize: TextUnit,
    val goalImageSize: Dp,
    val goalValueSize: TextUnit,
    val goalSuffixSize: TextUnit,
    val collectionTopSpacing: Dp,
    val collectionCardCorner: Dp,
    val collectionHorizontalPadding: Dp,
    val collectionVerticalPadding: Dp,
    val bookCoverWidth: Dp,
    val bookCoverHeight: Dp,
    val bookTitleSize: TextUnit,
    val bookAuthorSize: TextUnit,
    val bookMetaSize: TextUnit,
    val chipHeight: Dp,
    val chipTextSize: TextUnit,
    val bottomPanelHeight: Dp,
    val bottomSpacing: Dp
)

private data class PremiumCollectionBook(
    val title: StringResource,
    val author: StringResource,
    val views: StringResource,
    val firstGenre: StringResource,
    val secondGenre: StringResource,
    val cover: DrawableResource
)

private val premiumCollectionBooks = listOf(
    PremiumCollectionBook(
        title = Res.string.premium_book_olive_again,
        author = Res.string.premium_book_olive_author,
        views = Res.string.premium_views_1320,
        firstGenre = Res.string.premium_genre_horror,
        secondGenre = Res.string.premium_genre_fantasy,
        cover = Res.drawable.olive_again_book
    ),
    PremiumCollectionBook(
        title = Res.string.premium_book_hills_gold,
        author = Res.string.premium_book_hills_author,
        views = Res.string.premium_views_1320,
        firstGenre = Res.string.premium_genre_horror,
        secondGenre = Res.string.premium_genre_fantasy,
        cover = Res.drawable.book_cover_4
    )
)

@Composable
fun PremiumMembership(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    PremiumMembershipScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onNotificationClick = onNotificationClick,
        onMenuClick = onMenuClick
    )
}

@Composable
fun PremiumMembershipScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(ColorPrimary)
    ) {
        val metrics = rememberPremiumMembershipMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = metrics.horizontalPadding)
                .padding(top = metrics.topSpacing, bottom = metrics.bottomSpacing)
        ) {
            PremiumTopBar(
                metrics = metrics,
                onBackClick = onBackClick,
                onNotificationClick = onNotificationClick,
                onMenuClick = onMenuClick
            )

            Spacer(modifier = Modifier.height(metrics.profileTopSpacing))

            PremiumProfileHeader(metrics = metrics)

            Spacer(modifier = Modifier.height(metrics.sectionTopSpacing))

            PremiumSectionTitle(
                text = stringResource(Res.string.premium_reading_goals),
                metrics = metrics
            )

            Spacer(modifier = Modifier.height(metrics.goalTopSpacing))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(metrics.horizontalPadding)
            ) {
                PremiumReadingGoalCard(
                    title = stringResource(Res.string.premium_todays_reading),
                    value = stringResource(Res.string.premium_todays_reading_minutes),
                    suffix = stringResource(Res.string.premium_todays_reading_target),
                    metrics = metrics,
                    modifier = Modifier.weight(1f),
                    art = {
                        ReadingStopwatchArt(modifier = Modifier.size(metrics.goalImageSize))
                    }
                )

                PremiumReadingGoalCard(
                    title = stringResource(Res.string.premium_longest_reading_streak),
                    value = stringResource(Res.string.premium_longest_reading_days),
                    suffix = stringResource(Res.string.premium_days),
                    metrics = metrics,
                    modifier = Modifier.weight(1f),
                    highlightValue = true,
                    art = {
                        ReadingTrophyArt(modifier = Modifier.size(metrics.goalImageSize))
                    }
                )
            }

            Spacer(modifier = Modifier.height(metrics.collectionTopSpacing))

            PremiumSectionTitle(
                text = stringResource(Res.string.premium_collections),
                metrics = metrics
            )

            Spacer(modifier = Modifier.height(22.dp))

            PremiumCollectionsSheet(
                books = premiumCollectionBooks,
                metrics = metrics
            )
        }

        PremiumBottomWhitePanel(
            corner = metrics.collectionCardCorner,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(metrics.bottomPanelHeight)
        )
    }
}

@Composable
private fun PremiumTopBar(
    metrics: PremiumMembershipMetrics,
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PremiumCircleIconButton(
            size = metrics.topIconButtonSize,
            onClick = onBackClick
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = stringResource(Res.string.premium_back),
                tint = Color.White,
                modifier = Modifier.size(metrics.topIconSize)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            PremiumCircleIconButton(
                size = metrics.topIconButtonSize,
                onClick = onNotificationClick
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_notification),
                        contentDescription = stringResource(Res.string.premium_notifications),
                        tint = Color.White,
                        modifier = Modifier.size(metrics.topIconSize)
                    )
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .offset(x = 2.dp, y = (-1).dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF8A21))
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                }
            }

            PremiumCircleIconButton(
                size = metrics.topIconButtonSize,
                onClick = onMenuClick
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_grid_view),
                    contentDescription = stringResource(Res.string.premium_menu),
                    tint = Color.White,
                    modifier = Modifier.size(metrics.topIconSize)
                )
            }
        }
    }
}

@Composable
private fun PremiumCircleIconButton(
    size: Dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.12f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun PremiumProfileHeader(metrics: PremiumMembershipMetrics) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PremiumAvatar(metrics = metrics)

        Spacer(modifier = Modifier.width(26.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.premium_username),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = metrics.usernameSize,
                    lineHeight = metrics.usernameSize * 1.08f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                PremiumProfileStat(
                    value = stringResource(Res.string.premium_total_read_value),
                    label = stringResource(Res.string.premium_total_read),
                    metrics = metrics
                )
                PremiumProfileStat(
                    value = stringResource(Res.string.premium_total_reading_value),
                    label = stringResource(Res.string.premium_total_reading),
                    unit = stringResource(Res.string.premium_total_reading_unit),
                    metrics = metrics
                )
            }
        }
    }
}

@Composable
private fun PremiumAvatar(metrics: PremiumMembershipMetrics) {
    Box(contentAlignment = Alignment.BottomCenter) {
        Image(
            painter = painterResource(Res.drawable.premium_profile),
            contentDescription = stringResource(Res.string.premium_profile_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(metrics.avatarSize)
                .clip(CircleShape)
                .border(4.dp, Color.White, CircleShape)
        )

        Box(
            modifier = Modifier
                .size(metrics.badgeSize)
                .offset(y = metrics.badgeSize * 0.38f)
                .clip(CircleShape)
                .background(ColorPrimary)
                .border(4.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_premium),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(metrics.badgeSize * 0.48f)
            )
        }
    }
}

@Composable
private fun PremiumProfileStat(
    value: String,
    label: String,
    metrics: PremiumMembershipMetrics,
    unit: String? = null
) {
    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = metrics.statValueSize,
                    lineHeight = metrics.statValueSize,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
            if (unit != null) {
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = unit,
                    color = Color.White.copy(alpha = 0.72f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = metrics.statLabelSize,
                        lineHeight = metrics.statLabelSize,
                        letterSpacing = 0.sp
                    ),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.72f),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = metrics.statLabelSize,
                lineHeight = metrics.statLabelSize * 1.1f,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun PremiumSectionTitle(
    text: String,
    metrics: PremiumMembershipMetrics
) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = metrics.sectionTitleSize,
            lineHeight = metrics.sectionTitleSize * 1.16f,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        )
    )
}

@Composable
private fun PremiumReadingGoalCard(
    title: String,
    value: String,
    suffix: String,
    metrics: PremiumMembershipMetrics,
    modifier: Modifier = Modifier,
    highlightValue: Boolean = false,
    art: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .height(metrics.goalCardHeight)
            .clip(RoundedCornerShape(metrics.goalCardCorner))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Text(
            text = title,
            color = Color(0xFF64646D),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = metrics.goalTitleSize,
                lineHeight = metrics.goalTitleSize * 1.18f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

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
                color = if (highlightValue) ColorPrimary else Color(0xFF64646D),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = metrics.goalValueSize,
                    lineHeight = metrics.goalValueSize,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = suffix,
                color = if (highlightValue) ColorPrimary else Color(0xFF64646D),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = metrics.goalSuffixSize,
                    lineHeight = metrics.goalSuffixSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
private fun PremiumBottomWhitePanel(
    corner: Dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cornerPx = corner.toPx().coerceAtMost(size.height * 0.78f)
        val topBaseline = size.height * 0.54f
        val leftStartY = (topBaseline + cornerPx).coerceAtMost(size.height)
        val rightCurveStartX = (size.width - cornerPx * 2.8f).coerceAtLeast(size.width * 0.58f)

        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(0f, leftStartY)
            cubicTo(
                0f,
                topBaseline + cornerPx * 0.45f,
                cornerPx * 0.45f,
                topBaseline,
                cornerPx,
                topBaseline
            )
            lineTo(rightCurveStartX, topBaseline)
            cubicTo(
                size.width - cornerPx * 1f,
                topBaseline,
                size.width - cornerPx * 0.20f,
                0.7f,
                size.width,
                0.7f
            )
            lineTo(size.width, size.height)
            close()
        }

        drawPath(path = path, color = Color.White)
    }
}

@Composable
private fun PremiumCollectionsSheet(
    books: List<PremiumCollectionBook>,
    metrics: PremiumMembershipMetrics
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .padding(horizontal = 38.dp)
                .offset(y = (-22).dp)
                .clip(RoundedCornerShape(topStart = metrics.collectionCardCorner, topEnd = metrics.collectionCardCorner))
                .background(Color.White.copy(alpha = 0.32f))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .padding(horizontal = 20.dp)
                .offset(y = (-12).dp)
                .clip(RoundedCornerShape(topStart = metrics.collectionCardCorner, topEnd = metrics.collectionCardCorner))
                .background(Color.White.copy(alpha = 0.52f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = metrics.collectionCardCorner, topEnd = metrics.collectionCardCorner))
                .background(Color.White)
                .padding(
                    horizontal = metrics.collectionHorizontalPadding,
                    vertical = metrics.collectionVerticalPadding
                )
        ) {
            books.forEachIndexed { index, book ->
                PremiumCollectionBookRow(book = book, metrics = metrics)

                if (index != books.lastIndex) {
                    Spacer(modifier = Modifier.height(22.dp))
                    HorizontalDivider(color = Color.Transparent)
                    Spacer(modifier = Modifier.height(22.dp))
                }
            }
        }
    }
}

@Composable
private fun PremiumCollectionBookRow(
    book: PremiumCollectionBook,
    metrics: PremiumMembershipMetrics
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(book.cover),
            contentDescription = stringResource(book.title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(metrics.bookCoverWidth)
                .height(metrics.bookCoverHeight)
                .clip(RoundedCornerShape(7.dp))
        )

        Spacer(modifier = Modifier.width(22.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(book.title),
                color = Color(0xFF696A70),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = metrics.bookTitleSize,
                    lineHeight = metrics.bookTitleSize * 1.12f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(book.author),
                color = Color(0xFF696A70),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = metrics.bookAuthorSize,
                    lineHeight = metrics.bookAuthorSize * 1.2f,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.ic_visibility_on),
                    contentDescription = stringResource(Res.string.premium_views),
                    tint = ColorPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(book.views),
                    color = Color(0xFFB4B4B8),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.bookMetaSize,
                        lineHeight = metrics.bookMetaSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PremiumGenreTag(
                    text = stringResource(book.firstGenre),
                    color = ColorTertiary,
                    metrics = metrics
                )
                PremiumGenreTag(
                    text = stringResource(book.secondGenre),
                    color = ColorCategoryFantasy,
                    metrics = metrics
                )
            }
        }
    }
}

@Composable
private fun PremiumGenreTag(
    text: String,
    color: Color,
    metrics: PremiumMembershipMetrics
) {
    Box(
        modifier = Modifier
            .height(metrics.chipHeight)
            .clip(CircleShape)
            .background(color)
            .padding(horizontal = 20.dp),
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
private fun rememberPremiumMembershipMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): PremiumMembershipMetrics = remember(maxWidth, maxHeight) {
    val widthScale = (maxWidth / 390.dp).coerceIn(0.86f, 1.18f)
    val heightScale = (maxHeight / 844.dp).coerceIn(0.82f, 1.14f)
    val compactness = minOf(widthScale, heightScale)

    PremiumMembershipMetrics(
        horizontalPadding = (29.dp * widthScale).coerceIn(24.dp, 36.dp),
        topSpacing = (42.dp * heightScale).coerceIn(32.dp, 52.dp),
        topIconButtonSize = (40.dp * compactness).coerceIn(36.dp, 48.dp),
        topIconSize = (23.dp * compactness).coerceIn(15.dp, 20.dp),
        profileTopSpacing = (26.dp * heightScale).coerceIn(18.dp, 34.dp),
        avatarSize = (104.dp * compactness).coerceIn(92.dp, 118.dp),
        badgeSize = (32.dp * compactness).coerceIn(28.dp, 38.dp),
        usernameSize = (24f * widthScale).coerceIn(22f, 29f).sp,
        statValueSize = (25f * widthScale).coerceIn(23f, 31f).sp,
        statLabelSize = (15f * widthScale).coerceIn(13f, 18f).sp,
        sectionTopSpacing = (40.dp * heightScale).coerceIn(28.dp, 46.dp),
        sectionTitleSize = (20f * widthScale).coerceIn(18f, 24f).sp,
        goalTopSpacing = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        goalCardHeight = (202.dp * compactness).coerceIn(176.dp, 220.dp),
        goalCardCorner = (27.dp * compactness).coerceIn(22.dp, 32.dp),
        goalTitleSize = (15.5f * widthScale).coerceIn(14f, 18f).sp,
        goalImageSize = (112.dp * compactness).coerceIn(92.dp, 126.dp),
        goalValueSize = (24f * widthScale).coerceIn(21f, 28f).sp,
        goalSuffixSize = (9.5f * widthScale).coerceIn(8.5f, 11f).sp,
        collectionTopSpacing = (24.dp * heightScale).coerceIn(18.dp, 32.dp),
        collectionCardCorner = (38.dp * compactness).coerceIn(30.dp, 46.dp),
        collectionHorizontalPadding = (22.dp * widthScale).coerceIn(18.dp, 28.dp),
        collectionVerticalPadding = (30.dp * heightScale).coerceIn(24.dp, 36.dp),
        bookCoverWidth = (82.dp * widthScale).coerceIn(72.dp, 96.dp),
        bookCoverHeight = (114.dp * widthScale).coerceIn(100.dp, 132.dp),
        bookTitleSize = (17f * widthScale).coerceIn(15f, 22f).sp,
        bookAuthorSize = (14f * widthScale).coerceIn(12f, 17f).sp,
        bookMetaSize = (15f * widthScale).coerceIn(13f, 18f).sp,
        chipHeight = (23.dp * compactness).coerceIn(20.dp, 28.dp),
        chipTextSize = (11f * widthScale).coerceIn(9.5f, 13f).sp,
        bottomPanelHeight = (46.dp * heightScale).coerceIn(38.dp, 56.dp),
        bottomSpacing = (62.dp * heightScale).coerceIn(52.dp, 76.dp)
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PremiumMembershipScreenPreview() {
    DZTheme {
        PremiumMembershipScreen()
    }
}
