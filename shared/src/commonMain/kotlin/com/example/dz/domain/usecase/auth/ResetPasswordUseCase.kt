package com.example.dz.domain.usecase.auth

import com.example.dz.domain.repository.AuthRepository

class ResetPasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String, newPassword: String) =
        repository.resetPassword(email, code, newPassword)
}
