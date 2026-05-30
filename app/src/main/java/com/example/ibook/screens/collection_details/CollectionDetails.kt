package com.example.ibook.screens.collection_details

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.app_components.popups.DeleteBooksPopup
import com.example.ibook.app_components.popups.PurchasedBookPopup
import com.example.ibook.app_components.popups.PurchasedBookUiState
import com.example.ibook.ui.theme.ColorCategoryFantasy
import com.example.ibook.ui.theme.ColorPrimary
import com.example.ibook.ui.theme.ColorTertiary
import com.example.ibook.ui.theme.IBookTheme

private val CollectionDetailsActionTint = ColorPrimary

data class CollectionDetailsBookUiState(
    val imageRes: Int,
    val title: String,
    val author: String,
    val views: String
)

data class CollectionDetailsBookTagUiState(
    val text: String,
    val color: Color
)

private data class CollectionDetailsMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val actionButtonSize: Dp,
    val topIconSize: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val subtitleTopSpacing: Dp,
    val subtitleSize: TextUnit,
    val subtitleLineHeight: TextUnit,
    val listTopSpacing: Dp,
    val rowSpacing: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val coverCorner: Dp,
    val bookGap: Dp,
    val bookTopPadding: Dp,
    val bookTitleSize: TextUnit,
    val bookTitleLineHeight: TextUnit,
    val authorSize: TextUnit,
    val authorLineHeight: TextUnit,
    val viewsSize: TextUnit,
    val chipWidth: Dp,
    val chipHeight: Dp,
    val chipTextSize: TextUnit,
    val menuIconSize: Dp,
    val emptyIllustrationTopSpacing: Dp,
    val emptyIllustrationSize: Dp,
    val emptyTextTopSpacing: Dp,
    val emptyTextSize: TextUnit,
    val addBooksButtonTopSpacing: Dp,
    val addBooksButtonHeight: Dp,
    val addBooksButtonCorner: Dp,
    val editLabelSize: TextUnit,
    val editSelectionTopSpacing: Dp,
    val editSelectionCardHeight: Dp,
    val editSelectionCardCorner: Dp,
    val editActionButtonSize: Dp,
    val editSelectionTitleSize: TextUnit,
    val editSelectionCountSize: TextUnit,
    val editListTopSpacing: Dp,
    val editRowSpacing: Dp,
    val editSelectSize: Dp,
    val editSelectColumnWidth: Dp,
    val editMoveIconSize: Dp
)

private val defaultCollectionDetailsBooks = listOf(
    CollectionDetailsBookUiState(
        imageRes = R.drawable.book_cover,
        title = "Mexican Gothic",
        author = "By Silvia Moreno-Garcia",
        views = "1320"
    ),
    CollectionDetailsBookUiState(
        imageRes = R.drawable.book_cover_2,
        title = "The Immortalists",
        author = "By Chloe Benjamin",
        views = "1320"
    ),
    CollectionDetailsBookUiState(
        imageRes = R.drawable.book_cover_3,
        title = "Olive, Again",
        author = "Elizabeth Strout",
        views = "1320"
    ),
    CollectionDetailsBookUiState(
        imageRes = R.drawable.book_cover_4,
        title = "How Much of These Hills Is Gold",
        author = "By C Pam Zhang",
        views = "1320"
    ),
    CollectionDetailsBookUiState(
        imageRes = R.drawable.book_cove_6,
        title = "The Searcher",
        author = "By TANA FRENCH",
        views = "1320"
    )
)

private val defaultPurchasedBooks = defaultCollectionDetailsBooks.mapIndexed { index, book ->
    PurchasedBookUiState(
        id = "${book.title}-$index",
        imageRes = book.imageRes,
        title = book.title,
        author = book.author,
        views = book.views
    )
}

