package com.example.dz.presentation.collections.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCoverGradients
import com.example.dz.designsystem.components.organic.OrganicIconButton
import com.example.dz.designsystem.components.organic.OrganicPill
import com.example.dz.designsystem.components.organic.OrganicPrimaryButton
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3

private val previewUiState = CollectionDetailsUiState(
    collectionId = "winter-nights",
    title = "Winter nights",
    description = "Slow, cold, and a little haunted. For the months when it goes dark at four.",
    bookCount = 12,
    books = listOf(
        CollectionDetailsBookUiState("mexican-gothic", "Mexican Gothic", "Silvia Moreno-Garcia", coverRes = Res.drawable.book_cover, note = "33%", noteHighlighted = true),
        CollectionDetailsBookUiState("piranesi", "Piranesi", "Susanna Clarke", coverRes = Res.drawable.book_cover_2, note = "Next"),
        CollectionDetailsBookUiState("the-bear", "The Bear", "Andrew Krivak", coverRes = Res.drawable.book_cover_3, note = "Unread"),
    )
)

@Composable
fun CollectionDetails(
    uiState: CollectionDetailsUiState = previewUiState,
    onEvent: (CollectionDetailsEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OrganicColors.bg)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(OrganicColors.accent2_200, RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
                .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OrganicIconButton(
                    icon = OrganicIcons.ChevronLeft,
                    onClick = { onEvent(CollectionDetailsEvent.BackClicked) },
                    size = 38.dp,
                    background = OrganicColors.bg,
                    tint = OrganicColors.accent2_900,
                    iconSize = 17.dp,
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 1.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        modifier = Modifier
                            .background(OrganicColors.bg, RoundedCornerShape(999.dp))
                            .clickable { onEvent(CollectionDetailsEvent.EditClicked) }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = OrganicIcons.Edit,
                            contentDescription = null,
                            tint = OrganicColors.accent2_900,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Edit",
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = OrganicColors.accent2_900
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.size(width = 104.dp, height = 126.dp)) {
                    uiState.books.take(3).forEachIndexed { j, book ->
                        val (start, top) = when (j) {
                            0 -> 0.dp to 0.dp
                            1 -> 11.dp to 4.dp
                            else -> 22.dp to 8.dp
                        }
                        OrganicBookCover(
                            modifier = Modifier
                                .padding(start = start, top = top)
                                .size(width = 78.dp, height = 122.dp),
                            gradient = OrganicCoverGradients.forIndex(book.id.hashCode()),
                            radius = 14.dp,
                            elevation = if (j == 0) 6.dp else 0.dp,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = uiState.title,
                        fontFamily = organicDisplayFontFamily(),
                        fontSize = 30.sp,
                        lineHeight = 31.sp,
                        color = OrganicColors.accent2_900
                    )
                    Text(
                        text = "${uiState.bookCount} books",
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 13.sp,
                        color = OrganicColors.accent2_900.copy(alpha = 0.8f)
                    )
                }
            }

            if (uiState.description.isNotBlank()) {
                Text(
                    text = uiState.description,
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = OrganicColors.accent2_900.copy(alpha = 0.85f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                OrganicPrimaryButton(
                    text = "Read next up",
                    onClick = { onEvent(CollectionDetailsEvent.BookClicked(uiState.books.firstOrNull()?.id.orEmpty())) },
                    modifier = Modifier.weight(1f),
                    height = 52.dp,
                    leadingIcon = OrganicIcons.Play,
                )
                OrganicIconButton(
                    icon = OrganicIcons.Share,
                    onClick = { onEvent(CollectionDetailsEvent.ShareClicked) },
                    size = 52.dp,
                    background = OrganicColors.bg,
                    tint = OrganicColors.accent2_900,
                    iconSize = 18.dp,
                )
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "IN THIS COLLECTION",
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    color = OrganicColors.neutral600
                )
                Text(
                    text = "Manual order",
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = OrganicColors.accent700
                )
            }

            uiState.books.forEachIndexed { index, book ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEvent(CollectionDetailsEvent.BookClicked(book.id)) },
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}",
                        modifier = Modifier.size(18.dp),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OrganicColors.neutral500
                    )
                    OrganicBookCover(
                        modifier = Modifier.size(width = 46.dp, height = 68.dp),
                        gradient = OrganicCoverGradients.forIndex(book.id.hashCode()),
                        radius = 9.dp,
                    )
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = book.title,
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = OrganicColors.text
                        )
                        Text(
                            text = book.author,
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 12.sp,
                            color = OrganicColors.neutral700
                        )
                    }
                    CollectionBookStatus(note = book.note, highlighted = book.noteHighlighted)
                }
            }
        }
    }
}

@Composable
private fun CollectionBookStatus(note: String, highlighted: Boolean) {
    when {
        note.isBlank() -> Unit
        note.equals("Next", ignoreCase = true) -> OrganicPill(
            text = note,
            background = OrganicColors.accent2_200,
            textColor = OrganicColors.accent2_900,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            horizontalPadding = 10.dp,
            verticalPadding = 5.dp,
        )
        note.equals("Finished", ignoreCase = true) -> OrganicPill(
            text = note,
            background = OrganicColors.neutral200,
            textColor = OrganicColors.neutral800,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            horizontalPadding = 10.dp,
            verticalPadding = 5.dp,
        )
        highlighted -> Text(
            text = note,
            fontFamily = organicBodyFontFamily(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = OrganicColors.accent700
        )
        else -> Text(
            text = note,
            fontFamily = organicBodyFontFamily(),
            fontSize = 12.sp,
            color = OrganicColors.neutral600
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CollectionDetailsPreview() {
    CollectionDetails()
}
