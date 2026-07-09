package com.example.dz.domain.usecase.auth

import com.example.dz.domain.repository.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        repository.signUp(name, email, password)
}
