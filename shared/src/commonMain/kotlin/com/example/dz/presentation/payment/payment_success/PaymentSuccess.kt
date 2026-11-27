package com.example.dz.presentation.payment.payment_success

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.presentation.payment.common.*
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchaseSuccessScreen(
    uiState: PaymentSuccessUiState = PaymentSuccessUiState(),
    onEvent: (PaymentSuccessEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (uiState.bookId.isBlank()) {
        CheckoutPage(stringResource(Res.string.checkout_order_summary), { onEvent(PaymentSuccessEvent.BackToStoreClicked) },
            modifier, error = stringResource(Res.string.checkout_no_order)) { }
        return
    }
    CheckoutResult(true, stringResource(Res.string.checkout_book_is_yours, uiState.title),
        stringResource(Res.string.checkout_success_body, uiState.total), OrganicIcons.Check,
        { onEvent(PaymentSuccessEvent.BackToStoreClicked) }, modifier,
        footer = {
            CheckoutAction(stringResource(Res.string.ps_start_reading), { onEvent(PaymentSuccessEvent.StartReadingClicked) }, enabled = uiState.bookId.isNotBlank())
            CheckoutAction(stringResource(Res.string.checkout_view_receipt), { onEvent(PaymentSuccessEvent.ViewReceiptClicked) }, secondary = true, enabled = uiState.bookId.isNotBlank())
        }, detail = {
            OrganicCard(contentPadding = PaddingValues(18.dp)) { CheckoutBook(uiState.title, uiState.author, uiState.coverUrl, compact = true) }
        })
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun PaymentSuccessPreview() { PurchaseSuccessScreen(PaymentSuccessUiState(bookId = "preview", title = "Bestiary", author = "K-Ming Chang", total = "$6.00")) }
