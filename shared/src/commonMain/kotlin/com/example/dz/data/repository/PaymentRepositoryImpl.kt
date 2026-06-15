package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.PaymentMethod
import com.example.dz.domain.model.Purchase
import com.example.dz.domain.model.PurchaseStatus
import com.example.dz.domain.repository.PaymentRepository

class PaymentRepositoryImpl(private val local: LocalDataSource) : PaymentRepository {

    override suspend fun getPaymentMethods(): AppResult<List<PaymentMethod>> {
        val selectedId = local.getSetting(KEY_SELECTED_METHOD)
        val methods = if (selectedId.isNotBlank()) {
            FakeData.paymentMethods.map { it.copy(isSelected = it.id == selectedId) }
        } else {
            FakeData.paymentMethods
        }
        return AppResult.Success(methods)
    }

    override suspend fun purchaseBook(bookId: String, paymentMethodId: String): AppResult<Purchase> {
        local.saveSetting(KEY_SELECTED_METHOD, paymentMethodId)
        local.saveSetting("purchase_status_$bookId", PurchaseStatus.Success.name)
        val purchase = FakeData.purchase(bookId, paymentMethodId).copy(status = PurchaseStatus.Success)
        return AppResult.Success(purchase)
    }

    override suspend fun getPurchaseDetails(bookId: String): AppResult<Purchase> {
        val statusName = local.getSetting("purchase_status_$bookId")
        val status = PurchaseStatus.entries.firstOrNull { it.name == statusName }
            ?: PurchaseStatus.Pending
        return AppResult.Success(FakeData.purchase(bookId).copy(status = status))
    }

    companion object {
        private const val KEY_SELECTED_METHOD = "selected_payment_method"
    }
}
