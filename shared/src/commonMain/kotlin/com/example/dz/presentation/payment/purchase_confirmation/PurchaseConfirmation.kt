package com.example.dz.presentation.payment.purchase_confirmation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.presentation.payment.common.*
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchaseConfirmationScreen(
    uiState: PurchaseConfirmationUiState = PurchaseConfirmationUiState(),
    onEvent: (PurchaseConfirmationEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CheckoutPage(stringResource(Res.string.checkout_confirm_order), { onEvent(PurchaseConfirmationEvent.DismissClicked) }, modifier, error = if (uiState.bookId.isBlank()) stringResource(Res.string.checkout_no_order) else null,
        footer = {
            CheckoutText(stringResource(Res.string.checkout_review_note), size = 12)
            CheckoutAction(stringResource(Res.string.checkout_continue_amount, uiState.total), { onEvent(PurchaseConfirmationEvent.ConfirmClicked) })
        }) {
        OrganicCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(18.dp)) { CheckoutBook(uiState.title, uiState.author, uiState.coverUrl) }
        CheckoutBreakdown(uiState.title, uiState.bookPrice, uiState.discount, uiState.tax, uiState.total)
        CheckoutMethod(stringResource(Res.string.checkout_choose_method)) { onEvent(PurchaseConfirmationEvent.ConfirmClicked) }
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun PurchaseConfirmationPreview() { PurchaseConfirmationScreen(PurchaseConfirmationUiState(bookId = "preview", title = "Bestiary", author = "K-Ming Chang", bookPrice = "$6.40", discount = "−$1.40", tax = "$1.00", total = "$6.00")) }
