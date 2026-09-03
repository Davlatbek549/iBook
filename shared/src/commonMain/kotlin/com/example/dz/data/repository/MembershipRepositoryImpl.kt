package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.MembershipPlan
import com.example.dz.domain.repository.MembershipRepository

class MembershipRepositoryImpl : MembershipRepository {

    override suspend fun getMembershipPlans(): AppResult<List<MembershipPlan>> =
        AppResult.Success(emptyList())

    override suspend fun getCurrentMembership(): AppResult<MembershipPlan?> =
        AppResult.Success(null)
}
