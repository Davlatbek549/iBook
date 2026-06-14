package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.UserProfile
import com.example.dz.domain.repository.UserRepository

class FakeUserRepository : UserRepository {
    override suspend fun getProfile(): AppResult<UserProfile> = AppResult.Success(FakeData.profile)

    override suspend fun updateProfile(profile: UserProfile): AppResult<UserProfile> =
        AppResult.Success(profile)
}
