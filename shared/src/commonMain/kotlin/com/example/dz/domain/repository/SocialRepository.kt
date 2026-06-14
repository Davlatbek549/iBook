package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.Friend

interface SocialRepository {
    suspend fun getFriends(): AppResult<List<Friend>>
    suspend fun getFriendDetails(friendId: String): AppResult<Friend>
    suspend fun inviteFriend(contactId: String): AppResult<Unit>
}
