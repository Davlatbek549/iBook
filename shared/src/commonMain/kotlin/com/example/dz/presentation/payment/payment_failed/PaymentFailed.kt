package com.example.dz.presentation.payment.payment_failed

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.components.organic.OrganicCard
import com.example.dz.designsystem.components.organic.OrganicKicker
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.presentation.payment.common.*
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun PurchaseFailedScreen(
    uiState: PaymentFailedUiState = PaymentFailedUiState(),
    onEvent: (PaymentFailedEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    CheckoutResult(false, stringResource(Res.string.pf_title), stringResource(Res.string.checkout_failure_body),
        OrganicIcons.Close, { onEvent(PaymentFailedEvent.CloseClicked) }, modifier,
        footer = {
            CheckoutAction(stringResource(Res.string.pf_try_again), { onEvent(PaymentFailedEvent.TryAgainClicked) }, busy = uiState.isRetrying)
            CheckoutAction(stringResource(Res.string.pf_change_method), { onEvent(PaymentFailedEvent.ChangeMethodClicked) }, secondary = true)
        }, detail = {
            OrganicCard(Modifier.fillMaxWidth(), background = OrganicColors.accent100, contentPadding = PaddingValues(18.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OrganicKicker(stringResource(Res.string.checkout_worth_checking), OrganicColors.accent900)
                    CheckoutText(stringResource(Res.string.checkout_check_expiry), color = OrganicColors.accent900)
                    CheckoutText(stringResource(Res.string.checkout_check_bank), color = OrganicColors.accent900)
                }
            }
        })
}

@Preview(widthDp = 390, heightDp = 844)
@Composable
private fun PaymentFailedPreview() { PurchaseFailedScreen() }
