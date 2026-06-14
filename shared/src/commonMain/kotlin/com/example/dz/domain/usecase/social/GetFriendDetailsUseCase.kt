package com.example.dz.domain.usecase.social

import com.example.dz.domain.repository.SocialRepository

class GetFriendDetailsUseCase(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(friendId: String) = repository.getFriendDetails(friendId)
}
