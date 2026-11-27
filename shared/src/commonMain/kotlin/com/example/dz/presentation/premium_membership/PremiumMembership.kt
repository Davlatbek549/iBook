package com.example.dz.presentation.premium_membership

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.ORGANIC_GUTTER
import com.example.dz.designsystem.components.organic.ORGANIC_TAB_BAR_CLEARANCE
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicCircleIconButton
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.components.organic.OrganicScreen
import com.example.dz.designsystem.components.organic.OrganicScreenHeader
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.nav_back
import dz.shared.generated.resources.pmem_active_badge
import dz.shared.generated.resources.pmem_billing_history
import dz.shared.generated.resources.pmem_cancel
import dz.shared.generated.resources.pmem_change
import dz.shared.generated.resources.pmem_no_plan
import dz.shared.generated.resources.pmem_switch_plan
import dz.shared.generated.resources.pmem_title
import dz.shared.generated.resources.pmem_what_you_get
import dz.shared.generated.resources.pmem_your_plan
import org.jetbrains.compose.resources.stringResource

/**
 * Membership — the active plan, what it includes, and the one place to change either the plan or
 * the card that bills it. Geometry from `dz-all-screens.html`.
 */
@Composable
fun PremiumMembershipScreen(
    uiState: PremiumMembershipUiState = PremiumMembershipUiState(),
    onEvent: (PremiumMembershipEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OrganicScreen(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = ORGANIC_TAB_BAR_CLEARANCE),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item(key = "header") {
                OrganicScreenHeader(
                    title = stringResource(Res.string.pmem_title),
                    modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                    leading = {
                        OrganicCircleIconButton(
                            icon = OrganicIcons.ChevronLeft,
                            onClick = { onEvent(PremiumMembershipEvent.BackClicked) },
                            contentDescription = stringResource(Res.string.nav_back),
                            size = 38.dp,
                            iconSize = 18.dp,
                        )
                    },
                )
            }

            item(key = "plan-card") {
                PlanCard(uiState = uiState, onChangeCard = { onEvent(PremiumMembershipEvent.ChangeCardClicked) })
            }

            if (uiState.benefits.isNotEmpty()) {
                item(key = "benefits") {
                    Column(
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OrganicKicker(text = stringResource(Res.string.pmem_what_you_get), color = OrganicColors.neutral600)
                        uiState.benefits.forEach { BenefitRow(it) }
                    }
                }
            }

            if (uiState.alternatePlanTitle != null) {
                item(key = "switch-plan") {
                    Column(
                        modifier = Modifier.padding(horizontal = ORGANIC_GUTTER),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OrganicKicker(text = stringResource(Res.string.pmem_switch_plan), color = OrganicColors.neutral600)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(OrganicShape.radiusLg))
                                .border(2.dp, OrganicColors.accent2_500, RoundedCornerShape(OrganicShape.radiusLg))
                                .background(OrganicColors.accent2_100)
                                .clickable(role = Role.Button) { onEvent(PremiumMembershipEvent.SwitchPlanClicked) }
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(
                                    text = uiState.alternatePlanTitle,
                                    fontFamily = organicBodyFontFamily(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = OrganicColors.accent2_900
                                )
                                uiState.alternatePlanPrice?.let {
                                    Text(
                                        text = it,
                                        fontFamily = organicBodyFontFamily(),
                                        fontSize = 12.sp,
                                        color = OrganicColors.accent2_900.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item(key = "billing-history") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ORGANIC_GUTTER)
                        .clickable(role = Role.Button) { onEvent(PremiumMembershipEvent.BillingHistoryClicked) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.pmem_billing_history),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 14.sp,
                        color = OrganicColors.neutral800
                    )
                    Icon(
                        imageVector = OrganicIcons.ChevronRight,
                        contentDescription = null,
                        tint = OrganicColors.neutral600,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            item(key = "cancel") {
                Text(
                    text = stringResource(Res.string.pmem_cancel),
                    modifier = Modifier
                        .padding(horizontal = ORGANIC_GUTTER)
                        .clickable(role = Role.Button) { onEvent(PremiumMembershipEvent.CancelClicked) },
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 13.sp,
                    color = OrganicColors.neutral700,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

/** The terracotta hero: plan, price, and whichever card the subscription actually bills. */
@Composable
private fun PlanCard(uiState: PremiumMembershipUiState, onChangeCard: () -> Unit) {
    OrganicCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = ORGANIC_GUTTER),
        background = OrganicColors.accent,
        contentPadding = PaddingValues(22.dp),
        decoration = {
            drawCircle(
                color = OrganicColors.accent600.copy(alpha = 0.5f),
                radius = 65.dp.toPx(),
                center = Offset(size.width - 31.dp.toPx(), 31.dp.toPx())
            )
        },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(Res.string.pmem_your_plan),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.3.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                    Text(
                        text = uiState.planTitle.ifBlank { stringResource(Res.string.pmem_no_plan) },
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 28.sp,
                        lineHeight = 28.sp,
                        color = Color.White
                    )
                }
                if (uiState.hasActiveMembership) {
                    Text(
                        text = stringResource(Res.string.pmem_active_badge),
                        modifier = Modifier
                            .clip(RoundedCornerShape(OrganicShape.pill))
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                        color = OrganicColors.accent900
                    )
                }
            }
            if (uiState.planPrice.isNotBlank()) {
                Text(
                    text = uiState.planPrice,
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 26.sp,
                    color = Color.White
                )
            }
            if (uiState.paymentMethodLabel != null) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.28f)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiState.paymentMethodLabel,
                        modifier = Modifier.weight(1f),
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(Res.string.pmem_change),
                        modifier = Modifier.clickable(role = Role.Button, onClick = onChangeCard),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@Composable
private fun BenefitRow(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(28.dp).clip(CircleShape).background(OrganicColors.accent2_200),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = OrganicIcons.Check,
                contentDescription = null,
                tint = OrganicColors.accent2_900,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = text,
            fontFamily = organicBodyFontFamily(),
            fontSize = 14.sp,
            color = OrganicColors.text
        )
    }
}

@Preview
@Composable
private fun PremiumMembershipScreenPreview() {
    PremiumMembershipScreen()
}
