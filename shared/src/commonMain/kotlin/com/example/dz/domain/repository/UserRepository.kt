package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.UserProfile

interface UserRepository {
    suspend fun getProfile(): AppResult<UserProfile>
    suspend fun updateProfile(profile: UserProfile): AppResult<UserProfile>
}
