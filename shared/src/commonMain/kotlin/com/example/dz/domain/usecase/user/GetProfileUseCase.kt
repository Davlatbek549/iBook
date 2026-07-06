package com.example.dz.domain.usecase.user

import com.example.dz.domain.repository.UserRepository

class GetProfileUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getProfile()
}
