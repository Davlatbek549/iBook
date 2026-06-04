package com.example.ibook.screens.noMembership

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.ColorCategoryFantasy
import com.example.ibook.ui.theme.ColorSecondary
import com.example.ibook.ui.theme.ColorTertiary
import com.example.ibook.ui.theme.IBookTheme

private data class CollectionBook(
    @param:StringRes val title: Int,
    @param:StringRes val author: Int,
    @param:StringRes val views: Int,
    @param:StringRes val firstGenre: Int,
    @param:StringRes val secondGenre: Int,
    @param:DrawableRes val cover: Int
)

@Composable
fun NoMembershipScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val books = listOf(
        CollectionBook(
            title = R.string.no_membership_book_olive_again,
            author = R.string.no_membership_book_olive_author,
            views = R.string.no_membership_views_1320,
            firstGenre = R.string.no_membership_genre_horror,
            secondGenre = R.string.no_membership_genre_fantasy,
            cover = R.drawable.olive_again_book
        ),
        CollectionBook(
            title = R.string.no_membership_book_hills_gold,
            author = R.string.no_membership_book_hills_author,
            views = R.string.no_membership_views_1320,
            firstGenre = R.string.no_membership_genre_horror,
            secondGenre = R.string.no_membership_genre_fantasy,
            cover = R.drawable.app_img
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp, bottom = 20.dp)
        ) {
            NoMembershipTopBar(
                onBackClick = onBackClick,
                onNotificationClick = onNotificationClick,
                onMenuClick = onMenuClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            ProfileHeader()

            Spacer(modifier = Modifier.height(20.dp))

            MembershipBanner()

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(text = stringResource(R.string.no_membership_reading_goals))

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                ReadingGoalCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.no_membership_todays_reading),
                    imageRes = R.drawable.todays_reading,
                    value = stringResource(R.string.no_membership_todays_reading_minutes),
                    suffix = stringResource(R.string.no_membership_todays_reading_target)
                )

                ReadingGoalCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.no_membership_longest_reading_streak),
                    imageRes = R.drawable.longest_reading,
                    value = stringResource(R.string.no_membership_longest_reading_days),
                    suffix = stringResource(R.string.no_membership_days),
                    highlightValue = true
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(text = stringResource(R.string.no_membership_collections))

            Spacer(modifier = Modifier.height(28.dp))

            CollectionsSheet(books = books)
        }
    }
}

@Composable
private fun NoMembershipTopBar(
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = stringResource(R.string.no_membership_back),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CircleIconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = stringResource(R.string.no_membership_notifications),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            CircleIconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = stringResource(R.string.no_membership_menu),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun CircleIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.size(36.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Composable
private fun ProfileHeader() {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.book_olive_again),
            contentDescription = stringResource(R.string.no_membership_profile_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(4.dp, colors.onPrimary, CircleShape)
        )

        Spacer(modifier = Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.no_membership_username),
                color = colors.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ProfileStat(
                    value = stringResource(R.string.no_membership_total_read_value),
                    label = stringResource(R.string.no_membership_total_read)
                )
                ProfileStat(
                    value = stringResource(R.string.no_membership_total_reading_value),
                    label = stringResource(R.string.no_membership_total_reading),
                    unit = stringResource(R.string.no_membership_total_reading_unit)
                )
            }
        }
    }
}

@Composable
private fun ProfileStat(
    value: String,
    label: String,
    unit: String? = null
) {
    val colors = MaterialTheme.colorScheme

    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                color = colors.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            if (unit != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = unit,
                    color = colors.onPrimary.copy(alpha = 0.72f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }

        Text(
            text = label,
            color = colors.onPrimary.copy(alpha = 0.78f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MembershipBanner() {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(ColorCategoryFantasy, ColorSecondary)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
                .align(Alignment.TopCenter)
                .background(ColorCategoryFantasy)
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterStart)
                .offset(x = 14.dp)
                .clip(CircleShape)
                .background(colors.primary)
                .border(5.dp, colors.onPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = colors.onPrimary,
                modifier = Modifier.size(26.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 82.dp, end = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.no_membership_banner_title),
                color = colors.onPrimary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.no_membership_banner_description),
                color = colors.onPrimary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ReadingGoalCard(
    title: String,
    @DrawableRes imageRes: Int,
    value: String,
    suffix: String,
    modifier: Modifier = Modifier,
    highlightValue: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.height(177.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Text(
                text = title,
                color = colors.onSurface,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            )

            Image(
                painter = painterResource(imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 2.dp, bottom = 2.dp)
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    color = if (highlightValue) colors.primary else colors.onSurface,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = suffix,
                    color = if (highlightValue) colors.primary else colors.onSurface.copy(alpha = 0.74f),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CollectionsSheet(books: List<CollectionBook>) {
    val colors = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(horizontal = 26.dp)
                .offset(y = (-13).dp)
                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                .background(colors.onPrimary.copy(alpha = 0.24f))
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 26.dp)
            ) {
                books.forEachIndexed { index, book ->
                    CollectionBookRow(book = book)

                    if (index != books.lastIndex) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = colors.outline.copy(alpha = 0.24f))
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionBookRow(book: CollectionBook) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(book.cover),
            contentDescription = stringResource(book.title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(70.dp)
                .height(98.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(book.title),
                color = colors.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stringResource(book.author),
                color = colors.onSurface.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.RemoveRedEye,
                    contentDescription = stringResource(R.string.no_membership_views),
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(book.views),
                    color = colors.onSurface.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GenreTag(text = stringResource(book.firstGenre), color = ColorTertiary)
                GenreTag(text = stringResource(book.secondGenre), color = ColorCategoryFantasy)
            }
        }
    }
}

@Composable
private fun GenreTag(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .padding(horizontal = 13.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun NoMembershipScreenPreview() {
    IBookTheme {
        NoMembershipScreen()
    }
}
