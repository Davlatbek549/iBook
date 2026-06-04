package com.example.ibook.screens.friend

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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoNotDisturbAlt
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.vector.ImageVector
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

private data class FriendBook(
    @param:StringRes val title: Int,
    @param:StringRes val author: Int,
    @param:StringRes val views: Int,
    @param:StringRes val firstGenre: Int,
    @param:StringRes val secondGenre: Int,
    @param:DrawableRes val cover: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    var showSettingsSheet by remember { mutableStateOf(false) }
    val settingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val books = listOf(
        FriendBook(
            title = R.string.friend_read_at_the_bone,
            author = R.string.friend_read_at_the_bone_author,
            views = R.string.friend_views_1320,
            firstGenre = R.string.friend_genre_horror,
            secondGenre = R.string.friend_genre_fantasy,
            cover = R.drawable.olive_again_book
        ),
        FriendBook(
            title = R.string.friend_bestiary,
            author = R.string.friend_bestiary_author,
            views = R.string.friend_views_1320,
            firstGenre = R.string.friend_genre_horror,
            secondGenre = R.string.friend_genre_fantasy,
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
            FriendTopBar(
                onBackClick = onBackClick,
                onMessageClick = onMessageClick,
                onSettingsClick = {
                    onSettingsClick()
                    showSettingsSheet = true
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            FriendProfileHeader()

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle(text = stringResource(R.string.friend_reading))

            Spacer(modifier = Modifier.height(16.dp))

            ReadingNowCard()

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(text = stringResource(R.string.friend_collections))

            Spacer(modifier = Modifier.height(28.dp))

            FriendCollectionsSheet(books = books)
        }

        if (showSettingsSheet) {
            FriendSettingsSheet(
                sheetState = settingsSheetState,
                onDismiss = { showSettingsSheet = false }
            )
        }
    }
}

@Composable
private fun FriendTopBar(
    onBackClick: () -> Unit,
    onMessageClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = stringResource(R.string.friend_back),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CircleIconButton(onClick = onMessageClick) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = stringResource(R.string.friend_messages),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(21.dp)
                )
            }

            CircleIconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.friend_settings),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FriendSettingsSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        scrimColor = Color.Black.copy(alpha = 0.55f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 4.dp)
                    .width(32.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(colors.outline.copy(alpha = 0.45f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .padding(bottom = 20.dp)
        ) {
            SettingsActionRow(
                icon = Icons.Default.Delete,
                text = stringResource(R.string.friend_action_delete),
                onClick = onDismiss
            )
            SettingsDivider()
            SettingsActionRow(
                icon = Icons.Default.DoNotDisturbAlt,
                text = stringResource(R.string.friend_action_ignore),
                onClick = onDismiss
            )
            SettingsDivider()
            SettingsActionRow(
                icon = Icons.Default.NotificationsOff,
                text = stringResource(R.string.friend_action_mute),
                onClick = onDismiss
            )
            SettingsDivider()
            SettingsActionRow(
                icon = Icons.Default.Block,
                text = stringResource(R.string.friend_action_block),
                onClick = onDismiss
            )
        }
    }
}

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 44.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    )
}

@Composable
private fun FriendProfileHeader() {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            Image(
                painter = painterResource(R.drawable.book_olive_again),
                contentDescription = stringResource(R.string.friend_profile_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(4.dp, colors.onPrimary, CircleShape)
            )

            Box(
                modifier = Modifier
                    .offset(y = 9.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(colors.primary)
                    .border(3.dp, colors.onPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.friend_username),
                color = colors.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ProfileStat(
                    value = stringResource(R.string.friend_total_read_value),
                    label = stringResource(R.string.friend_total_read)
                )
                ProfileStat(
                    value = stringResource(R.string.friend_total_reading_value),
                    label = stringResource(R.string.friend_total_reading),
                    unit = stringResource(R.string.friend_total_reading_unit)
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
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ReadingNowCard() {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(ColorCategoryFantasy, colors.primary.copy(alpha = 0.98f), Color(0xFF26346D))
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.olive_again_book),
            contentDescription = stringResource(R.string.friend_featured_book_title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(116.dp)
                .height(120.dp)
                .align(Alignment.CenterStart)
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 126.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.friend_featured_book_title),
                color = colors.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stringResource(R.string.friend_featured_book_author),
                color = colors.onPrimary.copy(alpha = 0.86f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                FriendAvatar(color = ColorSecondary, offset = 0)
                FriendAvatar(color = ColorCategoryFantasy, offset = -8)
                FriendAvatar(color = ColorTertiary, offset = -16)

                Text(
                    text = stringResource(R.string.friend_other_readers),
                    color = colors.onPrimary.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.offset(x = (-8).dp)
                )
            }
        }
    }
}

@Composable
private fun FriendAvatar(
    color: Color,
    offset: Int
) {
    Box(
        modifier = Modifier
            .offset(x = offset.dp)
            .size(22.dp)
            .clip(CircleShape)
            .background(color)
            .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
    )
}

@Composable
private fun FriendCollectionsSheet(books: List<FriendBook>) {
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
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp, bottomEnd = 26.dp, bottomStart = 26.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 26.dp)
            ) {
                books.forEachIndexed { index, book ->
                    FriendBookRow(book = book)

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
private fun FriendBookRow(book: FriendBook) {
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
                    contentDescription = stringResource(R.string.friend_views),
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
private fun FriendScreenPreview() {
    IBookTheme {
        FriendScreen()
    }
}
