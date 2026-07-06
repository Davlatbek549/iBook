package com.example.dz.presentation.social.invite_friends

import dz.shared.generated.resources.Res
import dz.shared.generated.resources.img_masaa
import dz.shared.generated.resources.img_neil_alvin
import dz.shared.generated.resources.img_raunak_purohit
import dz.shared.generated.resources.img_yza_barretto
import org.jetbrains.compose.resources.DrawableResource

data class InviteFriendsUiState(
    val referralLink: String = "dz.app/r/amelia",
    val contacts: List<ContactUi> = defaultContacts,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class ContactUi(
    val id: String,
    val name: String,
    val avatarRes: DrawableResource,
    val onDz: Boolean
)

val defaultContacts: List<ContactUi> = listOf(
    ContactUi("masaa", "Masaa Okafor", Res.drawable.img_masaa, false),
    ContactUi("raunak", "Raunak Purohit", Res.drawable.img_raunak_purohit, false),
    ContactUi("yza", "Yza Barretto", Res.drawable.img_yza_barretto, true),
    ContactUi("neil", "Neil Alvin", Res.drawable.img_neil_alvin, false)
)
