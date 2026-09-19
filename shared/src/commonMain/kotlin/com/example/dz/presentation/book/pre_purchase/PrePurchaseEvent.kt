package com.example.dz.presentation.book.pre_purchase

sealed interface PrePurchaseEvent {
    data object BackClicked : PrePurchaseEvent
    /** The one primary action, whatever it currently is: read, continue, or buy. */
    data object PrimaryActionClicked : PrePurchaseEvent

    /** Opens the ratings, and the place to leave one. */
    data object ReviewsClicked : PrePurchaseEvent
    /** Files the book onto a shelf — the handoff sends this to Collections. */
    data object BookmarkClicked : PrePurchaseEvent
    data object DownloadClicked : PrePurchaseEvent
    data object AuthorClicked : PrePurchaseEvent
    data class RelatedBookClicked(val bookId: String) : PrePurchaseEvent
}
