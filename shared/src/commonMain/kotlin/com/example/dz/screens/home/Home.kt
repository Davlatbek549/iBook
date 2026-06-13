package com.example.dz.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkChip
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.app_components.ink.InkLabel
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
import dz.shared.generated.resources.home_browse
import dz.shared.generated.resources.home_continue_reading
import dz.shared.generated.resources.home_for_you
import dz.shared.generated.resources.home_from_your_circle
import dz.shared.generated.resources.home_greeting
import dz.shared.generated.resources.home_see_all
import dz.shared.generated.resources.home_trending
import dz.shared.generated.resources.profile_1
import dz.shared.generated.resources.profile_2
import dz.shared.generated.resources.profile_3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class HomeBook(
    val title: String,
    val author: String,
    val coverRes: DrawableResource,
    val rating: String = "4.5",
    val tags: List<String> = emptyList()
)

data class HomeAuthor(
    val name: String,
    val avatarRes: DrawableResource,
    val tags: List<String> = emptyList()
)

data class HomeCategory(
    val name: String,
    val iconRes: DrawableResource,
    val backgroundColor: Color
)

private val continueReadingBook = HomeBook(
    title = "Mexican Gothic",
    author = "Silvia Moreno-Garcia",
    coverRes = Res.drawable.book_cover,
    rating = "4.6",
    tags = listOf("Literary", "Gothic")
)

private val forYouBooks = listOf(
    HomeBook("The Archer", "Paulo Coelho", Res.drawable.book_cover_2, "4.4", listOf("Fable")),
    HomeBook("Red at the Bone", "Jacqueline Woodson", Res.drawable.book_cover_3, "4.5", listOf("Literary")),
    HomeBook("Bestiary", "K-Ming Chang", Res.drawable.book_cover_4, "4.2", listOf("Myth"))
)

private val trendingBooks = listOf(
    HomeBook("Red at the Bone", "Jacqueline Woodson", Res.drawable.book_cover_3, "4.5", listOf("Literary")),
    HomeBook("Bestiary", "K-Ming Chang", Res.drawable.book_cover_4, "4.2", listOf("Myth"))
)

private val browseGenres = listOf("Literary", "Fiction", "History", "Romance", "Essays", "Poetry")

