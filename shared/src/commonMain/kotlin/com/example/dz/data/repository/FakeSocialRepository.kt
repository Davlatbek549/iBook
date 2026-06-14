package com.example.dz.data.repository

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.Friend
import com.example.dz.domain.repository.SocialRepository

class FakeSocialRepository : SocialRepository {
    override suspend fun getFriends(): AppResult<List<Friend>> = AppResult.Success(FakeData.friends)

    override suspend fun getFriendDetails(friendId: String): AppResult<Friend> =
        FakeData.friends.firstOrNull { it.id == friendId }
            ?.let { AppResult.Success(it) }
            ?: AppResult.Error(AppError.NotFound)

    override suspend fun inviteFriend(contactId: String): AppResult<Unit> = AppResult.Success(Unit)
}
