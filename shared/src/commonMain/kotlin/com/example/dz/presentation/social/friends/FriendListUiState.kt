package com.example.dz.presentation.social.friends

import dz.shared.generated.resources.Res
import dz.shared.generated.resources.img_maria_renzy
import dz.shared.generated.resources.img_neil_alvin
import dz.shared.generated.resources.img_raunak_purohit
import dz.shared.generated.resources.img_yza_barretto
import dz.shared.generated.resources.profile_2
import dz.shared.generated.resources.profile_3
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

val defaultFriends: List<FriendUi> = listOf(
    FriendUi("patricia-lane", "Patricia Lane", "@patricia.reads", Res.drawable.profile_2, true),
    FriendUi("maria-renzy", "Maria Renzy", "@maria.renzy", Res.drawable.img_maria_renzy, true),
    FriendUi("yza-barretto", "Yza Barretto", "@yza.b", Res.drawable.img_yza_barretto, true),
    FriendUi("daniel-moreau", "Daniel Moreau", "@dmoreau", Res.drawable.profile_3, false),
    FriendUi("neil-alvin", "Neil Alvin", "@neilalvin", Res.drawable.img_neil_alvin, false),
    FriendUi("raunak-purohit", "Raunak Purohit", "@raunakp", Res.drawable.img_raunak_purohit, false)
)
