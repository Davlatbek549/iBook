package com.example.dz.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkLabel
import com.example.dz.app_components.ink.InkToggle
import com.example.dz.app_components.ink.InkTopBar
import com.example.dz.app_components.ink.inkCard
import com.example.dz.theme.InkColors
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.set_about
import dz.shared.generated.resources.set_account
import dz.shared.generated.resources.set_appearance
import dz.shared.generated.resources.set_daily_goal
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
fun Settings(
    modifier: Modifier = Modifier,
    notificationsEnabled: Boolean = true,
    onNotificationsEnabledChange: (Boolean) -> Unit = {},
    onBackClick: () -> Unit = {},
    onAppearanceClick: () -> Unit = {},
    onTextSizeClick: () -> Unit = {},
    onPageBackgroundClick: () -> Unit = {},
    onTextFontClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onPurchasedClick: () -> Unit = {}
) {
    SettingsScreen(
        modifier, notificationsEnabled, onNotificationsEnabledChange, onBackClick,
        onAppearanceClick, onTextSizeClick, onPageBackgroundClick, onTextFontClick,
        onTermsClick, onPrivacyPolicyClick, onPurchasedClick
    )
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    notificationsEnabled: Boolean = true,
    onNotificationsEnabledChange: (Boolean) -> Unit = {},
    onBackClick: () -> Unit = {},
    onAppearanceClick: () -> Unit = {},
    onTextSizeClick: () -> Unit = {},
    onPageBackgroundClick: () -> Unit = {},
    onTextFontClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onPurchasedClick: () -> Unit = {}
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
        InkTopBar(title = stringResource(Res.string.set_title), onBackClick = onBackClick, colors = colors)

        // Account
        SettingsGroup(stringResource(Res.string.set_account), colors) {
            ChevronRow(InkIcons.User, stringResource(Res.string.set_edit_profile), onPurchasedClick, colors)
            RowDivider(colors)
            ValueRow(InkIcons.Email, stringResource(Res.string.set_email), "amelia@hartwell.co", {}, colors)
            RowDivider(colors)
            ChevronRow(InkIcons.Lock, stringResource(Res.string.set_password), {}, colors)
        }

        // Reading
        SettingsGroup(stringResource(Res.string.set_reading), colors) {
            ValueRow(InkIcons.Appearance, stringResource(Res.string.set_appearance), "Light", onAppearanceClick, colors)
            RowDivider(colors)
            ValueRow(InkIcons.Book, stringResource(Res.string.set_text_size), "Medium", onTextSizeClick, colors)
            RowDivider(colors)
            ValueRow(InkIcons.Stats, stringResource(Res.string.set_daily_goal), "30 min", {}, colors)
        }

        // Notifications
        SettingsGroup(stringResource(Res.string.set_notifications), colors) {
            ToggleRow(InkIcons.Bell, stringResource(Res.string.set_reading_reminders), notificationsEnabled, onNotificationsEnabledChange, colors)
            RowDivider(colors)
            ToggleRow(InkIcons.Chat, stringResource(Res.string.set_messages), true, {}, colors)
            RowDivider(colors)
            ToggleRow(InkIcons.Tag, stringResource(Res.string.set_price_drops), false, {}, colors)
        }

        // About
        SettingsGroup(stringResource(Res.string.set_about), colors) {
            ChevronRow(InkIcons.HelpCentre, stringResource(Res.string.set_help), {}, colors)
            RowDivider(colors)
            ChevronRow(InkIcons.Terms, stringResource(Res.string.set_terms), onTermsClick, colors)
            RowDivider(colors)
            ChevronRow(InkIcons.Policy, stringResource(Res.string.set_privacy), onPrivacyPolicyClick, colors)
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(Res.string.set_sign_out),
                modifier = Modifier.clickable(onClick = onBackClick).padding(6.dp),
                fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = colors.danger
            )
            Text(
                text = stringResource(Res.string.set_version),
                fontFamily = bodyFont, fontSize = 10.5.sp, color = colors.muted
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
    trailing: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Icon(icon, null, tint = colors.inkSoft, modifier = Modifier.size(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontFamily = inkBodyFontFamily(), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = colors.ink)
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
