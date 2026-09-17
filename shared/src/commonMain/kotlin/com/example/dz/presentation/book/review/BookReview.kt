package com.example.dz.presentation.book.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicPrimaryButton
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.components.organic.OrganicSectionLabel
import com.example.dz.designsystem.components.organic.OrganicSkeleton
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.domain.model.BookRatings
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.nav_back
import dz.shared.generated.resources.reviews_delete
import dz.shared.generated.resources.reviews_edit
import dz.shared.generated.resources.reviews_edit_yours
import dz.shared.generated.resources.reviews_no_ratings
import dz.shared.generated.resources.reviews_none_yet
import dz.shared.generated.resources.reviews_ratings_count
import dz.shared.generated.resources.reviews_stars_cd
import dz.shared.generated.resources.reviews_title
import dz.shared.generated.resources.reviews_try_again
import dz.shared.generated.resources.reviews_write
import dz.shared.generated.resources.reviews_your_review
import org.jetbrains.compose.resources.stringResource

/**
 * Reviews.
 *
 * Two things, and no third: how everyone scored the book, and what this reader thought of it.
 *
 * The screen it replaces showed 4.6 out of 1,284 ratings with a five-bar histogram for every book
 * in the catalogue, all of it written into the source. The scores here are the ones OpenLibrary
 * holds, so most Gutenberg titles have none and the screen says so rather than drawing a shape.
 * The review is the reader's own, kept on the device — there is no server to carry anyone else's,
 * and no catalogue the app reads has review text in it.
 */
@Composable
fun BookReviewScreen(
    uiState: BookReviewUiState = BookReviewUiState(),
    onEvent: (BookReviewEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        OrganicScreen {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = ORGANIC_GUTTER,
                    end = ORGANIC_GUTTER,
                    top = 12.dp,
                    bottom = ACTION_CLEARANCE,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(key = "header") {
                    ReviewsHeader(uiState = uiState, onBack = { onEvent(BookReviewEvent.BackClicked) })
                }

                when {
                    uiState.isLoading -> item(key = "skeleton") { ReviewsSkeleton() }

                    uiState.errorMessage != null -> item(key = "error") {
                        ErrorCard(
                            message = uiState.errorMessage,
                            onRetry = { onEvent(BookReviewEvent.RetryClicked) },
                        )
                    }

                    else -> {
                        item(key = "ratings") {
                            uiState.ratings?.let { RatingsCard(it) } ?: NoRatingsCard()
                        }
                        item(key = "mine-label") {
                            OrganicSectionLabel(text = stringResource(Res.string.reviews_your_review))
                        }
                        item(key = "mine") {
                            uiState.myReview?.let { review ->
                                MyReviewCard(
                                    review = review,
                                    onEdit = { onEvent(BookReviewEvent.WriteReviewClicked) },
                                    onDelete = { onEvent(BookReviewEvent.DeleteReviewClicked) },
                                )
                            } ?: NoReviewCard()
                        }
                    }
                }
            }
        }

        // The one thing this screen is for, kept where a thumb is.
        if (!uiState.isLoading && uiState.errorMessage == null) {
            OrganicPrimaryButton(
                text = stringResource(
                    if (uiState.myReview == null) Res.string.reviews_write else Res.string.reviews_edit_yours
                ),
                onClick = { onEvent(BookReviewEvent.WriteReviewClicked) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = ORGANIC_GUTTER)
                    .padding(bottom = 22.dp),
            )
        }

        uiState.editor?.let { editor ->
            ReviewEditorSheet(
                editor = editor,
                onEvent = onEvent,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun ReviewsHeader(
    uiState: BookReviewUiState,
    onBack: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        OrganicScreenHeader(
            title = stringResource(Res.string.reviews_title),
            leading = {
                OrganicCircleIconButton(
                    icon = OrganicIcons.ChevronLeft,
                    onClick = onBack,
                    contentDescription = stringResource(Res.string.nav_back),
                    size = 38.dp,
                    iconSize = 17.dp,
                )
            },
        )
        // The book is named under the title rather than in it: "Reviews" is what the screen is,
        // and a long title would push the heading out of the display face's size.
        val subtitle = listOf(uiState.bookTitle, uiState.bookAuthor)
            .filter { it.isNotBlank() }
            .joinToString(" · ")
        if (subtitle.isNotBlank()) {
            Text(
                text = subtitle,
                modifier = Modifier.padding(start = 52.dp),
                fontFamily = organicBodyFontFamily(),
                fontSize = 13.sp,
                color = OrganicColors.neutral700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** The average, and how the scores actually fell. */
@Composable
private fun RatingsCard(ratings: BookRatings) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        background = OrganicColors.neutral100,
        contentPadding = PaddingValues(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = ratings.average.toOneDecimal(),
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 44.sp,
                    lineHeight = 46.sp,
                    color = OrganicColors.text
                )
                StarRow(stars = ratings.average, size = 13.dp)
                Text(
                    text = stringResource(Res.string.reviews_ratings_count, ratings.count.grouped()),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = OrganicColors.neutral700
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                BookRatings.STARS.forEach { star ->
                    HistogramRow(star = star, share = ratings.share(star))
                }
            }
        }
    }
}

@Composable
private fun HistogramRow(star: Int, share: Float) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = star.toString(),
            fontFamily = organicBodyFontFamily(),
            fontSize = 11.sp,
            color = OrganicColors.neutral700
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(OrganicShape.pill))
                .background(OrganicColors.neutral200)
        ) {
            if (share > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(share)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .background(OrganicColors.accent)
                )
            }
        }
    }
}

