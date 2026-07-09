package com.example.dz.presentation.book.author_detail

import dz.shared.generated.resources.Res
import dz.shared.generated.resources.author_detail_bestiary
import dz.shared.generated.resources.author_detail_patricia_avatar
import dz.shared.generated.resources.author_detail_red_at_bone
import dz.shared.generated.resources.book_olive_again
import org.jetbrains.compose.resources.DrawableResource

data class AuthorDetailUiState(
    val authorId: String = "",
    val name: String = "",
    val tagline: String = "",
    val about: String = "",
    val booksCount: String = "",
    val readersCount: String = "",
    val averageRating: String = "",
    val isFollowing: Boolean = false,
    val avatarRes: DrawableResource? = null,
    val books: List<AuthorBookUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class AuthorBookUi(
    val id: String,
    val title: String,
    val rating: String,
    val coverRes: DrawableResource
)

/**
 * Placeholder content for the author detail screen. There is no AuthorRepository / use case
 * yet, so the ViewModel seeds the UI state from this sample. Swap this for a domain use case
 * once author content has a real data source.
 */
internal fun sampleAuthorDetail(authorId: String): AuthorDetailUiState =
    AuthorDetailUiState(
        authorId = authorId,
        name = "Jacqueline Woodson",
        tagline = "Novelist · National Book Award winner",
        about = "Jacqueline Woodson writes spare, luminous novels about family, memory and " +
            "belonging. Her work moves between generations with a poet’s ear for what’s left unsaid.",
        booksCount = "34",
        readersCount = "212k",
        averageRating = "4.5",
        avatarRes = Res.drawable.author_detail_patricia_avatar,
        books = listOf(
            AuthorBookUi("red-at-the-bone", "Red at the Bone", "4.5", Res.drawable.author_detail_red_at_bone),
            AuthorBookUi("bestiary", "Bestiary", "4.2", Res.drawable.author_detail_bestiary),
            AuthorBookUi("olive-again", "Olive, Again", "4.3", Res.drawable.book_olive_again)
        )
    )
