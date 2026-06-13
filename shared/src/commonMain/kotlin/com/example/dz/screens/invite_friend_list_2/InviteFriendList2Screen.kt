package com.example.dz.screens.invite_friend_list_2

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkLabel
import com.example.dz.app_components.ink.InkTopBar
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.img_masaa
import dz.shared.generated.resources.img_neil_alvin
import dz.shared.generated.resources.img_raunak_purohit
import dz.shared.generated.resources.img_yza_barretto
import dz.shared.generated.resources.invite_add_friend
import dz.shared.generated.resources.invite_banner_label
import dz.shared.generated.resources.invite_from_contacts
import dz.shared.generated.resources.invite_invite
import dz.shared.generated.resources.invite_reads_on_dz
import dz.shared.generated.resources.invite_share
import dz.shared.generated.resources.invite_suggested
import dz.shared.generated.resources.invite_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class Contact(
    val name: String,
    val avatarRes: DrawableResource,
    val onDz: Boolean
)

private val contacts = listOf(
    Contact("Masaa Okafor", Res.drawable.img_masaa, false),
    Contact("Raunak Purohit", Res.drawable.img_raunak_purohit, false),
    Contact("Yza Barretto", Res.drawable.img_yza_barretto, true),
    Contact("Neil Alvin", Res.drawable.img_neil_alvin, false)
)

@Composable
fun InviteFriendList2Screen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    onDiscoverPeopleClick: () -> Unit = {}
) {
    val colors = inkColors()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        InkTopBar(
            title = stringResource(Res.string.invite_title),
            onBackClick = onBackClick,
            colors = colors
        )

        // referral banner
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(InkShape.radius))
                .background(colors.alt)
                .padding(18.dp)
        ) {
            InkLabel(text = stringResource(Res.string.invite_banner_label), colors = colors.copy(muted = colors.accent))
            Text(
                text = buildAnnotatedString {
                    append("Invite a friend — you each get ")
                    withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) {
                        append("100 coins")
                    }
                    append(" when they finish their first book.")
                },
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = bodyFont,
                fontSize = 13.sp,
                lineHeight = 21.sp,
                color = colors.inkSoft
            )
            Row(
                modifier = Modifier.padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // dashed referral link box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(InkShape.radiusSm))
                        .background(colors.surface),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val lineColor = colors.line
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = lineColor,
                            style = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(InkShape.radiusSm.toPx())
                        )
                    }
                    Text(
                        text = "dz.app/r/amelia",
                        modifier = Modifier.padding(horizontal = 14.dp),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp,
                        letterSpacing = 0.4.sp,
                        color = colors.inkSoft
                    )
                }
                Row(
                    modifier = Modifier
                        .height(44.dp)
                        .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .background(colors.accent)
                        .clickable(onClick = onMessageClick)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = InkIcons.Share,
                        contentDescription = null,
                        tint = colors.onAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = stringResource(Res.string.invite_share),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = colors.onAccent
                    )
                }
            }
        }

        // suggested contacts
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
            InkLabel(text = stringResource(Res.string.invite_suggested), colors = colors)
            Column(modifier = Modifier.padding(top = 4.dp)) {
                contacts.forEachIndexed { i, contact ->
                    ContactRow(contact = contact, onAction = onDiscoverPeopleClick, colors = colors)
                    if (i < contacts.size - 1) {
                        HorizontalDivider(thickness = 1.dp, color = colors.line)
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactRow(
    contact: Contact,
    onAction: () -> Unit,
    colors: InkColors,
) {
    val bodyFont = inkBodyFontFamily()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Image(
            painter = painterResource(contact.avatarRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(InkShape.radiusSm))
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contact.name,
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.5.sp,
                color = colors.ink
            )
            Text(
                text = if (contact.onDz) stringResource(Res.string.invite_reads_on_dz)
                else stringResource(Res.string.invite_from_contacts),
                modifier = Modifier.padding(top = 5.dp),
                fontFamily = bodyFont,
                fontSize = 11.5.sp,
                color = if (contact.onDz) colors.accent else colors.muted
            )
        }
        // action pill
        Box(
            modifier = Modifier
                .height(32.dp)
                .clip(CircleShape)
                .let {
                    if (contact.onDz) it.background(colors.accent)
                    else it.border(1.dp, colors.accent, CircleShape)
                }
                .clickable(onClick = onAction)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (contact.onDz) stringResource(Res.string.invite_add_friend)
                else stringResource(Res.string.invite_invite),
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = if (contact.onDz) colors.onAccent else colors.accent
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun InviteFriendList2ScreenPreview() {
    InviteFriendList2Screen()
}