/** Most of the catalogue. Said plainly, rather than drawn as five empty bars. */
@Composable
private fun NoRatingsCard() {
    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        background = OrganicColors.neutral100,
        contentPadding = PaddingValues(20.dp),
    ) {
        Text(
            text = stringResource(Res.string.reviews_no_ratings),
            fontFamily = organicBodyFontFamily(),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = OrganicColors.neutral700
        )
    }
}

@Composable
private fun MyReviewCard(
    review: MyReviewUi,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        elevated = true,
        contentPadding = PaddingValues(18.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarRow(stars = review.stars.toDouble(), size = 16.dp)
                Text(
                    text = review.writtenOn,
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = OrganicColors.neutral700
                )
            }
            if (review.note.isNotBlank()) {
                Text(
                    text = review.note,
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = OrganicColors.text
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                TextAction(
                    label = stringResource(Res.string.reviews_edit),
                    colour = OrganicColors.accent700,
                    onClick = onEdit,
                )
                TextAction(
                    label = stringResource(Res.string.reviews_delete),
                    colour = OrganicColors.danger,
                    onClick = onDelete,
                )
            }
        }
    }
}

@Composable
private fun NoReviewCard() {
    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(18.dp),
    ) {
        Text(
            text = stringResource(Res.string.reviews_none_yet),
            fontFamily = organicBodyFontFamily(),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = OrganicColors.neutral700
        )
    }
}

@Composable
private fun TextAction(
    label: String,
    colour: Color,
    onClick: () -> Unit,
) {
    Text(
        text = label,
        modifier = Modifier
            .clip(RoundedCornerShape(OrganicShape.pill))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        fontFamily = organicBodyFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = colour
    )
}

/**
 * Five stars, filled up to [stars].
 *
 * Half scores are rounded to the nearest whole star rather than drawn as halves: the number beside
 * them is already exact, and a half-star glyph at 13dp reads as a smudge.
 */
@Composable
internal fun StarRow(
    stars: Double,
    size: Dp,
    modifier: Modifier = Modifier,
    tint: Color = OrganicColors.accent,
) {
    val filled = (stars + 0.5).toInt().coerceIn(0, 5)
    Row(
        modifier = modifier.semantics {
            contentDescription = "$filled of 5"
        },
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < filled) OrganicIcons.StarFilled else OrganicIcons.Star,
                contentDescription = null,
                tint = if (index < filled) tint else OrganicColors.neutral400,
                modifier = Modifier.size(size)
            )
        }
    }
}

/** Five stars you can press. */
@Composable
internal fun StarPicker(
    stars: Int,
    onPick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        (1..5).forEach { star ->
            val chosen = star <= stars
            Icon(
                imageVector = if (chosen) OrganicIcons.StarFilled else OrganicIcons.Star,
                contentDescription = stringResource(Res.string.reviews_stars_cd, star),
                tint = if (chosen) OrganicColors.accent else OrganicColors.neutral400,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        role = Role.RadioButton,
                    ) { onPick(star) }
            )
        }
    }
}

@Composable
private fun ErrorCard(
    message: String,
    onRetry: () -> Unit,
) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                fontFamily = organicBodyFontFamily(),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = OrganicColors.neutral700,
                textAlign = TextAlign.Center
            )
            OrganicPrimaryButton(
                text = stringResource(Res.string.reviews_try_again),
                onClick = onRetry,
                fullWidth = false,
            )
        }
    }
}

/** The screen's own shape while the book and its scores are still coming. */
@Composable
private fun ReviewsSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OrganicSkeleton(
            modifier = Modifier.fillMaxWidth().height(148.dp),
            cornerRadius = OrganicShape.radiusLg,
        )
        OrganicSkeleton(
            modifier = Modifier.width(110.dp).height(12.dp),
            cornerRadius = OrganicShape.radiusSm,
        )
        OrganicSkeleton(
            modifier = Modifier.fillMaxWidth().height(104.dp),
            cornerRadius = OrganicShape.radiusLg,
        )
    }
}

/** One decimal place, without dragging in a formatter for a number that is always 0.0–5.0. */
private fun Double.toOneDecimal(): String {
    val tenths = ((this * 10) + 0.5).toInt().coerceIn(0, 50)
    return "${tenths / 10}.${tenths % 10}"
}

/** Thousands separated, so a well-read book does not read as "12840". */
private fun Int.grouped(): String =
    toString().reversed().chunked(3).joinToString(",").reversed()

/** Room under the list for the button that floats over it. */
private val ACTION_CLEARANCE = 110.dp

@Preview
@Composable
fun BookReviewScreenPreview() {
    BookReviewScreen()
}
