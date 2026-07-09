package com.example.dz.presentation.collections.details

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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkBookRow
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.remote.RemoteBookCover
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.coll_add_books
import dz.shared.generated.resources.coll_edit
import dz.shared.generated.resources.olive_again_book
import org.jetbrains.compose.resources.stringResource

private val previewUiState = CollectionDetailsUiState(
    collectionId = "quiet-novels",
    title = "Quiet novels",
    description = "“Small lives, carefully observed — the books I reach for on slow evenings.”",
    bookCount = 12,
    books = listOf(
        CollectionDetailsBookUiState("olive-again", "Olive, Again", "Elizabeth Strout", Res.drawable.olive_again_book, note = "read · 5★"),
        CollectionDetailsBookUiState("red-at-the-bone", "Red at the Bone", "Jacqueline Woodson", Res.drawable.book_cover_3, note = "reading · 62%", noteHighlighted = true),
        CollectionDetailsBookUiState("mexican-gothic", "Mexican Gothic", "Silvia Moreno-Garcia", Res.drawable.book_cover, note = "to read"),
        CollectionDetailsBookUiState("bestiary", "Bestiary", "K-Ming Chang", Res.drawable.book_cover_4, note = "to read")
    )
)

@Composable
fun CollectionDetails(
    uiState: CollectionDetailsUiState = previewUiState,
    onEvent: (CollectionDetailsEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        // top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 4.dp)
        ) {
            InkIconButton(icon = InkIcons.Back, onClick = { onEvent(CollectionDetailsEvent.BackClicked) }, colors = colors)
            Spacer(modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                InkIconButton(icon = InkIcons.Share, onClick = { onEvent(CollectionDetailsEvent.ShareClicked) }, colors = colors)
                InkIconButton(icon = InkIcons.MoreVertical, onClick = { onEvent(CollectionDetailsEvent.EditClicked) }, colors = colors)
            }
        }

        // header: fanned covers + name
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box {
                val stack = uiState.books.take(3)
                // draw right-to-left so the leftmost cover sits on top, like the design
                stack.indices.reversed().forEach { j ->
                    RemoteBookCover(
                        coverUrl = stack[j].coverUrl,
                        fallback = stack[j].coverRes,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .offset(x = (34 * j).dp)
                            .size(width = 56.dp, height = 82.dp)
                            .clip(RoundedCornerShape(InkShape.cover))
                            .border(2.dp, colors.paper, RoundedCornerShape(InkShape.cover))
                    )
                }
                // reserve the stack's visual footprint
                Spacer(modifier = Modifier.size(width = (56 + 34 * (stack.size - 1)).coerceAtLeast(56).dp, height = 82.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiState.title,
                    fontFamily = displayFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    lineHeight = 27.sp,
                    color = colors.ink
                )
                Text(
                    text = "${uiState.bookCount} books · updated recently",
                    modifier = Modifier.padding(top = 6.dp),
                    fontFamily = bodyFont,
                    fontSize = 12.sp,
                    color = colors.muted
                )
            }
        }

        // description
        if (uiState.description.isNotBlank()) {
            Text(
                text = uiState.description,
                modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 14.dp),
                fontFamily = displayFont,
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp,
                lineHeight = 21.5.sp,
                color = colors.inkSoft
            )
        }

        // actions
        Row(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .background(colors.accentSoft)
                    .clickable { onEvent(CollectionDetailsEvent.AddBooksClicked) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = InkIcons.Plus,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = stringResource(Res.string.coll_add_books),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = colors.accent
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .border(1.dp, colors.line, RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .clickable { onEvent(CollectionDetailsEvent.EditClicked) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.coll_edit),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = colors.ink
                )
            }
        }

        // book list
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 18.dp)) {
            uiState.books.forEachIndexed { i, book ->
                InkBookRow(
                    cover = book.coverRes,
                    coverUrl = book.coverUrl,
                    title = book.title,
                    author = book.author,
                    modifier = Modifier.clickable { onEvent(CollectionDetailsEvent.BookClicked(book.id)) },
                    showDivider = i > 0,
                    meta = {
                        Text(
                            text = book.note,
                            fontFamily = bodyFont,
                            fontSize = 11.sp,
                            color = if (book.noteHighlighted) colors.accent else colors.muted
                        )
                    },
                    trailing = {
                        Icon(
                            imageVector = InkIcons.MoreVertical,
                            contentDescription = null,
                            tint = colors.muted,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onEvent(CollectionDetailsEvent.BookOptionsClicked(book.id)) }
                        )
                    },
                    colors = colors
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun CollectionDetailsPreview() {
    CollectionDetails()
}
