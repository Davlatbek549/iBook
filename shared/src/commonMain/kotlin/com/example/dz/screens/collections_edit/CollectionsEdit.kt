package com.example.dz.screens.collections_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.ColorPrimary
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

private val CollectionsEditText = Color(0xFF6D6D70)
private val CollectionsEditMuted = Color(0xFFC3C3C7)
private val CollectionsEditDivider = Color(0xFFE0E0E2)
private val CollectionsEditBackBackground = Color(0xFFE3E3E3)
private val CollectionsEditDelete = Color(0xFFFF2D2D)
private val CollectionsEditCreate = Color(0xFFF46656)

data class CollectionsEditItemUiState(
    val id: String,
    val title: String,
    val count: Int,
    val iconType: CollectionsEditIconType,
    val isHighlighted: Boolean = false,
    val canDelete: Boolean = true
)

enum class CollectionsEditIconType {
    Folder,
    Heart,
    Purchased,
    Finished,
    Audiobook
}

private data class CollectionsEditMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val subtitleTopSpacing: Dp,
    val subtitleSize: TextUnit,
    val subtitleLineHeight: TextUnit,
    val listTopSpacing: Dp,
    val rowHeight: Dp,
    val rowIconSize: Dp,
    val rowTitleSize: TextUnit,
    val rowCountSize: TextUnit,
    val chevronSize: Dp,
    val deleteWidth: Dp,
    val deleteButtonSize: Dp,
    val newCollectionTopSpacing: Dp
)

private val defaultCollectionItems = listOf(
    CollectionsEditItemUiState(
        id = "self-help",
        title = "Self help book",
        count = 4,
        iconType = CollectionsEditIconType.Folder
    ),
    CollectionsEditItemUiState(
        id = "viet-nam",
        title = "Viet Nam book",
        count = 3,
        iconType = CollectionsEditIconType.Folder
    ),
    CollectionsEditItemUiState(
        id = "romantic",
        title = "Romantic",
        count = 1,
        iconType = CollectionsEditIconType.Folder
    ),
    CollectionsEditItemUiState(
        id = "bao-vui",
        title = "Bao vui ;)",
        count = 5,
        iconType = CollectionsEditIconType.Folder
    ),
    CollectionsEditItemUiState(
        id = "designer",
        title = "Book for designer",
        count = 5,
        iconType = CollectionsEditIconType.Folder
    ),
    CollectionsEditItemUiState(
        id = "want-to-read",
        title = "Want to Read",
        count = 36,
        iconType = CollectionsEditIconType.Heart
    ),
    CollectionsEditItemUiState(
        id = "purchased",
        title = "Purchased book",
        count = 18,
        iconType = CollectionsEditIconType.Purchased,
        isHighlighted = true
    ),
    CollectionsEditItemUiState(
        id = "finished",
        title = "Finished",
        count = 12,
        iconType = CollectionsEditIconType.Finished,
        isHighlighted = true
    ),
    CollectionsEditItemUiState(
        id = "audiobook",
        title = "Audiobook",
        count = 0,
        iconType = CollectionsEditIconType.Audiobook,
        isHighlighted = true
    )
)

@Composable
fun CollectionsEdit(
    modifier: Modifier = Modifier,
    collections: List<CollectionsEditItemUiState> = defaultCollectionItems,
    onBackClick: () -> Unit = {},
    onCollectionClick: (CollectionsEditItemUiState) -> Unit = {},
    onCollectionDelete: (CollectionsEditItemUiState) -> Unit = {},
    onNewCollectionClick: () -> Unit = {}
) {
    CollectionsEditScreen(
        modifier = modifier,
        collections = collections,
        onBackClick = onBackClick,
        onCollectionClick = onCollectionClick,
        onCollectionDelete = onCollectionDelete,
        onNewCollectionClick = onNewCollectionClick
    )
}

@Composable
fun CollectionsEditScreen(
    modifier: Modifier = Modifier,
    collections: List<CollectionsEditItemUiState> = defaultCollectionItems,
    onBackClick: () -> Unit = {},
    onCollectionClick: (CollectionsEditItemUiState) -> Unit = {},
    onCollectionDelete: (CollectionsEditItemUiState) -> Unit = {},
    onNewCollectionClick: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberCollectionsEditMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val visibleCollections = remember(collections) {
            mutableStateListOf<CollectionsEditItemUiState>().apply {
                addAll(collections)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            CollectionsEditBackButton(
                metrics = metrics,
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = "Collections",
                color = CollectionsEditText,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize * 1.1f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.subtitleTopSpacing))

            Text(
                text = "Synthesize favorite books your way. Everyone can see and share this collection",
                color = CollectionsEditText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = metrics.subtitleSize,
                    lineHeight = metrics.subtitleLineHeight,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.listTopSpacing))

            visibleCollections.forEach { item ->
                CollectionsEditSwipeRow(
                    item = item,
                    metrics = metrics,
                    onClick = { onCollectionClick(item) },
                    onDelete = {
                        visibleCollections.remove(item)
                        onCollectionDelete(item)
                    }
                )
            }

            Spacer(modifier = Modifier.height(metrics.newCollectionTopSpacing))

            CollectionsEditNewCollectionRow(
                metrics = metrics,
                onClick = onNewCollectionClick
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun CollectionsEditBackButton(
    metrics: CollectionsEditMetrics,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(metrics.backButtonSize)
            .clip(CircleShape)
            .background(CollectionsEditBackBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_back),
            contentDescription = "Back",
            tint = ColorPrimary,
            modifier = Modifier.size(metrics.backIconSize)
        )
    }
}

