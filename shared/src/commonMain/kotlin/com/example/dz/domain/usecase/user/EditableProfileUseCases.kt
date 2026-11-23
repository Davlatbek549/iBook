package com.example.dz.domain.usecase.user

import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.model.ProfileImage
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

/** A photo or banner change: [jpeg] replaces the picture, `null` removes it. */
class SaveProfileImageUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(image: ProfileImage, jpeg: ByteArray?) =
        if (jpeg == null) repository.removeImage(image) else repository.uploadImage(image, jpeg)
}
