package com.example.dz.presentation.collections.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicField
import com.example.dz.designsystem.components.organic.OrganicSectionLabel
import com.example.dz.designsystem.components.organic.OrganicShelfColorPicker
import com.example.dz.designsystem.components.organic.OrganicToggle
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.presentation.common.uniqueLazyKeys
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.collection_add_books
import dz.shared.generated.resources.collection_books_reorder
import dz.shared.generated.resources.collection_cancel
import dz.shared.generated.resources.collection_colour
import dz.shared.generated.resources.collection_delete
import dz.shared.generated.resources.collection_description
import dz.shared.generated.resources.collection_description_hint
import dz.shared.generated.resources.collection_edit_title
import dz.shared.generated.resources.collection_name
import dz.shared.generated.resources.collection_name_hint
import dz.shared.generated.resources.collection_new_title
import dz.shared.generated.resources.collection_remove_book
import dz.shared.generated.resources.collection_save
import dz.shared.generated.resources.collection_shared
import dz.shared.generated.resources.collection_shared_off
import dz.shared.generated.resources.collection_shared_on
import org.jetbrains.compose.resources.stringResource

/**
 * Collection edit — and collection *create*, which is the same screen with nothing in it yet.
 *
 * The handoff routes "New collection" here rather than to a second form, and the view model already
 * worked that way: an id it reads as "this shelf does not exist yet" makes Save create instead of
 * update. Only the title changes between the two.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun CollectionsEditScreen(
    uiState: CollectionsEditUiState = CollectionsEditUiState(),
    onEvent: (CollectionsEditEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val keys = uiState.books.uniqueLazyKeys { it.id }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(OrganicColors.bg)
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = ORGANIC_GUTTER,
                end = ORGANIC_GUTTER,
                top = 12.dp,
                bottom = ORGANIC_TAB_BAR_CLEARANCE,
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(key = "bar") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.collection_cancel),
                        modifier = Modifier.clickable(role = Role.Button) {
                            onEvent(CollectionsEditEvent.BackClicked)
                        },
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 15.sp,
                        color = OrganicColors.neutral700
                    )
                    Text(
                        text = stringResource(
                            if (uiState.isNewCollection) {
                                Res.string.collection_new_title
                            } else {
                                Res.string.collection_edit_title
                            }
                        ),
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        color = OrganicColors.text
                    )
                    Text(
                        text = stringResource(Res.string.collection_save),
                        modifier = Modifier.clickable(role = Role.Button) {
                            onEvent(CollectionsEditEvent.SaveClicked)
                        },
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = OrganicColors.accent700
                    )
                }
            }

            item(key = "name") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OrganicSectionLabel(text = stringResource(Res.string.collection_name))
                    OrganicField(
                        value = uiState.name,
                        onValueChange = { onEvent(CollectionsEditEvent.NameChanged(it)) },
                        placeholder = stringResource(Res.string.collection_name_hint),
                    )
                }
            }

            item(key = "description") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OrganicSectionLabel(text = stringResource(Res.string.collection_description))
                    OrganicField(
                        value = uiState.description,
                        onValueChange = { onEvent(CollectionsEditEvent.DescriptionChanged(it)) },
                        placeholder = stringResource(Res.string.collection_description_hint),
                        singleLine = false,
                        minHeight = 78.dp,
                    )
                }
            }

            item(key = "colour") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OrganicSectionLabel(text = stringResource(Res.string.collection_colour))
                    OrganicShelfColorPicker(
                        selectedIndex = uiState.colorIndex,
                        onSelect = { onEvent(CollectionsEditEvent.ColorSelected(it)) },
                    )
                }
            }

            item(key = "shared") {
                OrganicCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.collection_shared),
                                fontFamily = organicBodyFontFamily(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = OrganicColors.text
                            )
                            Text(
                                text = stringResource(
                                    if (uiState.visibleToFriends) {
                                        Res.string.collection_shared_on
                                    } else {
                                        Res.string.collection_shared_off
                                    }
                                ),
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
                }
            }

            if (uiState.books.isNotEmpty()) {
                item(key = "books-label") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        OrganicSectionLabel(text = stringResource(Res.string.collection_books_reorder))
                        Text(
                            text = stringResource(Res.string.collection_add_books),
                            modifier = Modifier.clickable(role = Role.Button) {
                                onEvent(CollectionsEditEvent.BackClicked)
                            },
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = OrganicColors.accent700
                        )
                    }
                }
                items(uiState.books.size, key = { keys[it] }) { index ->
                    val book = uiState.books[index]
                    EditBookRow(
                        book = book,
                        modifier = Modifier.padding(bottom = 10.dp),
                        onRemove = { onEvent(CollectionsEditEvent.BookRemoved(book.id)) },
                    )
                }
            }

            if (!uiState.isNewCollection) {
                item(key = "delete") {
                    Text(
                        text = stringResource(Res.string.collection_delete),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(role = Role.Button) { onEvent(CollectionsEditEvent.DeleteClicked) }
                            .padding(vertical = 12.dp),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = OrganicColors.danger
                    )
                }
            }
        }
    }
}

/**
 * A book on the shelf being edited: a pill rather than a card, so the list reads as something
 * being rearranged rather than as content.
 *
 * The design draws a drag handle. Reordering is not wired — nothing persists an order — so the
 * handle is left off rather than drawn as a control that does not move anything.
 */
@Composable
private fun EditBookRow(
    book: CollectionsEditBookUi,
    modifier: Modifier = Modifier,
    onRemove: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral100)
            .padding(start = 12.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganicBookCover(
            title = book.title,
            coverUrl = book.coverUrl,
            width = 30.dp,
            height = 44.dp,
            cornerRadius = 7.dp,
        )
        Text(
            text = book.title,
            modifier = Modifier.weight(1f),
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = OrganicColors.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(OrganicColors.neutral200)
                .clickable(role = Role.Button, onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = OrganicIcons.Close,
                contentDescription = stringResource(Res.string.collection_remove_book),
                tint = OrganicColors.neutral800,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview
@Composable
fun CollectionsEditScreenPreview() {
    CollectionsEditScreen()
}
