package com.example.dz.screens.purchase_details

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.app_components.ink.InkLabel
import com.example.dz.app_components.ink.InkSecondaryButton
import com.example.dz.app_components.ink.InkTopBar
import com.example.dz.app_components.ink.inkCard
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.po_book_price
import dz.shared.generated.resources.po_breakdown
import dz.shared.generated.resources.po_coins_applied
import dz.shared.generated.resources.po_completed
import dz.shared.generated.resources.po_details_title
import dz.shared.generated.resources.po_need_help
import dz.shared.generated.resources.po_payment
import dz.shared.generated.resources.po_read_now
import dz.shared.generated.resources.po_receipt
import dz.shared.generated.resources.po_tax
import dz.shared.generated.resources.po_total_charged
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchaseReceiptScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onReadNowClick: () -> Unit = {},
    onReceiptClick: () -> Unit = {}
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
            InkTopBar(
                title = stringResource(Res.string.po_details_title),
                onBackClick = onBackClick,
                right = { InkIconButton(icon = InkIcons.Share, onClick = {}, colors = colors) },
                colors = colors
            )

            // order card
            Column(
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp, top = 8.dp)
                    .fillMaxWidth()
                    .inkCard(colors)
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        InkLabel(text = "Order DZ-20614", colors = colors)
                    }
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.accentSoft)
                            .padding(horizontal = 11.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(InkIcons.Done, null, tint = colors.accent, modifier = Modifier.size(10.dp))
                        Text(
                            text = stringResource(Res.string.po_completed).uppercase(),
                            fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 10.sp,
                            letterSpacing = 0.4.sp, color = colors.accent
                        )
                    }
                }
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.book_cover),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 54.dp, height = 80.dp)
                            .shadow(8.dp, RoundedCornerShape(InkShape.cover), clip = true)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mexican Gothic", fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = colors.ink)
                        Text("Silvia Moreno-Garcia", modifier = Modifier.padding(top = 4.dp), fontFamily = bodyFont, fontSize = 12.sp, color = colors.muted)
                        Text("June 8, 2026 · 14:12", modifier = Modifier.padding(top = 8.dp), fontFamily = bodyFont, fontSize = 11.sp, color = colors.muted)
                    }
                }
            }

            // payment
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
                InkLabel(text = stringResource(Res.string.po_payment), colors = colors)
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
                    Text("$11.43", fontFamily = displayFont, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = colors.ink)
                }
            }

            // breakdown
            Column(modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
                InkLabel(text = stringResource(Res.string.po_breakdown), colors = colors)
                Column(
                    modifier = Modifier.padding(top = 13.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PriceRow(stringResource(Res.string.po_book_price), "$12.99", colors = colors)
                    PriceRow(stringResource(Res.string.po_coins_applied), "−$2.40", accent = true, colors = colors)
                    PriceRow(stringResource(Res.string.po_tax), "$0.84", colors = colors)
                    HorizontalDivider(thickness = 1.dp, color = colors.line)
                    PriceRow(stringResource(Res.string.po_total_charged), "$11.43", strong = true, colors = colors)
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.po_need_help),
                    fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp, color = colors.muted
                )
            }
        }

        // action bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(colors.paper)
        ) {
            HorizontalDivider(thickness = 1.dp, color = colors.line)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InkSecondaryButton(
                    text = stringResource(Res.string.po_receipt),
                    onClick = onReceiptClick,
                    modifier = Modifier.weight(1f),
                    colors = colors
                )
                Row(
                    modifier = Modifier
                        .weight(1.4f)
                        .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
                        .background(colors.accent)
                        .clickable(onClick = onReadNowClick)
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally)
                ) {
                    Icon(InkIcons.BookOpen, null, tint = colors.onAccent, modifier = Modifier.size(16.dp))
                    Text(
                        text = stringResource(Res.string.po_read_now),
                        fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = colors.onAccent
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun PurchaseReceiptScreenPreview() {
    PurchaseReceiptScreen()
}
