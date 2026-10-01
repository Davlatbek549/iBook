package com.example.dz.presentation.collections.details

import com.example.dz.domain.model.Book
import com.example.dz.domain.model.Collection
import com.example.dz.presentation.collections.edit.CollectionsEditBookUi
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import org.jetbrains.compose.resources.DrawableResource

data class CollectionDetailsUiState(
    val collectionId: String = "",
    val title: String = "",
    val description: String = "",
    val colorIndex: Int = 0,
    val books: List<CollectionDetailsBookUiState> = emptyList(),
    /** Everything on the reader's shelf, as candidates for the picker. Loaded when it first opens. */
    val libraryBooks: List<CollectionsEditBookUi> = emptyList(),
    val isPickerOpen: Boolean = false,
    val isLibraryLoading: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /** Derived from the shelf's own books, so the header count can never drift from the list. */
    val bookCount: Int get() = books.size

    /** What the picker ticks. Derived so the two lists cannot disagree about what is on the shelf. */
    val pickedBookIds: Set<String> get() = books.mapTo(mutableSetOf()) { it.id }
}

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
        colorIndex = colorIndex,
        books = books.map { it.toCollectionDetailsBookUi() }
    )

fun Book.toCollectionDetailsBookUi(): CollectionDetailsBookUiState =
    CollectionDetailsBookUiState(
        id = id,
        title = title,
        author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        coverUrl = coverUrl
    )
