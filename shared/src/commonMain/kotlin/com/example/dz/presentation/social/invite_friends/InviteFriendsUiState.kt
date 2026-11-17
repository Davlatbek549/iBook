package com.example.dz.presentation.social.invite_friends

data class InviteFriendsUiState(
    val referralLink: String = "dz.app/amelia",
    val contacts: List<ContactUi> = defaultContacts,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class ContactUi(
    val id: String,
    val name: String,
    val onDz: Boolean,
    val invited: Boolean = false
)

// There's no contacts-permission integration or backend contact-matching endpoint
// (SocialRepository only has getFriends/getFriendDetails/inviteFriend) — this list is the
// same static placeholder the old Ink screen already showed, just split across the two
// sections the redesign asks for.
val defaultContacts: List<ContactUi> = listOf(
    ContactUi("masaa", "Masaa Okafor", onDz = false),
    ContactUi("raunak", "Raunak Purohit", onDz = false),
    ContactUi("yza", "Yza Barretto", onDz = true),
    ContactUi("neil", "Neil Alvin", onDz = false)
)
