package com.example.dz.presentation.book.pre_purchase

import com.example.dz.domain.model.Book
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import org.jetbrains.compose.resources.DrawableResource

data class PrePurchaseUiState(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val authorId: String? = null,
    val rating: String = "4.6",
    val reviews: String = "",
    val overview: String = "",
    val price: String = "$12.99",
    val pages: String = "320",
    val readTime: String = "6h 20m",
    val language: String = "EN",
    val tags: List<String> = listOf("Literary", "Gothic"),
    val coverRes: DrawableResource = Res.drawable.book_cover,
    val coverUrl: String? = null,
    val relatedBooks: List<PrePurchaseRelatedBookUi> = emptyList(),
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class PrePurchaseRelatedBookUi(
    val id: String,
    val coverRes: DrawableResource = Res.drawable.book_cover,
    val coverUrl: String? = null
)

fun Book.toPrePurchaseUiState(relatedBooks: List<Book> = emptyList()): PrePurchaseUiState =
    PrePurchaseUiState(
        bookId = id,
        title = title,
        author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        authorId = authors.firstOrNull()?.id,
        rating = rating?.toRatingText() ?: "4.6",
        reviews = reviewCount?.toString().orEmpty(),
        overview = description.orEmpty(),
        price = price ?: if (isFree) "$0.00" else "$12.99",
        pages = pageCount?.toString() ?: "320",
        language = language?.uppercase() ?: "EN",
        tags = categories.map { it.name }.ifEmpty { listOf("Literary", "Gothic") },
        coverRes = Res.drawable.book_cover,
        coverUrl = coverUrl,
        relatedBooks = relatedBooks.map { it.toPrePurchaseRelatedBookUi() }
    )

private fun Book.toPrePurchaseRelatedBookUi(): PrePurchaseRelatedBookUi =
    PrePurchaseRelatedBookUi(id = id, coverUrl = coverUrl)

private fun Double.toRatingText(): String =
    ((this * 10).toInt() / 10.0).toString()
