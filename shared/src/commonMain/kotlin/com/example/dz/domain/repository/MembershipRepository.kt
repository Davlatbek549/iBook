package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.MembershipPlan

interface MembershipRepository {
    suspend fun getMembershipPlans(): AppResult<List<MembershipPlan>>
    suspend fun getCurrentMembership(): AppResult<MembershipPlan?>
}
