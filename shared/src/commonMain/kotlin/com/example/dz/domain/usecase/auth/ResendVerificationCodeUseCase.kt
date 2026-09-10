package com.example.dz.domain.usecase.auth

import com.example.dz.domain.repository.AuthRepository

class ResendVerificationCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String) = repository.resendVerificationCode(email)
}
