package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.PaymentMethod
import com.example.dz.domain.model.Purchase
import com.example.dz.domain.model.PurchaseStatus
import com.example.dz.domain.repository.PaymentRepository

class FakePaymentRepository : PaymentRepository {
    override suspend fun getPaymentMethods(): AppResult<List<PaymentMethod>> =
        AppResult.Success(FakeData.paymentMethods)

    override suspend fun purchaseBook(bookId: String, paymentMethodId: String): AppResult<Purchase> =
        AppResult.Success(FakeData.purchase(bookId, paymentMethodId).copy(status = PurchaseStatus.Success))

    override suspend fun getPurchaseDetails(bookId: String): AppResult<Purchase> =
        AppResult.Success(FakeData.purchase(bookId))
}
