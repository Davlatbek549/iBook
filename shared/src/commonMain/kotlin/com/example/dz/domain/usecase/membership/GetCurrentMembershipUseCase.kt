package com.example.dz.domain.usecase.membership

import com.example.dz.domain.repository.MembershipRepository

class GetCurrentMembershipUseCase(
    private val repository: MembershipRepository
) {
    suspend operator fun invoke() = repository.getCurrentMembership()
}
