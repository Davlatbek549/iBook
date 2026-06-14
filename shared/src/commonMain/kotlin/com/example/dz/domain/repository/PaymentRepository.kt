package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.PaymentMethod
import com.example.dz.domain.model.Purchase

interface PaymentRepository {
    suspend fun getPaymentMethods(): AppResult<List<PaymentMethod>>
    suspend fun purchaseBook(bookId: String, paymentMethodId: String): AppResult<Purchase>
    suspend fun getPurchaseDetails(bookId: String): AppResult<Purchase>
}
