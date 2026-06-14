package com.example.dz.domain.usecase.payment

import com.example.dz.domain.repository.PaymentRepository

class GetPaymentMethodsUseCase(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke() = repository.getPaymentMethods()
}
