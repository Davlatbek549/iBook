package com.example.ibook.screens.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.IBookTheme

private data class SettingsMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val titleBottomSpacing: Dp,
    val sectionHeight: Dp,
    val sectionTextSize: TextUnit,
    val rowHeight: Dp,
    val rowIconSize: Dp,
    val rowIconColumnWidth: Dp,
    val rowTextSize: TextUnit,
    val rowTextLineHeight: TextUnit,
    val dividerStartPadding: Dp,
    val switchWidth: Dp,
    val switchHeight: Dp,
    val bottomSpacing: Dp
)

private data class SettingsSectionUiState(
    val title: String,
    val items: List<SettingsItemUiState>
)

private data class SettingsItemUiState(
    val title: String,
    @param:DrawableRes val iconRes: Int,
    val type: SettingsItemType,
    val onClick: () -> Unit
)

private enum class SettingsItemType {
    Action,
    NotificationSwitch
}

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    notificationsEnabled: Boolean = false,
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
        modifier = modifier,
        notificationsEnabled = notificationsEnabled,
        onNotificationsEnabledChange = onNotificationsEnabledChange,
        onBackClick = onBackClick,
        onAppearanceClick = onAppearanceClick,
        onTextSizeClick = onTextSizeClick,
        onPageBackgroundClick = onPageBackgroundClick,
        onTextFontClick = onTextFontClick,
        onTermsClick = onTermsClick,
        onPrivacyPolicyClick = onPrivacyPolicyClick,
        onPurchasedClick = onPurchasedClick
    )
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    notificationsEnabled: Boolean = false,
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
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberSettingsMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val sections = rememberSettingsSections(
            onNotificationsClick = { onNotificationsEnabledChange(!notificationsEnabled) },
            onAppearanceClick = onAppearanceClick,
            onTextSizeClick = onTextSizeClick,
            onPageBackgroundClick = onPageBackgroundClick,
            onTextFontClick = onTextFontClick,
            onTermsClick = onTermsClick,
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onPurchasedClick = onPurchasedClick
        )
        val colorScheme = MaterialTheme.colorScheme

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            SettingsBackButton(
                metrics = metrics,
                onClick = onBackClick,
                modifier = Modifier.padding(horizontal = metrics.horizontalPadding)
            )

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = stringResource(R.string.settings_title),
                color = colorScheme.onSurface.copy(alpha = 0.76f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = metrics.horizontalPadding),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = metrics.titleSize,
                    lineHeight = metrics.titleSize * 1.08f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            Spacer(modifier = Modifier.height(metrics.titleBottomSpacing))

            sections.forEach { section ->
                SettingsSectionHeader(
                    title = section.title,
                    metrics = metrics
                )

                section.items.forEachIndexed { index, item ->
                    SettingsRow(
                        item = item,
                        metrics = metrics,
                        notificationsEnabled = notificationsEnabled,
                        onNotificationsEnabledChange = onNotificationsEnabledChange,
                        showDivider = index != section.items.lastIndex
                    )
                }
            }

            Spacer(modifier = Modifier.height(metrics.bottomSpacing))
        }
    }
}

@Composable
private fun SettingsBackButton(
    metrics: SettingsMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(metrics.backButtonSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.16f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = stringResource(R.string.cd_back),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(metrics.backIconSize)
        )
    }
}

@Composable
private fun SettingsSectionHeader(
    title: String,
    metrics: SettingsMetrics
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.sectionHeight)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
            .padding(horizontal = metrics.horizontalPadding),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.sectionTextSize,
                lineHeight = metrics.sectionTextSize * 1.15f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun SettingsRow(
    item: SettingsItemUiState,
    metrics: SettingsMetrics,
    notificationsEnabled: Boolean,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    showDivider: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = metrics.horizontalPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(metrics.rowHeight)
                .clickable(onClick = item.onClick),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(metrics.rowIconColumnWidth),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    painter = painterResource(item.iconRes),
                    contentDescription = item.title,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(metrics.rowIconSize)
                )
            }

            Text(
                text = item.title,
                color = colorScheme.onSurface.copy(alpha = 0.72f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.rowTextSize,
                    lineHeight = metrics.rowTextLineHeight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            if (item.type == SettingsItemType.NotificationSwitch) {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = onNotificationsEnabledChange,
                    modifier = Modifier
                        .width(metrics.switchWidth)
                        .height(metrics.switchHeight),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorScheme.onPrimary,
                        checkedTrackColor = colorScheme.primary,
                        uncheckedThumbColor = colorScheme.surface,
                        uncheckedTrackColor = colorScheme.outline.copy(alpha = 0.75f),
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = metrics.dividerStartPadding)
                    .height(1.dp)
                    .background(colorScheme.outline.copy(alpha = 0.24f))
            )
        }
    }
}

