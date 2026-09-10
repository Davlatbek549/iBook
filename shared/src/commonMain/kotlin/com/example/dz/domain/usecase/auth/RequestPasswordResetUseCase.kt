package com.example.dz.domain.usecase.auth

import com.example.dz.domain.repository.AuthRepository

class RequestPasswordResetUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String) = repository.requestPasswordReset(email)
}
