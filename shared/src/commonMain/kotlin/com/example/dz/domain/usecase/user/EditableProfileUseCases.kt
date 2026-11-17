package com.example.dz.domain.usecase.user

import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.repository.ProfileRepository

class GetEditableProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke() = repository.getEditableProfile()
}

class SaveEditableProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: EditableProfile) = repository.saveEditableProfile(profile)
}

class CheckUsernameUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String) = repository.checkUsername(username)
}
