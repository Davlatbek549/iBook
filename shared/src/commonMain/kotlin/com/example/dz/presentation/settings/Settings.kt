package com.example.dz.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.navigationBarsPadding
import com.example.dz.designsystem.components.organic.OrganicToast
import dz.shared.generated.resources.ep_saved_body
import dz.shared.generated.resources.ep_saved_title
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.domain.model.PageTheme
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicButton
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.components.organic.OrganicToggle
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.nav_back
import dz.shared.generated.resources.reader_font_sans
import dz.shared.generated.resources.reader_font_serif
import dz.shared.generated.resources.reader_theme_cream
import dz.shared.generated.resources.reader_theme_night
import dz.shared.generated.resources.reader_theme_paper
import dz.shared.generated.resources.reader_theme_sage
import dz.shared.generated.resources.set_app_kicker
import dz.shared.generated.resources.set_auto_night_mode
import dz.shared.generated.resources.set_auto_night_mode_sub
import dz.shared.generated.resources.set_daily_goal_nudge
import dz.shared.generated.resources.set_daily_goal_nudge_sub
import dz.shared.generated.resources.set_delete_account
import dz.shared.generated.resources.set_delete_body
import dz.shared.generated.resources.set_delete_cancel
import dz.shared.generated.resources.set_delete_title
import dz.shared.generated.resources.set_downloads
import dz.shared.generated.resources.set_downloads_wifi_only
import dz.shared.generated.resources.set_edit_action
import dz.shared.generated.resources.set_friend_activity
import dz.shared.generated.resources.set_friend_activity_sub
import dz.shared.generated.resources.set_help_contact
import dz.shared.generated.resources.set_keep_screen_awake
import dz.shared.generated.resources.set_keep_screen_awake_sub
import dz.shared.generated.resources.set_notifications
import dz.shared.generated.resources.set_privacy_kicker
import dz.shared.generated.resources.set_reading
import dz.shared.generated.resources.set_shelves_friends_only
import dz.shared.generated.resources.set_shelves_privacy
import dz.shared.generated.resources.set_sign_out
import dz.shared.generated.resources.set_store_offers
import dz.shared.generated.resources.set_store_offers_sub
import dz.shared.generated.resources.set_terms_privacy
import dz.shared.generated.resources.set_title
import dz.shared.generated.resources.set_type_theme
import dz.shared.generated.resources.set_type_theme_value
import dz.shared.generated.resources.set_version
import org.jetbrains.compose.resources.stringResource

/**
 * Settings — identity, then four grouped lists: Reading, Notifications, Privacy, App. Every row
 * icon sits on the same 32dp neutral circle; only the trailing control (chevron, toggle, or
 * nothing) tells you what a row does.
 *
 * The handoff's own mock has no delete-account row — its "Edit" pill is annotated as leading to
 * name/email/password editing, which presumably has one. That destination is not built yet, and
 * this app already has a real, working delete flow, so it keeps its spot here next to Sign out
 * rather than disappearing because the reference frame does not draw it.
 *
 * Geometry from `dz-all-screens.html`.
 */
