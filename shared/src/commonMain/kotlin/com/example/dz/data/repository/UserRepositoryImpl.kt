package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.User
import com.example.dz.domain.model.UserProfile
import com.example.dz.domain.repository.UserRepository

class UserRepositoryImpl(private val local: LocalDataSource) : UserRepository {

    override suspend fun getProfile(): AppResult<UserProfile> {
        val userId = local.getUserId() ?: return AppResult.Error(AppError.Unauthorized)
        val name = local.getUserName() ?: return AppResult.Error(AppError.Unauthorized)
        val email = local.getUserEmail()

        val profile = UserProfile(
            user = User(id = userId, name = name, email = email),
            booksRead = local.getSetting(KEY_BOOKS_READ, "0").toIntOrNull() ?: 0,
            friendsCount = local.getSetting(KEY_FRIENDS_COUNT, "0").toIntOrNull() ?: 0,
            collectionsCount = local.getSetting(KEY_COLLECTIONS_COUNT, "0").toIntOrNull() ?: 0,
            currentGoalMinutes = local.getSetting(KEY_GOAL_MINUTES, "30").toIntOrNull() ?: 30
        )
        return AppResult.Success(profile)
    }

    override suspend fun updateProfile(profile: UserProfile): AppResult<UserProfile> {
        local.saveUserSession(
            userId = profile.user.id,
            name = profile.user.name,
            email = profile.user.email ?: "",
            token = local.getToken() ?: ""
        )
        local.saveSetting(KEY_BOOKS_READ, profile.booksRead.toString())
        local.saveSetting(KEY_FRIENDS_COUNT, profile.friendsCount.toString())
        local.saveSetting(KEY_COLLECTIONS_COUNT, profile.collectionsCount.toString())
        profile.currentGoalMinutes?.let { local.saveSetting(KEY_GOAL_MINUTES, it.toString()) }
        return AppResult.Success(profile)
    }

    companion object {
        private const val KEY_BOOKS_READ = "profile_books_read"
        private const val KEY_FRIENDS_COUNT = "profile_friends_count"
        private const val KEY_COLLECTIONS_COUNT = "profile_collections_count"
        private const val KEY_GOAL_MINUTES = "profile_goal_minutes"
    }
}
