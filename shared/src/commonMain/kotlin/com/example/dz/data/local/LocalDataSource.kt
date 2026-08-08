package com.example.dz.data.local

interface LocalDataSource {
    fun getToken(): String?
    fun getRefreshToken(): String?

    /** Stores a rotated pair from `/auth/refresh`, leaving the signed-in user untouched. */
    fun saveTokens(token: String, refreshToken: String?)
    fun getUserId(): String?
    fun getUserEmail(): String?
    fun getUserName(): String?
    fun saveUserSession(
        userId: String,
        name: String,
        email: String,
        token: String,
        refreshToken: String?
    )
    fun isLoggedIn(): Boolean
    fun clearSession()
    fun getSetting(key: String, default: String = ""): String
    fun saveSetting(key: String, value: String)
    fun removeSetting(key: String)
}