@Composable
fun CollectionDetails(
    modifier: Modifier = Modifier,
    title: String = "Self help book",
    description: String = "From individual reflection to the power of positive thought and from every perspective and theism.",
    books: List<CollectionDetailsBookUiState> = defaultCollectionDetailsBooks,
    purchasedBooks: List<PurchasedBookUiState> = defaultPurchasedBooks,
    initialEditMode: Boolean = false,
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onRemoveFromCollectionClick: (List<CollectionDetailsBookUiState>) -> Unit = {},
    onRemoveEverywhereClick: (List<CollectionDetailsBookUiState>) -> Unit = {},
    onBookOptionsClick: (CollectionDetailsBookUiState) -> Unit = {}
) {
    CollectionDetailsScreen(
        modifier = modifier,
        title = title,
        description = description,
        books = books,
        purchasedBooks = purchasedBooks,
        initialEditMode = initialEditMode,
        onBackClick = onBackClick,
        onAddClick = onAddClick,
        onSettingsClick = onSettingsClick,
        onRemoveFromCollectionClick = onRemoveFromCollectionClick,
        onRemoveEverywhereClick = onRemoveEverywhereClick,
        onBookOptionsClick = onBookOptionsClick
    )
}

@Composable
fun CollectionDetailsScreen(
    modifier: Modifier = Modifier,
    title: String = "Self help book",
    description: String = "From individual reflection to the power of positive thought and from every perspective and theism.",
    books: List<CollectionDetailsBookUiState> = defaultCollectionDetailsBooks,
    purchasedBooks: List<PurchasedBookUiState> = defaultPurchasedBooks,
    initialEditMode: Boolean = false,
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onRemoveFromCollectionClick: (List<CollectionDetailsBookUiState>) -> Unit = {},
    onRemoveEverywhereClick: (List<CollectionDetailsBookUiState>) -> Unit = {},
    onBookOptionsClick: (CollectionDetailsBookUiState) -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberCollectionDetailsMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme
        val scrollState = rememberScrollState()
        val visibleBooks = remember(books) {
            mutableStateListOf<CollectionDetailsBookUiState>().apply {
                addAll(books)
            }
        }
        var isPurchasedBookPopupVisible by remember { mutableStateOf(false) }
        var isDeleteBooksPopupVisible by remember { mutableStateOf(false) }
        var isEditMode by remember { mutableStateOf(initialEditMode) }
        var purchasedBookSearchQuery by remember { mutableStateOf("") }
        var selectedPurchasedBookIds by remember { mutableStateOf(emptySet<String>()) }
        var selectedEditBookKeys by remember { mutableStateOf(emptySet<String>()) }
        val openPurchasedBookPopup = {
            isPurchasedBookPopupVisible = true
            onAddClick()
        }
        val removeSelectedEditBooks = {
            val selectedKeys = selectedEditBookKeys
            val removedBooks = visibleBooks.filter { book ->
                book.selectionKey() in selectedKeys
            }

            visibleBooks.removeAll { book ->
                book.selectionKey() in selectedKeys
            }
            selectedEditBookKeys = emptySet()
            removedBooks
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            CollectionDetailsTopActions(
                metrics = metrics,
                onBackClick = onBackClick,
                onAddClick = openPurchasedBookPopup,
                isEditMode = isEditMode,
                onSettingsClick = {
                    isEditMode = !isEditMode
                    if (isEditMode) {
                        selectedEditBookKeys = emptySet()
                    }
                    onSettingsClick()
                }
            )

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            if (isEditMode) {
                Text(
                    text = stringResource(R.string.collection_details_label_title),
                    color = colorScheme.onSurface.copy(alpha = 0.42f),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = metrics.editLabelSize,
                        lineHeight = metrics.editLabelSize * 1.25f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = title,
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize * 1.08f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            if (visibleBooks.isEmpty()) {
                CollectionDetailsEmptyState(
                    metrics = metrics,
                    onAddBooksClick = openPurchasedBookPopup
                )
            } else if (isEditMode) {
                Spacer(modifier = Modifier.height(metrics.subtitleTopSpacing))

                Text(
                    text = stringResource(R.string.collection_details_label_description),
                    color = colorScheme.onSurface.copy(alpha = 0.42f),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = metrics.editLabelSize,
                        lineHeight = metrics.editLabelSize * 1.25f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = description,
                    color = colorScheme.onSurface.copy(alpha = 0.76f),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = metrics.subtitleSize,
                        lineHeight = metrics.subtitleLineHeight,
                        letterSpacing = 0.sp
                    )
                )

                if (selectedEditBookKeys.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(metrics.editSelectionTopSpacing))

                    CollectionDetailsSelectionActions(
                        selectedCount = selectedEditBookKeys.size,
                        metrics = metrics,
                        onAddToCollectionClick = openPurchasedBookPopup,
                        onDeleteClick = {
                            isDeleteBooksPopupVisible = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(metrics.editListTopSpacing))

                Column(
                    verticalArrangement = Arrangement.spacedBy(metrics.editRowSpacing)
                ) {
                    visibleBooks.forEach { book ->
                        val bookKey = book.selectionKey()
                        CollectionDetailsEditableBookRow(
                            book = book,
                            metrics = metrics,
                            isSelected = bookKey in selectedEditBookKeys,
                            onClick = {
                                selectedEditBookKeys = if (bookKey in selectedEditBookKeys) {
                                    selectedEditBookKeys - bookKey
                                } else {
                                    selectedEditBookKeys + bookKey
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(34.dp))
            } else {
                Spacer(modifier = Modifier.height(metrics.subtitleTopSpacing))

                Text(
                    text = description,
                    color = colorScheme.onSurface.copy(alpha = 0.76f),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = metrics.subtitleSize,
                        lineHeight = metrics.subtitleLineHeight,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.listTopSpacing))

                Column(
                    verticalArrangement = Arrangement.spacedBy(metrics.rowSpacing)
                ) {
                    visibleBooks.forEach { book ->
                        CollectionDetailsBookRow(
                            book = book,
                            metrics = metrics,
                            onOptionsClick = { onBookOptionsClick(book) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(34.dp))
            }
        }

        if (isPurchasedBookPopupVisible) {
            PurchasedBookPopup(
                books = purchasedBooks,
                selectedBookIds = selectedPurchasedBookIds,
                searchQuery = purchasedBookSearchQuery,
                onSearchQueryChange = { purchasedBookSearchQuery = it },
                onBookToggle = { book ->
                    selectedPurchasedBookIds = if (book.id in selectedPurchasedBookIds) {
                        selectedPurchasedBookIds - book.id
                    } else {
                        selectedPurchasedBookIds + book.id
                    }
                },
                onDoneClick = {
                    isPurchasedBookPopupVisible = false
                    purchasedBookSearchQuery = ""
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (isDeleteBooksPopupVisible) {
            DeleteBooksPopup(
                onRemoveFromCollectionClick = {
                    val removedBooks = removeSelectedEditBooks()
                    isDeleteBooksPopupVisible = false
                    onRemoveFromCollectionClick(removedBooks)
                },
                onRemoveEverywhereClick = {
                    val removedBooks = removeSelectedEditBooks()
                    isDeleteBooksPopupVisible = false
                    onRemoveEverywhereClick(removedBooks)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun CollectionDetailsTopActions(
    metrics: CollectionDetailsMetrics,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    isEditMode: Boolean,
    onSettingsClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CollectionDetailsCircleButton(
            metrics = metrics,
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.cd_back),
                tint = CollectionDetailsActionTint,
                modifier = Modifier.size(metrics.topIconSize)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CollectionDetailsCircleButton(
                metrics = metrics,
                onClick = onAddClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.cd_add_book),
                    tint = CollectionDetailsActionTint,
                    modifier = Modifier.size(metrics.topIconSize * 0.88f)
                )
            }

            CollectionDetailsCircleButton(
                metrics = metrics,
                onClick = onSettingsClick,
                backgroundColor = if (isEditMode) ColorPrimary else colorScheme.outline.copy(alpha = 0.16f)
            ) {
                Icon(
                    painter = painterResource(if (isEditMode) R.drawable.ic_done else R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.cd_collection_settings),
                    tint = if (isEditMode) colorScheme.onPrimary else CollectionDetailsActionTint,
                    modifier = Modifier.size(metrics.topIconSize * 0.74f)
                )
            }
        }
    }
}

@Composable
private fun CollectionDetailsCircleButton(
    metrics: CollectionDetailsMetrics,
    onClick: () -> Unit,
    backgroundColor: Color? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(metrics.actionButtonSize)
            .clip(CircleShape)
            .background(backgroundColor ?: MaterialTheme.colorScheme.outline.copy(alpha = 0.16f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun CollectionDetailsEmptyState(
    metrics: CollectionDetailsMetrics,
    onAddBooksClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(metrics.emptyIllustrationTopSpacing))

        EmptyCollectionIllustration(
            modifier = Modifier.size(metrics.emptyIllustrationSize)
        )

        Spacer(modifier = Modifier.height(metrics.emptyTextTopSpacing))

        Text(
            text = stringResource(R.string.collection_details_empty),
            color = colorScheme.onSurface.copy(alpha = 0.76f),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.emptyTextSize,
                lineHeight = metrics.emptyTextSize * 1.25f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(metrics.addBooksButtonTopSpacing))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(metrics.addBooksButtonHeight)
                .clip(RoundedCornerShape(metrics.addBooksButtonCorner))
                .background(ColorPrimary)
                .clickable(onClick = onAddBooksClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.collection_details_add_books),
                color = colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun EmptyCollectionIllustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        drawEmptyCollectionArt()
    }
}

private fun DrawScope.drawEmptyCollectionArt() {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension * 0.45f

    drawCircle(
        color = Color(0xFFF4F4F4),
        radius = radius,
        center = center
    )

    drawConfetti()

    val paperSize = Size(size.width * 0.48f, size.height * 0.58f)
    val paperTopLeft = Offset(
        x = center.x - paperSize.width * 0.6f,
        y = center.y - paperSize.height * 0.42f
    )

    rotate(degrees = -8f, pivot = center) {
        repeat(3) { index ->
            drawRoundRect(
                color = Color(0xFFE5E5E8).copy(alpha = 0.72f - index * 0.12f),
                topLeft = paperTopLeft + Offset(index * 13f, -index * 11f),
                size = paperSize,
                cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx())
            )
        }

        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.White, Color(0xFFE2E2E5)),
                startY = paperTopLeft.y,
                endY = paperTopLeft.y + paperSize.height
            ),
            topLeft = paperTopLeft,
            size = paperSize,
            cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx())
        )

        val lineStart = paperTopLeft.x + paperSize.width * 0.22f
        val lineEnd = paperTopLeft.x + paperSize.width * 0.76f
        val firstLineY = paperTopLeft.y + paperSize.height * 0.28f
        repeat(7) { index ->
            val y = firstLineY + index * paperSize.height * 0.075f
            val end = if (index == 4) lineEnd - paperSize.width * 0.18f else lineEnd
            drawLine(
                color = Color(0xFF4D4146),
                start = Offset(lineStart, y),
                end = Offset(end, y),
                strokeWidth = 2.2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        drawLine(
            color = Color(0xFF4D4146),
            start = Offset(lineStart + paperSize.width * 0.33f, paperTopLeft.y + paperSize.height * 0.72f),
            end = Offset(lineEnd - paperSize.width * 0.2f, paperTopLeft.y + paperSize.height * 0.72f),
            strokeWidth = 2.2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }

    drawMagnifier(center)
}

private fun DrawScope.drawMagnifier(center: Offset) {
    val lensRadius = size.minDimension * 0.18f
    val lensCenter = Offset(
        x = center.x + size.width * 0.11f,
        y = center.y - size.height * 0.06f
    )
    val handleStart = Offset(
        x = lensCenter.x + lensRadius * 0.63f,
        y = lensCenter.y + lensRadius * 0.72f
    )
    val handleEnd = Offset(
        x = handleStart.x + size.width * 0.12f,
        y = handleStart.y + size.height * 0.17f
    )

    drawLine(
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFFFC49A), Color(0xFFFF6A00)),
            start = handleStart,
            end = handleEnd
        ),
        start = handleStart,
        end = handleEnd,
        strokeWidth = size.minDimension * 0.075f,
        cap = StrokeCap.Square
    )

    drawCircle(
        color = Color(0xFFFF6A00),
        radius = lensRadius + size.minDimension * 0.045f,
        center = lensCenter
    )

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFF5B2B5).copy(alpha = 0.82f),
                Color(0xFF4B3F43).copy(alpha = 0.65f)
            ),
            center = lensCenter,
            radius = lensRadius
        ),
        radius = lensRadius,
        center = lensCenter
    )

    val stripeColor = Color(0xFF3E363A).copy(alpha = 0.78f)
    repeat(3) { index ->
        val y = lensCenter.y - lensRadius * 0.5f + index * lensRadius * 0.48f
        drawLine(
            color = stripeColor,
            start = Offset(lensCenter.x - lensRadius * 0.54f, y),
            end = Offset(lensCenter.x + lensRadius * 0.52f, y - lensRadius * 0.05f),
            strokeWidth = size.minDimension * 0.035f,
            cap = StrokeCap.Square
        )
    }

    drawCircle(
        color = Color(0xFFFF8800),
        radius = lensRadius + size.minDimension * 0.045f,
        center = lensCenter,
        style = Stroke(width = size.minDimension * 0.07f)
    )
}

private fun DrawScope.drawConfetti() {
    val purple = ColorPrimary
    val orange = Color(0xFFFFB536)
    val blue = Color(0xFF2F94F5)
    val mint = Color(0xFF65D3CB)
    val green = Color(0xFF79EC91)
    val pink = Color(0xFFFF4FA1)

    drawRing(Offset(size.width * 0.12f, size.height * 0.26f), purple, size.minDimension * 0.024f)
    drawRing(Offset(size.width * 0.87f, size.height * 0.3f), purple, size.minDimension * 0.035f)
    drawRing(Offset(size.width * 0.08f, size.height * 0.75f), orange, size.minDimension * 0.033f)
    drawRing(Offset(size.width * 0.85f, size.height * 0.74f), purple, size.minDimension * 0.018f)

    drawPlus(Offset(size.width * 0.86f, size.height * 0.67f), Color(0xFFFFB98C), size.minDimension * 0.021f)
    drawPlus(Offset(size.width * 0.92f, size.height * 0.53f), Color(0xFF64A4F2), size.minDimension * 0.021f)
    drawPlus(Offset(size.width * 0.3f, size.height * 0.88f), Color(0xFFA65EEA), size.minDimension * 0.022f)

    drawSquiggle(Offset(size.width * 0.1f, size.height * 0.54f), mint, size.minDimension * 0.024f)
    drawSquiggle(Offset(size.width * 0.73f, size.height * 0.56f), green, size.minDimension * 0.016f)

    drawCircle(color = pink, radius = size.minDimension * 0.012f, center = Offset(size.width * 0.36f, size.height * 0.24f))
    drawCircle(color = blue, radius = size.minDimension * 0.018f, center = Offset(size.width * 0.56f, size.height * 0.92f))
}

private fun DrawScope.drawRing(center: Offset, color: Color, radius: Float) {
    drawCircle(
        color = color,
        radius = radius,
        center = center,
        style = Stroke(width = radius * 0.42f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawPlus(center: Offset, color: Color, length: Float) {
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

private fun DrawScope.drawSquiggle(start: Offset, color: Color, length: Float) {
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
private fun CollectionDetailsSelectionActions(
    selectedCount: Int,
    metrics: CollectionDetailsMetrics,
    onAddToCollectionClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(metrics.editSelectionCardHeight)
                .shadow(14.dp, RoundedCornerShape(metrics.editSelectionCardCorner))
                .clip(RoundedCornerShape(metrics.editSelectionCardCorner))
                .background(colorScheme.surface)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.collection_details_selected),
                color = colorScheme.onSurface.copy(alpha = 0.42f),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.editSelectionTitleSize,
                    lineHeight = metrics.editSelectionTitleSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.collection_details_selected_count, selectedCount),
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.editSelectionCountSize,
                    lineHeight = metrics.editSelectionCountSize,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp
                )
            )
        }

        CollectionDetailsEditActionButton(
            metrics = metrics,
            backgroundColor = ColorPrimary,
            iconRes = R.drawable.ic_add_collection,
            contentDescription = stringResource(R.string.cd_add_selected_books),
            onClick = onAddToCollectionClick
        )

        CollectionDetailsEditActionButton(
            metrics = metrics,
            backgroundColor = Color(0xFFFF2D2D),
            iconRes = R.drawable.ic_delete,
            contentDescription = stringResource(R.string.cd_delete_selected_books),
            onClick = onDeleteClick
        )
    }
}

@Composable
private fun CollectionDetailsEditActionButton(
    metrics: CollectionDetailsMetrics,
    backgroundColor: Color,
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(metrics.editActionButtonSize)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun CollectionDetailsEditableBookRow(
    book: CollectionDetailsBookUiState,
    metrics: CollectionDetailsMetrics,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(metrics.editSelectColumnWidth)
                .height(metrics.coverHeight),
            contentAlignment = Alignment.CenterStart
        ) {
            CollectionDetailsSelectionMark(
                isSelected = isSelected,
                metrics = metrics
            )
        }

        Image(
            painter = painterResource(book.imageRes),
            contentDescription = stringResource(R.string.cd_book_cover, book.title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(metrics.coverWidth)
                .height(metrics.coverHeight)
                .shadow(8.dp, RoundedCornerShape(metrics.coverCorner))
                .clip(RoundedCornerShape(metrics.coverCorner))
        )

        Spacer(modifier = Modifier.width(metrics.bookGap))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = metrics.bookTopPadding)
        ) {
            Text(
                text = book.title,
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.bookTitleSize,
                    lineHeight = metrics.bookTitleLineHeight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = book.author,
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.authorSize,
                    lineHeight = metrics.authorLineHeight,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(29.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = null,
                    tint = ColorPrimary,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = book.views,
                    color = colorScheme.onSurface.copy(alpha = 0.42f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.viewsSize,
                        lineHeight = metrics.viewsSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CollectionDetailsGenreChip(
                    text = stringResource(R.string.category_horror),
                    color = ColorTertiary,
                    metrics = metrics
                )
                CollectionDetailsGenreChip(
                    text = stringResource(R.string.category_fantasy),
                    color = ColorCategoryFantasy,
                    metrics = metrics
                )
            }
        }

        Box(
            modifier = Modifier
                .height(metrics.coverHeight)
                .width(metrics.editMoveIconSize),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_move),
            contentDescription = stringResource(R.string.cd_move_book),
                tint = Color.Unspecified,
                modifier = Modifier.size(metrics.editMoveIconSize)
            )
        }
    }
}