@Composable
fun SettingsScreen(
    uiState: SettingsUiState = SettingsUiState(),
    onEvent: (SettingsEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = stringResource(Res.string.set_title),
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = { onEvent(SettingsEvent.BackClicked) },
                            contentDescription = stringResource(Res.string.nav_back),
                            size = 38.dp,
                            iconSize = 18.dp,
                        )
                    },
                )
            }

            item(key = "identity") {
                IdentityCard(uiState = uiState, onEdit = { onEvent(SettingsEvent.EditProfileClicked) })
            }

            item(key = "reading") {
                Column(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OrganicKicker(
                        text = stringResource(Res.string.set_reading),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    SettingsChevronRow(
                        icon = OrganicIcons.TextSize,
                        label = stringResource(Res.string.set_type_theme),
                        value = stringResource(
                            Res.string.set_type_theme_value,
                            stringResource(if (uiState.useSerif) Res.string.reader_font_serif else Res.string.reader_font_sans),
                            uiState.pageTheme.label(),
                        ),
                        onClick = { onEvent(SettingsEvent.TypeAndThemeClicked) },
                    )
                    SettingsToggleRow(
                        icon = OrganicIcons.Sun,
                        label = stringResource(Res.string.set_auto_night_mode),
                        sublabel = stringResource(Res.string.set_auto_night_mode_sub),
                        checked = uiState.isAutoNightMode,
                        onCheckedChange = { onEvent(SettingsEvent.AutoNightModeToggled(it)) },
                    )
                    SettingsToggleRow(
                        icon = OrganicIcons.Device,
                        label = stringResource(Res.string.set_keep_screen_awake),
                        sublabel = stringResource(Res.string.set_keep_screen_awake_sub),
                        checked = uiState.isKeepScreenAwake,
                        onCheckedChange = { onEvent(SettingsEvent.KeepScreenAwakeToggled(it)) },
                    )
                    SettingsChevronRow(
                        icon = OrganicIcons.Download,
                        label = stringResource(Res.string.set_downloads),
                        value = stringResource(Res.string.set_downloads_wifi_only),
                        onClick = { onEvent(SettingsEvent.DownloadsClicked) },
                    )
                }
            }

            item(key = "notifications") {
                Column(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OrganicKicker(
                        text = stringResource(Res.string.set_notifications),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    SettingsToggleRow(
                        icon = OrganicIcons.Bell,
                        label = stringResource(Res.string.set_daily_goal_nudge),
                        sublabel = stringResource(Res.string.set_daily_goal_nudge_sub),
                        checked = uiState.isDailyGoalNudgeEnabled,
                        onCheckedChange = { onEvent(SettingsEvent.DailyGoalNudgeToggled(it)) },
                    )
                    SettingsToggleRow(
                        icon = OrganicIcons.Friends,
                        label = stringResource(Res.string.set_friend_activity),
                        sublabel = stringResource(Res.string.set_friend_activity_sub),
                        checked = uiState.isFriendActivityEnabled,
                        onCheckedChange = { onEvent(SettingsEvent.FriendActivityToggled(it)) },
                    )
                    SettingsToggleRow(
                        icon = OrganicIcons.Mail,
                        label = stringResource(Res.string.set_store_offers),
                        sublabel = stringResource(Res.string.set_store_offers_sub),
                        checked = uiState.isStoreOffersEnabled,
                        onCheckedChange = { onEvent(SettingsEvent.StoreOffersToggled(it)) },
                    )
                }
            }

            item(key = "privacy") {
                Column(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OrganicKicker(
                        text = stringResource(Res.string.set_privacy_kicker),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    SettingsChevronRow(
                        icon = OrganicIcons.Lock,
                        label = stringResource(Res.string.set_shelves_privacy),
                        value = stringResource(Res.string.set_shelves_friends_only),
                        onClick = { onEvent(SettingsEvent.ShelvesPrivacyClicked) },
                    )
                }
            }

            item(key = "app") {
                Column(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    OrganicKicker(
                        text = stringResource(Res.string.set_app_kicker),
                        color = OrganicColors.neutral600,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    SettingsChevronRow(
                        icon = OrganicIcons.Info,
                        label = stringResource(Res.string.set_help_contact),
                        value = null,
                        onClick = { onEvent(SettingsEvent.HelpClicked) },
                    )
                    SettingsChevronRow(
                        icon = OrganicIcons.Shield,
                        label = stringResource(Res.string.set_terms_privacy),
                        value = null,
                        onClick = { onEvent(SettingsEvent.TermsClicked) },
                    )
                }
            }

            item(key = "sign-out") {
                Column(
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER).padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.set_sign_out),
                            modifier = Modifier.clickable(role = Role.Button) { onEvent(SettingsEvent.SignOutClicked) },
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = OrganicColors.accent800
                        )
                        Text(
                            text = stringResource(Res.string.set_version),
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 12.sp,
                            color = OrganicColors.neutral600
                        )
                    }
                    Text(
                        text = stringResource(Res.string.set_delete_account),
                        modifier = Modifier.clickable(role = Role.Button) { onEvent(SettingsEvent.DeleteAccountClicked) },
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 12.sp,
                        color = OrganicColors.neutral600
                    )
                }
            }
        }

        if (uiState.isDeleteConfirmationVisible) {
            DeleteAccountDialog(
                isDeleting = uiState.isDeletingAccount,
                error = uiState.deleteAccountError,
                onConfirm = { onEvent(SettingsEvent.DeleteAccountConfirmed) },
                onDismiss = { onEvent(SettingsEvent.DeleteAccountDismissed) },
            )
        }

        // Back from Edit Profile after a save. Shown for three seconds, then gone on its own.
        LaunchedEffect(uiState.showProfileUpdated) {
            if (uiState.showProfileUpdated) {
                delay(PROFILE_UPDATED_VISIBLE_MS)
                onEvent(SettingsEvent.ProfileUpdatedShown)
            }
        }
        OrganicToast(
            visible = uiState.showProfileUpdated,
            title = stringResource(Res.string.ep_saved_title),
            body = stringResource(Res.string.ep_saved_body),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = ORGANIC_GUTTER, end = ORGANIC_GUTTER, bottom = 24.dp),
        )
    }
}

