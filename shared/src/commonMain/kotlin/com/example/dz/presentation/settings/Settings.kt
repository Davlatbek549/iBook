package com.example.dz.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkButton
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkSecondaryButton
import com.example.dz.designsystem.components.ink.InkToggle
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.set_about
import dz.shared.generated.resources.set_account
import dz.shared.generated.resources.set_appearance
import dz.shared.generated.resources.set_daily_goal
import dz.shared.generated.resources.set_delete_account
import dz.shared.generated.resources.set_delete_body
import dz.shared.generated.resources.set_delete_cancel
import dz.shared.generated.resources.set_delete_title
import dz.shared.generated.resources.set_edit_profile
import dz.shared.generated.resources.set_email
import dz.shared.generated.resources.set_help
import dz.shared.generated.resources.set_messages
import dz.shared.generated.resources.set_notifications
import dz.shared.generated.resources.set_password
import dz.shared.generated.resources.set_price_drops
import dz.shared.generated.resources.set_privacy
import dz.shared.generated.resources.set_reading
import dz.shared.generated.resources.set_reading_reminders
import dz.shared.generated.resources.set_sign_out
import dz.shared.generated.resources.set_terms
import dz.shared.generated.resources.set_text_size
import dz.shared.generated.resources.set_title
import dz.shared.generated.resources.set_version
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    uiState: SettingsUiState = SettingsUiState(),
    onEvent: (SettingsEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = inkColors()
    val bodyFont = inkBodyFontFamily()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 26.dp)
    ) {
        InkTopBar(title = stringResource(Res.string.set_title), onBackClick = { onEvent(SettingsEvent.BackClicked) }, colors = colors)

        // Account
        SettingsGroup(stringResource(Res.string.set_account), colors) {
            ChevronRow(InkIcons.User, stringResource(Res.string.set_edit_profile), { onEvent(SettingsEvent.EditProfileClicked) }, colors)
            RowDivider(colors)
            ValueRow(InkIcons.Email, stringResource(Res.string.set_email), uiState.email, { onEvent(SettingsEvent.EmailClicked) }, colors)
            RowDivider(colors)
            ChevronRow(InkIcons.Lock, stringResource(Res.string.set_password), { onEvent(SettingsEvent.PasswordClicked) }, colors)
            RowDivider(colors)
            BaseRow(InkIcons.Delete, stringResource(Res.string.set_delete_account), { onEvent(SettingsEvent.DeleteAccountClicked) }, colors, tint = colors.danger) {}
        }

        // Reading
        SettingsGroup(stringResource(Res.string.set_reading), colors) {
            ValueRow(InkIcons.Appearance, stringResource(Res.string.set_appearance), uiState.appearance, { onEvent(SettingsEvent.AppearanceClicked) }, colors)
            RowDivider(colors)
            ValueRow(InkIcons.Book, stringResource(Res.string.set_text_size), uiState.textSize, { onEvent(SettingsEvent.TextSizeClicked) }, colors)
            RowDivider(colors)
            ValueRow(InkIcons.Stats, stringResource(Res.string.set_daily_goal), uiState.dailyGoal, { onEvent(SettingsEvent.DailyGoalClicked) }, colors)
        }

        // Notifications
        SettingsGroup(stringResource(Res.string.set_notifications), colors) {
            ToggleRow(InkIcons.Bell, stringResource(Res.string.set_reading_reminders), uiState.readingRemindersEnabled, { onEvent(SettingsEvent.ReadingRemindersToggled(it)) }, colors)
            RowDivider(colors)
            ToggleRow(InkIcons.Chat, stringResource(Res.string.set_messages), uiState.messagesEnabled, { onEvent(SettingsEvent.MessagesToggled(it)) }, colors)
            RowDivider(colors)
            ToggleRow(InkIcons.Tag, stringResource(Res.string.set_price_drops), uiState.priceDropsEnabled, { onEvent(SettingsEvent.PriceDropsToggled(it)) }, colors)
        }

        // About
        SettingsGroup(stringResource(Res.string.set_about), colors) {
            ChevronRow(InkIcons.HelpCentre, stringResource(Res.string.set_help), { onEvent(SettingsEvent.HelpClicked) }, colors)
            RowDivider(colors)
            ChevronRow(InkIcons.Terms, stringResource(Res.string.set_terms), { onEvent(SettingsEvent.TermsClicked) }, colors)
            RowDivider(colors)
            ChevronRow(InkIcons.Policy, stringResource(Res.string.set_privacy), { onEvent(SettingsEvent.PrivacyClicked) }, colors)
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(Res.string.set_sign_out),
                modifier = Modifier.clickable { onEvent(SettingsEvent.SignOutClicked) }.padding(6.dp),
                fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = colors.danger
            )
            Text(
                text = stringResource(Res.string.set_version),
                fontFamily = bodyFont, fontSize = 10.5.sp, color = colors.muted
            )
        }
    }

    if (uiState.isDeleteConfirmationVisible) {
        DeleteAccountDialog(
            isDeleting = uiState.isDeletingAccount,
            error = uiState.deleteAccountError,
            onConfirm = { onEvent(SettingsEvent.DeleteAccountConfirmed) },
            onDismiss = { onEvent(SettingsEvent.DeleteAccountDismissed) },
            colors = colors
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
    colors: InkColors,
) {
    val bodyFont = inkBodyFontFamily()
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = !isDeleting, dismissOnClickOutside = !isDeleting)
    ) {
        Column(modifier = Modifier.fillMaxWidth().inkCard(colors).padding(22.dp)) {
            Text(
                text = stringResource(Res.string.set_delete_title),
                fontFamily = inkDisplayFontFamily(), fontWeight = FontWeight.Medium, fontSize = 21.sp, color = colors.ink
            )
            Text(
                text = stringResource(Res.string.set_delete_body),
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = bodyFont, fontSize = 13.sp, lineHeight = 19.sp, color = colors.inkSoft
            )
            if (error != null) {
                Text(
                    text = error,
                    modifier = Modifier.padding(top = 12.dp),
                    fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 17.sp, color = colors.danger
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            // The primary button on a danger ground, so the one irreversible action here looks it.
            InkButton(
                text = stringResource(Res.string.set_delete_account),
                onClick = onConfirm,
                isBusy = isDeleting,
                colors = colors.copy(accent = colors.danger)
            )
            InkSecondaryButton(
                text = stringResource(Res.string.set_delete_cancel),
                onClick = onDismiss,
                modifier = Modifier.padding(top = 10.dp).alpha(if (isDeleting) 0.45f else 1f),
                height = 48.dp,
                colors = colors
            )
        }
    }
}

@Composable
private fun SettingsGroup(label: String, colors: InkColors, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 20.dp)) {
        InkLabel(text = label, colors = colors)
        Column(modifier = Modifier.padding(top = 11.dp).fillMaxWidth().inkCard(colors)) {
            content()
        }
    }
}

