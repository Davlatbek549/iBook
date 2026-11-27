package com.example.dz.presentation.social.friends

import com.example.dz.domain.model.Friend

data class FriendListUiState(
    val query: String = "",
    val friends: List<FriendRowUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    private val filtered: List<FriendRowUi>
        get() = friends.filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }

    val readingNow: List<FriendRowUi> get() = filtered.filter { it.currentBookTitle != null }
    val quietLately: List<FriendRowUi> get() = filtered.filter { it.currentBookTitle == null }

    /** Load finished, no error, nothing there — the handoff's cue to hand off to NoFriends. */
    val isEmpty: Boolean get() = !isLoading && errorMessage == null && friends.isEmpty()
}

data class FriendRowUi(
    val id: String,
    val name: String,
    val currentBookTitle: String?
)

fun Friend.toFriendRowUi(): FriendRowUi =
    FriendRowUi(id = id, name = name, currentBookTitle = currentBook?.title)