@Composable
private fun rememberSettingsSections(
    onNotificationsClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    onTextSizeClick: () -> Unit,
    onPageBackgroundClick: () -> Unit,
    onTextFontClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onPurchasedClick: () -> Unit
): List<SettingsSectionUiState> {
    return listOf(
        SettingsSectionUiState(
            title = stringResource(R.string.settings_section_options),
            items = listOf(
                SettingsItemUiState(
                    title = stringResource(R.string.settings_notifications),
                    iconRes = R.drawable.ic_bell,
                    type = SettingsItemType.NotificationSwitch,
                    onClick = onNotificationsClick
                ),
                SettingsItemUiState(
                    title = stringResource(R.string.settings_appearance),
                    iconRes = R.drawable.ic_appearance,
                    type = SettingsItemType.Action,
                    onClick = onAppearanceClick
                )
            )
        ),
        SettingsSectionUiState(
            title = stringResource(R.string.settings_section_book),
            items = listOf(
                SettingsItemUiState(
                    title = stringResource(R.string.settings_text_size),
                    iconRes = R.drawable.ic_text_size,
                    type = SettingsItemType.Action,
                    onClick = onTextSizeClick
                ),
                SettingsItemUiState(
                    title = stringResource(R.string.settings_page_background),
                    iconRes = R.drawable.ic_background,
                    type = SettingsItemType.Action,
                    onClick = onPageBackgroundClick
                ),
                SettingsItemUiState(
                    title = stringResource(R.string.settings_text_font),
                    iconRes = R.drawable.ic_text_font,
                    type = SettingsItemType.Action,
                    onClick = onTextFontClick
                )
            )
        ),
        SettingsSectionUiState(
            title = stringResource(R.string.settings_section_about),
            items = listOf(
                SettingsItemUiState(
                    title = stringResource(R.string.settings_terms_of_use),
                    iconRes = R.drawable.ic_terms,
                    type = SettingsItemType.Action,
                    onClick = onTermsClick
                ),
                SettingsItemUiState(
                    title = stringResource(R.string.settings_privacy_policy),
                    iconRes = R.drawable.ic_policy,
                    type = SettingsItemType.Action,
                    onClick = onPrivacyPolicyClick
                ),
                SettingsItemUiState(
                    title = stringResource(R.string.settings_purchased),
                    iconRes = R.drawable.ic_purchased,
                    type = SettingsItemType.Action,
                    onClick = onPurchasedClick
                )
            )
        )
    )
}

@Composable
private fun rememberSettingsMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): SettingsMetrics {
    return remember(maxWidth, maxHeight) {
        val horizontalPadding = (maxWidth * 0.075f).coerceIn(28.dp, 36.dp)
        val rowTextSize = (maxWidth.value * 0.043f).coerceIn(18f, 21f).sp
        val sectionTextSize = (maxWidth.value * 0.039f).coerceIn(16f, 19f).sp
        val titleSize = (maxWidth.value * 0.084f).coerceIn(34f, 40f).sp
        val backButtonSize = (maxWidth * 0.108f).coerceIn(46.dp, 54.dp)

        SettingsMetrics(
            horizontalPadding = horizontalPadding,
            topSpacing = (maxHeight * 0.052f).coerceIn(36.dp, 52.dp),
            backButtonSize = backButtonSize,
            backIconSize = backButtonSize * 0.36f,
            titleTopSpacing = (maxHeight * 0.032f).coerceIn(24.dp, 34.dp),
            titleSize = titleSize,
            titleBottomSpacing = (maxHeight * 0.04f).coerceIn(32.dp, 48.dp),
            sectionHeight = (maxHeight * 0.053f).coerceIn(50.dp, 58.dp),
            sectionTextSize = sectionTextSize,
            rowHeight = (maxHeight * 0.077f).coerceIn(70.dp, 84.dp),
            rowIconSize = (maxWidth * 0.052f).coerceIn(22.dp, 28.dp),
            rowIconColumnWidth = (maxWidth * 0.13f).coerceIn(56.dp, 68.dp),
            rowTextSize = rowTextSize,
            rowTextLineHeight = rowTextSize * 1.2f,
            dividerStartPadding = (maxWidth * 0.14f).coerceIn(62.dp, 76.dp),
            switchWidth = (maxWidth * 0.108f).coerceIn(46.dp, 54.dp),
            switchHeight = (maxHeight * 0.032f).coerceIn(28.dp, 34.dp),
            bottomSpacing = (maxHeight * 0.026f).coerceIn(20.dp, 34.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    IBookTheme {
        SettingsScreen()
    }
}
