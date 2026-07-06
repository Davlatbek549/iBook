package com.example.dz.presentation.payment.payment_failed

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkButton
import com.example.dz.designsystem.components.ink.InkGhostButton
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.InkSecondaryButton
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.pf_body
import dz.shared.generated.resources.pf_change_method
import dz.shared.generated.resources.pf_contact
import dz.shared.generated.resources.pf_error
import dz.shared.generated.resources.pf_title
import dz.shared.generated.resources.pf_try_again
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchaseFailedScreen(
    uiState: PaymentFailedUiState = PaymentFailedUiState(),
    onEvent: (PaymentFailedEvent) -> Unit = {},
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
            .navigationBarsPadding()
            .padding(start = 26.dp, end = 26.dp, bottom = 26.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.End) {
            InkIconButton(icon = InkIcons.Close, onClick = { onEvent(PaymentFailedEvent.CloseClicked) }, colors = colors)
        }

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // X medallion
            Box(
                modifier = Modifier.size(76.dp).clip(CircleShape).background(colors.danger.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(54.dp).clip(CircleShape).border(2.dp, colors.danger, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(InkIcons.Close, null, tint = colors.danger, modifier = Modifier.size(18.dp))
                }
            }

            Text(
                text = stringResource(Res.string.pf_title),
                modifier = Modifier.padding(top = 26.dp).widthIn(max = 280.dp),
                fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 28.sp, lineHeight = 32.sp,
                textAlign = TextAlign.Center, color = colors.ink
            )
            Text(
                text = stringResource(Res.string.pf_body),
                modifier = Modifier.padding(top = 12.dp).widthIn(max = 270.dp),
                fontFamily = bodyFont, fontSize = 13.5.sp, lineHeight = 22.sp, textAlign = TextAlign.Center, color = colors.muted
            )
            Box(
                modifier = Modifier
                    .padding(top = 22.dp)
                    .widthIn(max = 270.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm))
                    .background(colors.alt)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.pf_error),
                    fontFamily = bodyFont, fontWeight = FontWeight.Medium, fontSize = 11.5.sp, lineHeight = 17.sp,
                    textAlign = TextAlign.Center, color = colors.inkSoft
                )
            }
        }

        InkButton(text = stringResource(Res.string.pf_try_again), onClick = { onEvent(PaymentFailedEvent.TryAgainClicked) }, colors = colors)
        Spacer(modifier = Modifier.height(10.dp))
        InkSecondaryButton(text = stringResource(Res.string.pf_change_method), onClick = { onEvent(PaymentFailedEvent.ChangeMethodClicked) }, colors = colors)
        Spacer(modifier = Modifier.height(6.dp))
        InkGhostButton(text = stringResource(Res.string.pf_contact), onClick = { onEvent(PaymentFailedEvent.ContactSupportClicked) }, height = 40.dp, colors = colors)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun PurchaseFailedScreenPreview() {
    PurchaseFailedScreen()
}
