package com.example.dz.presentation.collections.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.presentation.collections.edit.CollectionsEditBookUi
import com.example.dz.presentation.common.uniqueLazyKeys
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.collection_picker_done
import dz.shared.generated.resources.collection_picker_empty
import dz.shared.generated.resources.collection_picker_title
import org.jetbrains.compose.resources.stringResource

/**
 * The shelf, offered as somewhere to pick from.
 *
 * Picking happens here rather than through Search — which is where the handoff sends "Add" —
 * because what a shelf is for is arranging books you already own, and reaching those through a
 * search of the whole store would be the long way round to a list the app already has. Search
 * stays the right door for a book that is not in the library yet; this is the one for the rest.
 *
 * Every row is a toggle, ticked when the book is on the shelf, so the same sheet takes books off
 * as puts them on. Shared between Collection details and Collection edit — both open the same
 * picker against the same library list, so there is exactly one of these to keep in step.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryPickerSheet(
    books: List<CollectionsEditBookUi>,
    pickedIds: Set<String>,
    isLoading: Boolean,
    onToggle: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val allowedSheetValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    val sheetState = rememberBottomSheetState(SheetValue.Hidden, allowedSheetValues)
    val keys = books.uniqueLazyKeys { it.id }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = OrganicColors.bg,
        scrimColor = OrganicColors.neutral900.copy(alpha = 0.42f),
        dragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .background(OrganicColors.neutral300)
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.82f)
                .navigationBarsPadding()
                .padding(horizontal = ORGANIC_GUTTER)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.collection_picker_title),
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    color = OrganicColors.text
                )
                Text(
                    text = stringResource(Res.string.collection_picker_done),
                    modifier = Modifier.clickable(role = Role.Button, onClick = onDismiss),
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = OrganicColors.accent700
                )
            }

            when {
                isLoading -> Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OrganicColors.accent)
                }

                books.isEmpty() -> Text(
                    text = stringResource(Res.string.collection_picker_empty),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 14.sp,
                    color = OrganicColors.neutral700
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(books.size, key = { keys[it] }) { index ->
                        val book = books[index]
                        PickerBookRow(
                            book = book,
                            picked = book.id in pickedIds,
                            modifier = Modifier.padding(bottom = 10.dp),
                            onClick = { onToggle(book.id) },
                        )
                    }
                }
            }
        }
    }
}

/** One candidate. The tick is the whole state: on the shelf, or not. */
@Composable
private fun PickerBookRow(
    book: CollectionsEditBookUi,
    picked: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(OrganicShape.radiusMd))
            .background(if (picked) OrganicColors.accent100 else OrganicColors.neutral100)
            .clickable(role = Role.Checkbox, onClick = onClick)
            .padding(start = 12.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganicBookCover(
            title = book.title,
            coverUrl = book.coverUrl,
            width = 34.dp,
            height = 50.dp,
            cornerRadius = 7.dp,
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.title,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = OrganicColors.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.author,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(if (picked) OrganicColors.accent else OrganicColors.neutral200),
            contentAlignment = Alignment.Center
        ) {
            if (picked) {
                Icon(
                    imageVector = OrganicIcons.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}
