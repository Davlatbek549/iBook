package com.example.dz.presentation.book.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkButton
import com.example.dz.designsystem.components.ink.InkChip
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkProgressBar
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.img_neil_alvin
import dz.shared.generated.resources.profile_2
import dz.shared.generated.resources.reviews_critical
import dz.shared.generated.resources.reviews_helpful
import dz.shared.generated.resources.reviews_most_helpful
import dz.shared.generated.resources.reviews_recent
import dz.shared.generated.resources.reviews_reply
import dz.shared.generated.resources.reviews_title
import dz.shared.generated.resources.reviews_write
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class Review(
    val avatarRes: DrawableResource,
    val name: String,
    val date: String,
    val stars: Int,
    val text: String,
    val helpfulCount: Int,
    val markedHelpful: Boolean
)

private val reviews = listOf(
    Review(
        avatarRes = Res.drawable.profile_2,
        name = "Patricia Lane",
        date = "2 days ago",
        stars = 5,
        text = "Atmospheric and slow in the best way. I read the last hundred pages in one sitting with the lights low.",
        helpfulCount = 32,
        markedHelpful = true
    ),
    Review(
        avatarRes = Res.drawable.img_neil_alvin,
        name = "Neil Alvin",
        date = "1 week ago",
        stars = 4,
        text = "Gorgeous prose. The middle drags slightly, but the house itself is the best character I’ve met all year.",
        helpfulCount = 11,
        markedHelpful = false
    )
)

private val ratingBars = listOf(0.72f, 0.18f, 0.06f, 0.03f, 0.01f)

@Composable
fun BookReview(
    modifier: Modifier = Modifier,
    coverResId: DrawableResource = Res.drawable.book_cover,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onReviewsClick: () -> Unit = {},
    onStartReadingClick: () -> Unit = {}
) {
    BookReviewScreen(
        modifier = modifier,
        coverResId = coverResId,
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onReviewsClick = onReviewsClick,
        onStartReadingClick = onStartReadingClick
    )
}

@Composable
fun BookReviewScreen(
    modifier: Modifier = Modifier,
    coverResId: DrawableResource = Res.drawable.book_cover,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onReviewsClick: () -> Unit = {},
    onStartReadingClick: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 140.dp)
        ) {
            // top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                InkIconButton(icon = InkIcons.Back, onClick = onBackClick, colors = colors)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.reviews_title),
                        fontFamily = displayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 19.sp,
                        color = colors.ink
                    )
                    Text(
                        text = "Mexican Gothic · Silvia Moreno-Garcia",
                        modifier = Modifier.padding(top = 3.dp),
                        fontFamily = bodyFont,
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colors.muted
                    )
                }
                InkIconButton(icon = InkIcons.Share, onClick = onShareClick, colors = colors)
            }

            // summary card
            Row(
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp, top = 8.dp)
                    .fillMaxWidth()
                    .inkCard(colors)
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "4.6",
                        fontFamily = displayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 42.sp,
                        color = colors.ink
                    )
                    Stars(filled = 5, size = 11.dp, colors = colors, modifier = Modifier.padding(top = 7.dp))
                    Text(
                        text = "1,284 ratings",
                        modifier = Modifier.padding(top = 7.dp),
                        fontFamily = bodyFont,
                        fontSize = 11.sp,
                        color = colors.muted
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ratingBars.forEachIndexed { i, fraction ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${5 - i}",
                                modifier = Modifier.width(8.dp),
                                fontFamily = bodyFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = colors.muted
                            )
                            InkProgressBar(
                                progress = fraction,
                                modifier = Modifier.weight(1f),
                                colors = colors
                            )
                        }
                    }
                }
            }

            // filter chips
            Row(
                modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InkChip(text = stringResource(Res.string.reviews_most_helpful), solid = true, colors = colors)
                InkChip(text = stringResource(Res.string.reviews_recent), colors = colors)
                InkChip(text = stringResource(Res.string.reviews_critical), colors = colors)
            }

            // reviews
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 8.dp)) {
                reviews.forEachIndexed { i, review ->
                    if (i > 0) {
                        HorizontalDivider(thickness = 1.dp, color = colors.line)
                    }
                    ReviewItem(review = review, colors = colors)
                }
            }
        }

        // write bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(colors.paper)
        ) {
            HorizontalDivider(thickness = 1.dp, color = colors.line)
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 18.dp)
            ) {
                InkButton(
                    text = stringResource(Res.string.reviews_write),
                    onClick = onReviewsClick,
                    leadingIcon = InkIcons.Plus,
                    colors = colors
                )
            }
        }
    }
}

@Composable
private fun ReviewItem(
    review: Review,
    colors: InkColors,
) {
    val bodyFont = inkBodyFontFamily()

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            Image(
                painter = painterResource(review.avatarRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.name,
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = colors.ink
                )
                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Stars(filled = review.stars, size = 10.dp, colors = colors)
                    Text(
                        text = review.date,
                        fontFamily = bodyFont,
                        fontSize = 11.sp,
                        color = colors.muted
                    )
                }
            }
            Icon(
                imageVector = InkIcons.MoreVertical,
                contentDescription = null,
                tint = colors.muted,
                modifier = Modifier.size(15.dp)
            )
        }

        Text(
            text = review.text,
            modifier = Modifier.padding(top = 11.dp),
            fontFamily = bodyFont,
            fontSize = 13.5.sp,
            lineHeight = 23.sp,
            color = colors.inkSoft
        )

        Row(
            modifier = Modifier.padding(top = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = InkIcons.Favorite,
                    contentDescription = null,
                    tint = if (review.markedHelpful) colors.accent else colors.muted,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "${review.helpfulCount} ${stringResource(Res.string.reviews_helpful)}",
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.5.sp,
                    color = colors.muted
                )
            }
            Text(
                text = stringResource(Res.string.reviews_reply),
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.5.sp,
                color = colors.muted
            )
        }
    }
}

@Composable
private fun Stars(
    filled: Int,
    size: androidx.compose.ui.unit.Dp,
    colors: InkColors,
    modifier: Modifier = Modifier,
    total: Int = 5,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(total) { i ->
            Icon(
                imageVector = InkIcons.Star,
                contentDescription = null,
                tint = if (i < filled) colors.accent else colors.line,
                modifier = Modifier.size(size)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun BookReviewScreenPreview() {
    BookReviewScreen()
}
