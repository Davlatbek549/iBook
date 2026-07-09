package com.example.dz.domain.usecase.user

import com.example.dz.domain.model.UserProfile
import com.example.dz.domain.repository.UserRepository

class UpdateProfileUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(profile: UserProfile) =
        repository.updateProfile(profile)
}
