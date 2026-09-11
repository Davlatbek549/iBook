package com.example.dz.data.local

interface LocalDataSource {
    fun getToken(): String?
    fun getRefreshToken(): String?

    /** Stores a rotated pair from `/auth/refresh`, leaving the signed-in user untouched. */
    fun saveTokens(token: String, refreshToken: String?)
    fun getUserId(): String?
    fun getUserEmail(): String?
    fun getUserName(): String?

    /** Whether the stored session belongs to an account that has proved its address. */
    fun isEmailVerified(): Boolean

    /** Records that a code was accepted, so a relaunch stops routing back to the code screen. */
    fun setEmailVerified(verified: Boolean)
    fun saveUserSession(
        userId: String,
        name: String,
        email: String,
        token: String,
        refreshToken: String?,
        emailVerified: Boolean
    )
    fun isLoggedIn(): Boolean
    fun clearSession()

    /**
     * Removes everything this device holds for the signed-in account: the session and every
     * per-account setting. Not what belongs to the device itself, such as having seen onboarding.
     */
    fun clearUserData()
    fun getSetting(key: String, default: String = ""): String
    fun saveSetting(key: String, value: String)
    fun removeSetting(key: String)
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)
}
