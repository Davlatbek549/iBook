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
    val colorIndex: Int = 0,
    /** A new shelf is private until the reader says otherwise. */
    val visibleToFriends: Boolean = false,
    val books: List<CollectionsEditBookUi> = emptyList(),
    /** Everything on the reader's shelf, as candidates for the picker. Loaded when it first opens. */
    val libraryBooks: List<CollectionsEditBookUi> = emptyList(),
    val isPickerOpen: Boolean = false,
    val isLibraryLoading: Boolean = false,
    val isNewCollection: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    /** What the picker ticks. Derived so the two lists cannot disagree about what is on the shelf. */
    val pickedBookIds: Set<String> get() = books.mapTo(mutableSetOf()) { it.id }
}

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
        colorIndex = colorIndex,
        visibleToFriends = isShared,
        books = books.map { it.toCollectionsEditBookUi() }
    )

fun Book.toCollectionsEditBookUi(): CollectionsEditBookUi =
    CollectionsEditBookUi(
        id = id,
        title = title,
        author = authors.firstOrNull()?.name.orEmpty().ifBlank { "Unknown author" },
        coverUrl = coverUrl
    )
