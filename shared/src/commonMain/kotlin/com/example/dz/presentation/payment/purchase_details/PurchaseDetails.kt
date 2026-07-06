package com.example.dz.presentation.payment.purchase_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.paypal
import dz.shared.generated.resources.po_apply
import dz.shared.generated.resources.po_book_price
import dz.shared.generated.resources.po_change
import dz.shared.generated.resources.po_coins_applied
import dz.shared.generated.resources.po_confirm_purchase
import dz.shared.generated.resources.po_paying_with
import dz.shared.generated.resources.po_review_title
import dz.shared.generated.resources.po_summary
import dz.shared.generated.resources.po_tax
import dz.shared.generated.resources.po_total
import dz.shared.generated.resources.po_yours_forever
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchaseDetailsScreen(
    uiState: PurchaseDetailsUiState = PurchaseDetailsUiState(),
    onEvent: (PurchaseDetailsEvent) -> Unit = {},
    modifier: Modifier = Modifier
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
            InkTopBar(title = stringResource(Res.string.po_review_title), onBackClick = { onEvent(PurchaseDetailsEvent.BackClicked) }, colors = colors)

            // book row
            Row(
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp, top = 8.dp)
                    .fillMaxWidth()
                    .inkCard(colors)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.book_cover),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 58.dp, height = 86.dp)
                        .shadow(8.dp, RoundedCornerShape(InkShape.cover), clip = true)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(uiState.title, fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = colors.ink)
                    Text(uiState.author, modifier = Modifier.padding(top = 4.dp), fontFamily = bodyFont, fontSize = 12.sp, color = colors.muted)
                    Text(stringResource(Res.string.po_yours_forever), modifier = Modifier.padding(top = 8.dp), fontFamily = bodyFont, fontSize = 11.sp, color = colors.muted)
                }
                Text(uiState.bookPrice, fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = colors.ink)
            }

            // coins
            Row(
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp, top = 14.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(InkShape.radius))
                    .background(colors.alt)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(InkIcons.Tag, null, tint = colors.accent, modifier = Modifier.size(17.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Use ")
                        withStyle(SpanStyle(color = colors.ink, fontWeight = FontWeight.SemiBold)) { append("240 coins") }
                        append(" for $2.40 off")
                    },
                    modifier = Modifier.weight(1f),
                    fontFamily = bodyFont, fontSize = 12.5.sp, color = colors.inkSoft
                )
                Text(
                    text = stringResource(Res.string.po_apply),
                    modifier = Modifier.clickable { onEvent(PurchaseDetailsEvent.ApplyCoinsClicked) },
                    fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = colors.accent
                )
            }

            // paying with
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
                InkLabel(text = stringResource(Res.string.po_paying_with), colors = colors)
                Row(
                    modifier = Modifier
                        .padding(top = 11.dp)
                        .fillMaxWidth()
                        .inkCard(colors)
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    PayPalGlyph(colors)
                    Column(modifier = Modifier.weight(1f)) {
                        Text("PayPal", fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp, color = colors.ink)
                        Text("amelia@hartwell.co", modifier = Modifier.padding(top = 5.dp), fontFamily = bodyFont, fontSize = 11.5.sp, color = colors.muted)
                    }
                    Text(
                        text = stringResource(Res.string.po_change),
                        modifier = Modifier.clickable { onEvent(PurchaseDetailsEvent.ChangePaymentClicked) },
                        fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = colors.accent
                    )
                }
            }

            // summary
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
                InkLabel(text = stringResource(Res.string.po_summary), colors = colors)
                Column(
                    modifier = Modifier.padding(top = 13.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PriceRow(stringResource(Res.string.po_book_price), uiState.bookPrice, colors = colors)
                    PriceRow(stringResource(Res.string.po_coins_applied), uiState.coinsDiscount, accent = true, colors = colors)
                    PriceRow(stringResource(Res.string.po_tax), uiState.tax, colors = colors)
                    HorizontalDivider(thickness = 1.dp, color = colors.line)
                    PriceRow(stringResource(Res.string.po_total), uiState.total, strong = true, colors = colors)
                }
            }
        }

        // confirm bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(colors.paper)
        ) {
            HorizontalDivider(thickness = 1.dp, color = colors.line)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 18.dp)
                    .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                    .background(colors.accent)
                    .clickable { onEvent(PurchaseDetailsEvent.PayNowClicked) }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${stringResource(Res.string.po_confirm_purchase)} · ${uiState.total}",
                    fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 14.5.sp, color = colors.onAccent
                )
            }
        }
    }
}

@Composable
internal fun PriceRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    strong: Boolean = false,
    accent: Boolean = false,
    colors: InkColors,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = label,
            fontFamily = inkBodyFontFamily(),
            fontWeight = if (strong) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 13.sp,
            color = if (strong) colors.ink else colors.muted
        )
        if (strong) {
            Text(text = value, fontFamily = inkDisplayFontFamily(), fontWeight = FontWeight.Medium, fontSize = 19.sp, color = colors.ink)
        } else {
            Text(text = value, fontFamily = inkBodyFontFamily(), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = if (accent) colors.accent else colors.ink)
        }
    }
}

@Composable
internal fun PayPalGlyph(colors: InkColors) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(InkShape.radiusSm))
            .background(colors.alt),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.paypal),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun PurchaseDetailsScreenPreview() {
    PurchaseDetailsScreen()
}
