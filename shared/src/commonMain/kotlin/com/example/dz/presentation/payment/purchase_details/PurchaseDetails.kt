package com.example.dz.presentation.payment.purchase_details

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.presentation.payment.common.*
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/** The app's existing order summary; card entry awaits a payment-provider integration. */
@Composable
fun PurchaseDetailsScreen(
    uiState: PurchaseDetailsUiState = PurchaseDetailsUiState(),
    onEvent: (PurchaseDetailsEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CheckoutPage(stringResource(Res.string.checkout_order_details), { onEvent(PurchaseDetailsEvent.BackClicked) },
        modifier, uiState.isLoading, uiState.errorMessage,
        footer = {
            CheckoutAction(stringResource(Res.string.checkout_review_order), { onEvent(PurchaseDetailsEvent.PayNowClicked) }, enabled = uiState.bookId.isNotBlank())
        }) {
        OrganicCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(18.dp)) {
            CheckoutBook(uiState.title, uiState.author, uiState.coverUrl)
        }
        CheckoutBreakdown(uiState.title, uiState.bookPrice, uiState.coinsDiscount, uiState.tax, uiState.total)
        CheckoutMethod(stringResource(Res.string.checkout_choose_method)) { onEvent(PurchaseDetailsEvent.ChangePaymentClicked) }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun PurchaseDetailsPreview() {
    PurchaseDetailsScreen(PurchaseDetailsUiState(bookId = "preview", title = "Bestiary", author = "K-Ming Chang", bookPrice = "$6.40", coinsDiscount = "−$1.40", tax = "$1.00", total = "$6.00"))
}
