package com.example.dz.presentation.collections.edit

import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import org.jetbrains.compose.resources.DrawableResource

data class CollectionsEditUiState(
    val collectionId: String = "",
    val name: String = "",
    val description: String = "",
    val visibleToFriends: Boolean = true,
    val books: List<CollectionsEditBookUi> = emptyList(),
    val isNewCollection: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class CollectionsEditBookUi(
    val id: String,
    val title: String,
    val author: String,
    val coverRes: DrawableResource = Res.drawable.book_cover,
    val coverUrl: String? = null
)

fun Collection.toCollectionsEditUiState(): CollectionsEditUiState =
    CollectionsEditUiState(
        collectionId = id,
        name = title,
        description = description.orEmpty(),
        books = books.map { it.toCollectionsEditBookUi() }
    )

fun Book.toCollectionsEditBookUi(): CollectionsEditBookUi =
    CollectionsEditBookUi(
        id = id,
        title = title,
        author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        coverUrl = coverUrl
    )
