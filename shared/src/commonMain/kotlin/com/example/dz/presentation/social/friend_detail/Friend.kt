package com.example.dz.presentation.social.friend_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkProgressBar
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import dz.shared.generated.resources.friend_friends
import dz.shared.generated.resources.friend_message
import dz.shared.generated.resources.friend_reading_now
import dz.shared.generated.resources.friend_see_all
import dz.shared.generated.resources.olive_again_book
import dz.shared.generated.resources.profile_2
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val shelfCovers = listOf(
    Res.drawable.book_cover_4,
    Res.drawable.book_cover,
    Res.drawable.book_cover_2,
    Res.drawable.book_cover_3
)

@Composable
fun FriendScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
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
            InkIconButton(icon = InkIcons.Back, onClick = onBackClick, colors = colors)
            Spacer(modifier = Modifier.weight(1f))
            InkIconButton(icon = InkIcons.MoreVertical, onClick = onSettingsClick, colors = colors)
        }

        // identity
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 26.dp, end = 26.dp, top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Image(
                    painter = painterResource(Res.drawable.profile_2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(92.dp)
                        .shadow(14.dp, RoundedCornerShape(InkShape.radiusSm + 6.dp), clip = true)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(colors.paper)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(colors.accent)
                )
            }
            Text(
                text = "Patricia Lane",
                modifier = Modifier.padding(top = 16.dp),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = colors.ink
            )
            Text(
                text = "@patricia.reads · friends since March",
                modifier = Modifier.padding(top = 7.dp),
                fontFamily = bodyFont,
                fontSize = 12.5.sp,
                color = colors.muted
            )
        }

        // stats card
        Row(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 20.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(InkShape.radius))
                .background(colors.surface)
                .border(1.dp, colors.line, RoundedCornerShape(InkShape.radius))
                .padding(vertical = 15.dp)
        ) {
            StatCell("73", "Books", colors, Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
            StatCell("28", "Friends", colors, Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
            StatCell("9", "In common", colors, Modifier.weight(1f))
        }

        // action buttons
        Row(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .background(colors.accentSoft)
                    .clickable(onClick = onSettingsClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = InkIcons.Done,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = stringResource(Res.string.friend_friends),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = colors.accent
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .background(colors.accent)
                    .clickable(onClick = onMessageClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = InkIcons.Chat,
                    contentDescription = null,
                    tint = colors.onAccent,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = stringResource(Res.string.friend_message),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = colors.onAccent
                )
            }
        }

        // reading now
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
            InkLabel(text = stringResource(Res.string.friend_reading_now), colors = colors)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(InkShape.radius))
                    .background(colors.surface)
                    .border(1.dp, colors.line, RoundedCornerShape(InkShape.radius))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.olive_again_book),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 46.dp, height = 68.dp)
                        .shadow(6.dp, RoundedCornerShape(InkShape.cover), clip = true)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Olive, Again",
                        fontFamily = displayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = colors.ink
                    )
                    Text(
                        text = "Elizabeth Strout",
                        modifier = Modifier.padding(top = 4.dp),
                        fontFamily = bodyFont,
                        fontSize = 11.5.sp,
                        color = colors.muted
                    )
                    InkProgressBar(
                        progress = 0.44f,
                        modifier = Modifier.fillMaxWidth(0.8f).padding(top = 10.dp),
                        colors = colors
                    )
                }
            }
        }

        // friend's shelf
        Column(modifier = Modifier.padding(top = 22.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Patricia's shelf",
                    modifier = Modifier.weight(1f),
                    fontFamily = displayFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = colors.ink
                )
                Text(
                    text = stringResource(Res.string.friend_see_all),
                    fontFamily = bodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colors.accent
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 13.dp)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                shelfCovers.forEach { cover ->
                    Image(
                        painter = painterResource(cover),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 72.dp, height = 106.dp)
                            .shadow(6.dp, RoundedCornerShape(InkShape.cover), clip = true)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCell(
    value: String,
    label: String,
    colors: InkColors,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            color = colors.ink
        )
        Text(
            text = label.uppercase(),
            modifier = Modifier.padding(top = 5.dp),
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            letterSpacing = 0.8.sp,
            color = colors.muted
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun FriendScreenPreview() {
    FriendScreen()
}
