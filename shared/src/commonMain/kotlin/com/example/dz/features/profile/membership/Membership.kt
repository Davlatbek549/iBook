package com.example.dz.features.profile.membership

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkButton
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.app_components.ink.InkLabel
import com.example.dz.app_components.ink.inkCard
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.mem_b1
import dz.shared.generated.resources.mem_b2
import dz.shared.generated.resources.mem_b3
import dz.shared.generated.resources.mem_b4
import dz.shared.generated.resources.mem_monthly
import dz.shared.generated.resources.mem_per_month
import dz.shared.generated.resources.mem_premium
import dz.shared.generated.resources.mem_save_badge
import dz.shared.generated.resources.mem_start_trial
import dz.shared.generated.resources.mem_subtitle
import dz.shared.generated.resources.mem_title
import dz.shared.generated.resources.mem_trial_note
import dz.shared.generated.resources.mem_yearly
import org.jetbrains.compose.resources.stringResource

@Composable
fun MembershipScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onStartTrialClick: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 110.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(start = 22.dp, end = 22.dp, top = 4.dp)) {
                InkIconButton(icon = InkIcons.Back, onClick = onBackClick, colors = colors)
            }

            Column(modifier = Modifier.padding(start = 26.dp, end = 26.dp, top = 16.dp)) {
                // premium pill
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.accentSoft)
                        .padding(horizontal = 13.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Icon(InkIcons.Premium, null, tint = colors.accent, modifier = Modifier.size(13.dp))
                    Text(
                        text = stringResource(Res.string.mem_premium).uppercase(),
                        fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 11.sp,
                        letterSpacing = 1.sp, color = colors.accent
                    )
                }
                Text(
                    text = stringResource(Res.string.mem_title),
                    modifier = Modifier.padding(top = 16.dp),
                    fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 32.sp, lineHeight = 37.sp, color = colors.ink
                )
                Text(
                    text = stringResource(Res.string.mem_subtitle),
                    modifier = Modifier.padding(top = 12.dp),
                    fontFamily = bodyFont, fontSize = 13.5.sp, lineHeight = 22.sp, color = colors.muted
                )
            }

            // benefits
            Column(
                modifier = Modifier.padding(start = 26.dp, end = 26.dp, top = 22.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                BenefitRow(stringResource(Res.string.mem_b1), colors)
                BenefitRow(stringResource(Res.string.mem_b2), colors)
                BenefitRow(stringResource(Res.string.mem_b3), colors)
                BenefitRow(stringResource(Res.string.mem_b4), colors)
            }

            // plans
            Row(
                modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // monthly
                Column(
                    modifier = Modifier.weight(1f).inkCard(colors).padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 18.dp)
                ) {
                    InkLabel(text = stringResource(Res.string.mem_monthly), colors = colors)
                    Text("$7.99", modifier = Modifier.padding(top = 12.dp), fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 24.sp, color = colors.ink)
                    Text(stringResource(Res.string.mem_per_month), modifier = Modifier.padding(top = 6.dp), fontFamily = bodyFont, fontSize = 11.sp, color = colors.muted)
                }
                // yearly (highlighted)
                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(InkShape.radius))
                            .background(colors.surface)
                            .border(1.5.dp, colors.accent, RoundedCornerShape(InkShape.radius))
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 18.dp)
                    ) {
                        InkLabel(text = stringResource(Res.string.mem_yearly), colors = colors.copy(muted = colors.accent))
                        Text("$59.99", modifier = Modifier.padding(top = 12.dp), fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 24.sp, color = colors.ink)
                        Text("$5.00 / month", modifier = Modifier.padding(top = 6.dp), fontFamily = bodyFont, fontSize = 11.sp, color = colors.muted)
                    }
                    // save badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-12).dp, y = (-11).dp)
                            .clip(CircleShape)
                            .background(colors.accent)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.mem_save_badge),
                            fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, letterSpacing = 0.4.sp, color = colors.onAccent
                        )
                    }
                }
            }

            Text(
                text = stringResource(Res.string.mem_trial_note),
                modifier = Modifier.fillMaxWidth().padding(start = 26.dp, end = 26.dp, top = 16.dp),
                fontFamily = bodyFont, fontSize = 11.5.sp, lineHeight = 18.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = colors.muted
            )
        }

        // pinned CTA
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(colors.paper)
        ) {
            HorizontalDivider(thickness = 1.dp, color = colors.line)
            Box(modifier = Modifier.navigationBarsPadding().padding(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 18.dp)) {
                InkButton(text = stringResource(Res.string.mem_start_trial), onClick = onStartTrialClick, colors = colors)
            }
        }
    }
}

@Composable
private fun BenefitRow(text: String, colors: InkColors) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier.size(24.dp).clip(CircleShape).background(colors.accentSoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(InkIcons.Done, null, tint = colors.accent, modifier = Modifier.size(9.dp))
        }
        Text(text, fontFamily = inkBodyFontFamily(), fontSize = 13.5.sp, lineHeight = 19.sp, color = colors.inkSoft)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun MembershipScreenPreview() {
    MembershipScreen()
}