@Composable
private fun CollectionDetailsSelectionMark(
    isSelected: Boolean,
    metrics: CollectionDetailsMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .size(metrics.editSelectSize)
            .clip(CircleShape)
            .background(if (isSelected) ColorPrimary else colorScheme.surface)
            .then(
                if (isSelected) {
                    Modifier
                } else {
                    Modifier.background(Color.Transparent)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!isSelected) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = ColorPrimary,
                    radius = size.minDimension / 2f - 1.5.dp.toPx(),
                    style = Stroke(width = 2.2.dp.toPx())
                )
            }
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_done),
                contentDescription = null,
                tint = colorScheme.onPrimary,
                modifier = Modifier.size(metrics.editSelectSize * 0.5f)
            )
        }
    }
}

@Composable
private fun CollectionDetailsBookRow(
    book: CollectionDetailsBookUiState,
    metrics: CollectionDetailsMetrics,
    onOptionsClick: () -> Unit
) {
    CollectionDetailsBookListRow(
        book = book,
        tags = listOf(
            CollectionDetailsBookTagUiState("Horror", ColorTertiary),
            CollectionDetailsBookTagUiState("Fantasy", ColorCategoryFantasy)
        ),
        modifier = Modifier.fillMaxWidth(),
        coverWidth = metrics.coverWidth,
        coverHeight = metrics.coverHeight,
        coverCorner = metrics.coverCorner,
        bookGap = metrics.bookGap,
        bookTopPadding = metrics.bookTopPadding,
        titleSize = metrics.bookTitleSize,
        titleLineHeight = metrics.bookTitleLineHeight,
        authorSize = metrics.authorSize,
        authorLineHeight = metrics.authorLineHeight,
        viewsSize = metrics.viewsSize,
        chipHeight = metrics.chipHeight,
        chipTextSize = metrics.chipTextSize,
        chipHorizontalPadding = 16.dp,
        menuIconSize = metrics.menuIconSize,
        onOptionsClick = onOptionsClick
    )
}

