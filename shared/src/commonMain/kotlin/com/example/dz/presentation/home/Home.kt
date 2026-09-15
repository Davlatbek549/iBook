package com.example.dz.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicAvatarInitial
import com.example.dz.designsystem.components.organic.OrganicAvatarStack
import com.example.dz.designsystem.components.organic.OrganicBookCover
import com.example.dz.designsystem.components.organic.OrganicCoverGradients
import com.example.dz.designsystem.components.organic.OrganicIconButton
import com.example.dz.designsystem.components.organic.OrganicProgressRing
import com.example.dz.designsystem.components.organic.OrganicSectionHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicDisplayFontFamily
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.LibraryBook as DomainLibraryBook

/** A single "Picked for you" / "New this week" entry — real book, or a design placeholder. */
private data class HomeBook(
    val id: String,
    val title: String,
    val author: String,
    val coverIndex: Int,
    val coverUrl: String? = null,
    val price: String? = null,
)

private val pickedForYouFallback = listOf(
    HomeBook("archer", "The Archer", "Paulo Coelho", 3),
    HomeBook("red-at-the-bone", "Red at the Bone", "J. Woodson", 1),
    HomeBook("bestiary", "Bestiary", "K-Ming Chang", 2),
    HomeBook("piranesi", "Piranesi", "Susanna Clarke", 9),
    HomeBook("snow-country", "Snow Country", "Y. Kawabata", 4),
    HomeBook("the-bear", "The Bear", "Andrew Krivak", 0),
    HomeBook("mother-thing", "Mother Thing", "A. Hogarth", 8),
)

private val newThisWeek = listOf(
    HomeBook("the-hacienda", "The Hacienda", "Isabel Cañas", 7, price = "$10.99"),
    HomeBook("mother-thing-new", "Mother Thing", "Ainslie Hogarth", 5, price = "$9.40"),
)

@Composable
fun HomeScreen(
    uiState: HomeUiState = HomeUiState(),
    onKeepReadingClick: () -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onReadingNowClick: () -> Unit = {},
    onSeeAllClick: () -> Unit = {},
) {
    val pickedForYou = uiState.books.mapIndexed { index, book -> book.toHomeBook(index) }
        .ifEmpty { pickedForYouFallback }
    val continueBook = uiState.continueReading?.toHomeBook()
    val continueTitle = continueBook?.title ?: "Mexican Gothic"
    val continueProgress = uiState.continueReading?.progressPercent ?: 62
    val continueCoverIndex = continueBook?.coverIndex ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrganicColors.bg)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp, bottom = 108.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Greeting
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OrganicAvatarInitial(
                initial = "A",
                modifier = Modifier.clickableOnce(onProfileClick),
                size = 46.dp,
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Good evening,",
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = OrganicColors.neutral700
                )
                Text(
                    text = "Amelia",
                    fontFamily = organicDisplayFontFamily(),
                    fontSize = 24.sp,
                    lineHeight = 26.sp,
                    color = OrganicColors.text
                )
            }
            OrganicIconButton(
                icon = OrganicIcons.Search,
                onClick = onSearchClick,
                size = 42.dp,
                iconSize = 19.dp,
            )
        }

        // Keep going card
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OrganicColors.accent200, androidx.compose.foundation.shape.RoundedCornerShape(28.dp))
                    .clickableOnce(onKeepReadingClick)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OrganicBookCover(
                    modifier = Modifier.size(width = 66.dp, height = 96.dp),
                    gradient = OrganicCoverGradients.forIndex(continueCoverIndex),
                    radius = 12.dp,
                )
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Keep going",
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 11.sp,
                        letterSpacing = 0.7.sp,
                        color = OrganicColors.accent800
                    )
                    Text(
                        text = continueTitle,
                        fontFamily = organicDisplayFontFamily(),
                        fontSize = 20.sp,
                        lineHeight = 23.sp,
                        color = OrganicColors.accent900,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "18 min left in Chapter Four",
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 12.sp,
                        color = OrganicColors.accent800
                    )
                }
                OrganicProgressRing(percent = continueProgress, size = 58.dp, innerSize = 44.dp) {
                    Text(
                        text = "$continueProgress%",
                        fontFamily = organicDisplayFontFamily(),
                        fontSize = 13.sp,
                        color = OrganicColors.accent900
                    )
                }
            }
        }

        // Picked for you
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OrganicSectionHeader(
                title = "Picked for you",
                actionLabel = "See all",
                onActionClick = onSeeAllClick,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                pickedForYou.forEach { book ->
                    Column(
                        modifier = Modifier.width(112.dp).clickableOnce { onBookClick(book.id) },
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OrganicBookCover(
                            modifier = Modifier.fillMaxWidth().height(118.dp),
                            gradient = OrganicCoverGradients.forIndex(book.coverIndex),
                            radius = 18.dp,
                            elevation = 4.dp,
                        )
                        Text(
                            text = book.title,
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = OrganicColors.text
                        )
                        Text(
                            text = book.author,
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 11.sp,
                            color = OrganicColors.neutral700,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        // Reading now presence card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(OrganicColors.accent2_200, androidx.compose.foundation.shape.RoundedCornerShape(28.dp))
                .clickableOnce(onReadingNowClick)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OrganicAvatarStack(
                colors = listOf(OrganicColors.accent2_400, OrganicColors.accent300, OrganicColors.accent2_600),
                size = 32.dp,
                overlap = 10.dp,
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(OrganicColors.accent2_700, androidx.compose.foundation.shape.CircleShape)
                    )
                    Text(
                        text = "1,284 reading right now",
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = OrganicColors.accent2_900
                    )
                }
                Text(
                    text = "Patricia is one of them",
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = OrganicColors.accent2_800
                )
            }
            Icon(
                imageVector = OrganicIcons.ChevronRight,
                contentDescription = null,
                tint = OrganicColors.accent2_800,
                modifier = Modifier.size(15.dp)
            )
        }

        // New this week
        Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
            OrganicSectionHeader(
                title = "New this week",
                actionLabel = "See all",
                onActionClick = onSeeAllClick,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                newThisWeek.forEach { book ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickableOnce { onBookClick(book.id) },
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OrganicBookCover(
                            modifier = Modifier.size(width = 44.dp, height = 60.dp),
                            gradient = OrganicCoverGradients.forIndex(book.coverIndex),
                            radius = 10.dp,
                        )
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = book.title,
                                fontFamily = organicDisplayFontFamily(),
                                fontSize = 17.sp,
                                lineHeight = 19.sp,
                                color = OrganicColors.text
                            )
                            Text(
                                text = book.author,
                                fontFamily = organicBodyFontFamily(),
                                fontSize = 12.sp,
                                color = OrganicColors.neutral700
                            )
                            Text(
                                text = book.price.orEmpty(),
                                fontFamily = organicBodyFontFamily(),
                                fontSize = 12.sp,
                                color = OrganicColors.accent700
                            )
                        }
                        Icon(
                            imageVector = OrganicIcons.ChevronRight,
                            contentDescription = null,
                            tint = OrganicColors.neutral600,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun Modifier.clickableOnce(onClick: () -> Unit): Modifier =
    this.clickable(onClick = onClick)

private fun Book.toHomeBook(index: Int): HomeBook = HomeBook(
    id = id,
    title = title,
    author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
    coverIndex = index,
    coverUrl = coverUrl,
)

private fun DomainLibraryBook.toHomeBook(): HomeBook = book.toHomeBook(index = 0).copy(coverIndex = 0)

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
