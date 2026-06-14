package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.fake.FakeData
import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.MembershipPlan
import com.example.dz.domain.repository.MembershipRepository

class MembershipRepositoryImpl(private val local: LocalDataSource) : MembershipRepository {

    override suspend fun getMembershipPlans(): AppResult<List<MembershipPlan>> {
        val activePlanId = local.getSetting(KEY_ACTIVE_PLAN)
        val plans = if (activePlanId.isNotBlank()) {
            FakeData.membershipPlans.map { it.copy(isCurrentPlan = it.id == activePlanId) }
        } else {
            FakeData.membershipPlans
        }
        return AppResult.Success(plans)
    }

    override suspend fun getCurrentMembership(): AppResult<MembershipPlan?> {
        val activePlanId = local.getSetting(KEY_ACTIVE_PLAN)
        val plan = if (activePlanId.isNotBlank()) {
            FakeData.membershipPlans.firstOrNull { it.id == activePlanId }
        } else {
            FakeData.membershipPlans.firstOrNull { it.isCurrentPlan }
        }
        return AppResult.Success(plan)
    }

    companion object {
        private const val KEY_ACTIVE_PLAN = "active_membership_plan"
    }
}
