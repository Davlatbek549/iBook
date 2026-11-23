package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.model.ProfileImage
import com.example.dz.domain.model.UsernameAvailability

/**
 * The signed-in person's own profile as the server holds it, for Edit Profile.
 *
 * Separate from [UserRepository], which the Profile and Home screens read instantly from the
 * session on this device. This one always asks the server, because a form that saves the whole
 * profile back must start from what the server has, not a copy that might be stale.
 */
interface ProfileRepository {
    suspend fun getEditableProfile(): AppResult<EditableProfile>

    /** Saves all of [profile] and returns what the server stored, which may be normalised. */
    suspend fun saveEditableProfile(profile: EditableProfile): AppResult<EditableProfile>

    suspend fun checkUsername(username: String): AppResult<UsernameAvailability>

    /** Replaces [image] with [jpeg], already cut to shape. Returns the profile as stored after. */
    suspend fun uploadImage(image: ProfileImage, jpeg: ByteArray): AppResult<EditableProfile>

    /** Takes [image] off the profile. Returns the profile as stored after. */
    suspend fun removeImage(image: ProfileImage): AppResult<EditableProfile>
}
