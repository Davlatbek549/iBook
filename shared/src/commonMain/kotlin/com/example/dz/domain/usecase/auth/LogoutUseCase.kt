package com.example.dz.domain.usecase.auth

import com.example.dz.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.logout()
}
