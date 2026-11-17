package com.example.dz.data.repository

import com.example.dz.core.result.AppResult
import com.example.dz.data.local.LocalDataSource
import com.example.dz.data.mapper.ProfileMapper
import com.example.dz.data.remote.api.ProfileApi
import com.example.dz.data.remote.runRemote
import com.example.dz.domain.model.EditableProfile
import com.example.dz.domain.model.UsernameAvailability
import com.example.dz.domain.repository.ProfileRepository

class RemoteProfileRepository(
    private val api: ProfileApi,
    private val local: LocalDataSource,
) : ProfileRepository {

    override suspend fun getEditableProfile(): AppResult<EditableProfile> =
        runRemote { ProfileMapper.toDomain(api.profile()) }

    override suspend fun saveEditableProfile(profile: EditableProfile): AppResult<EditableProfile> {
        val result = runRemote { ProfileMapper.toDomain(api.updateProfile(ProfileMapper.toRequest(profile))) }
        if (result is AppResult.Success) rememberName(result.data.name)
        return result
    }

    override suspend fun checkUsername(username: String): AppResult<UsernameAvailability> =
        runRemote { ProfileMapper.toDomain(api.usernameAvailability(username)) }

    /**
     * Settings, Profile and Home read the name from the session on this device, not the server,
     * so a new name is written there too or those screens would go on showing the old one.
     * Tokens and the verified flag are carried over exactly as they are.
     */
    private fun rememberName(name: String) {
        val userId = local.getUserId() ?: return
        val token = local.getToken() ?: return
        local.saveUserSession(
            userId = userId,
            name = name,
            email = local.getUserEmail().orEmpty(),
            token = token,
            refreshToken = local.getRefreshToken(),
            emailVerified = local.isEmailVerified(),
        )
    }
}
