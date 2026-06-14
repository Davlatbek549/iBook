package com.example.dz.domain.usecase.social

import com.example.dz.domain.repository.SocialRepository

class GetFriendsUseCase(
    private val repository: SocialRepository
) {
    suspend operator fun invoke() = repository.getFriends()
}
