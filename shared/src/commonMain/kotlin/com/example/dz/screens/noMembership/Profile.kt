package com.example.dz.screens.noMembership

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.ColorCategoryFantasy
import com.example.dz.theme.ColorTertiary
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class CollectionBook(
    val title: StringResource,
    val author: StringResource,
    val views: StringResource,
    val firstGenre: StringResource,
    val secondGenre: StringResource,
    val cover: DrawableResource
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
            title = Res.string.no_membership_book_olive_again,
            author = Res.string.no_membership_book_olive_author,
            views = Res.string.no_membership_views_1320,
            firstGenre = Res.string.no_membership_genre_horror,
            secondGenre = Res.string.no_membership_genre_fantasy,
            cover = Res.drawable.olive_again_book
        ),
        CollectionBook(
            title = Res.string.no_membership_book_hills_gold,
            author = Res.string.no_membership_book_hills_author,
            views = Res.string.no_membership_views_1320,
            firstGenre = Res.string.no_membership_genre_horror,
            secondGenre = Res.string.no_membership_genre_fantasy,
            cover = Res.drawable.app_img
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

            SectionTitle(text = stringResource(Res.string.no_membership_reading_goals))

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                ReadingGoalCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(Res.string.no_membership_todays_reading),
                    imageRes = Res.drawable.todays_reading,
                    value = stringResource(Res.string.no_membership_todays_reading_minutes),
                    suffix = stringResource(Res.string.no_membership_todays_reading_target)
                )

                ReadingGoalCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(Res.string.no_membership_longest_reading_streak),
                    imageRes = Res.drawable.longest_reading,
                    value = stringResource(Res.string.no_membership_longest_reading_days),
                    suffix = stringResource(Res.string.no_membership_days),
                    highlightValue = true
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(text = stringResource(Res.string.no_membership_collections))

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
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = stringResource(Res.string.no_membership_back),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CircleIconButton(onClick = onNotificationClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bell),
                    contentDescription = stringResource(Res.string.no_membership_notifications),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            CircleIconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_grid_view),
                    contentDescription = stringResource(Res.string.no_membership_menu),
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
            painter = painterResource(Res.drawable.book_olive_again),
            contentDescription = stringResource(Res.string.no_membership_profile_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(4.dp, colors.onPrimary, CircleShape)
        )

        Spacer(modifier = Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.no_membership_username),
                color = colors.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ProfileStat(
                    value = stringResource(Res.string.no_membership_total_read_value),
                    label = stringResource(Res.string.no_membership_total_read)
                )
                ProfileStat(
                    value = stringResource(Res.string.no_membership_total_reading_value),
                    label = stringResource(Res.string.no_membership_total_reading),
                    unit = stringResource(Res.string.no_membership_total_reading_unit)
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
            .height(66.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFC84A8A),
                        Color(0xFFFF3032),
                        Color(0xFFA04FBC)
                    )
                )
            )
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
        ) {
            val orangeWave = Path().apply {
                moveTo(size.width * 0.15f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height * 0.40f)
                lineTo(size.width * 0.31f, size.height * 0.40f)
                cubicTo(
                    size.width * 0.26f,
                    size.height * 0.40f,
                    size.width * 0.25f,
                    size.height * 0.18f,
                    size.width * 0.20f,
                    size.height * 0.04f
                )
                cubicTo(
                    size.width * 0.18f,
                    0f,
                    size.width * 0.16f,
                    0f,
                    size.width * 0.15f,
                    0f
                )
                close()
            }

            drawPath(
                path = orangeWave,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF7B20),
                        Color(0xFFFFB928),
                        Color(0xFFFFC934)
                    )
                )
            )

            val yellowWave = Path().apply {
                moveTo(size.width * 0.19f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height * 0.35f)
                lineTo(size.width * 0.32f, size.height * 0.35f)
                cubicTo(
                    size.width * 0.27f,
                    size.height * 0.35f,
                    size.width * 0.27f,
                    size.height * 0.14f,
                    size.width * 0.23f,
                    size.height * 0.03f
                )
                cubicTo(
                    size.width * 0.22f,
                    0f,
                    size.width * 0.20f,
                    0f,
                    size.width * 0.19f,
                    0f
                )
                close()
            }

            drawPath(
                path = yellowWave,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFFC739),
                        Color(0xFFFFD43E),
                        Color(0xFFFFC934)
                    )
                )
            )
        }

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
                painter = painterResource(Res.drawable.ic_premium),
                contentDescription = null,
                tint = colors.onPrimary,
                modifier = Modifier.size(26.dp)
            )
        }

        Text(
            text = stringResource(Res.string.no_membership_banner_title),
            color = colors.onPrimary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 17.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxWidth()
                .padding(start = 92.dp, end = 16.dp, top = 3.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = stringResource(Res.string.no_membership_banner_description),
            color = colors.onPrimary,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 13.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            ),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 92.dp, end = 12.dp, top = 34.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
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
    imageRes: DrawableResource,
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
                    painter = painterResource(Res.drawable.ic_visibility_on),
                    contentDescription = stringResource(Res.string.no_membership_views),
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
    DZTheme {
        NoMembershipScreen()
    }
}
