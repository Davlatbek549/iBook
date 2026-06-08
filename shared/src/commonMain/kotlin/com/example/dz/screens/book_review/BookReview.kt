package com.example.dz.screens.book_review

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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.dz.app_components.buttons.app_button.AppButton
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.min

private data class BookReviewMetric(
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
    val titleLineHeight: TextUnit,
    val authorFontSize: TextUnit,
    val authorLineHeight: TextUnit,
    val authorTopSpacing: Dp,
    val statsTopSpacing: Dp,
    val statsHeight: Dp,
    val statsCorner: Dp,
    val statLabelFontSize: TextUnit,
    val statLabelLineHeight: TextUnit,
    val statValueFontSize: TextUnit,
    val statValueLineHeight: TextUnit,
    val statsSheetOverlap: Dp,
    val sheetCorner: Dp,
    val sheetHorizontalPadding: Dp,
    val sheetTopPadding: Dp,
    val sectionTitleFontSize: TextUnit,
    val sectionTitleLineHeight: TextUnit,
    val bodyFontSize: TextUnit,
    val bodyLineHeight: TextUnit,
    val bottomBarHeight: Dp,
    val bottomBarCorner: Dp,
    val bottomIconSize: Dp,
    val bottomTextFontSize: TextUnit,
    val bottomTextLineHeight: TextUnit,
    val bottomSafePadding: Dp
)

private data class BookComment(
    val avatarRes: DrawableResource,
    val author: String,
    val comment: String
)

private enum class BookReviewMode {
    Overview,
    Comments,
    Writing
}

private val bookComments = listOf(
    BookComment(
        avatarRes = Res.drawable.profile_8,
        author = "OuluPulu",
        comment = "This book... my favorite book of the year! Maybe even this decade! ;)"
    ),
    BookComment(
        avatarRes = Res.drawable.profile_7,
        author = "Andy D.",
        comment = "I pre-ordered this book and forgot about it. I was very pleased when the delivery lady set off my ring doorbell..."
    ),
    BookComment(
        avatarRes = Res.drawable.profile_7,
        author = "Andy D.",
        comment = "I pre-ordered this book and forgot about it. I was very pleased when the delivery lady set off my ring doorbell..."
    ),
    BookComment(
        avatarRes = Res.drawable.profile_5,
        author = "Mona C.",
        comment = "A sharp, moody read with a setting that stays in your head after the last page."
    ),
    BookComment(
        avatarRes = Res.drawable.profile_3,
        author = "Natalia L.",
        comment = "Loved the atmosphere and pacing. It feels classic, eerie, and fresh at the same time."
    )
)

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
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberBookReviewMetric(maxWidth = maxWidth, maxHeight = maxHeight)
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        var reviewMode by remember { mutableStateOf(BookReviewMode.Overview) }
        var isShareSheetOpen by remember { mutableStateOf(false) }
        var showWriteReviewShortcut by remember { mutableStateOf(true) }

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            BookReviewBackground(coverResId = coverResId)

            TopActions(
                metrics = metrics,
                onBackClick = onBackClick,
                onShareClick = {
                    isShareSheetOpen = true
                    onShareClick()
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(3f)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(metrics.coverTopSpacing))

                BookHeader(
                    metrics = metrics,
                    coverResId = coverResId
                )

                if (reviewMode != BookReviewMode.Writing) {
                    Spacer(modifier = Modifier.height(metrics.statsTopSpacing))

                    BookStatsPanel(
                        metrics = metrics,
                        modifier = Modifier.zIndex(0f)
                    )
                } else {
                    Spacer(modifier = Modifier.height(metrics.sheetTopPadding))
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .zIndex(1f)
                ) {
                    val sheetModifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .requiredHeight(maxHeight + metrics.statsSheetOverlap)

                    when (reviewMode) {
                        BookReviewMode.Overview -> {
                            BookInfoSheet(
                                metrics = metrics,
                                modifier = sheetModifier
                            )
                        }
                        BookReviewMode.Comments -> {
                            BookCommentsSheet(
                                metrics = metrics,
                                modifier = sheetModifier
                            )
                        }
                        BookReviewMode.Writing -> {
                            WriteReviewSheet(
                                metrics = metrics,
                                onPostClick = {
                                    showWriteReviewShortcut = false
                                    reviewMode = BookReviewMode.Comments
                                },
                                modifier = sheetModifier
                            )
                        }
                    }
                }
            }

            if (reviewMode != BookReviewMode.Writing) {
                BottomReadingBar(
                    metrics = metrics,
                    isCommentsOpen = reviewMode == BookReviewMode.Comments,
                    onReviewsClick = {
                        reviewMode = BookReviewMode.Comments
                        onReviewsClick()
                    },
                    onCloseReviewsClick = { reviewMode = BookReviewMode.Overview },
                    showWriteReviewShortcut = showWriteReviewShortcut,
                    onWriteReviewClick = { reviewMode = BookReviewMode.Writing },
                    onStartReadingClick = onStartReadingClick,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            if (isShareSheetOpen) {
                ShareOptionsOverlay(
                    coverResId = coverResId,
                    maxWidth = screenWidth,
                    maxHeight = screenHeight,
                    onDismiss = { isShareSheetOpen = false },
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(8f)
                )
            }
        }
    }
}

