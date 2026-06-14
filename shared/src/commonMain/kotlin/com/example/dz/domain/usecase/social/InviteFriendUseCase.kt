package com.example.dz.domain.usecase.social

import com.example.dz.domain.repository.SocialRepository

class InviteFriendUseCase(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(contactId: String) = repository.inviteFriend(contactId)
}
