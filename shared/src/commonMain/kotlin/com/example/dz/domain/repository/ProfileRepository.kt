package com.example.dz.domain.repository

import com.example.dz.core.result.AppResult
import com.example.dz.domain.model.EditableProfile
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
}