private const val PROFILE_UPDATED_VISIBLE_MS = 3_000L

/** Sage identity card: initial, name, email and plan, and the one way into editing any of them. */
@Composable
private fun IdentityCard(uiState: SettingsUiState, onEdit: () -> Unit) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = ORGANIC_GUTTER),
        background = OrganicColors.accent2_200,
        contentPadding = PaddingValues(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                modifier = Modifier.size(50.dp).clip(CircleShape).background(OrganicColors.accent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.avatarInitial,
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    color = Color.White
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = uiState.name,
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = OrganicColors.accent2_900
                )
                Text(
                    text = "${uiState.email} · ${uiState.membershipLabel}",
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = OrganicColors.accent2_900.copy(alpha = 0.8f)
                )
            }
            Text(
                text = stringResource(Res.string.set_edit_action),
                modifier = Modifier
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(OrganicColors.bg)
                    .clickable(role = Role.Button, onClick = onEdit)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = OrganicColors.accent2_900
            )
        }
    }
}

/** A row that leads somewhere: icon, label, an optional trailing value, then a chevron. */
@Composable
private fun SettingsChevronRow(
    icon: ImageVector,
    label: String,
    value: String?,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsRowIcon(icon)
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontFamily = organicBodyFontFamily(),
            fontSize = 15.sp,
            color = OrganicColors.text
        )
        if (value != null) {
            Text(
                text = value,
                fontFamily = organicBodyFontFamily(),
                fontSize = 13.sp,
                color = OrganicColors.neutral700
            )
        }
        Icon(
            imageVector = OrganicIcons.ChevronRight,
            contentDescription = null,
            tint = OrganicColors.neutral600,
            modifier = Modifier.size(14.dp)
        )
    }
}

/** A row that flips a switch: icon, label over a one-line subtitle, then the toggle itself. */
@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    label: String,
    sublabel: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsRowIcon(icon)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                fontFamily = organicBodyFontFamily(),
                fontSize = 15.sp,
                color = OrganicColors.text
            )
            Text(
                text = sublabel,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.neutral700
            )
        }
        OrganicToggle(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsRowIcon(icon: ImageVector) {
    Box(
        modifier = Modifier.size(32.dp).clip(CircleShape).background(OrganicColors.neutral200),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OrganicColors.neutral800,
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * Asks before anything is deleted. While the request is out, nothing dismisses it — not the
 * back gesture, not a tap outside — so the reader cannot walk away unsure whether it happened.
 */
@Composable
private fun DeleteAccountDialog(
    isDeleting: Boolean,
    error: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = !isDeleting, dismissOnClickOutside = !isDeleting)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(OrganicShape.radiusLg))
                .background(OrganicColors.bg)
                .padding(22.dp)
        ) {
            Text(
                text = stringResource(Res.string.set_delete_title),
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 21.sp,
                color = OrganicColors.text
            )
            Text(
                text = stringResource(Res.string.set_delete_body),
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = organicBodyFontFamily(),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = OrganicColors.neutral700
            )
            if (error != null) {
                Text(
                    text = error,
                    modifier = Modifier.padding(top = 12.dp),
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = OrganicColors.danger
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            // The primary button on a danger ground, so the one irreversible action here looks it.
            OrganicButton(
                text = stringResource(Res.string.set_delete_account),
                onClick = onConfirm,
                isBusy = isDeleting,
                background = OrganicColors.danger,
            )
            Text(
                text = stringResource(Res.string.set_delete_cancel),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .alpha(if (isDeleting) 0.45f else 1f)
                    .clickable(enabled = !isDeleting, role = Role.Button, onClick = onDismiss)
                    .padding(vertical = 13.dp),
                textAlign = TextAlign.Center,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = OrganicColors.neutral700
            )
        }
    }
}

@Composable
private fun PageTheme.label(): String = when (this) {
    PageTheme.CREAM -> stringResource(Res.string.reader_theme_cream)
    PageTheme.PAPER -> stringResource(Res.string.reader_theme_paper)
    PageTheme.SAGE -> stringResource(Res.string.reader_theme_sage)
    PageTheme.NIGHT -> stringResource(Res.string.reader_theme_night)
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}
