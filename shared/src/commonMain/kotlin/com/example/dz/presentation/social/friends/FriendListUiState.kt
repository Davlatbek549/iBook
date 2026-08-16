package com.example.dz.presentation.social.friends

import dz.shared.generated.resources.Res
import dz.shared.generated.resources.profile_1
import org.jetbrains.compose.resources.DrawableResource

data class FriendListUiState(
    val query: String = "",
    val friends: List<FriendUi> = defaultFriends,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    private val filtered: List<FriendUi>
        get() = friends.filter {
            query.isBlank() || it.name.contains(query, ignoreCase = true) || it.handle.contains(query, ignoreCase = true)
        }

    val onlineFriends: List<FriendUi> get() = filtered.filter { it.online }
    val everyoneFriends: List<FriendUi> get() = filtered.filter { !it.online }
}

data class FriendUi(
    val id: String,
    val name: String,
    val handle: String,
    val avatarRes: DrawableResource,
    val online: Boolean
)

val defaultFriends: List<FriendUi> = emptyList()

fun com.example.dz.domain.model.Friend.toFriendUi(): FriendUi =
    FriendUi(
        id = id,
        name = name,
        handle = "@${name.lowercase().replace(Regex("[^a-z0-9]+"), ".").trim('.')}",
        avatarRes = Res.drawable.profile_1,
        online = isOnline
    )
