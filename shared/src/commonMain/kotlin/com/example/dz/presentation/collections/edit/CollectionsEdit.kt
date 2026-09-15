package com.example.dz.presentation.collections.edit

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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCoverGradients
import com.example.dz.designsystem.components.organic.OrganicIconButton
import com.example.dz.designsystem.components.organic.OrganicMultilineField
import com.example.dz.designsystem.components.organic.OrganicPill
import com.example.dz.designsystem.components.organic.OrganicTextField
import com.example.dz.designsystem.components.organic.OrganicToggle
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3

private val previewUiState = CollectionsEditUiState(
    collectionId = "winter-nights",
    name = "Winter nights",
    description = "Slow, cold, and a little haunted. For the months when it goes dark at four.",
    visibleToFriends = true,
    books = listOf(
        CollectionsEditBookUi("mexican-gothic", "Mexican Gothic", "Silvia Moreno-Garcia", coverRes = Res.drawable.book_cover),
        CollectionsEditBookUi("piranesi", "Piranesi", "Susanna Clarke", coverRes = Res.drawable.book_cover_2),
        CollectionsEditBookUi("the-bear", "The Bear", "Andrew Krivak", coverRes = Res.drawable.book_cover_3),
    )
)

private val shelfColors = listOf(
    OrganicColors.accent2_400,
    OrganicColors.accent400,
    OrganicColors.neutral400,
    OrganicColors.accent2_700,
    OrganicColors.accent700,
)

@Composable
fun CollectionsEdit(
    uiState: CollectionsEditUiState = previewUiState,
    onEvent: (CollectionsEditEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OrganicColors.bg)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Cancel",
                modifier = Modifier.weight(1f).clickable { onEvent(CollectionsEditEvent.BackClicked) },
                fontFamily = organicBodyFontFamily(),
                fontSize = 15.sp,
                color = OrganicColors.neutral700
            )
            Text(
                text = "Edit collection",
                fontFamily = organicDisplayFontFamily(),
                fontSize = 20.sp,
                color = OrganicColors.text
            )
            Text(
                text = "Save",
                modifier = Modifier.weight(1f).clickable { onEvent(CollectionsEditEvent.SaveClicked) },
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = OrganicColors.accent700,
                textAlign = TextAlign.End,
            )
        }

        FieldLabel("Name")
        OrganicTextField(
            value = uiState.name,
            onValueChange = { onEvent(CollectionsEditEvent.NameChanged(it)) },
            placeholder = "Collection name",
        )

        FieldLabel("Description")
        OrganicMultilineField(
            value = uiState.description,
            onValueChange = { onEvent(CollectionsEditEvent.DescriptionChanged(it)) },
            placeholder = "What's this shelf for?",
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FieldLabel("Shelf colour")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                shelfColors.forEachIndexed { index, color ->
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (index == 0) Modifier.border(3.dp, OrganicColors.neutral900, CircleShape)
                                else Modifier
                            )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(OrganicColors.neutral100, RoundedCornerShape(28.dp))
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = "Shared with friends",
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = OrganicColors.text
                )
                Text(
                    text = "Maya can add books",
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = OrganicColors.neutral700
                )
            }
            OrganicToggle(
                checked = uiState.visibleToFriends,
                onCheckedChange = { onEvent(CollectionsEditEvent.VisibilityChanged(it)) },
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "BOOKS · DRAG TO REORDER",
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    color = OrganicColors.neutral600
                )
                Text(
                    text = "Add",
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = OrganicColors.accent700
                )
            }
            uiState.books.forEach { book ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OrganicColors.neutral100, RoundedCornerShape(999.dp))
                        .padding(start = 12.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = OrganicIcons.DragHandle,
                        contentDescription = null,
                        tint = OrganicColors.neutral500,
                        modifier = Modifier.size(16.dp)
                    )
                    OrganicBookCover(
                        modifier = Modifier.size(width = 30.dp, height = 44.dp),
                        gradient = OrganicCoverGradients.forIndex(book.id.hashCode()),
                        radius = 7.dp,
                    )
                    Text(
                        text = book.title,
                        modifier = Modifier.weight(1f),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = OrganicColors.text
                    )
                    OrganicIconButton(
                        icon = OrganicIcons.Minus,
                        onClick = { onEvent(CollectionsEditEvent.BookRemoved(book.id)) },
                        size = 30.dp,
                        background = OrganicColors.neutral200,
                        tint = OrganicColors.neutral800,
                        iconSize = 14.dp,
                    )
                }
            }
            OrganicPill(
                text = "Delete collection",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onEvent(CollectionsEditEvent.DeleteClicked) },
                background = OrganicColors.accent2_100,
                textColor = OrganicColors.accent2_900,
                fontSize = 13.sp,
                horizontalPadding = 16.dp,
                verticalPadding = 8.dp,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontFamily = organicBodyFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 1.sp,
        color = OrganicColors.neutral600
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CollectionsEditPreview() {
    CollectionsEdit()
}