@Composable
fun CollectionDetailsBookListRow(
    book: CollectionDetailsBookUiState,
    tags: List<CollectionDetailsBookTagUiState>,
    modifier: Modifier = Modifier,
    coverWidth: Dp = 86.dp,
    coverHeight: Dp = 116.dp,
    coverCorner: Dp = 8.dp,
    bookGap: Dp = 24.dp,
    bookTopPadding: Dp = 7.dp,
    titleSize: TextUnit = 21.sp,
    titleLineHeight: TextUnit = 25.sp,
    authorSize: TextUnit = 15.sp,
    authorLineHeight: TextUnit = 20.sp,
    viewsSize: TextUnit = 18.sp,
    chipHeight: Dp = 24.dp,
    chipTextSize: TextUnit = 14.sp,
    chipHorizontalPadding: Dp = 16.dp,
    menuIconSize: Dp = 27.dp,
    titleColor: Color? = null,
    authorColor: Color? = null,
    viewsColor: Color? = null,
    actionTint: Color = ColorPrimary,
    menuTint: Color? = null,
    onOptionsClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val resolvedTitleColor = titleColor ?: colorScheme.onSurface.copy(alpha = 0.76f)
    val resolvedAuthorColor = authorColor ?: colorScheme.onSurface.copy(alpha = 0.76f)
    val resolvedViewsColor = viewsColor ?: colorScheme.onSurface.copy(alpha = 0.42f)
    val resolvedMenuTint = menuTint ?: colorScheme.onSurface.copy(alpha = 0.76f)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(book.imageRes),
            contentDescription = stringResource(R.string.cd_book_cover, book.title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(coverWidth)
                .height(coverHeight)
                .shadow(8.dp, RoundedCornerShape(coverCorner))
                .clip(RoundedCornerShape(coverCorner))
        )

        Spacer(modifier = Modifier.width(bookGap))

        Column(
            modifier = Modifier
                .weight(1f)
                .height(coverHeight)
                .padding(top = bookTopPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = book.title,
                    color = resolvedTitleColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = titleSize,
                        lineHeight = titleLineHeight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = book.author,
                    color = resolvedAuthorColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = authorSize,
                        lineHeight = authorLineHeight,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )
                )
            }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = actionTint,
                        modifier = Modifier.size(chipHeight * 0.92f)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = book.views,
                        color = resolvedViewsColor,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = viewsSize,
                            lineHeight = viewsSize,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tags.forEach { tag ->
                        CollectionDetailsInlineGenreChip(
                            tag = tag,
                            chipHeight = chipHeight,
                            chipTextSize = chipTextSize,
                            horizontalPadding = chipHorizontalPadding
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .height(coverHeight)
                .width(menuIconSize)
                .clip(RoundedCornerShape(50))
                .clickable(onClick = onOptionsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_three_vertical_dots),
                contentDescription = stringResource(R.string.cd_book_options),
                tint = resolvedMenuTint,
                modifier = Modifier.size(menuIconSize)
            )
        }
    }
}

