package com.example.dz.features.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkBookRow
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.app_components.ink.InkProgressBar
import com.example.dz.app_components.ink.InkSectionTitle
import com.example.dz.app_components.ink.inkCard
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.library_collections
import dz.shared.generated.resources.library_tab_finished
import dz.shared.generated.resources.library_tab_reading
import dz.shared.generated.resources.library_tab_to_read
import dz.shared.generated.resources.library_title
import dz.shared.generated.resources.home_see_all
import dz.shared.generated.resources.olive_again_book
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class LibraryBook(
    val title: String,
    val author: String,
    val coverRes: DrawableResource,
    val tags: List<String> = emptyList(),
    val progress: String? = null,
    val timeLeft: String? = null
)

data class LibraryCollection(
    val title: String,
    val books: List<LibraryBook>
)

private val readingBooks = listOf(
    LibraryBook("Mexican Gothic", "Silvia Moreno-Garcia", Res.drawable.book_cover, progress = "62", timeLeft = "18 min left"),
    LibraryBook("Olive, Again", "Elizabeth Strout", Res.drawable.olive_again_book, progress = "31", timeLeft = "2h 40m left"),
    LibraryBook("The Archer", "Paulo Coelho", Res.drawable.book_cover_2, progress = "88", timeLeft = "12 min left")
)

private val toReadBooks = listOf(
    LibraryBook("Red at the Bone", "Jacqueline Woodson", Res.drawable.book_cover_3),
    LibraryBook("Bestiary", "K-Ming Chang", Res.drawable.book_cover_4)
)

private val finishedBooks = listOf(
    LibraryBook("The Archer", "Paulo Coelho", Res.drawable.book_cover_2),
    LibraryBook("Mexican Gothic", "Silvia Moreno-Garcia", Res.drawable.book_cover)
)

private val collections = listOf(
    LibraryCollection("Quiet novels", listOf(
        LibraryBook("Olive, Again", "Elizabeth Strout", Res.drawable.olive_again_book),
        LibraryBook("Red at the Bone", "Jacqueline Woodson", Res.drawable.book_cover_3)
    )),
    LibraryCollection("For the train", listOf(
        LibraryBook("The Archer", "Paulo Coelho", Res.drawable.book_cover_2),
        LibraryBook("Bestiary", "K-Ming Chang", Res.drawable.book_cover_4)
    ))
)

private val collectionSizes = listOf(12, 5)

@Composable
fun Library(
    onSettingsClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onBookClick: (LibraryBook) -> Unit = {},
    onGoalClick: () -> Unit = {}
) {
    LibraryScreen(
        onSettingsClick = onSettingsClick,
        onSortClick = onSortClick,
        onBookClick = onBookClick,
        onGoalClick = onGoalClick
    )
}

@Composable
fun LibraryScreen(
    onSettingsClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onBookClick: (LibraryBook) -> Unit = {},
    onGoalClick: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(Res.string.library_tab_reading),
        stringResource(Res.string.library_tab_to_read),
        stringResource(Res.string.library_tab_finished)
    )
    val tabBooks = when (selectedTab) {
        1 -> toReadBooks
        2 -> finishedBooks
        else -> readingBooks
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        // header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.library_title),
                modifier = Modifier.weight(1f),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = colors.ink
            )
            InkIconButton(icon = InkIcons.Grid, onClick = onSettingsClick, colors = colors)
        }

        // tabs
        Box(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                thickness = 1.dp,
                color = colors.line
            )
            Row(
                modifier = Modifier.padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                tabs.forEachIndexed { i, tab ->
                    val selected = i == selectedTab
                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .clickable { selectedTab = i },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = tab,
                            modifier = Modifier.padding(bottom = 11.dp),
                            fontFamily = bodyFont,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 13.5.sp,
                            color = if (selected) colors.ink else colors.muted
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(if (selected) colors.accent else androidx.compose.ui.graphics.Color.Transparent)
                        )
                    }
                }
            }
        }

        // book rows
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 6.dp)) {
            tabBooks.forEachIndexed { i, book ->
                InkBookRow(
                    cover = book.coverRes,
                    title = book.title,
                    author = book.author,
                    modifier = Modifier.clickable { onBookClick(book) },
                    showDivider = i > 0,
                    meta = if (book.progress != null) {
                        {
                            Column {
                                InkProgressBar(
                                    progress = book.progress.toFloat() / 100f,
                                    modifier = Modifier.fillMaxWidth(0.85f),
                                    colors = colors
                                )
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.SemiBold)) {
                                            append("${book.progress}%")
                                        }
                                        append(" · ${book.timeLeft.orEmpty()}")
                                    },
                                    modifier = Modifier.padding(top = 6.dp),
                                    fontFamily = bodyFont,
                                    fontSize = 11.sp,
                                    color = colors.muted
                                )
                            }
                        }
                    } else null,
                    trailing = {
                        Icon(
                            imageVector = InkIcons.MoreVertical,
                            contentDescription = null,
                            tint = colors.muted,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable(onClick = onSortClick)
                        )
                    },
                    colors = colors
                )
            }
        }

        // collections
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 20.dp)) {
            InkSectionTitle(
                text = stringResource(Res.string.library_collections),
                action = stringResource(Res.string.home_see_all),
                onActionClick = onSettingsClick,
                colors = colors
            )
            Row(
                modifier = Modifier.padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                collections.forEachIndexed { i, collection ->
                    CollectionCard(
                        collection = collection,
                        bookCount = collectionSizes[i],
                        onClick = onSettingsClick,
                        modifier = Modifier.weight(1f),
                        colors = colors
                    )
                }
            }
        }

        // reading goal banner
        Row(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(InkShape.radius))
                .background(colors.alt)
                .clickable(onClick = onGoalClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = InkIcons.Stats,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = buildAnnotatedString {
                    append("You’ve read ")
                    withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) {
                        append("26 min")
                    }
                    append(" today — 4 min to your goal.")
                },
                fontFamily = bodyFont,
                fontSize = 12.5.sp,
                lineHeight = 17.5.sp,
                color = colors.inkSoft
            )
        }
    }
}

@Composable
private fun CollectionCard(
    collection: LibraryCollection,
    bookCount: Int,
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
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            collection.books.forEach { book ->
                Image(
                    painter = painterResource(book.coverRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 40.dp, height = 58.dp)
                        .clip(RoundedCornerShape(InkShape.cover - 1.dp))
                )
            }
        }
        Text(
            text = collection.title,
            modifier = Modifier.padding(top = 11.dp),
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 13.5.sp,
            lineHeight = 16.sp,
            color = colors.ink
        )
        Text(
            text = "$bookCount books",
            modifier = Modifier.padding(top = 4.dp),
            fontFamily = inkBodyFontFamily(),
            fontSize = 11.sp,
            color = colors.muted
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun LibraryScreenPreview() {
    LibraryScreen()
}
