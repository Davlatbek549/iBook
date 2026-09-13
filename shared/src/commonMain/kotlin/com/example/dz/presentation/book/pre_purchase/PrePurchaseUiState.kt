package com.example.dz.presentation.book.pre_purchase

import com.example.dz.domain.model.Book

/**
 * Where the reader stands with this book, which is what the primary button has to say.
 *
 * A free book counts as owned the moment it is opened — there is nothing to buy — so the button
 * offers to read it rather than to pay for it.
 */
enum class BookOwnership { NOT_OWNED, NOT_STARTED, IN_PROGRESS, FINISHED }

data class PrePurchaseUiState(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val authorId: String? = null,
    /** Null when the source has no rating, rather than a number invented to fill the pill. */
    val rating: String? = null,
    val overview: String = "",
    val price: String? = null,
    val isFree: Boolean = false,
    val pages: Int? = null,
    val genre: String? = null,
    val coverUrl: String? = null,
    val relatedBooks: List<PrePurchaseRelatedBookUi> = emptyList(),
    val ownership: BookOwnership = BookOwnership.NOT_OWNED,
    val progressPercent: Int = 0,
    val isDownloaded: Boolean = false,
    val isDownloading: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class PrePurchaseRelatedBookUi(
    val id: String,
    val title: String,
    val coverUrl: String? = null,
)

fun Book.toPrePurchaseUiState(relatedBooks: List<Book> = emptyList()): PrePurchaseUiState =
    PrePurchaseUiState(
        bookId = id,
        title = title,
        author = authors.firstOrNull()?.name.orEmpty(),
        authorId = authors.firstOrNull()?.id,
        rating = rating?.toRatingText(),
        overview = description.orEmpty(),
        price = price,
        isFree = isFree,
        pages = pageCount,
        genre = categories.firstOrNull()?.name,
        coverUrl = coverUrl,
        relatedBooks = relatedBooks.map { it.toPrePurchaseRelatedBookUi() }
    )

private fun Book.toPrePurchaseRelatedBookUi(): PrePurchaseRelatedBookUi =
    PrePurchaseRelatedBookUi(id = id, title = title, coverUrl = coverUrl)

/** One decimal place — "4.6", not "4.5999999". */
private fun Double.toRatingText(): String {
    val tenths = (this * 10).toInt()
    return "${tenths / 10}.${tenths % 10}"
}
