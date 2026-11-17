package com.example.dz.presentation.social.invite_friends

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicAvatar
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.components.organic.organicIdentityTint
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.invite_add
import dz.shared.generated.resources.invite_already_on_dz
import dz.shared.generated.resources.invite_by_text
import dz.shared.generated.resources.invite_copied
import dz.shared.generated.resources.invite_copy
import dz.shared.generated.resources.invite_email
import dz.shared.generated.resources.invite_headline
import dz.shared.generated.resources.invite_invite
import dz.shared.generated.resources.invite_share
import dz.shared.generated.resources.invite_subtext
import dz.shared.generated.resources.invite_title
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

/**
 * Invite Friends — referral link sharing and contact list matching in the Organic design system.
 *
 * Geometry from `#scr-invite-friends`: 24dp gutter, dark olive referral card with a decorative
 * circle and an inset "Copy" pill, then Email/Share pills, then two contact sections. "Copy" is
 * wired to the real clipboard; Email/Share have no platform share-sheet yet (nothing in this
 * codebase hands a link to the OS share sheet), so they dispatch ShareClicked and currently no-op.
 * Per-contact subtitles from the mock ("From your contacts", a phone number) aren't shown — there's
 * no contacts-permission integration behind this static list, so nothing backs them.
 */
@Composable
fun InviteFriendList2Screen(
    uiState: InviteFriendsUiState = InviteFriendsUiState(),
    onEvent: (InviteFriendsEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboardManager.current
    var justCopied by remember { mutableStateOf(false) }
    LaunchedEffect(justCopied) {
        if (justCopied) {
            delay(1500)
            justCopied = false
        }
    }

    val onDzContacts = uiState.contacts.filter { it.onDz }
    val textContacts = uiState.contacts.filter { !it.onDz }

    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = ORGANIC_TAB_BAR_CLEARANCE
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = stringResource(Res.string.invite_title),
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = { onEvent(InviteFriendsEvent.BackClicked) },
                            contentDescription = "Back",
                            size = 38.dp
                        )
                    }
                )
            }

            // Referral Card
            item(key = "referral") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER)
                        .clip(RoundedCornerShape(OrganicShape.radiusLg))
                        .background(OrganicColors.accent2_800)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 50.dp, y = (-60).dp)
                            .size(170.dp)
                            .clip(CircleShape)
                            .background(OrganicColors.accent2_700)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.invite_headline),
                            fontFamily = organicHeadingFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            lineHeight = 27.sp,
                            color = OrganicColors.bg
                        )
                        Text(
                            text = stringResource(Res.string.invite_subtext),
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = OrganicColors.accent2_200
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .height(46.dp)
                                .clip(RoundedCornerShape(OrganicShape.pill))
                                .background(OrganicColors.bg)
                                .padding(start = 18.dp, end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.referralLink,
                                modifier = Modifier.weight(1f),
                                fontFamily = organicBodyFontFamily(),
                                fontSize = 13.sp,
                                color = OrganicColors.neutral800
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(OrganicShape.pill))
                                    .background(OrganicColors.accent)
                                    .clickable(role = Role.Button) {
                                        clipboard.setText(AnnotatedString(uiState.referralLink))
                                        justCopied = true
                                    }
                                    .padding(horizontal = 15.dp, vertical = 9.dp)
                            ) {
                                Text(
                                    text = stringResource(
                                        if (justCopied) Res.string.invite_copied else Res.string.invite_copy
                                    ),
                                    fontFamily = organicBodyFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Email / Share
            item(key = "share_row") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShareOptionButton(
                        icon = OrganicIcons.Mail,
                        label = stringResource(Res.string.invite_email),
                        onClick = { onEvent(InviteFriendsEvent.ShareClicked) },
                        modifier = Modifier.weight(1f)
                    )
                    ShareOptionButton(
                        icon = OrganicIcons.Share,
                        label = stringResource(Res.string.invite_share),
                        onClick = { onEvent(InviteFriendsEvent.ShareClicked) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (onDzContacts.isNotEmpty()) {
                item(key = "on_dz_header") {
                    OrganicKicker(
                        text = stringResource(Res.string.invite_already_on_dz),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER)
                    )
                }
                itemsIndexed(items = onDzContacts, key = { _, c -> c.id }) { index, contact ->
                    Column(modifier = Modifier.padding(horizontal = ORGANIC_GUTTER)) {
                        ContactRow(
                            contact = contact,
                            actionLabel = stringResource(Res.string.invite_add),
                            actionBackground = OrganicColors.accent,
                            actionTextColor = Color.White,
                            onAction = { onEvent(InviteFriendsEvent.ContactActionClicked(contact.id)) }
                        )
                        if (index < onDzContacts.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 8.dp),
                                thickness = 1.dp,
                                color = OrganicColors.neutral200
                            )
                        }
                    }
                }
            }

            if (textContacts.isNotEmpty()) {
                item(key = "by_text_header") {
                    OrganicKicker(
                        text = stringResource(Res.string.invite_by_text),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER, vertical = 4.dp)
                    )
                }
                itemsIndexed(items = textContacts, key = { _, c -> c.id }) { index, contact ->
                    Column(modifier = Modifier.padding(horizontal = ORGANIC_GUTTER)) {
                        ContactRow(
                            contact = contact,
                            actionLabel = stringResource(Res.string.invite_invite),
                            actionBackground = OrganicColors.neutral200,
                            actionTextColor = OrganicColors.neutral800,
                            onAction = { onEvent(InviteFriendsEvent.ContactActionClicked(contact.id)) }
                        )
                        if (index < textContacts.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 8.dp),
                                thickness = 1.dp,
                                color = OrganicColors.neutral200
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShareOptionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral200)
            .clickable(role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OrganicColors.neutral800,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = OrganicColors.neutral900
        )
    }
}

@Composable
private fun ContactRow(
    contact: ContactUi,
    actionLabel: String,
    actionBackground: Color,
    actionTextColor: Color,
    onAction: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        OrganicAvatar(
            name = contact.name,
            size = 44.dp,
            fontSize = 17.sp,
            background = organicIdentityTint(contact.id)
        )

        Text(
            text = contact.name,
            modifier = Modifier.weight(1f),
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = OrganicColors.text
        )

        val invited = contact.invited
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(OrganicShape.pill))
                .background(if (invited) OrganicColors.neutral200 else actionBackground)
                .then(
                    if (invited) Modifier else Modifier.clickable(role = Role.Button, onClick = onAction)
                )
                .padding(horizontal = 16.dp, vertical = 9.dp)
        ) {
            Text(
                text = if (invited) "✓" else actionLabel,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (invited) OrganicColors.neutral600 else actionTextColor
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 900)
@Composable
private fun InviteFriendList2ScreenPreview() {
    InviteFriendList2Screen()
}
