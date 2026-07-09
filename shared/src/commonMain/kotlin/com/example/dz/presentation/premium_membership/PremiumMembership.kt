package com.example.dz.presentation.premium_membership

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.pmem_b1
import dz.shared.generated.resources.pmem_b2
import dz.shared.generated.resources.pmem_b3
import dz.shared.generated.resources.pmem_benefits
import dz.shared.generated.resources.pmem_billing_date
import dz.shared.generated.resources.pmem_billing_history
import dz.shared.generated.resources.pmem_cancel
import dz.shared.generated.resources.pmem_member_since
import dz.shared.generated.resources.pmem_read_year
import dz.shared.generated.resources.pmem_renews
import dz.shared.generated.resources.pmem_saved
import dz.shared.generated.resources.pmem_status
import dz.shared.generated.resources.pmem_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun PremiumMembershipScreen(
    uiState: PremiumMembershipUiState = PremiumMembershipUiState(),
    onEvent: (PremiumMembershipEvent) -> Unit = {},
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
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        InkTopBar(title = stringResource(Res.string.pmem_title), onBackClick = { onEvent(PremiumMembershipEvent.BackClicked) }, colors = colors)

        // status card — solid accent
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 8.dp)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(InkShape.radius + 4.dp))
                .clip(RoundedCornerShape(InkShape.radius + 4.dp))
                .background(colors.accent)
                .padding(20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(InkIcons.Premium, null, tint = colors.onAccent.copy(alpha = 0.85f), modifier = Modifier.size(14.dp))
                    Text(
                        text = stringResource(Res.string.pmem_status).uppercase(),
                        fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, letterSpacing = 1.sp,
                        color = colors.onAccent.copy(alpha = 0.85f)
                    )
                }
                Icon(InkIcons.BookOpen, null, tint = colors.onAccent.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
            }
            Text(uiState.memberName, modifier = Modifier.padding(top = 16.dp), fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 26.sp, color = colors.onAccent)
            Text(stringResource(Res.string.pmem_member_since), modifier = Modifier.padding(top = 6.dp), fontFamily = bodyFont, fontSize = 12.5.sp, color = colors.onAccent.copy(alpha = 0.85f))
            HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), thickness = 1.dp, color = colors.onAccent.copy(alpha = 0.25f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(Res.string.pmem_renews), fontFamily = bodyFont, fontSize = 12.sp, color = colors.onAccent.copy(alpha = 0.85f))
                Text(uiState.renewalPrice, fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = colors.onAccent)
            }
        }

        // usage cards
        Row(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UsageCard(stringResource(Res.string.pmem_read_year), uiState.booksReadThisYear, Modifier.weight(1f), colors)
            UsageCard(stringResource(Res.string.pmem_saved), uiState.amountSaved, Modifier.weight(1f), colors)
        }

        // benefits
        Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp)) {
            InkLabel(text = stringResource(Res.string.pmem_benefits), colors = colors)
            Column(modifier = Modifier.padding(top = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                BenefitRow(stringResource(Res.string.pmem_b1), colors)
                BenefitRow(stringResource(Res.string.pmem_b2), colors)
                BenefitRow(stringResource(Res.string.pmem_b3), colors)
            }
        }

        // menu
        Column(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp).fillMaxWidth().inkCard(colors)
        ) {
            MenuRow(InkIcons.Calendar, stringResource(Res.string.pmem_billing_date), { onEvent(PremiumMembershipEvent.BillingDateClicked) }, colors, showChevron = true)
            HorizontalDivider(thickness = 1.dp, color = colors.line)
            MenuRow(InkIcons.Purchased, stringResource(Res.string.pmem_billing_history), { onEvent(PremiumMembershipEvent.BillingHistoryClicked) }, colors, showChevron = true)
            HorizontalDivider(thickness = 1.dp, color = colors.line)
            MenuRow(InkIcons.Close, stringResource(Res.string.pmem_cancel), { onEvent(PremiumMembershipEvent.CancelClicked) }, colors, showChevron = false, danger = true)
        }
    }
}

@Composable
private fun UsageCard(label: String, value: String, modifier: Modifier, colors: InkColors) {
    Column(modifier = modifier.inkCard(colors).padding(horizontal = 16.dp, vertical = 14.dp)) {
        InkLabel(text = label, colors = colors)
        Text(value, modifier = Modifier.padding(top = 10.dp), fontFamily = inkDisplayFontFamily(), fontWeight = FontWeight.Medium, fontSize = 20.sp, color = colors.ink)
    }
}

@Composable
private fun BenefitRow(text: String, colors: InkColors) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(colors.accentSoft), contentAlignment = Alignment.Center) {
            Icon(InkIcons.Done, null, tint = colors.accent, modifier = Modifier.size(9.dp))
        }
        Text(text, fontFamily = inkBodyFontFamily(), fontSize = 13.5.sp, color = colors.inkSoft)
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    colors: InkColors,
    showChevron: Boolean,
    danger: Boolean = false,
) {
    val tint = if (danger) colors.danger else colors.inkSoft
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(16.dp))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontFamily = inkBodyFontFamily(), fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
            color = if (danger) colors.danger else colors.ink
        )
        if (showChevron) {
            Icon(InkIcons.Back, null, tint = colors.muted, modifier = Modifier.size(13.dp).graphicsLayer { rotationZ = 180f })
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun PremiumMembershipScreenPreview() {
    PremiumMembershipScreen()
}
