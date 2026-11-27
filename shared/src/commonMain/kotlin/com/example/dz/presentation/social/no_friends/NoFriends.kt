package com.example.dz.presentation.social.no_friends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.OrganicButton
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.friends_title
import dz.shared.generated.resources.nofriends_body
import dz.shared.generated.resources.nofriends_find
import dz.shared.generated.resources.nofriends_invite_by_link
import dz.shared.generated.resources.nofriends_title
import org.jetbrains.compose.resources.stringResource

/**
 * No Friends Yet — the empty variant of the social list in the Organic design system.
 *
 * Geometry from `#scr-no-friends-yet`: "Friends" header (this replaces the list, same title),
 * two soft background blobs, a dashed-card + person-plus badge illustration, Caprasimo headline,
 * and two full-width actions. The mock's "N people from your contacts are on dz" teaser is left
 * out — there's no contacts-permission or matching backend behind it, so nothing makes that count
 * true for a real user (the static placeholder list Invite Friends uses isn't a fit for a factual
 * claim like this one).
 */
@Composable
fun NoFriendsScreen(
    onEvent: (NoFriendsEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    OrganicScreen(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            OrganicScreenHeader(
                title = stringResource(Res.string.friends_title),
                fontSize = 28.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ORGANIC_GUTTER, vertical = 12.dp),
                leading = {
                    OrganicCircleIconButton(
                        icon = OrganicIcons.ChevronLeft,
                        onClick = { onEvent(NoFriendsEvent.BackClicked) },
                        contentDescription = "Back",
                        size = 38.dp
                    )
                }
            )

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .offset(x = (-110).dp, y = 150.dp)
                        .size(300.dp)
                        .clip(CircleShape)
                        .background(OrganicColors.accent2_100)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 80.dp, y = 400.dp)
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(OrganicColors.accent100)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = ORGANIC_GUTTER),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    EmptyFriendsIllustration()

                    Text(
                        text = stringResource(Res.string.nofriends_title),
                        modifier = Modifier.padding(top = 24.dp),
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp,
                        lineHeight = 33.sp,
                        color = OrganicColors.text,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(Res.string.nofriends_body),
                        modifier = Modifier.padding(top = 10.dp),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 16.sp,
                        lineHeight = 26.sp,
                        textAlign = TextAlign.Center,
                        color = OrganicColors.neutral700
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ORGANIC_GUTTER, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OrganicButton(
                    text = stringResource(Res.string.nofriends_find),
                    onClick = { onEvent(NoFriendsEvent.FindFriendsClicked) },
                    background = OrganicColors.accent
                )
                OrganicButton(
                    text = stringResource(Res.string.nofriends_invite_by_link),
                    onClick = { onEvent(NoFriendsEvent.InviteByLinkClicked) },
                    background = OrganicColors.neutral200,
                    textColor = OrganicColors.neutral900
                )
            }
        }
    }
}

@Composable
private fun EmptyFriendsIllustration() {
    Box(
        modifier = Modifier.size(width = 210.dp, height = 150.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        DashedCard(rotationDegrees = -7f, modifier = Modifier.align(Alignment.CenterStart))
        DashedCard(rotationDegrees = 7f, modifier = Modifier.align(Alignment.CenterEnd))

        Box(
            modifier = Modifier
                .size(82.dp)
                .clip(CircleShape)
                .background(OrganicColors.accent2_700),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = OrganicIcons.UserPlus,
                contentDescription = null,
                tint = OrganicColors.bg,
                modifier = Modifier.size(38.dp)
            )
        }
    }
}

@Composable
private fun DashedCard(
    rotationDegrees: Float,
    modifier: Modifier = Modifier,
) {
    val lineColor = OrganicColors.neutral400
    Box(
        modifier = modifier
            .size(width = 74.dp, height = 104.dp)
            .rotate(rotationDegrees)
            .clip(RoundedCornerShape(12.dp))
            .background(OrganicColors.neutral200)
            .drawBehind {
                drawRoundRect(
                    color = lineColor,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(9f, 7f))
                    ),
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
            }
    )
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun NoFriendsScreenPreview() {
    NoFriendsScreen()
}
