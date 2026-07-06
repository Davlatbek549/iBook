package com.example.dz.presentation.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkBookRow
import com.example.dz.designsystem.components.ink.InkChip
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkSectionTitle
import com.example.dz.designsystem.components.remote.RemoteBookCover
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import com.example.dz.domain.model.Book
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.home_see_all
import dz.shared.generated.resources.olive_again_book
import dz.shared.generated.resources.store_book_of_the_week
import dz.shared.generated.resources.store_new_releases
import dz.shared.generated.resources.store_title
import dz.shared.generated.resources.store_top_sellers
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class StoreBook(
    val title: String,
    val author: String,
    val coverRes: DrawableResource,
    val views: String = "",
    val tags: List<String> = emptyList(),
    val price: String = "",
    val rating: String = "4.5",
    val id: String = title,
    val coverUrl: String? = null
)

private val featuredBook = StoreBook(
    title = "Red at the Bone",
    author = "Jacqueline Woodson",
    coverRes = Res.drawable.book_cover_3,
    tags = listOf("Literary", "Family"),
    price = "7.00",
    rating = "4.5"
)

private val newReleases = listOf(
    StoreBook("The Archer", "Paulo Coelho", Res.drawable.book_cover_2, price = "8.50", rating = "4.4"),
    StoreBook("Bestiary", "K-Ming Chang", Res.drawable.book_cover_4, price = "9.20", rating = "4.2"),
    StoreBook("Olive, Again", "Elizabeth Strout", Res.drawable.olive_again_book, price = "13.00", rating = "4.3")
)

private val topSellers = listOf(
    StoreBook("Mexican Gothic", "Silvia Moreno-Garcia", Res.drawable.book_cover, price = "12.99", rating = "4.6"),
    StoreBook("Olive, Again", "Elizabeth Strout", Res.drawable.olive_again_book, price = "13.00", rating = "4.3"),
    StoreBook("Bestiary", "K-Ming Chang", Res.drawable.book_cover_4, price = "9.20", rating = "4.2")
)

private val storeCoverFallbacks = listOf(
    Res.drawable.book_cover_3,
    Res.drawable.book_cover_2,
    Res.drawable.olive_again_book,
    Res.drawable.book_cover,
    Res.drawable.book_cover_4
)

@Composable
fun StoreScreen(
    uiState: StoreUiState = StoreUiState(),
    onViewMoreClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onBookClick: (StoreBook) -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()
    val domainBooks = uiState.featuredBooks.mapIndexed { index, book -> book.toStoreBook(index) }
    val displayFeaturedBook = domainBooks.firstOrNull() ?: featuredBook
    val displayNewReleases = domainBooks.drop(1).take(3).ifEmpty { newReleases }
    val displayTopSellers = domainBooks.ifEmpty { topSellers }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        // header with credits pill
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.store_title),
                modifier = Modifier.weight(1f),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = colors.ink
            )
            Row(
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, colors.line, CircleShape)
                    .clickable(onClick = onViewMoreClick)
                    .padding(horizontal = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = InkIcons.Tag,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "240",
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colors.ink
                )
            }
        }

        // featured: book of the week
        Row(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 18.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(InkShape.radius))
                .background(colors.alt)
                .clickable { onBookClick(displayFeaturedBook) }
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RemoteBookCover(
                coverUrl = displayFeaturedBook.coverUrl,
                fallback = displayFeaturedBook.coverRes,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 84.dp, height = 124.dp)
                    .shadow(12.dp, RoundedCornerShape(InkShape.cover), clip = true)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.store_book_of_the_week).uppercase(),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.5.sp,
                    letterSpacing = 1.5.sp,
                    color = colors.accent
                )
                Text(
                    text = displayFeaturedBook.title,
                    modifier = Modifier.padding(top = 8.dp),
                    fontFamily = displayFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp,
                    lineHeight = 23.sp,
                    color = colors.ink
                )
                Text(
                    text = displayFeaturedBook.author,
                    modifier = Modifier.padding(top = 4.dp),
                    fontFamily = bodyFont,
                    fontSize = 12.sp,
                    color = colors.muted
                )
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "$${displayFeaturedBook.price}",
                        fontFamily = displayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = colors.ink
                    )
                    Text(
                        text = "$10.00",
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        fontFamily = bodyFont,
                        fontSize = 12.sp,
                        color = colors.muted
                    )
                    InkChip(text = "−30%", solid = true, colors = colors)
                }
            }
        }

        // new releases rail
        Column(modifier = Modifier.padding(top = 24.dp)) {
            InkSectionTitle(
                text = stringResource(Res.string.store_new_releases),
                modifier = Modifier.padding(horizontal = 22.dp),
                action = stringResource(Res.string.home_see_all),
                onActionClick = { onCategoryClick("New releases") },
                colors = colors
            )
            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                displayNewReleases.forEach { book ->
                    ReleaseCard(book = book, onClick = { onBookClick(book) }, colors = colors)
                }
            }
        }

        // top sellers
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
            InkSectionTitle(
                text = stringResource(Res.string.store_top_sellers),
                colors = colors
            )
            Column(modifier = Modifier.padding(top = 4.dp)) {
                displayTopSellers.forEachIndexed { i, book ->
                    InkBookRow(
                        cover = book.coverRes,
                        coverUrl = book.coverUrl,
                        title = book.title,
                        author = book.author,
                        modifier = Modifier.clickable { onBookClick(book) },
                        showDivider = i > 0,
                        meta = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Stars(filled = 4, colors = colors)
                                Text(
                                    text = book.rating,
                                    fontFamily = bodyFont,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = colors.inkSoft
                                )
                            }
                        },
                        trailing = {
                            Text(
                                text = "$${book.price}",
                                fontFamily = displayFont,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = colors.ink
                            )
                        },
                        colors = colors
                    )
                }
            }
        }
    }
}

private fun Book.toStoreBook(index: Int): StoreBook =
    StoreBook(
        title = title,
        author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        coverRes = storeCoverFallbacks[index % storeCoverFallbacks.size],
        tags = categories.take(2).map { it.name },
        price = price?.removePrefix("\$") ?: if (isFree) "0.00" else "12.99",
        rating = rating?.toRatingText() ?: "4.5",
        id = id,
        coverUrl = coverUrl
    )

private fun Double.toRatingText(): String =
    ((this * 10).toInt() / 10.0).toString()

@Composable
private fun ReleaseCard(
    book: StoreBook,
    onClick: () -> Unit,
    colors: InkColors,
) {
    Column(modifier = Modifier.width(104.dp).clickable(onClick = onClick)) {
        RemoteBookCover(
            coverUrl = book.coverUrl,
            fallback = book.coverRes,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 104.dp, height = 152.dp)
                .shadow(8.dp, RoundedCornerShape(InkShape.cover), clip = true)
        )
        Text(
            text = book.title,
            modifier = Modifier.padding(top = 9.dp),
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = colors.ink
        )
        Text(
            text = "$${book.price}",
            modifier = Modifier.padding(top = 5.dp),
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = colors.accent
        )
    }
}

@Composable
private fun Stars(
    filled: Int,
    colors: InkColors,
    total: Int = 5,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(total) { i ->
            Icon(
                imageVector = InkIcons.Star,
                contentDescription = null,
                tint = if (i < filled) colors.accent else colors.line,
                modifier = Modifier.size(11.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun StoreScreenPreview() {
    StoreScreen()
}
