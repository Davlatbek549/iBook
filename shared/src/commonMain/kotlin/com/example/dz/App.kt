package com.example.dz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*

private val DZPurple = Color(0xFF6556FF)
private val DZPink = Color(0xFFF8448F)
private val DZLavender = Color(0xFFA65EEA)
private val DZInk = Color(0xFF303030)
private val DZMuted = Color(0xFF8D8D98)
private val DZSurface = Color.White
private val DZPage = Color(0xFFF5F6FA)

private enum class DZTab(
    val route: String,
    val icon: DrawableResource,
    val label: String
) {
    Home("home", Res.drawable.ic_home, "Home"),
    Library("library", Res.drawable.ic_book, "Library"),
    Store("store", Res.drawable.ic_shop, "Store"),
    Search("search", Res.drawable.ic_search, "Search")
}

private data class BookUi(
    val title: String,
    val author: String,
    val cover: DrawableResource,
    val tags: List<String>,
    val progress: String? = null
)

private data class CategoryUi(
    val name: String,
    val image: DrawableResource,
    val color: Color
)

private data class AuthorUi(
    val name: String,
    val avatar: DrawableResource,
    val tags: List<String>
)

private val categories = listOf(
    CategoryUi("Arts", Res.drawable.img_arts, Color(0xFFFC5C65)),
    CategoryUi("Biography", Res.drawable.img_biography, Color(0xFF26DE81)),
    CategoryUi("Business", Res.drawable.img_business, Color(0xFFFED330)),
    CategoryUi("Comic", Res.drawable.img_commic, Color(0xFF616161)),
    CategoryUi("Cooking", Res.drawable.img_cooking, Color(0xFF20BF6B)),
    CategoryUi("Horror", Res.drawable.img_horror, DZLavender),
    CategoryUi("Fantasy", Res.drawable.img_fantasy, Color(0xFFF7B731)),
    CategoryUi("Travel", Res.drawable.img_travel, Color(0xFFEB4848))
)

private val featuredBooks = listOf(
    BookUi("Murder Board", "By Brian Shea", Res.drawable.book_cover, listOf("Horror", "Mystery"), "30%"),
    BookUi("The Archer", "By Paulo Coelho", Res.drawable.book_cover_2, listOf("Fantasy", "Fiction")),
    BookUi("Red at the Bone", "By Jacqueline Woodson", Res.drawable.book_cover_3, listOf("History", "Novel")),
    BookUi("Olive, Again", "By Elizabeth Strout", Res.drawable.book_cover_4, listOf("Romance", "Drama")),
    BookUi("Bestiary", "By K-Ming Chang", Res.drawable.book_cove_6, listOf("Fantasy", "Literary"))
)

private val authors = listOf(
    AuthorUi("Maria Remy", Res.drawable.img_maria_renzy, listOf("History", "Romance")),
    AuthorUi("Neil Alvin", Res.drawable.img_neil_alvin, listOf("Adventure", "Comic")),
    AuthorUi("Ysa Barreto", Res.drawable.img_yza_barretto, listOf("Biography", "Heroic"))
)

@Composable
fun App() {
    com.example.dz.theme.DZTheme {
        com.example.dz.screens.home.Home()
    }
}

@Composable
private fun DZSharedTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = DZPurple,
            secondary = DZPink,
            tertiary = DZLavender,
            background = DZPage,
            surface = DZSurface,
            onSurface = DZInk
        ),
        content = content
    )
}

@Composable
private fun HomeScreen(
    onKeepReading: () -> Unit,
    onBookClick: (BookUi) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item {
            HomeHeader()
        }

        item {
            ContinueReadingCard(onClick = onKeepReading)
        }

        item {
            SectionHeader(title = "Categories", action = "View all")
            CategoryStrip()
        }

        item {
            SectionHeader(title = stringResource(Res.string.home_trending_book))
            BookList(books = featuredBooks, onBookClick = onBookClick)
        }

        item {
            SectionHeader(title = stringResource(Res.string.home_popular_authors))
            AuthorStrip()
        }

        item {
            SectionHeader(title = stringResource(Res.string.home_reading_goals))
            ReadingGoalCards()
        }

        item {
            Spacer(modifier = Modifier.height(92.dp))
        }
    }
}

