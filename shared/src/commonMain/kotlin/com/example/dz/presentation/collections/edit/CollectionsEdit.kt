package com.example.dz.presentation.collections.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkField
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkToggle
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.coll_books_reorder
import dz.shared.generated.resources.coll_delete
import dz.shared.generated.resources.coll_description
import dz.shared.generated.resources.coll_edit_title
import dz.shared.generated.resources.coll_name
import dz.shared.generated.resources.coll_save
import dz.shared.generated.resources.coll_visible
import dz.shared.generated.resources.coll_visible_sub
import dz.shared.generated.resources.olive_again_book
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class EditableBook(
    val title: String,
    val author: String,
    val coverRes: DrawableResource
)

private val initialBooks = listOf(
    EditableBook("Olive, Again", "Elizabeth Strout", Res.drawable.olive_again_book),
    EditableBook("Red at the Bone", "Jacqueline Woodson", Res.drawable.book_cover_3),
    EditableBook("Mexican Gothic", "Silvia Moreno-Garcia", Res.drawable.book_cover)
)

@Composable
fun CollectionsEdit(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val colors = inkColors()
    val bodyFont = inkBodyFontFamily()

    var name by remember { mutableStateOf("Quiet novels") }
    var description by remember { mutableStateOf("Small lives, carefully observed — the books I reach for on slow evenings.") }
    var visibleToFriends by remember { mutableStateOf(true) }
    val books = remember { initialBooks.toMutableStateList() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 130.dp)
        ) {
            InkTopBar(
                title = stringResource(Res.string.coll_edit_title),
                onBackClick = onBackClick,
                right = {
                    Text(
                        text = stringResource(Res.string.coll_save),
                        modifier = Modifier
                            .clickable(onClick = onSaveClick)
                            .padding(6.dp),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = colors.accent
                    )
                },
                colors = colors
            )

            // name
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 8.dp)) {
                InkLabel(text = stringResource(Res.string.coll_name), colors = colors)
                Box(modifier = Modifier.padding(top = 10.dp)) {
                    InkField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "",
                        colors = colors
                    )
                }
            }

            // description (multiline)
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 20.dp)) {
                InkLabel(text = stringResource(Res.string.coll_description), colors = colors)
                BasicTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .defaultMinSize(minHeight = 70.dp)
                        .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.line, RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .padding(horizontal = 15.dp, vertical = 13.dp),
                    textStyle = TextStyle(
                        fontFamily = bodyFont,
                        fontSize = 13.sp,
                        lineHeight = 21.sp,
                        color = colors.ink
                    ),
                    cursorBrush = SolidColor(colors.accent)
                )
            }

            // visibility toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, end = 22.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.coll_visible),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp,
                        color = colors.ink
                    )
                    Text(
                        text = stringResource(Res.string.coll_visible_sub),
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = bodyFont,
                        fontSize = 11.5.sp,
                        color = colors.muted
                    )
                }
                InkToggle(
                    checked = visibleToFriends,
                    onCheckedChange = { visibleToFriends = it },
                    colors = colors
                )
            }

            // books
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
                InkLabel(text = stringResource(Res.string.coll_books_reorder), colors = colors)
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    books.forEachIndexed { i, book ->
                        if (i > 0) {
                            HorizontalDivider(thickness = 1.dp, color = colors.line)
                        }
                        EditableBookRow(
                            book = book,
                            onRemove = { books.remove(book) },
                            colors = colors
                        )
                    }
                }
            }
        }

        // pinned delete bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(colors.paper)
        ) {
            HorizontalDivider(thickness = 1.dp, color = colors.line)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(top = 14.dp, bottom = 18.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .clickable(onClick = onDeleteClick)
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = InkIcons.Delete,
                        contentDescription = null,
                        tint = colors.danger,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = stringResource(Res.string.coll_delete),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = colors.danger
                    )
                }
            }
        }
    }
}

@Composable
private fun EditableBookRow(
    book: EditableBook,
    onRemove: () -> Unit,
    colors: InkColors,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Icon(
            imageVector = InkIcons.Move,
            contentDescription = null,
            tint = colors.muted,
            modifier = Modifier.size(15.dp)
        )
        Image(
            painter = painterResource(book.coverRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 38.dp, height = 56.dp)
                .clip(RoundedCornerShape(InkShape.cover - 1.dp))
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = book.title,
                fontFamily = inkDisplayFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colors.ink
            )
            Text(
                text = book.author,
                modifier = Modifier.padding(top = 4.dp),
                fontFamily = inkBodyFontFamily(),
                fontSize = 11.5.sp,
                color = colors.muted
            )
        }
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .border(1.dp, colors.line, CircleShape)
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = InkIcons.Close,
                contentDescription = null,
                tint = colors.muted,
                modifier = Modifier.size(10.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun CollectionsEditPreview() {
    CollectionsEdit()
}
