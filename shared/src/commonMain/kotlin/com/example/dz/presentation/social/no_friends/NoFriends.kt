package com.example.dz.presentation.social.no_friends

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkButton
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.friends_title
import dz.shared.generated.resources.nofriends_body
import dz.shared.generated.resources.nofriends_find
import dz.shared.generated.resources.nofriends_invite_contacts
import dz.shared.generated.resources.nofriends_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoFriendsScreen(
    onEvent: (NoFriendsEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
    ) {
        InkTopBar(
            title = stringResource(Res.string.friends_title),
            onBackClick = { onEvent(NoFriendsEvent.BackClicked) },
            colors = colors
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 44.dp, end = 44.dp, bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            // stacked-cards illustration
            Box(
                modifier = Modifier.size(width = 96.dp, height = 72.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 12.dp)
                        .size(52.dp)
                        .graphicsLayer { rotationZ = -8f }
                        .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .background(colors.alt)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 12.dp)
                        .size(52.dp)
                        .graphicsLayer { rotationZ = 8f }
                        .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .background(colors.alt)
                )
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .background(colors.accentSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = InkIcons.User,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Text(
                text = stringResource(Res.string.nofriends_title),
                modifier = Modifier.padding(top = 26.dp),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = colors.ink
            )
            Text(
                text = stringResource(Res.string.nofriends_body),
                modifier = Modifier.padding(top = 12.dp),
                fontFamily = bodyFont,
                fontSize = 13.5.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                color = colors.muted
            )
            InkButton(
                text = stringResource(Res.string.nofriends_find),
                onClick = { onEvent(NoFriendsEvent.FindFriendsClicked) },
                modifier = Modifier.padding(top = 26.dp),
                colors = colors
            )
            Text(
                text = stringResource(Res.string.nofriends_invite_contacts),
                modifier = Modifier
                    .padding(top = 18.dp)
                    .clickable { onEvent(NoFriendsEvent.InviteContactsClicked) }
                    .padding(6.dp),
                fontFamily = bodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = colors.accent
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun NoFriendsScreenPreview() {
    NoFriendsScreen()
}
