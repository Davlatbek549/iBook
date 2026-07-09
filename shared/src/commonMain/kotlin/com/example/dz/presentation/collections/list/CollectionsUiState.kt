package com.example.dz.presentation.collections.list

import com.example.dz.domain.model.Collection
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import org.jetbrains.compose.resources.DrawableResource

data class CollectionsUiState(
    val collections: List<CollectionUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class CollectionUiState(
    val id: String,
    val name: String,
    val bookCount: Int,
    val covers: List<CollectionCoverUi>
)

data class CollectionCoverUi(
    val coverUrl: String? = null,
    val coverRes: DrawableResource = Res.drawable.book_cover
)

fun Collection.toCollectionUiState(): CollectionUiState =
    CollectionUiState(
        id = id,
        name = title,
        bookCount = books.size,
        covers = books.take(3).map { CollectionCoverUi(coverUrl = it.coverUrl) }
    )
