package com.example.dz.presentation.book.pre_purchase

sealed interface PrePurchaseEvent {
    data object BackClicked : PrePurchaseEvent
    data object ShareClicked : PrePurchaseEvent
    data object FavoriteClicked : PrePurchaseEvent
    data object ViewSampleClicked : PrePurchaseEvent
    data object PurchaseClicked : PrePurchaseEvent
    data object AuthorClicked : PrePurchaseEvent
    data object TagClicked : PrePurchaseEvent
    data class RelatedBookClicked(val bookId: String) : PrePurchaseEvent
}