@Composable
private fun RowDivider(colors: InkColors) {
    HorizontalDivider(thickness = 1.dp, color = colors.line)
}

@Composable
private fun BaseRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    colors: InkColors,
    /** Overrides the icon and title colour, for a row whose action is destructive. */
    tint: Color? = null,
    trailing: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Icon(icon, null, tint = tint ?: colors.inkSoft, modifier = Modifier.size(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontFamily = inkBodyFontFamily(), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = tint ?: colors.ink)
        trailing()
    }
}

@Composable
private fun ChevronRow(icon: ImageVector, title: String, onClick: () -> Unit, colors: InkColors) {
    BaseRow(icon, title, onClick, colors) {
        Icon(InkIcons.Back, null, tint = colors.muted, modifier = Modifier.size(13.dp).graphicsLayer { rotationZ = 180f })
    }
}

@Composable
private fun ValueRow(icon: ImageVector, title: String, value: String, onClick: () -> Unit, colors: InkColors) {
    BaseRow(icon, title, onClick, colors) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(value, fontFamily = inkBodyFontFamily(), fontSize = 12.sp, color = colors.muted)
            Icon(InkIcons.Back, null, tint = colors.muted, modifier = Modifier.size(13.dp).graphicsLayer { rotationZ = 180f })
        }
    }
}

@Composable
private fun ToggleRow(icon: ImageVector, title: String, checked: Boolean, onChange: (Boolean) -> Unit, colors: InkColors) {
    BaseRow(icon, title, { onChange(!checked) }, colors) {
        InkToggle(checked = checked, onCheckedChange = onChange, colors = colors)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}
