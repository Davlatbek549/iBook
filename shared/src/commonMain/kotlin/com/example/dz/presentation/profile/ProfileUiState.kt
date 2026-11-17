package com.example.dz.presentation.profile

import com.example.dz.domain.model.Friend
import com.example.dz.domain.model.ReadingGoal

data class ProfileUiState(
    val name: String = "",
    val booksOwned: Int = 0,
    val readingGoal: ReadingGoal? = null,
    val friends: List<ProfileFriendUi> = emptyList(),
    val membershipLabel: String = "Free plan",
    val isLoading: Boolean = false,
    val isSigningOut: Boolean = false,
    val errorMessage: String? = null
) {
    val avatarInitial: String get() = name.trim().firstOrNull()?.uppercase() ?: "?"

    val streakDays: Int get() = readingGoal?.streakDays ?: 0
    val minutesToday: Int get() = readingGoal?.minutesToday ?: 0
    val targetMinutes: Int get() = readingGoal?.targetMinutes ?: ReadingGoal.DEFAULT_TARGET_MINUTES

    /**
     * "Time read" wants a lifetime total the app does not track anywhere; the last seven days is
     * the longest real span [ReadingGoal] carries, so that stands in for it here.
     */
    val hoursReadThisWeek: Int get() = readingGoal?.week.orEmpty().sumOf { it.minutes } / 60
}

data class ProfileFriendUi(val id: String, val name: String)

fun Friend.toProfileFriendUi(): ProfileFriendUi = ProfileFriendUi(id = id, name = name)