@Composable
private fun CollectionsEditSwipeRow(
    item: CollectionsEditItemUiState,
    metrics: CollectionsEditMetrics,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val density = LocalDensity.current
    val deleteWidthPx = with(density) { metrics.deleteWidth.toPx() }
    var offsetX by remember(item.id) { mutableStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        if (item.canDelete) {
            offsetX = (offsetX + delta).coerceIn(-deleteWidthPx, 0f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.rowHeight)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(metrics.deleteButtonSize)
                .clip(RoundedCornerShape(12.dp))
                .background(CollectionsEditDelete)
                .clickable(enabled = item.canDelete, onClick = onDelete),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = "Delete collection",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        CollectionsEditRowContent(
            item = item,
            metrics = metrics,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    enabled = item.canDelete,
                    onDragStopped = {
                        offsetX = when {
                            offsetX <= -deleteWidthPx * 0.82f -> {
                                onDelete()
                                0f
                            }

                            offsetX <= -deleteWidthPx * 0.34f -> -deleteWidthPx
                            else -> 0f
                        }
                    }
                )
                .clickable(onClick = onClick)
        )
    }
}

@Composable
private fun CollectionsEditRowContent(
    item: CollectionsEditItemUiState,
    metrics: CollectionsEditMetrics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(metrics.rowHeight)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconFor(item.iconType)),
                contentDescription = null,
                tint = if (item.iconType == CollectionsEditIconType.Folder) {
                    ColorPrimary
                } else if (item.iconType == CollectionsEditIconType.Finished) {
                    ColorPrimary
                } else {
                    if (item.isHighlighted) ColorPrimary else CollectionsEditText
                },
                modifier = Modifier.size(metrics.rowIconSize)
            )

            Spacer(modifier = Modifier.width(13.dp))

            Text(
                text = item.title,
                color = if (item.isHighlighted) ColorPrimary else CollectionsEditText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.rowTitleSize,
                    lineHeight = metrics.rowTitleSize * 1.25f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                ),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = item.count.toString(),
                color = CollectionsEditText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.rowCountSize,
                    lineHeight = metrics.rowCountSize,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = null,
                tint = CollectionsEditMuted,
                modifier = Modifier
                    .size(metrics.chevronSize)
                    .graphicsLayer { rotationZ = 180f }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp)
                .height(0.5.dp)
                .background(CollectionsEditDivider)
        )
    }
}

@Composable
private fun CollectionsEditNewCollectionRow(
    metrics: CollectionsEditMetrics,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.rowHeight)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_plus),
            contentDescription = null,
            tint = CollectionsEditCreate,
            modifier = Modifier.size(metrics.rowIconSize)
        )

        Spacer(modifier = Modifier.width(13.dp))

        Text(
            text = "New Collection",
            color = ColorPrimary,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.rowTitleSize,
                lineHeight = metrics.rowTitleSize * 1.25f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

private fun iconFor(type: CollectionsEditIconType): DrawableResource {
    return when (type) {
        CollectionsEditIconType.Folder -> Res.drawable.ic_add_collection
        CollectionsEditIconType.Heart -> Res.drawable.ic_bookmark
        CollectionsEditIconType.Purchased -> Res.drawable.ic_purchased
        CollectionsEditIconType.Finished -> Res.drawable.ic_done
        CollectionsEditIconType.Audiobook -> Res.drawable.ic_book_open
    }
}

@Composable
private fun rememberCollectionsEditMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): CollectionsEditMetrics {
    val compact = maxWidth < 370.dp
    val short = maxHeight < 760.dp

    return remember(maxWidth, maxHeight) {
        CollectionsEditMetrics(
            horizontalPadding = if (compact) 24.dp else 29.dp,
            topSpacing = if (short) 33.dp else 48.dp,
            backButtonSize = if (compact) 44.dp else 46.dp,
            backIconSize = if (compact) 31.dp else 33.dp,
            titleTopSpacing = if (short) 26.dp else 34.dp,
            titleSize = if (compact) 37.sp else 40.sp,
            subtitleTopSpacing = 19.dp,
            subtitleSize = if (compact) 14.sp else 15.sp,
            subtitleLineHeight = if (compact) 22.sp else 24.sp,
            listTopSpacing = if (short) 30.dp else 36.dp,
            rowHeight = if (short) 59.dp else 62.dp,
            rowIconSize = 22.dp,
            rowTitleSize = if (compact) 18.sp else 19.sp,
            rowCountSize = if (compact) 18.sp else 19.sp,
            chevronSize = 23.dp,
            deleteWidth = 78.dp,
            deleteButtonSize = 50.dp,
            newCollectionTopSpacing = 12.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionsEditScreenPreview() {
    DZTheme {
        CollectionsEditScreen()
    }
}
