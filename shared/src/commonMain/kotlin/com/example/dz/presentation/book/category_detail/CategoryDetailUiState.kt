package com.example.dz.presentation.book.category_detail

import com.example.dz.domain.model.Book
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import org.jetbrains.compose.resources.DrawableResource

data class CategoryDetailUiState(
    val categoryId: String = "",
    val title: String = "",
    val description: String = "",
    val books: List<CategoryDetailBookUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class CategoryDetailBookUi(
    val id: String,
    val title: String,
    val author: String,
    val coverRes: DrawableResource = Res.drawable.book_cover,
    val coverUrl: String? = null,
    val rating: String = "4.5",
    val price: String = "12.99",
    val tag: String = "",
    val bookmarked: Boolean = false
)

fun Book.toCategoryDetailBookUi(): CategoryDetailBookUi =
    CategoryDetailBookUi(
        id = id,
        title = title,
        author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        coverUrl = coverUrl,
        rating = rating?.toRatingText() ?: "4.5",
        price = price ?: if (isFree) "$0.00" else "$12.99",
        tag = categories.firstOrNull()?.name.orEmpty()
    )

private fun Double.toRatingText(): String =
    ((this * 10).toInt() / 10.0).toString()
