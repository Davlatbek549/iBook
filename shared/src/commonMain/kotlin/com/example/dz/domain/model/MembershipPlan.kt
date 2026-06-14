package com.example.dz.domain.model

data class MembershipPlan(
    val id: String,
    val title: String,
    val price: String,
    val benefits: List<String>,
    val isCurrentPlan: Boolean = false
)
