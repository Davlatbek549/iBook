package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.Friend
import com.example.dz.domain.repository.SocialRepository

class SocialRepositoryImpl(private val local: LocalDataSource) : SocialRepository {

    override suspend fun getFriends(): AppResult<List<Friend>> =
        AppResult.Success(emptyList())

    override suspend fun getFriendDetails(friendId: String): AppResult<Friend> =
        AppResult.Error(AppError.NotFound)

    override suspend fun inviteFriend(contactId: String): AppResult<Unit> {
        local.saveSetting("invited_$contactId", "true")
        return AppResult.Success(Unit)
    }
}
