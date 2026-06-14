package com.example.dz.presentation.collections.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.coll_loved
import dz.shared.generated.resources.coll_purchased
import dz.shared.generated.resources.coll_saved_for_later
import dz.shared.generated.resources.coll_smart_shelves
import dz.shared.generated.resources.coll_subtitle
import dz.shared.generated.resources.library_collections
import dz.shared.generated.resources.new_collection
import dz.shared.generated.resources.olive_again_book
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class CollectionUiState(
    val id: String,
    val name: String,
    val bookCount: Int,
    val covers: List<DrawableResource>
)

private val defaultCollections = listOf(
    CollectionUiState("quiet-novels", "Quiet novels", 12, listOf(Res.drawable.olive_again_book, Res.drawable.book_cover_3, Res.drawable.book_cover)),
    CollectionUiState("for-the-train", "For the train", 5, listOf(Res.drawable.book_cover_2, Res.drawable.book_cover_4)),
    CollectionUiState("lent-to-friends", "Lent to friends", 3, listOf(Res.drawable.book_cover_3))
)

@Composable
fun Collections(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNewCollectionClick: () -> Unit = {},
    onCollectionClick: (collectionId: String) -> Unit = {}
) {
    CollectionsScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onSettingsClick = onSettingsClick,
        onNewCollectionClick = onNewCollectionClick,
        onCollectionClick = onCollectionClick
    )
}

@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    collections: List<CollectionUiState> = defaultCollections,
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNewCollectionClick: () -> Unit = {},
    onCollectionClick: (collectionId: String) -> Unit = {}
) {
    val colors = inkColors()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        InkTopBar(
            title = stringResource(Res.string.library_collections),
            subtitle = stringResource(Res.string.coll_subtitle),
            onBackClick = onBackClick,
            right = { InkIconButton(icon = InkIcons.Search, onClick = onSettingsClick, colors = colors) },
            colors = colors
        )

        // collection grid: cards plus the trailing "new collection" tile
        Column(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val cells: List<CollectionUiState?> = collections + null
            cells.chunked(2).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowItems.forEach { collection ->
                        if (collection != null) {
                            CollectionCard(
                                collection = collection,
                                onClick = { onCollectionClick(collection.id) },
                                modifier = Modifier.weight(1f),
                                colors = colors
                            )
                        } else {
                            NewCollectionTile(
                                onClick = onNewCollectionClick,
                                modifier = Modifier.weight(1f),
                                colors = colors
                            )
                        }
                    }
                    if (rowItems.size == 1) Box(modifier = Modifier.weight(1f))
                }
            }
        }

        // smart shelves
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
            InkLabel(text = stringResource(Res.string.coll_smart_shelves), colors = colors)
            Column(modifier = Modifier.padding(top = 6.dp)) {
                SmartShelfRow(InkIcons.Bookmark, stringResource(Res.string.coll_saved_for_later), "8 books", colors)
                HorizontalDivider(thickness = 1.dp, color = colors.line)
                SmartShelfRow(InkIcons.Purchased, stringResource(Res.string.coll_purchased), "14 books", colors)
                HorizontalDivider(thickness = 1.dp, color = colors.line)
                SmartShelfRow(InkIcons.Favorite, stringResource(Res.string.coll_loved), "6 books", colors)
            }
        }
    }
}

@Composable
private fun CollectionCard(
    collection: CollectionUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: InkColors,
) {
    Column(
        modifier = modifier
            .inkCard(colors)
            .clickable(onClick = onClick)
            .padding(13.dp)
    ) {
        Box {
            // draw right-to-left so the leftmost cover sits on top, like the design
            collection.covers.indices.reversed().forEach { j ->
                Image(
                    painter = painterResource(collection.covers[j]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .offset(x = (28 * j).dp)
                        .size(width = 44.dp, height = 64.dp)
                        .clip(RoundedCornerShape(InkShape.cover - 1.dp))
                        .border(2.dp, colors.surface, RoundedCornerShape(InkShape.cover - 1.dp))
                )
            }
            Spacer(modifier = Modifier.size(width = (44 + 28 * (collection.covers.size - 1)).dp, height = 64.dp))
        }
        Text(
            text = collection.name,
            modifier = Modifier.padding(top = 12.dp),
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 14.5.sp,
            lineHeight = 18.sp,
            color = colors.ink
        )
        Text(
            text = "${collection.bookCount} books",
            modifier = Modifier.padding(top = 5.dp),
            fontFamily = inkBodyFontFamily(),
            fontSize = 11.sp,
            color = colors.muted
        )
    }
}

@Composable
private fun NewCollectionTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: InkColors,
) {
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 128.dp)
            .clip(RoundedCornerShape(InkShape.radius))
            .clickable(onClick = onClick)
    ) {
        // dashed hairline border
        val lineColor = colors.line
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = lineColor,
                style = Stroke(
                    width = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f))
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(InkShape.radius.toPx())
            )
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(colors.accentSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = InkIcons.Plus,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = stringResource(Res.string.new_collection),
                fontFamily = inkBodyFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = colors.accent
            )
        }
    }
}

@Composable
private fun SmartShelfRow(
    icon: ImageVector,
    title: String,
    count: String,
    colors: InkColors,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(InkShape.radiusSm))
                .background(colors.alt),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(16.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = inkBodyFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.5.sp,
                color = colors.ink
            )
            Text(
                text = count,
                modifier = Modifier.padding(top = 4.dp),
                fontFamily = inkBodyFontFamily(),
                fontSize = 11.sp,
                color = colors.muted
            )
        }
        Icon(
            imageVector = InkIcons.Back,
            contentDescription = null,
            tint = colors.muted,
            modifier = Modifier
                .size(14.dp)
                .graphicsLayer { rotationZ = 180f }
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun CollectionsScreenPreview() {
    CollectionsScreen()
}
