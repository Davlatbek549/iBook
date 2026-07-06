package com.example.dz.presentation.social.friend_detail

import com.example.dz.domain.model.Friend
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_cover_2
import dz.shared.generated.resources.book_cover_3
import dz.shared.generated.resources.book_cover_4
import org.jetbrains.compose.resources.DrawableResource

data class FriendDetailUiState(
    val friendId: String = "",
    val name: String = "Patricia Lane",
    val subtitle: String = "@patricia.reads · friends since March",
    val online: Boolean = true,
    val booksCount: String = "73",
    val friendsCount: String = "28",
    val inCommonCount: String = "9",
    val currentBookTitle: String = "Olive, Again",
    val currentBookAuthor: String = "Elizabeth Strout",
    val currentBookProgress: Float = 0.44f,
    val shelfCovers: List<DrawableResource> = defaultShelfCovers,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val firstName: String get() = name.substringBefore(' ')
}

val defaultShelfCovers: List<DrawableResource> = listOf(
    Res.drawable.book_cover_4,
    Res.drawable.book_cover,
    Res.drawable.book_cover_2,
    Res.drawable.book_cover_3
)

fun Friend.toFriendDetailUiState(): FriendDetailUiState =
    FriendDetailUiState(
        friendId = id,
        name = name,
        online = isOnline,
        currentBookTitle = currentBook?.title ?: "Olive, Again",
        currentBookAuthor = currentBook?.authors?.firstOrNull()?.name ?: "Elizabeth Strout"
    )