@Composable
fun HomeScreen(
    onKeepReadingClick: () -> Unit = {},
    onViewAllCategoriesClick: () -> Unit = {},
    onBookClick: (HomeBook) -> Unit = {},
    onAuthorClick: (HomeAuthor) -> Unit = {},
    onGoalsKeepReadingClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

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
                .padding(start = 22.dp, end = 22.dp, top = 6.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // profile avatar — opens the (pushed) Profile screen
            Image(
                painter = painterResource(Res.drawable.profile_1),
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .clickable(onClick = onProfileClick)
            )
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    text = stringResource(Res.string.home_greeting),
                    fontFamily = bodyFont,
                    fontSize = 12.sp,
                    color = colors.muted
                )
                Text(
                    text = "Amelia",
                    modifier = Modifier.padding(top = 6.dp),
                    fontFamily = displayFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    color = colors.ink
                )
            }
            InkIconButton(
                icon = InkIcons.Search,
                onClick = onViewAllCategoriesClick,
                colors = colors
            )
        }

        // continue reading
        Column(modifier = Modifier.padding(horizontal = 22.dp)) {
            InkLabel(text = stringResource(Res.string.home_continue_reading), colors = colors)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .inkCard(colors)
                    .clickable(onClick = onKeepReadingClick)
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Image(
                    painter = painterResource(continueReadingBook.coverRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 52.dp, height = 76.dp)
                        .shadow(6.dp, RoundedCornerShape(InkShape.cover), clip = true)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = continueReadingBook.title,
                        fontFamily = displayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colors.ink
                    )
                    Text(
                        text = continueReadingBook.author,
                        modifier = Modifier.padding(top = 3.dp),
                        fontFamily = bodyFont,
                        fontSize = 12.sp,
                        color = colors.muted
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    InkProgressBar(
                        progress = 0.62f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.SemiBold)) {
                                append("62%")
                            }
                            append(" · 18 min left")
                        },
                        modifier = Modifier.padding(top = 7.dp),
                        fontFamily = bodyFont,
                        fontSize = 11.sp,
                        color = colors.muted
                    )
                }
            }
        }

        // for you rail
        Column(modifier = Modifier.padding(top = 26.dp)) {
            InkSectionTitle(
                text = stringResource(Res.string.home_for_you),
                modifier = Modifier.padding(horizontal = 22.dp),
                action = stringResource(Res.string.home_see_all),
                onActionClick = onViewAllCategoriesClick,
                colors = colors
            )
            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                forYouBooks.forEach { book ->
                    RailBookCard(book = book, onClick = { onBookClick(book) }, colors = colors)
                }
            }
        }

        // browse chips
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 26.dp)) {
            InkLabel(text = stringResource(Res.string.home_browse), colors = colors)
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                browseGenres.forEachIndexed { i, genre ->
                    Box(modifier = Modifier.clickable(onClick = onViewAllCategoriesClick)) {
                        InkChip(text = genre, solid = i == 0, colors = colors)
                    }
                }
            }
        }

        // trending now
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 26.dp)) {
            Text(
                text = stringResource(Res.string.home_trending),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = colors.ink
            )
            Spacer(modifier = Modifier.height(7.dp))
            trendingBooks.forEachIndexed { i, book ->
                TrendingRow(
                    book = book,
                    showDivider = i > 0,
                    onClick = { onBookClick(book) },
                    colors = colors
                )
            }
        }

        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
            InkLabel(text = stringResource(Res.string.home_from_your_circle), colors = colors)
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CircleRow(
                    avatar = Res.drawable.profile_2,
                    name = "Patricia",
                    activity = "is reading Olive, Again",
                    online = true,
                    colors = colors
                )
                CircleRow(
                    avatar = Res.drawable.profile_3,
                    name = "Daniel",
                    activity = "finished Bestiary",
                    online = false,
                    colors = colors
                )
            }
        }
    }
}

@Composable
private fun RailBookCard(
    book: HomeBook,
    onClick: () -> Unit,
    colors: InkColors,
) {
    Column(modifier = Modifier.width(104.dp).clickable(onClick = onClick)) {
        Image(
            painter = painterResource(book.coverRes),
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
            lineHeight = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = colors.ink
        )
        Text(
            text = book.author,
            modifier = Modifier.padding(top = 3.dp),
            fontFamily = inkBodyFontFamily(),
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = colors.muted
        )
    }
}

@Composable
private fun TrendingRow(
    book: HomeBook,
    showDivider: Boolean,
    onClick: () -> Unit,
    colors: InkColors,
) {
    Column {
        if (showDivider) {
            androidx.compose.material3.HorizontalDivider(thickness = 1.dp, color = colors.line)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(book.coverRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 46.dp, height = 66.dp)
                    .clip(RoundedCornerShape(InkShape.cover))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    fontFamily = inkDisplayFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = colors.ink
                )
                Text(
                    text = book.author,
                    modifier = Modifier.padding(top = 3.dp),
                    fontFamily = inkBodyFontFamily(),
                    fontSize = 12.sp,
                    color = colors.muted
                )
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = InkIcons.Star,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = book.rating,
                        fontFamily = inkBodyFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = colors.inkSoft
                    )
                    Text(
                        text = "· ${book.tags.firstOrNull().orEmpty()}",
                        fontFamily = inkBodyFontFamily(),
                        fontSize = 11.sp,
                        color = colors.muted
                    )
                }
            }
            Icon(
                imageVector = InkIcons.Bookmark,
                contentDescription = null,
                tint = colors.muted,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Composable
private fun CircleRow(
    avatar: DrawableResource,
    name: String,
    activity: String,
    online: Boolean,
    colors: InkColors,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box {
            Image(
                painter = painterResource(avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm))
            )
            if (online) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(11.dp)
                        .clip(CircleShape)
                        .background(colors.paper)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(colors.accent)
                )
            }
        }
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) {
                    append(name)
                }
                append(" ")
                append(activity)
            },
            fontFamily = inkBodyFontFamily(),
            fontSize = 13.sp,
            color = colors.inkSoft
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