@Composable
private fun BookReviewBackground(
    coverResId: DrawableResource
) {
    val isPreview = LocalInspectionMode.current
    val backgroundColor = rememberCoverBackgroundColor(coverResId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        if (!isPreview) {
            Image(
                painter = painterResource(coverResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.62f,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = 1.18f
                        scaleY = 1.18f
                    }
                    .blur(48.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.38f))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.03f),
                            Color.Black.copy(alpha = 0.18f),
                            Color.Black.copy(alpha = 0.48f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun rememberCoverBackgroundColor(
    coverResId: DrawableResource
): Color {
    return remember(coverResId) {
        Color(0xFF270716)
    }
}

@Composable
private fun BookHeader(
    metrics: BookReviewMetric,
    coverResId: DrawableResource
) {
    PreviewSafeBookCover(
        metrics = metrics,
        coverResId = coverResId
    )

    Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

    Text(
        text = stringResource(Res.string.book_review_title),
        color = Color.White,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontSize = metrics.titleFontSize,
            lineHeight = metrics.titleLineHeight,
            fontWeight = FontWeight.Bold
        )
    )

    Spacer(modifier = Modifier.height(metrics.authorTopSpacing))

    Text(
        text = stringResource(Res.string.book_review_author),
        color = Color.White.copy(alpha = 0.82f),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = metrics.authorFontSize,
            lineHeight = metrics.authorLineHeight,
                    fontWeight = FontWeight.Normal
        )
    )
}

@Composable
private fun PreviewSafeBookCover(
    metrics: BookReviewMetric,
    coverResId: DrawableResource
) {
    val coverModifier = Modifier
        .size(width = metrics.coverWidth, height = metrics.coverHeight)
        .shadow(
            elevation = 22.dp,
            shape = RoundedCornerShape(metrics.coverCorner),
            clip = false
        )
        .clip(RoundedCornerShape(metrics.coverCorner))

    if (LocalInspectionMode.current) {
        Box(
            modifier = coverModifier.background(Color(0xFF6D263D)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.book_review_title),
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 20.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    } else {
        Image(
            painter = painterResource(coverResId),
            contentDescription = stringResource(Res.string.book_review_cover_cd),
            contentScale = ContentScale.Crop,
            modifier = coverModifier
        )
    }
}

@Composable
private fun TopActions(
    metrics: BookReviewMetric,
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
        BookReviewCircleButton(
            onClick = onBackClick,
            size = metrics.topButtonSize
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = stringResource(Res.string.cd_back),
                tint = Color.White,
                modifier = Modifier.size(metrics.topIconSize * 0.82f)
            )
        }

        BookReviewCircleButton(
            onClick = onShareClick,
            size = metrics.topButtonSize
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_share),
                contentDescription = stringResource(Res.string.cd_share),
                tint = Color.White,
                modifier = Modifier.size(metrics.topIconSize)
            )
        }
    }
}

