package com.example.dz.presentation.collections.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicBackButton
import com.example.dz.designsystem.components.organic.OrganicCoverGradients
import com.example.dz.designsystem.components.organic.OrganicIconButton
import com.example.dz.designsystem.components.organic.OrganicPill
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.olive_again_book

private val previewUiState = CollectionsUiState(
    collections = listOf(
        CollectionUiState("winter-nights", "Winter nights", 12, listOf(CollectionCoverUi(coverRes = Res.drawable.book_cover), CollectionCoverUi(coverRes = Res.drawable.olive_again_book), CollectionCoverUi(coverRes = Res.drawable.book_cover_3))),
        CollectionUiState("read-with-maya", "Read with Maya", 5, listOf(CollectionCoverUi(coverRes = Res.drawable.book_cover_2), CollectionCoverUi(coverRes = Res.drawable.book_cover_3))),
        CollectionUiState("reread-someday", "Reread someday", 7, listOf(CollectionCoverUi(coverRes = Res.drawable.book_cover_3))),
    )
)

@Composable
fun CollectionsScreen(
    uiState: CollectionsUiState = previewUiState,
    onEvent: (CollectionsEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OrganicColors.bg)
            .statusBarsPadding()
            .padding(bottom = 104.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OrganicBackButton(onClick = { onEvent(CollectionsEvent.BackClicked) }, size = 38.dp)
            Text(
                text = "Collections",
                modifier = Modifier.weight(1f),
                fontFamily = organicDisplayFontFamily(),
                fontSize = 30.sp,
                color = OrganicColors.text
            )
            OrganicIconButton(
                icon = OrganicIcons.Plus,
                onClick = { onEvent(CollectionsEvent.NewCollectionClicked) },
                size = 38.dp,
                background = OrganicColors.accent,
                tint = Color.White,
                iconSize = 18.dp,
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            uiState.collections.forEachIndexed { index, collection ->
                CollectionRow(
                    collection = collection,
                    shared = index == 1,
                    onClick = { onEvent(CollectionsEvent.CollectionClicked(collection.id)) },
                )
            }
            NewCollectionRow(onClick = { onEvent(CollectionsEvent.NewCollectionClicked) })
        }
    }
}

@Composable
private fun CollectionRow(
    collection: CollectionUiState,
    shared: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(OrganicColors.neutral100, RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(width = 78.dp, height = 96.dp)) {
            collection.covers.take(3).forEachIndexed { j, _ ->
                val (start, top) = when (j) {
                    0 -> 0.dp to 0.dp
                    1 -> 8.dp to 3.dp
                    else -> 16.dp to 6.dp
                }
                val gradient = OrganicCoverGradients.forIndex(collection.id.hashCode() + j)
                Box(
                    modifier = Modifier
                        .offset(x = start, y = top)
                        .size(width = 60.dp, height = 92.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                listOf(gradient.first, gradient.second)
                            )
                        )
                )
            }
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = collection.name,
                fontFamily = organicDisplayFontFamily(),
                fontSize = 20.sp,
                lineHeight = 22.sp,
                color = OrganicColors.text
            )
            Text(
                text = "${collection.bookCount} books",
                fontFamily = organicBodyFontFamily(),
                fontSize = 13.sp,
                color = OrganicColors.neutral700
            )
            OrganicPill(
                text = if (shared) "2 readers" else "Private",
                background = if (shared) OrganicColors.accent2_200 else OrganicColors.accent200,
                textColor = if (shared) OrganicColors.accent2_900 else OrganicColors.accent900,
                fontSize = 11.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                horizontalPadding = 11.dp,
                verticalPadding = 5.dp,
            )
        }
        Icon(
            imageVector = OrganicIcons.ChevronRight,
            contentDescription = null,
            tint = OrganicColors.neutral600,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun NewCollectionRow(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
    ) {
        val lineColor = OrganicColors.accent400
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = lineColor,
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx())
            )
        }
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(OrganicColors.accent200),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = OrganicIcons.Plus,
                    contentDescription = null,
                    tint = OrganicColors.accent800,
                    modifier = Modifier.size(17.dp)
                )
            }
            Text(
                text = "New collection",
                fontFamily = organicBodyFontFamily(),
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                fontSize = 15.sp,
                color = OrganicColors.accent800
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CollectionsScreenPreview() {
    CollectionsScreen()
}
