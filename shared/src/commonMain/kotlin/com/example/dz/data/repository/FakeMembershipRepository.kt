package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.domain.model.MembershipPlan
import com.example.dz.domain.repository.MembershipRepository

class FakeMembershipRepository : MembershipRepository {
    override suspend fun getMembershipPlans(): AppResult<List<MembershipPlan>> =
        AppResult.Success(FakeData.membershipPlans)

    override suspend fun getCurrentMembership(): AppResult<MembershipPlan?> =
        AppResult.Success(FakeData.membershipPlans.firstOrNull { it.isCurrentPlan })
}