@Composable
private fun BookStatsPanel(
    metrics: BookReviewMetric,
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
                color = Color.White.copy(alpha = 0.22f),
                shape = RoundedCornerShape(
                    topStart = metrics.statsCorner,
                    topEnd = metrics.statsCorner
                )
            )
            .padding(
                start = metrics.horizontalPadding,
                end = metrics.horizontalPadding
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(
            label = stringResource(Res.string.book_review_reads),
            value = stringResource(Res.string.book_review_reads_value),
            metrics = metrics,
            modifier = Modifier.weight(1f)
        )

        StatItem(
            label = stringResource(Res.string.book_review_rating),
            value = stringResource(Res.string.book_review_rating_value),
            metrics = metrics,
            modifier = Modifier.weight(1f)
        )

        StatItem(
            label = stringResource(Res.string.book_review_review),
            value = stringResource(Res.string.book_review_review_value),
            metrics = metrics,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    metrics: BookReviewMetric,
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
                lineHeight = metrics.statLabelLineHeight,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.statValueFontSize,
                lineHeight = metrics.statValueLineHeight,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun BookInfoSheet(
    metrics: BookReviewMetric,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = metrics.sheetCorner,
                    topEnd = metrics.sheetCorner
                )
            )
            .background(Color(0xFFEFF3FA))
            .padding(
                start = metrics.sheetHorizontalPadding,
                top = metrics.sheetTopPadding,
                end = metrics.sheetHorizontalPadding,
                bottom = metrics.bottomBarHeight + metrics.bottomSafePadding
            )
    ) {
        Text(
            text = stringResource(Res.string.book_review_categories),
            color = Color(0xFF3E4147),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.sectionTitleFontSize,
                lineHeight = metrics.sectionTitleLineHeight,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(Res.string.book_review_category_values),
            color = Color(0xFF4C4F55),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.bodyFontSize,
                lineHeight = metrics.bodyLineHeight,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = stringResource(Res.string.book_review_overview),
            color = Color(0xFF3E4147),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.sectionTitleFontSize,
                lineHeight = metrics.sectionTitleLineHeight,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(Res.string.book_review_overview_body),
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
}

@Composable
private fun BookCommentsSheet(
    metrics: BookReviewMetric,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = metrics.sheetCorner,
                    topEnd = metrics.sheetCorner
                )
            )
            .background(Color(0xFFEFF3FA))
            .padding(
                start = metrics.sheetHorizontalPadding,
                top = metrics.sheetTopPadding,
                end = metrics.sheetHorizontalPadding,
                bottom = metrics.bottomBarHeight + metrics.bottomSafePadding + 16.dp
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(bookComments) { comment ->
                BookCommentRow(
                    comment = comment,
                    metrics = metrics
                )
            }
        }
    }
}

