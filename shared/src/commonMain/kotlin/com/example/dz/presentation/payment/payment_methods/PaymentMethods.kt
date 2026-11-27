package com.example.dz.presentation.payment.payment_methods

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.presentation.payment.common.*
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentMethodsScreen(
    uiState: PaymentMethodsUiState = PaymentMethodsUiState(),
    onEvent: (PaymentMethodsEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CheckoutPage(stringResource(Res.string.pm_title), { onEvent(PaymentMethodsEvent.BackClicked) }, modifier,
        uiState.isLoading, uiState.errorMessage) {
        if (uiState.methods.isEmpty()) {
            OrganicCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(22.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(OrganicIcons.Lock, null, tint = OrganicColors.accent700, modifier = Modifier.size(28.dp))
                    CheckoutText(stringResource(Res.string.checkout_no_methods), size = 17, bold = true, color = OrganicColors.text)
                    CheckoutText(stringResource(Res.string.checkout_cards_unavailable))
                }
            }
        }
        uiState.methods.forEach { method ->
            val selected = method.id == uiState.selectedId
            val shape = RoundedCornerShape(28.dp)
            Row(Modifier.fillMaxWidth().clip(shape)
                .background(if (selected) OrganicColors.accent100 else OrganicColors.neutral100)
                .then(if (selected) Modifier.border(2.dp, OrganicColors.accent, shape) else Modifier)
                .semantics { this.selected = selected }
                .clickable(role = Role.RadioButton) { onEvent(PaymentMethodsEvent.MethodSelected(method.id)) }
                .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                CheckoutBrand(when (method.brand) {
                    PaymentBrand.Visa -> "VISA"
                    PaymentBrand.Mastercard -> "MC"
                    PaymentBrand.Paypal -> "PP"
                    PaymentBrand.ApplePay -> "Pay"
                }, if (method.brand == PaymentBrand.Paypal) OrganicColors.accent2_700 else OrganicColors.neutral900)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    CheckoutText(method.title, size = 15, bold = true, color = OrganicColors.text)
                    if (method.subtitle.isNotBlank()) CheckoutText(method.subtitle, size = 12)
                }
                if (selected) Icon(OrganicIcons.Check, stringResource(Res.string.checkout_selected), tint = OrganicColors.accent700, modifier = Modifier.size(20.dp))
            }
        }
        // No provider is connected yet: don't present a working card form or fictional balances.
        CheckoutAction(stringResource(Res.string.checkout_add_card), { onEvent(PaymentMethodsEvent.AddMethodClicked) }, secondary = true, enabled = false)
        if (uiState.methods.isNotEmpty()) CheckoutNotice(stringResource(Res.string.checkout_cards_unavailable))
    }
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun PaymentMethodsPreview() {
    PaymentMethodsScreen(PaymentMethodsUiState(methods = listOf(
        PaymentMethodItem("visa", PaymentBrand.Visa, "•••• 4417", "Amelia Hart · exp 04/29"),
        PaymentMethodItem("mc", PaymentBrand.Mastercard, "•••• 8802", "Amelia Hart · exp 11/27"),
        PaymentMethodItem("pp", PaymentBrand.Paypal, "PayPal", "amelia@hart.co")), selectedId = "visa"))
}
