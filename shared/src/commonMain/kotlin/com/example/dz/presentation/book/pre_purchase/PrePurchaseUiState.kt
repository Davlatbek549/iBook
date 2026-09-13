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
    val ratingCount: Int? = null,
    val overview: String = "",
    val price: String? = null,
    val isFree: Boolean = false,
    val pages: Int? = null,
    /** Roughly how long it takes to read, from its length. Approximate, and labelled as such. */
    val minutesToRead: Int? = null,
    val genre: String? = null,
    val publisher: String? = null,
    val firstPublishYear: Int? = null,
    val downloadCount: Int? = null,
    val language: String? = null,
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
        ratingCount = reviewCount,
        overview = description.orEmpty(),
        price = price,
        isFree = isFree,
        pages = pageCount,
        minutesToRead = pageCount?.let { it * MINUTES_PER_PAGE },
        genre = categories.firstOrNull()?.name?.toDisplayCategory(),
        publisher = publisher,
        firstPublishYear = firstPublishYear,
        downloadCount = downloadCount,
        language = language?.uppercase(),
        coverUrl = coverUrl,
        relatedBooks = relatedBooks.map { it.toPrePurchaseRelatedBookUi() }
    )

private fun Book.toPrePurchaseRelatedBookUi(): PrePurchaseRelatedBookUi =
    PrePurchaseRelatedBookUi(id = id, title = title, coverUrl = coverUrl)

/**
 * Catalogue subjects arrive in library cataloguing style — "Courtship -- Fiction", "Fiction,
 * general" — which reads as a database field rather than a genre. This takes the first real part
 * and gives it a capital.
 */
private fun String.toDisplayCategory(): String =
    split(" -- ", "--", ",")
        .firstOrNull { it.isNotBlank() }
        ?.trim()
        ?.replaceFirstChar { it.uppercase() }
        ?: this

/**
 * A page a minute and a half. Every reader is different, which is why the screen says "about" and
 * never presents this as the book's own fact.
 */
private const val MINUTES_PER_PAGE = 2

/** One decimal place — "4.6", not "4.5999999". */
private fun Double.toRatingText(): String {
    val tenths = (this * 10).toInt()
    return "${tenths / 10}.${tenths % 10}"
}