@Composable
private fun BookCommentRow(
    comment: BookComment,
    metrics: BookReviewMetric
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(comment.avatarRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = comment.author,
                color = Color(0xFF56585C),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.bodyFontSize,
                    lineHeight = metrics.bodyLineHeight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = comment.comment,
                modifier = Modifier.weight(1f),
                color = Color(0xFF989A9F),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = metrics.bodyFontSize,
                    lineHeight = metrics.bodyLineHeight,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun WriteReviewSheet(
    metrics: BookReviewMetric,
    onPostClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableStateOf(4) }
    var comment by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = metrics.sheetCorner,
                    topEnd = metrics.sheetCorner
                )
            )
            .background(Color(0xFFEFF3FA))
            .padding(
                start = metrics.sheetHorizontalPadding,
                top = 28.dp,
                end = metrics.sheetHorizontalPadding,
                bottom = 0.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) { index ->
                Icon(
                    painter = painterResource(Res.drawable.ic_star),
                    contentDescription = null,
                    tint = if (index < rating) Color(0xFFFF684F) else Color(0xFFC6C9CE),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { rating = index + 1 }
                )

                if (index < 4) {
                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(34.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.profile_6),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(62.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF56585C),
                        fontSize = metrics.bodyFontSize,
                        lineHeight = metrics.bodyLineHeight
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (comment.isEmpty()) {
                                Text(
                                    text = stringResource(Res.string.book_review_comment_hint),
                                    color = Color(0xFF9D9FA5),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = metrics.bodyFontSize,
                                        lineHeight = metrics.bodyLineHeight
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(Res.string.book_review_post),
                    color = Color(0xFFFF684F),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.bodyFontSize,
                        lineHeight = metrics.bodyLineHeight,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable {
                        comment = ""
                        onPostClick()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("😀", "😱", "🥳", "👄", "😈", "💋", "😎").forEach { emoji ->
                Text(
                    text = emoji,
                    fontSize = 30.sp,
                    lineHeight = 34.sp,
                    modifier = Modifier.clickable {
                        comment += emoji
                    }
                )
            }
        }
    }
}

@Composable
private fun ShareOptionsOverlay(
    coverResId: DrawableResource,
    maxWidth: Dp,
    maxHeight: Dp,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isCollectionPickerOpen by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.18f))
            .clickable(onClick = onDismiss)
    ) {
        BookReviewBackground(coverResId = coverResId)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.42f))
        )

        if (isCollectionPickerOpen) {
            SaveToCollectionPicker(
                onDoneClick = onDismiss,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(coverResId),
                contentDescription = stringResource(Res.string.book_review_cover_cd),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = (maxHeight * 0.15f).coerceIn(88.dp, 150.dp))
                    .size(
                        width = (maxWidth * 0.74f).coerceIn(250.dp, 330.dp),
                        height = (maxWidth * 0.98f).coerceIn(340.dp, 440.dp)
                    )
                    .shadow(
                        elevation = 34.dp,
                        shape = RoundedCornerShape(18.dp),
                        clip = false
                    )
                    .clip(RoundedCornerShape(18.dp))
            )

            ShareOptionsSheet(
                onSaveClick = { isCollectionPickerOpen = true },
                onShareClick = onDismiss,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun SaveToCollectionPicker(
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val collections = listOf(
        stringResource(Res.string.collection_self_help_book),
        stringResource(Res.string.collection_viet_nam_book),
        stringResource(Res.string.collection_romantic),
        stringResource(Res.string.collection_bao_vui),
        stringResource(Res.string.collection_book_for_designer)
    )
    var selectedIndex by remember { mutableStateOf(4) }

    Column(
        modifier = modifier
            .padding(horizontal = 42.dp)
            .padding(top = 174.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.book_review_save_to_collection),
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 40.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(46.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(36.dp))
                .background(Color.White)
                .padding(horizontal = 34.dp, vertical = 24.dp)
        ) {
            collections.forEachIndexed { index, collection ->
                CollectionRow(
                    text = collection,
                    isSelected = selectedIndex == index,
                    onClick = { selectedIndex = index }
                )

                Box(
                    modifier = Modifier
                        .padding(start = 36.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE6E6E6))
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            NewCollectionRow(
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            onClick = onDoneClick,
            text = stringResource(Res.string.done),
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp),
            isWhite = true
        )
    }
}

@Composable
private fun CollectionRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectionCircle(isSelected = isSelected)

        Text(
            text = text,
            color = Color(0xFF6B6C70),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 19.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun SelectionCircle(
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                }
            )
            .border(
                width = 2.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(Res.drawable.ic_done),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun NewCollectionRow(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF684F)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_plus),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(19.dp)
            )
        }

        Text(
            text = stringResource(Res.string.collection_new_collection),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 19.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun ShareOptionsSheet(
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 42.dp, topEnd = 42.dp))
            .background(Color.White)
            .padding(start = 48.dp, top = 12.dp, end = 48.dp, bottom = 42.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 62.dp, height = 8.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE1E1E1))
        )

        Spacer(modifier = Modifier.height(34.dp))

        ShareOptionRow(
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_bookmark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(33.dp)
                )
            },
            text = stringResource(Res.string.book_review_save_to_collection),
            onClick = onSaveClick
        )

        Box(
            modifier = Modifier
                .padding(start = 78.dp, top = 26.dp, bottom = 24.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE6E6E6))
        )

        ShareOptionRow(
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_share),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(33.dp)
                )
            },
            text = stringResource(Res.string.book_review_share),
            onClick = onShareClick
        )
    }
}