@Composable
private fun CollectionDetailsInlineGenreChip(
    tag: CollectionDetailsBookTagUiState,
    chipHeight: Dp,
    chipTextSize: TextUnit,
    horizontalPadding: Dp
) {
    Box(
        modifier = Modifier
            .height(chipHeight)
            .clip(RoundedCornerShape(50))
            .background(tag.color)
            .padding(horizontal = horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag.text,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = chipTextSize,
                lineHeight = chipTextSize,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

private fun CollectionDetailsBookUiState.selectionKey(): String {
    return "$imageRes-$title-$author"
}

@Composable
private fun CollectionDetailsGenreChip(
    text: String,
    color: Color,
    metrics: CollectionDetailsMetrics
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
            color = MaterialTheme.colorScheme.onPrimary,
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
private fun rememberCollectionDetailsMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): CollectionDetailsMetrics {
    val compact = maxWidth < 370.dp
    val short = maxHeight < 760.dp

    return remember(maxWidth, maxHeight) {
        CollectionDetailsMetrics(
            horizontalPadding = if (compact) 24.dp else 29.dp,
            topSpacing = if (short) 32.dp else 47.dp,
            actionButtonSize = if (compact) 42.dp else 44.dp,
            topIconSize = if (compact) 30.dp else 32.dp,
            titleTopSpacing = if (short) 24.dp else 38.dp,
            titleSize = if (compact) 36.sp else 40.sp,
            subtitleTopSpacing = 20.dp,
            subtitleSize = if (compact) 14.sp else 15.sp,
            subtitleLineHeight = if (compact) 23.sp else 25.sp,
            listTopSpacing = if (short) 34.dp else 51.dp,
            rowSpacing = if (compact) 25.dp else 28.dp,
            coverWidth = if (compact) 82.dp else 86.dp,
            coverHeight = if (compact) 106.dp else 116.dp,
            coverCorner = 8.dp,
            bookGap = if (compact) 20.dp else 24.dp,
            bookTopPadding = 7.dp,
            bookTitleSize = if (compact) 20.sp else 21.sp,
            bookTitleLineHeight = if (compact) 23.sp else 25.sp,
            authorSize = if (compact) 14.sp else 15.sp,
            authorLineHeight = if (compact) 18.sp else 20.sp,
            viewsSize = if (compact) 17.sp else 18.sp,
            chipWidth = if (compact) 72.dp else 76.dp,
            chipHeight = 24.dp,
            chipTextSize = if (compact) 13.sp else 14.sp,
            menuIconSize = 27.dp,
            emptyIllustrationTopSpacing = if (short) 58.dp else 89.dp,
            emptyIllustrationSize = if (compact) 280.dp else 314.dp,
            emptyTextTopSpacing = if (short) 36.dp else 52.dp,
            emptyTextSize = if (compact) 18.sp else 19.sp,
            addBooksButtonTopSpacing = if (short) 104.dp else 146.dp,
            addBooksButtonHeight = if (compact) 66.dp else 70.dp,
            addBooksButtonCorner = 18.dp,
            editLabelSize = if (compact) 17.sp else 18.sp,
            editSelectionTopSpacing = if (short) 28.dp else 38.dp,
            editSelectionCardHeight = if (compact) 58.dp else 60.dp,
            editSelectionCardCorner = 10.dp,
            editActionButtonSize = if (compact) 58.dp else 60.dp,
            editSelectionTitleSize = if (compact) 15.sp else 16.sp,
            editSelectionCountSize = if (compact) 14.sp else 15.sp,
            editListTopSpacing = if (short) 32.dp else 42.dp,
            editRowSpacing = if (compact) 26.dp else 30.dp,
            editSelectSize = 25.dp,
            editSelectColumnWidth = if (compact) 42.dp else 45.dp,
            editMoveIconSize = 28.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionDetailsScreenPreview() {
    IBookTheme {
        CollectionDetailsScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyCollectionDetailsScreenPreview() {
    IBookTheme {
        CollectionDetailsScreen(
            title = "New Collection",
            description = "",
            books = emptyList()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditableCollectionDetailsScreenPreview() {
    IBookTheme {
        CollectionDetailsScreen(initialEditMode = true)
    }
}