@Composable
private fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(188.dp)
            .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(DZPurple, DZLavender)
                )
            )
            .safeContentPadding()
            .padding(horizontal = 22.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.profile_1),
                    contentDescription = stringResource(Res.string.cd_profile),
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = stringResource(Res.string.home_hi),
                        color = Color.White.copy(alpha = 0.78f),
                        fontSize = 13.sp
                    )
                    Text(
                        text = stringResource(Res.string.home_username),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                color = Color.White.copy(alpha = 0.16f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("13", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Point", color = Color.White.copy(alpha = 0.78f), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ContinueReadingCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.book_cover),
                contentDescription = stringResource(Res.string.cd_featured_book),
                modifier = Modifier
                    .width(86.dp)
                    .height(126.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("Keep Reading", color = DZMuted, fontSize = 13.sp)
                Text(
                    text = stringResource(Res.string.reading_book_title),
                    color = DZInk,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(Res.string.reading_book_author),
                    color = DZMuted,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                TagRow(tags = listOf("Horror", "Mystery"))
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DZPurple),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(stringResource(Res.string.reading_keep_reading))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, action: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = DZInk,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (action != null) {
            Text(
                text = action,
                color = DZPurple,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CategoryStrip() {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(start = 22.dp, end = 22.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        categories.forEach { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(78.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(category.color.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(category.image),
                        contentDescription = category.name,
                        modifier = Modifier.size(42.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.name,
                    color = DZInk,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BookList(
    books: List<BookUi>,
    onBookClick: (BookUi) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 22.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        books.forEach { book ->
            BookRow(book = book, onClick = { onBookClick(book) })
        }
    }
}

@Composable
private fun BookRow(book: BookUi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(book.cover),
                contentDescription = book.title,
                modifier = Modifier
                    .width(58.dp)
                    .height(84.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    color = DZInk,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author,
                    color = DZMuted,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                TagRow(tags = book.tags)
            }
            if (book.progress != null) {
                Text(book.progress, color = DZPurple, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AuthorStrip() {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        authors.forEach { author ->
            Card(
                modifier = Modifier.width(168.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(author.avatar),
                        contentDescription = author.name,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(author.name, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(author.tags.joinToString(" / "), color = DZMuted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReadingGoalCards() {
    Row(
        modifier = Modifier.padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GoalCard(
            title = stringResource(Res.string.reading_today_title),
            value = stringResource(Res.string.reading_today_value),
            unit = stringResource(Res.string.reading_today_unit),
            image = Res.drawable.todays_reading,
            modifier = Modifier.weight(1f)
        )
        GoalCard(
            title = stringResource(Res.string.reading_streak_title),
            value = stringResource(Res.string.reading_streak_value),
            unit = stringResource(Res.string.reading_streak_unit),
            image = Res.drawable.longest_reading,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GoalCard(
    title: String,
    value: String,
    unit: String,
    image: DrawableResource,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = DZInk, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Image(
                painter = painterResource(image),
                contentDescription = title,
                modifier = Modifier.size(82.dp),
                contentScale = ContentScale.Fit
            )
            Text(value, color = DZPurple, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(unit, color = DZMuted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun LibraryScreen(onBookClick: (BookUi) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DZPurple)
            .safeContentPadding(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = stringResource(Res.string.library_title),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp)
            )
        }

        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.16f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = stringResource(Res.string.library_collections),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    featuredBooks.take(2).forEach { book ->
                        BookRowOnColor(book = book, onClick = { onBookClick(book) })
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(Res.string.library_all_book),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
        }

        items(featuredBooks) { book ->
            Box(modifier = Modifier.padding(horizontal = 22.dp)) {
                BookRow(book = book, onClick = { onBookClick(book) })
            }
        }

        item {
            Spacer(modifier = Modifier.height(92.dp))
        }
    }
}

@Composable
private fun BookRowOnColor(book: BookUi, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(book.cover),
            contentDescription = book.title,
            modifier = Modifier
                .width(54.dp)
                .height(78.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(book.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(book.author, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}

@Composable
private fun StoreScreen(onBookClick: (BookUi) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
                    .background(
                        Brush.linearGradient(listOf(DZPink, DZPurple))
                    )
                    .safeContentPadding()
                    .padding(horizontal = 22.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text("DZ Store", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text("Find stories for every mood", color = Color.White.copy(alpha = 0.78f))
                }
            }
        }

        item {
            SectionHeader(title = "Promotions")
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PromotionCard(Res.drawable.book_cover, Res.drawable.book_cover_2)
                PromotionCard(Res.drawable.book_cover_3, Res.drawable.book_cover_4)
            }
        }

        item {
            SectionHeader(title = "Categories")
            CategoryStrip()
        }

        item {
            SectionHeader(title = "Popular Books")
            BookList(books = featuredBooks, onBookClick = onBookClick)
        }

        item {
            Spacer(modifier = Modifier.height(92.dp))
        }
    }
}

@Composable
private fun PromotionCard(first: DrawableResource, second: DrawableResource) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(142.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = DZInk)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Special Offer", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Get 2 books today", color = Color.White.copy(alpha = 0.72f), fontSize = 12.sp)
            }
            Image(
                painter = painterResource(first),
                contentDescription = null,
                modifier = Modifier
                    .width(46.dp)
                    .height(70.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painterResource(second),
                contentDescription = null,
                modifier = Modifier
                    .width(46.dp)
                    .height(70.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun SearchScreen(onBookClick: (BookUi) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 22.dp, vertical = 18.dp)
                    .fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_search),
                        contentDescription = stringResource(Res.string.cd_search),
                        tint = DZMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(Res.string.friend_list_search_hint),
                        color = DZMuted
                    )
                }
            }
        }

        item {
            SectionHeader(title = "Browse Categories")
            CategoryGrid()
        }

        item {
            SectionHeader(title = "Trending")
            Column(
                modifier = Modifier.padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Stephen King", "The Great Gatsby", "1984", "A Promised Land").forEach {
                    Text(it, color = DZInk, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        item {
            SectionHeader(title = "Results")
            BookList(books = featuredBooks.take(3), onBookClick = onBookClick)
        }

        item {
            Spacer(modifier = Modifier.height(92.dp))
        }
    }
}

@Composable
private fun CategoryGrid() {
    Column(
        modifier = Modifier.padding(horizontal = 22.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        categories.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                row.forEach { category ->
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = category.color.copy(alpha = 0.16f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(category.image),
                                contentDescription = category.name,
                                modifier = Modifier
                                    .fillMaxWidth(0.72f)
                                    .aspectRatio(1f),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(category.name, color = DZInk, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReaderScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF7A2634), Color(0xFF0A6372), Color(0xFF10233B))
                )
            )
            .safeContentPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleIconButton(
                        icon = Res.drawable.ic_back,
                        contentDescription = stringResource(Res.string.cd_back),
                        onClick = onBack
                    )
                    CircleIconButton(
                        icon = Res.drawable.ic_three_vertical_dots,
                        contentDescription = stringResource(Res.string.cd_more_options),
                        onClick = {}
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9F0))
                ) {
                    Row(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = stringResource(Res.string.reading_left_page),
                            color = DZInk,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(Res.string.reading_book_title),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 28.dp)
                )
                Text(
                    text = stringResource(Res.string.reading_book_author),
                    color = Color.White.copy(alpha = 0.78f),
                    modifier = Modifier.padding(horizontal = 28.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 22.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ReadingGoalCards()
                        Text(
                            text = stringResource(Res.string.book_review_overview),
                            color = DZInk,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(Res.string.reading_overview_body),
                            color = DZMuted,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CircleIconButton(
    icon: DrawableResource,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.18f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun TagRow(tags: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        tags.take(2).forEach { tag ->
            Surface(
                color = DZPurple.copy(alpha = 0.10f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = tag,
                    color = DZPurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedTab: DZTab,
    onSelect: (DZTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 22.dp, vertical = 14.dp)
            .fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(26.dp),
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .height(62.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DZTab.entries.forEach { tab ->
                val selected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (selected) DZPurple.copy(alpha = 0.10f) else Color.Transparent)
                        .clickable { onSelect(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(tab.icon),
                        contentDescription = tab.label,
                        tint = if (selected) DZPurple else DZMuted,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