@Composable
private fun ShareOptionRow(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(34.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Text(
            text = text,
            color = Color(0xFF6B6C70),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 19.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun BottomReadingBar(
    metrics: BookReviewMetric,
    isCommentsOpen: Boolean,
    onReviewsClick: () -> Unit,
    onCloseReviewsClick: () -> Unit,
    showWriteReviewShortcut: Boolean,
    onWriteReviewClick: () -> Unit,
    onStartReadingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.bottomBarHeight)
            .navigationBarsPadding()
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = metrics.bottomBarCorner))
                .background(Color.White)
        )

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isCommentsOpen) {
                Box(
                    modifier = Modifier
                        .weight(if (showWriteReviewShortcut) 0.72f else 1f)
                        .fillMaxSize()
                        .clickable(onClick = onCloseReviewsClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.cd_close_reviews),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(metrics.bottomIconSize * 1.12f)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .weight(0.85f)
                        .fillMaxSize()
                        .clickable(onClick = onReviewsClick),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_message),
                        contentDescription = stringResource(Res.string.cd_message),
                        tint = Color(0xFFBABDC3),
                        modifier = Modifier.size(metrics.bottomIconSize)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(Res.string.book_review_review_value),
                        color = Color(0xFFBABDC3),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = metrics.bottomTextFontSize,
                            lineHeight = metrics.bottomTextLineHeight,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            if (!isCommentsOpen || showWriteReviewShortcut) {
                Row(
                    modifier = Modifier
                        .weight(if (isCommentsOpen) 2.12f else 1.85f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = metrics.bottomBarCorner))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            if (isCommentsOpen) {
                                onWriteReviewClick()
                            } else {
                                onStartReadingClick()
                            }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            if (isCommentsOpen) {
                                Res.drawable.ic_message
                            } else {
                                Res.drawable.ic_book_open
                            }
                        ),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(metrics.bottomIconSize)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = stringResource(
                            if (isCommentsOpen) {
                                Res.string.book_review_write_review
                            } else {
                                Res.string.book_review_start_reading
                            }
                        ),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = metrics.bottomTextFontSize,
                            lineHeight = metrics.bottomTextLineHeight,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BookReviewCircleButton(
    onClick: () -> Unit,
    size: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.22f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun rememberBookReviewMetric(
    maxWidth: Dp,
    maxHeight: Dp
): BookReviewMetric {
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
    val coverWidth = (204.dp * compactness).coerceIn(176.dp, 214.dp)

    return remember(maxWidth, maxHeight) {
        BookReviewMetric(
            horizontalPadding = (29.dp * widthRatio).coerceIn(22.dp, 36.dp),
            topSpacing = (46.dp * spacingScale).coerceIn(40.dp, 52.dp),
            topButtonSize = (42.dp * compactness).coerceIn(38.dp, 48.dp),
            topIconSize = (26.dp * compactness).coerceIn(22.dp, 30.dp),
            coverTopSpacing = (58.dp * spacingScale).coerceIn(48.dp, 66.dp),
            coverWidth = coverWidth,
            coverHeight = coverWidth * 1.20f,
            coverCorner = (15.dp * compactness).coerceIn(12.dp, 18.dp),
            titleTopSpacing = (20.dp * spacingScale).coerceIn(14.dp, 22.dp),
            titleFontSize = (22f * compactness).coerceIn(20f, 26f).sp,
            titleLineHeight = (26f * compactness).coerceIn(24f, 31f).sp,
            authorFontSize = (16f * compactness).coerceIn(14f, 18f).sp,
            authorLineHeight = (20f * compactness).coerceIn(18f, 23f).sp,
            authorTopSpacing = (10.dp * spacingScale).coerceIn(6.dp, 12.dp),
            statsTopSpacing = (14.dp * spacingScale).coerceIn(10.dp, 18.dp),
            statsHeight = (118.dp * compactness).coerceIn(104.dp, 132.dp),
            statsCorner = (42.dp * compactness).coerceIn(34.dp, 50.dp),
            statLabelFontSize = (15f * compactness).coerceIn(13f, 17f).sp,
            statLabelLineHeight = (19f * compactness).coerceIn(16f, 21f).sp,
            statValueFontSize = (20f * compactness).coerceIn(18f, 23f).sp,
            statValueLineHeight = (23f * compactness).coerceIn(20f, 26f).sp,
            statsSheetOverlap = 72.dp,
            sheetCorner = (43.dp * compactness).coerceIn(34.dp, 52.dp),
            sheetHorizontalPadding = (20.dp * widthRatio).coerceIn(20.dp, 30.dp),
            sheetTopPadding = (20.dp * spacingScale).coerceIn(18.dp, 30.dp),
            sectionTitleFontSize = (21f * compactness).coerceIn(19f, 24f).sp,
            sectionTitleLineHeight = (25f * compactness).coerceIn(22f, 29f).sp,
            bodyFontSize = (16f * compactness).coerceIn(14f, 18f).sp,
            bodyLineHeight = (26f * compactness).coerceIn(23f, 30f).sp,
            bottomBarHeight = (76.dp * compactness).coerceIn(68.dp, 86.dp),
            bottomBarCorner = (47.dp * compactness).coerceIn(36.dp, 54.dp),
            bottomIconSize = (26.dp * compactness).coerceIn(22.dp, 30.dp),
            bottomTextFontSize = (19f * compactness).coerceIn(17f, 22f).sp,
            bottomTextLineHeight = (23f * compactness).coerceIn(20f, 26f).sp,
            bottomSafePadding = 0.dp
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
fun BookReviewScreenPreview() {
    DZTheme(darkTheme = true) {
        BookReview()
    }
}
