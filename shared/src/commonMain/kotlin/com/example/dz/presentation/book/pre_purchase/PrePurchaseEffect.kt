package com.example.dz.presentation.book.pre_purchase

sealed interface PrePurchaseEffect {
    data object NavigateBack : PrePurchaseEffect
    data class NavigateToReading(val bookId: String) : PrePurchaseEffect
    data class NavigateToPurchase(val bookId: String) : PrePurchaseEffect
    data class NavigateToAuthor(val authorId: String) : PrePurchaseEffect
    data class NavigateToBook(val bookId: String) : PrePurchaseEffect
}
