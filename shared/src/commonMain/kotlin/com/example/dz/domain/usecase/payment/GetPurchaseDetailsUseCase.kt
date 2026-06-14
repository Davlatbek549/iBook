package com.example.dz.domain.usecase.payment

import com.example.dz.domain.repository.PaymentRepository

class GetPurchaseDetailsUseCase(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(bookId: String) = repository.getPurchaseDetails(bookId)
}
