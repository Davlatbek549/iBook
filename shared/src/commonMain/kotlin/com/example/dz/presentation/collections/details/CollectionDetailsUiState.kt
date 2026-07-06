package com.example.dz.presentation.collections.details

import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import org.jetbrains.compose.resources.DrawableResource

data class CollectionDetailsUiState(
    val collectionId: String = "",
    val title: String = "",
    val description: String = "",
    val bookCount: Int = 0,
    val books: List<CollectionDetailsBookUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class CollectionDetailsBookUiState(
    val id: String,
    val title: String,
    val author: String,
    val coverRes: DrawableResource = Res.drawable.book_cover,
    val coverUrl: String? = null,
    val note: String = "",
    val noteHighlighted: Boolean = false
)

fun Collection.toCollectionDetailsUiState(): CollectionDetailsUiState =
    CollectionDetailsUiState(
        collectionId = id,
        title = title,
        description = description.orEmpty(),
        bookCount = books.size,
        books = books.map { it.toCollectionDetailsBookUi() }
    )

fun Book.toCollectionDetailsBookUi(): CollectionDetailsBookUiState =
    CollectionDetailsBookUiState(
        id = id,
        title = title,
        author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        coverUrl = coverUrl
    )
