package com.example.dz

import com.example.dz.domain.model.*
import com.example.dz.presentation.payment.common.CheckoutSession
import com.example.dz.presentation.payment.payment_success.*
import com.example.dz.presentation.payment.purchase_confirmation.PurchaseConfirmationViewModel
import com.example.dz.presentation.payment.purchase_receipt.toPurchaseReceiptUiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutPresentationTest {
    private fun local(user: String = "alice") = FakeLocalDataSource().apply {
        saveUserSession(user, user, "$user@example.com", "token", "refresh", true)
    }
    private fun order() = Purchase("order-actual", Book("gutenberg-84", "Frankenstein", authors = listOf(Author("mary", "Mary Shelley"))),
        bookPrice = "$6.40", discount = "−$1.40", tax = "$1.00", total = "$6.00", status = PurchaseStatus.Pending)

    @Test fun confirmationUsesSelectedOrderAndDoesNotLeakItToAnotherAccount() {
        val local = local()
        val session = CheckoutSession(local).apply { purchase = order() }
        val state = PurchaseConfirmationViewModel(session).uiState.value
        assertEquals("Frankenstein", state.title)
        assertEquals("$6.00", state.total)
        assertEquals("−$1.40", state.discount)
        local.saveUserSession("bob", "Bob", "bob@example.com", "bob-token", "refresh", true)
        assertNull(session.purchase)
        assertEquals("", PurchaseConfirmationViewModel(session).uiState.value.bookId)
    }

    @Test fun successLinksUseTheActualBookId() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val session = CheckoutSession(local()).apply { purchase = order() }
            val model = PaymentSuccessViewModel(session)
            val receipt = async(start = CoroutineStart.UNDISPATCHED) { model.effects.first() }
            model.onEvent(PaymentSuccessEvent.ViewReceiptClicked)
            assertEquals(PaymentSuccessEffect.NavigateToReceipt("gutenberg-84"), receipt.await())
            val read = async(start = CoroutineStart.UNDISPATCHED) { model.effects.first() }
            model.onEvent(PaymentSuccessEvent.StartReadingClicked)
            assertEquals(PaymentSuccessEffect.NavigateToReading("gutenberg-84"), read.await())
        } finally { Dispatchers.resetMain() }
    }

    @Test fun receiptDoesNotInventPaymentOrBillingDetails() {
        val state = order().toPurchaseReceiptUiState()
        assertEquals("order-actual", state.orderId)
        assertFalse(state.isPaid)
        assertEquals("", state.dateLabel)
        assertEquals("", state.paymentEmail)
        assertEquals("", state.paymentLabel)
        assertTrue(order().copy(status = PurchaseStatus.Success).toPurchaseReceiptUiState().isPaid)
    }
}
