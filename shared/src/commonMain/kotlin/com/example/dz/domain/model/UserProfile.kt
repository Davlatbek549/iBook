package com.example.dz.domain.model

data class UserProfile(
    val user: User,
    val booksRead: Int = 0,
    val friendsCount: Int = 0,
    val collectionsCount: Int = 0,
    val currentGoalMinutes: Int? = null,
    val membershipPlan: MembershipPlan? = null
)
