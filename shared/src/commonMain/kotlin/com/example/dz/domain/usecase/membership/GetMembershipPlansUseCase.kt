package com.example.dz.domain.usecase.membership

import com.example.dz.domain.repository.MembershipRepository

class GetMembershipPlansUseCase(
    private val repository: MembershipRepository
) {
    suspend operator fun invoke() = repository.getMembershipPlans()
}
