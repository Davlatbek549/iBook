package com.example.dz.presentation.payment.payment_success

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkGhostButton
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.ps_back_to_store
import dz.shared.generated.resources.ps_start_reading
import dz.shared.generated.resources.ps_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentSuccessScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onDiscountCodeClick: () -> Unit = {},
    onChangePaymentClick: () -> Unit = {},
    onReadNowClick: () -> Unit = {}
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
            InkIconButton(icon = InkIcons.Close, onClick = onBackClick, colors = colors)
        }

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // check medallion
            Box(
                modifier = Modifier.size(76.dp).clip(CircleShape).background(colors.accentSoft),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .shadow(12.dp, CircleShape)
                        .clip(CircleShape)
                        .background(colors.accent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(InkIcons.Done, null, tint = colors.onAccent, modifier = Modifier.size(22.dp))
                }
            }

            Text(
                text = stringResource(Res.string.ps_title),
                modifier = Modifier.padding(top = 26.dp),
                fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 28.sp, color = colors.ink
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) { append("Mexican Gothic") }
                    append(" is on your shelf. Receipt sent to amelia@hartwell.co.")
                },
                modifier = Modifier.padding(top = 12.dp).widthIn(max = 260.dp),
                fontFamily = bodyFont, fontSize = 13.5.sp, lineHeight = 22.sp, textAlign = TextAlign.Center, color = colors.muted
            )

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .inkCard(colors)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.book_cover),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(width = 44.dp, height = 64.dp).shadow(6.dp, RoundedCornerShape(InkShape.cover), clip = true)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text("Mexican Gothic", fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 14.5.sp, color = colors.ink)
                    Text("Silvia Moreno-Garcia", modifier = Modifier.padding(top = 4.dp), fontFamily = bodyFont, fontSize = 11.5.sp, color = colors.muted)
                }
                Text("$11.43", fontFamily = bodyFont, fontSize = 11.sp, color = colors.muted)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                .background(colors.accent)
                .clickable(onClick = onReadNowClick)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally)
        ) {
            Icon(InkIcons.BookOpen, null, tint = colors.onAccent, modifier = Modifier.size(16.dp))
            Text(stringResource(Res.string.ps_start_reading), fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 14.5.sp, color = colors.onAccent)
        }
        Spacer(modifier = Modifier.height(10.dp))
        InkGhostButton(text = stringResource(Res.string.ps_back_to_store), onClick = onBackClick, colors = colors)
    }
}

@Composable
fun PurchaseSuccessScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onDiscountCodeClick: () -> Unit = {},
    onChangePaymentClick: () -> Unit = {},
    onReadNowClick: () -> Unit = {}
) {
    PaymentSuccessScreen(modifier, onBackClick, onDiscountCodeClick, onChangePaymentClick, onReadNowClick)
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun PaymentSuccessScreenPreview() {
    PaymentSuccessScreen()
}
