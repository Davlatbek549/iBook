package com.example.dz.presentation.social.friend_detail

import com.example.dz.domain.model.Friend

data class FriendDetailUiState(
    val friendId: String = "",
    val name: String = "",
    val isOnline: Boolean = false,
    val currentBookTitle: String? = null,
    val currentBookAuthor: String? = null,
    val currentBookCoverUrl: String? = null,
    // Nothing tracks a friendship's start date, a reading streak per friend, a yearly book count
    // for them, how far into their current book they are, or a shared-collection link between two
    // readers — all carry the same not-yet-wired defaults the Ink screen already showed; the
    // screen resolves fixed copy for the first two directly rather than routing it through state.
    val booksThisYear: Int = 31,
    val dayStreak: Int = 58,
    val currentBookProgress: Float = 0.55f,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val avatarInitial: String get() = name.trim().firstOrNull()?.uppercase() ?: "?"
    val firstName: String get() = name.substringBefore(' ').ifBlank { name }
}

fun Friend.toFriendDetailUiState(): FriendDetailUiState =
    FriendDetailUiState(
        friendId = id,
        name = name,
        isOnline = isOnline,
        currentBookTitle = currentBook?.title,
        currentBookAuthor = currentBook?.authors?.firstOrNull()?.name,
        currentBookCoverUrl = currentBook?.coverUrl,
    )
