package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.User
import com.example.dz.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): AppResult<User> =
        AppResult.Success(FakeData.user.copy(email = email))

    override suspend fun signUp(name: String, email: String, password: String): AppResult<User> =
        AppResult.Success(FakeData.user.copy(name = name, email = email))

    override suspend fun logout(): AppResult<Unit> = AppResult.Success(Unit)

    override suspend fun getCurrentUser(): AppResult<User?> = AppResult.Success(FakeData.user)
}
