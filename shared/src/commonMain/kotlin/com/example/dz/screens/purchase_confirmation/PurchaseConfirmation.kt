package com.example.dz.screens.purchase_confirmation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.ink.InkButton
import com.example.dz.app_components.ink.InkSecondaryButton
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.po_confirm_body
import dz.shared.generated.resources.po_confirm_q
import dz.shared.generated.resources.po_not_yet
import dz.shared.generated.resources.po_yes_buy
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class PurchaseConfirmationUiState(
    val title: String = "Mexican Gothic",
    val author: String = "Silvia Moreno-Garcia",
    val total: String = "$11.43"
)

@Composable
fun PurchaseConfirmationScreen(
    modifier: Modifier = Modifier,
    uiState: PurchaseConfirmationUiState = PurchaseConfirmationUiState(),
    onConfirm: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x59211C16))
            .clickable(onClick = onBackClick),
        contentAlignment = Alignment.BottomCenter
    ) {
        // sheet — disabled clickable swallows taps so they don't dismiss
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(colors.paper)
                .clickable(enabled = false) {}
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // grabber
            Box(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.line)
            )

            Text(
                text = stringResource(Res.string.po_confirm_q),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 23.sp,
                textAlign = TextAlign.Center,
                color = colors.ink
            )
            Text(
                text = stringResource(Res.string.po_confirm_body),
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = bodyFont,
                fontSize = 13.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                color = colors.muted
            )

            // book row
            Row(
                modifier = Modifier
                    .padding(top = 18.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(InkShape.radius))
                    .background(colors.surface)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.book_cover),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 42.dp, height = 62.dp)
                        .clip(RoundedCornerShape(InkShape.cover - 1.dp))
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(uiState.title, fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 14.5.sp, color = colors.ink)
                    Text(uiState.author, modifier = Modifier.padding(top = 4.dp), fontFamily = bodyFont, fontSize = 11.5.sp, color = colors.muted)
                }
                Text(uiState.total, fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 17.sp, color = colors.ink)
            }

            Spacer(modifier = Modifier.height(18.dp))
            InkButton(text = stringResource(Res.string.po_yes_buy), onClick = onConfirm, colors = colors)
            Spacer(modifier = Modifier.height(10.dp))
            InkSecondaryButton(text = stringResource(Res.string.po_not_yet), onClick = onBackClick, colors = colors)
        }
    }
}

@Composable
fun PurchaseConfirmation(
    modifier: Modifier = Modifier,
    uiState: PurchaseConfirmationUiState = PurchaseConfirmationUiState(),
    onConfirm: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    PurchaseConfirmationScreen(modifier = modifier, uiState = uiState, onConfirm = onConfirm, onBackClick = onBackClick)
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun PurchaseConfirmationScreenPreview() {
    PurchaseConfirmationScreen()
}
