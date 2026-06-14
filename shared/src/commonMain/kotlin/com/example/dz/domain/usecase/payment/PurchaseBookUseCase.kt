package com.example.dz.domain.usecase.payment

import com.example.dz.domain.repository.PaymentRepository

class PurchaseBookUseCase(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(bookId: String, paymentMethodId: String) =
        repository.purchaseBook(bookId, paymentMethodId)
}
