package com.example.dz.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.app_components.ink.inkCard
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.img_maria_renzy
import dz.shared.generated.resources.img_neil_alvin
import dz.shared.generated.resources.profile_1
import dz.shared.generated.resources.profile_2
import dz.shared.generated.resources.profile_3
import dz.shared.generated.resources.profile_menu_collections
import dz.shared.generated.resources.profile_menu_goals
import dz.shared.generated.resources.profile_menu_membership
import dz.shared.generated.resources.profile_menu_purchases
import dz.shared.generated.resources.profile_menu_settings
import dz.shared.generated.resources.profile_edit
import dz.shared.generated.resources.profile_stat_books
import dz.shared.generated.resources.profile_stat_friends
import dz.shared.generated.resources.profile_stat_streak
import dz.shared.generated.resources.profile_you
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val friendPreviewAvatars = listOf(
    Res.drawable.profile_2,
    Res.drawable.profile_3,
    Res.drawable.img_maria_renzy,
    Res.drawable.img_neil_alvin
)

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onFriendsClick: () -> Unit = {},
    onGoalsClick: () -> Unit = {},
    onCollectionsClick: () -> Unit = {},
    onPurchasesClick: () -> Unit = {},
    onMembershipClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    ProfileScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onNotificationsClick = onNotificationsClick,
        onFriendsClick = onFriendsClick,
        onGoalsClick = onGoalsClick,
        onCollectionsClick = onCollectionsClick,
        onPurchasesClick = onPurchasesClick,
        onMembershipClick = onMembershipClick,
        onSettingsClick = onSettingsClick
    )
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onFriendsClick: () -> Unit = {},
    onGoalsClick: () -> Unit = {},
    onCollectionsClick: () -> Unit = {},
    onPurchasesClick: () -> Unit = {},
    onMembershipClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    val menu = listOf(
        Triple(InkIcons.Stats, stringResource(Res.string.profile_menu_goals), "21-day streak") to onGoalsClick,
        Triple(InkIcons.Book, stringResource(Res.string.profile_menu_collections), "3 shelves") to onCollectionsClick,
        Triple(InkIcons.Purchased, stringResource(Res.string.profile_menu_purchases), "14 books") to onPurchasesClick,
        Triple(InkIcons.Premium, stringResource(Res.string.profile_menu_membership), "Free plan") to onMembershipClick,
        Triple(InkIcons.Settings, stringResource(Res.string.profile_menu_settings), "") to onSettingsClick
    )

    Column(
        modifier = modifier
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
                .padding(start = 22.dp, end = 22.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            InkIconButton(icon = InkIcons.Back, onClick = onBackClick, colors = colors)
            Text(
                text = stringResource(Res.string.profile_you),
                modifier = Modifier.weight(1f),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = colors.ink
            )
            InkIconButton(icon = InkIcons.Bell, onClick = onNotificationsClick, colors = colors)
        }

        // identity row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.profile_1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .shadow(10.dp, RoundedCornerShape(InkShape.radiusSm + 4.dp), clip = true)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Amelia Hartwell",
                    fontFamily = displayFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 21.sp,
                    color = colors.ink
                )
                Text(
                    text = "@amelia.reads · since 2024",
                    modifier = Modifier.padding(top = 6.dp),
                    fontFamily = bodyFont,
                    fontSize = 12.5.sp,
                    color = colors.muted
                )
            }
            Text(
                text = stringResource(Res.string.profile_edit),
                modifier = Modifier
                    .clickable(onClick = onEditClick)
                    .padding(6.dp),
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = colors.accent
            )
        }

        // stats card
        Row(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 20.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .inkCard(colors)
                .padding(vertical = 15.dp)
        ) {
            StatCell("48", stringResource(Res.string.profile_stat_books), colors, Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
            StatCell("12", stringResource(Res.string.profile_stat_friends), colors, Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = colors.line)
            StatCell("21d", stringResource(Res.string.profile_stat_streak), colors, Modifier.weight(1f))
        }

        // friends preview
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, top = 20.dp)
                .clickable(onClick = onFriendsClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box {
                friendPreviewAvatars.indices.reversed().forEach { i ->
                    Image(
                        painter = painterResource(friendPreviewAvatars[i]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .offset(x = (23 * i).dp)
                            .size(32.dp)
                            .clip(RoundedCornerShape(InkShape.radiusSm - 2.dp))
                            .border(2.dp, colors.paper, RoundedCornerShape(InkShape.radiusSm - 2.dp))
                    )
                }
                Spacer(modifier = Modifier.size(width = (32 + 23 * (friendPreviewAvatars.size - 1)).dp, height = 32.dp))
            }
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) {
                        append("12 friends")
                    }
                    append(" · 3 reading now")
                },
                modifier = Modifier.weight(1f),
                fontFamily = bodyFont,
                fontSize = 12.5.sp,
                color = colors.inkSoft
            )
            Icon(
                imageVector = InkIcons.Back,
                contentDescription = null,
                tint = colors.muted,
                modifier = Modifier
                    .size(14.dp)
                    .graphicsLayer { rotationZ = 180f }
            )
        }

        // menu card
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 20.dp)
                .fillMaxWidth()
                .inkCard(colors)
        ) {
            menu.forEachIndexed { i, (entry, onClick) ->
                if (i > 0) HorizontalDivider(thickness = 1.dp, color = colors.line)
                MenuRow(
                    icon = entry.first,
                    title = entry.second,
                    value = entry.third,
                    onClick = onClick,
                    colors = colors
                )
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

@Composable
private fun MenuRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit,
    colors: InkColors,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(InkShape.radiusSm))
                .background(colors.alt),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.5.sp,
            color = colors.ink
        )
        if (value.isNotEmpty()) {
            Text(
                text = value,
                fontFamily = inkBodyFontFamily(),
                fontSize = 11.5.sp,
                color = colors.muted
            )
        }
        Icon(
            imageVector = InkIcons.Back,
            contentDescription = null,
            tint = colors.muted,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(13.dp)
                .graphicsLayer { rotationZ = 180f }
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen()
}
