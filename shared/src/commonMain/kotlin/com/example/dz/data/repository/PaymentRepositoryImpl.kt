package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.Book
import com.example.dz.domain.model.PaymentMethod
import com.example.dz.domain.model.Purchase
import com.example.dz.domain.model.PurchaseStatus
import com.example.dz.domain.repository.BookRepository
import com.example.dz.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val local: LocalDataSource,
    private val books: BookRepository
) : PaymentRepository {

    override suspend fun getPaymentMethods(): AppResult<List<PaymentMethod>> =
        AppResult.Success(emptyList())

    override suspend fun purchaseBook(bookId: String, paymentMethodId: String): AppResult<Purchase> {
        local.saveSetting(KEY_SELECTED_METHOD, paymentMethodId)
        local.saveSetting("purchase_status_$bookId", PurchaseStatus.Success.name)
        return purchaseFor(bookId, PurchaseStatus.Success)
    }

    override suspend fun getPurchaseDetails(bookId: String): AppResult<Purchase> {
        val statusName = local.getSetting("purchase_status_$bookId")
        val status = PurchaseStatus.entries.firstOrNull { it.name == statusName }
            ?: PurchaseStatus.Pending
        return purchaseFor(bookId, status)
    }

    private suspend fun purchaseFor(bookId: String, status: PurchaseStatus): AppResult<Purchase> =
        when (val result = books.getBookDetails(bookId)) {
            is AppResult.Success -> AppResult.Success(result.data.toPurchase(status))
            is AppResult.Error -> AppResult.Error(result.error)
        }

    private fun Book.toPurchase(status: PurchaseStatus): Purchase {
        val price = price?.takeIf { it.isNotBlank() } ?: "$0.00"
        return Purchase(
            id = "purchase-$id",
            book = this,
            paymentMethod = null,
            bookPrice = price,
            discount = "$0.00",
            tax = "$0.00",
            total = price,
            status = status
        )
    }

    companion object {
        private const val KEY_SELECTED_METHOD = "selected_payment_method"
    }
}
