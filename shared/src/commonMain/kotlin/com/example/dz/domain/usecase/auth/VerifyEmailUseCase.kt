package com.example.dz.domain.usecase.auth

import com.example.dz.domain.repository.AuthRepository

class VerifyEmailUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String) = repository.verifyEmail(email, code)
}
