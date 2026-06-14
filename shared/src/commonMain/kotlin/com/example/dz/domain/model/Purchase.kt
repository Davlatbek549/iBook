package com.example.dz.domain.model

data class Purchase(
    val id: String,
    val book: Book,
    val paymentMethod: PaymentMethod? = null,
    val bookPrice: String,
    val discount: String? = null,
    val tax: String? = null,
    val total: String,
    val status: PurchaseStatus
)

enum class PurchaseStatus {
    Pending,
    Success,
    Failed
}
