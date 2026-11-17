package com.example.dz.presentation.payment.purchase_receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.theme.*
import com.example.dz.presentation.payment.common.*
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchaseReceiptScreen(
    uiState: PurchaseReceiptUiState = PurchaseReceiptUiState(),
    onEvent: (PurchaseReceiptEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CheckoutPage(stringResource(Res.string.checkout_receipt), { onEvent(PurchaseReceiptEvent.BackClicked) }, modifier,
        uiState.isLoading, uiState.errorMessage,
        footer = {
            CheckoutAction(stringResource(Res.string.ps_start_reading), { onEvent(PurchaseReceiptEvent.ReadNowClicked) }, secondary = true, enabled = uiState.bookId.isNotBlank())
        }) {
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(OrganicColors.neutral100)) {
            Column(Modifier.fillMaxWidth().background(OrganicColors.accent2_200).padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OrganicKicker(stringResource(if (uiState.isPaid) Res.string.checkout_paid else Res.string.checkout_order_summary), OrganicColors.accent2_900)
                Text(uiState.totalCharged, fontFamily = organicHeadingFontFamily(), fontSize = 30.sp, color = OrganicColors.accent2_900)
                if (uiState.dateLabel.isNotBlank()) CheckoutText(uiState.dateLabel, size = 13, color = OrganicColors.accent2_900)
            }
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                CheckoutBook(uiState.title, uiState.author, uiState.coverUrl, compact = true, trailing = uiState.bookPrice)
                CheckoutAmount(stringResource(Res.string.checkout_discount), uiState.coinsDiscount)
                CheckoutAmount(stringResource(Res.string.checkout_tax), uiState.tax)
                CheckoutAmount(stringResource(Res.string.checkout_total), uiState.totalCharged, total = true)
            }
        }
        CheckoutAmount(stringResource(Res.string.checkout_order), uiState.orderId)
        if (uiState.paymentLabel.isNotBlank()) CheckoutAmount(stringResource(Res.string.checkout_method), uiState.paymentLabel)
        if (uiState.paymentEmail.isNotBlank()) CheckoutAmount(stringResource(Res.string.checkout_billed_to), uiState.paymentEmail)
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun ReceiptPreview() {
    PurchaseReceiptScreen(PurchaseReceiptUiState(bookId = "preview", title = "Bestiary", author = "K-Ming Chang",
        orderId = "DZ-40912-BST", dateLabel = "15 Aug 2026 · 9:38", paymentLabel = "VISA •••• 4417", paymentEmail = "amelia@hart.co",
        bookPrice = "$6.40", coinsDiscount = "−$1.40", tax = "$1.00", totalCharged = "$6.00", isPaid = true))
}
