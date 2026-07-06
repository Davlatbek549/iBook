package com.example.dz.presentation.profile

import com.example.dz.domain.model.UserProfile

data class ProfileUiState(
    val name: String = "Amelia Hartwell",
    val handle: String = "@amelia.reads · since 2024",
    val booksRead: String = "48",
    val friendsCount: Int = 12,
    val streak: String = "21d",
    val goalsSubtitle: String = "21-day streak",
    val collectionsSubtitle: String = "3 shelves",
    val purchasesSubtitle: String = "14 books",
    val membershipLabel: String = "Free plan",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val friendsSummary: String get() = "$friendsCount friends"
}

fun UserProfile.toProfileUiState(): ProfileUiState =
    ProfileUiState(
        name = user.name,
        booksRead = booksRead.toString(),
        friendsCount = friendsCount,
        collectionsSubtitle = "$collectionsCount shelves",
        membershipLabel = membershipPlan?.title ?: "Free plan"
    )
