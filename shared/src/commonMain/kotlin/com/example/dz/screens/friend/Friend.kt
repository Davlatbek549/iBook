package com.example.dz.screens.friend

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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.ColorCategoryFantasy
import com.example.dz.theme.ColorSecondary
import com.example.dz.theme.ColorTertiary
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class FriendBook(
    val title: StringResource,
    val author: StringResource,
    val views: StringResource,
    val firstGenre: StringResource,
    val secondGenre: StringResource,
    val cover: DrawableResource
)

private data class ReadingCardPalette(
    val gradientColors: List<Color>,
    val ambientColor: Color
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
            title = Res.string.friend_read_at_the_bone,
            author = Res.string.friend_read_at_the_bone_author,
            views = Res.string.friend_views_1320,
            firstGenre = Res.string.friend_genre_horror,
            secondGenre = Res.string.friend_genre_fantasy,
            cover = Res.drawable.author_detail_red_at_bone
        ),
        FriendBook(
            title = Res.string.friend_bestiary,
            author = Res.string.friend_bestiary_author,
            views = Res.string.friend_views_1320,
            firstGenre = Res.string.friend_genre_horror,
            secondGenre = Res.string.friend_genre_fantasy,
            cover = Res.drawable.author_detail_bestiary
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

            SectionTitle(text = stringResource(Res.string.friend_reading))

            Spacer(modifier = Modifier.height(16.dp))

            ReadingNowCard()

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(text = stringResource(Res.string.friend_collections))

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
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = stringResource(Res.string.friend_back),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CircleIconButton(onClick = onMessageClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_message),
                    contentDescription = stringResource(Res.string.friend_messages),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(21.dp)
                )
            }

            CircleIconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_settings),
                    contentDescription = stringResource(Res.string.friend_settings),
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
                icon = Res.drawable.ic_delete,
                text = stringResource(Res.string.friend_action_delete),
                onClick = onDismiss
            )
            SettingsDivider()
            SettingsActionRow(
                icon = Res.drawable.ic_visibility_off,
                text = stringResource(Res.string.friend_action_ignore),
                onClick = onDismiss
            )
            SettingsDivider()
            SettingsActionRow(
                icon = Res.drawable.ic_bell,
                text = stringResource(Res.string.friend_action_mute),
                onClick = onDismiss
            )
            SettingsDivider()
            SettingsActionRow(
                icon = Res.drawable.ic_close,
                text = stringResource(Res.string.friend_action_block),
                onClick = onDismiss
            )
        }
    }
}

@Composable
private fun SettingsActionRow(
    icon: DrawableResource,
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
                painter = painterResource(icon),
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
                painter = painterResource(Res.drawable.book_olive_again),
                contentDescription = stringResource(Res.string.friend_profile_image),
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
                    painter = painterResource(Res.drawable.ic_premium),
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.friend_username),
                color = colors.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ProfileStat(
                    value = stringResource(Res.string.friend_total_read_value),
                    label = stringResource(Res.string.friend_total_read)
                )
                ProfileStat(
                    value = stringResource(Res.string.friend_total_reading_value),
                    label = stringResource(Res.string.friend_total_reading),
                    unit = stringResource(Res.string.friend_total_reading_unit)
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
    val coverRes = Res.drawable.book_cover_4
    val palette = rememberReadingCardPalette(coverRes)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    colors = palette.gradientColors
                )
            )
    ) {
        Image(
            painter = painterResource(coverRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.28f,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = 1.42f
                    scaleY = 1.42f
                }
                .blur(18.dp)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            palette.ambientColor.copy(alpha = 0.26f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.24f)
                        )
                    )
                )
        )

        Image(
            painter = painterResource(coverRes),
            contentDescription = stringResource(Res.string.friend_featured_book_title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 10.dp)
                .width(100.dp)
                .height(128.dp)
                .align(Alignment.BottomStart)
                .offset(y = 30.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 134.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.friend_featured_book_title),
                color = colors.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stringResource(Res.string.friend_featured_book_author),
                color = colors.onPrimary.copy(alpha = 0.86f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                FriendAvatar(color = ColorSecondary, offset = 0)
                FriendAvatar(color = ColorCategoryFantasy, offset = -8)
                FriendAvatar(color = ColorTertiary, offset = -16)

                Text(
                    text = stringResource(Res.string.friend_other_readers),
                    color = colors.onPrimary.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.offset(x = (-8).dp)
                )
            }
        }
    }
}

@Composable
private fun rememberReadingCardPalette(
    coverRes: DrawableResource
): ReadingCardPalette {
    return remember(coverRes) {
        ReadingCardPalette(
            gradientColors = listOf(Color(0xFF9F641E), Color(0xFF19326B), Color(0xFF121A45)),
            ambientColor = ColorCategoryFantasy
        )
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
                .width(80.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(10.dp))
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
                    contentDescription = stringResource(Res.string.friend_views),
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
    DZTheme {
        FriendScreen()
    }
}
